package com.safer.safer.facility.application;

import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.common.infrastructure.s3.S3Service;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.repository.FacilityReportRepository;
import com.safer.safer.facility.domain.repository.FacilityRepository;
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.facility.dto.report.*;
import com.safer.safer.user.domain.User;
import com.safer.safer.user.domain.repository.UserRepository;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.safer.safer.common.exception.ExceptionCode.*;
import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityReportService {

    @Value("${webhook.slack.url}")
    private String slackUrl;
    private final Slack slackClient = Slack.getInstance();
    private final S3Service s3Service;
    private final FacilityService facilityService;
    private final FacilityReportRepository facilityReportRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    private final static String DONE = " 완료";
    private final static String GUIDE = "{수락/거절} {제보한 유저의 아이디} {추가/수정} 양식을 확인해주세요.\n먼저 들어온 제보부터 처리됩니다.";

    public void reportCreation(FacilityReportRequest request, MultipartFile file, UserInfo userInfo) throws IOException {
        Long userId = userInfo.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER_ACCOUNT));

        String imageUrl = s3Service.saveImage(file, userId);
        FacilityReport facilityReport = FacilityReport.from(request, imageUrl);

        facilityReportRepository.save(userId, facilityReport);
        sendMessage(FacilityCreationReport.from(facilityReport), user.getName(), userId);
    }

    public void reportModification(Long facilityId, FacilityReportRequest request, MultipartFile file, UserInfo userInfo) throws IOException {
        Long userId = userInfo.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER_ACCOUNT));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY));

        String imageUrl = s3Service.saveImage(file, userId);
        FacilityReport facilityReport = FacilityReport.from(facilityId, request, imageUrl);
        FacilityModificationReport facilityModificationReport = FacilityModificationReport.from(
                FacilityDetailResponse.from(facility), facility.getCoordinate(), facilityReport);

        facilityReportRepository.save(userId, facilityReport);
        sendMessage(facilityModificationReport, user.getName(), userId);
    }

    public SlackResponse handleFacilityReport(SlackMessage message) {
        if(message.isValid()) {
            FacilityReport facilityReport = facilityReportRepository.findReport(message.userId());
            if(message.isAccept())
                saveOrUpdateFacility(facilityReport);

            return SlackResponse.of(message.operation() + DONE);
        }
        return SlackResponse.of(GUIDE);
    }

    private void saveOrUpdateFacility(FacilityReport facilityReport) {
        if(facilityReport.facilityId() != 0)
            facilityService.updateFacility(facilityReport);
        else
            facilityService.saveFacility(facilityReport);
    }

    private void sendMessage(FacilityReportConvertible report, String userName, Long userId) throws IOException {
        slackClient.send(slackUrl, payload(p -> p
                .text(report.getTitle(userName, userId))
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
