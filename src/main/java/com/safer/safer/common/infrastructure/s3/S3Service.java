package com.safer.safer.common.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.safer.safer.common.exception.ExceptionCode;
import com.safer.safer.common.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.safer.safer.common.exception.ExceptionCode.FAIL_TO_UPLOAD_IMAGE;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveImage(MultipartFile multipartFile, Long userId) {
        String filename = userId + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        } catch(IOException e) {
            throw new S3Exception(FAIL_TO_UPLOAD_IMAGE);
        }

        return amazonS3.getUrl(bucket, filename).toString();
    }

    public void deleteImage(MultipartFile multipartFile, Long userId)  {
        String fileName = userId + "-" + multipartFile.getOriginalFilename();

        amazonS3.deleteObject(bucket, fileName);
    }
}
