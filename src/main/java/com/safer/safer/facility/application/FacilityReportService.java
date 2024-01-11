package com.safer.safer.facility.application;

import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.common.infrastructure.s3.S3Service;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.repository.FacilityRepository;
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.facility.dto.report.FacilityReport;
import com.safer.safer.facility.dto.report.FacilityUpdateReport;
import com.safer.safer.facility.dto.report.NewFacilityReport;
import com.safer.safer.facility.dto.report.FacilityRequest;
import com.safer.safer.user.domain.User;
import com.safer.safer.user.domain.repository.UserRepository;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_FACILITY;
import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_USER_ACCOUNT;
import static com.slack.api.webhook.WebhookPayloads.payload;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityReportService {

    @Value("${webhook.slack.url}")
    private String slackUrl;
    private final Slack slackClient = Slack.getInstance();
    private final RedisTemplate<String, Object> redisTemplate;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    private final static int REPORT_EXPIRATION_DAYS = 7;

    public void reportFacility(FacilityRequest request, MultipartFile file, UserInfo userInfo) throws IOException {
        Long userId = userInfo.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER_ACCOUNT));

        String imageUrl = s3Service.saveImage(file, userId);
        NewFacilityReport facilityReport = NewFacilityReport.from(request, imageUrl);

        saveFacilityReport(facilityReport, userId);
        sendMessage(facilityReport, user.getName());
    }

    public void reportFacilityUpdate(Long facilityId, FacilityRequest request, MultipartFile file, UserInfo userInfo) throws IOException {
        Long userId = userInfo.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER_ACCOUNT));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY));

        String imageUrl = s3Service.saveImage(file, userId);
        FacilityUpdateReport facilityReport = FacilityUpdateReport.from(FacilityDetailResponse.from(facility), facility.getCoordinate(), request, imageUrl);

        saveFacilityReport(facilityReport, userId);
        sendMessage(facilityReport, user.getName());
    }

    private void saveFacilityReport(FacilityReport report, Long userId) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();

        String key = "userId::" + userId;
        Date expirationDate = Date.from(now().plus(REPORT_EXPIRATION_DAYS, DAYS));

        listOperations.leftPush(key, report);
        redisTemplate.expireAt(key, expirationDate);
    }

    private void sendMessage(FacilityReport report, String userName) throws IOException {
        slackClient.send(slackUrl, payload(p -> p
                .text(report.getTitle(userName))
                .attachments(List.of(Attachment.builder()
                        .color(report.getMessageColor())
                        .fields(report.toMap().entrySet().stream()
                                .map(this::generateSlackField)
                                .toList()
                        )
                        .build()
                ))
        ));
    }

    private Field generateSlackField(Map.Entry<String,String> entry) {
        return Field.builder()
                .title(entry.getKey())
                .value(entry.getValue())
                .valueShortEnough(false)
                .build();
    }
}
