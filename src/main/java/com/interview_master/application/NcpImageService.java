package com.interview_master.application;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NcpImageService {

    @Value("${cloud.ncp.bucketname}")
    private String bucketName;

    private final S3Client s3Client;

    public String uploadImage(MultipartFile image) {
        return upload(image);
    }


    public String upload(MultipartFile file) {
        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            throw new ApiException(ErrorCode.EMPTY_FILE_EXCEPTION);
        }

        validateImageFileExtension(file.getOriginalFilename());

        String fileName = "collection/" + UUID.randomUUID().toString().substring(0, 13) + "_" + file.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("Image {} has been uploaded.", fileName);

            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toString();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to NCloud Storage", e);
        }
    }

    /**
     * 파일 확장자 검증
     */
    private void validateImageFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new ApiException(ErrorCode.NO_FILE_EXTENTION);
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtensionList.contains(extension)) {
            throw new ApiException(ErrorCode.INVALID_FILE_EXTENTION);
        }
    }

    public void deleteImageFromS3(String imgUrl) {
        String key = getKeyFromImageUrl(imgUrl);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting image from S3", e);
        }
    }

    private String getKeyFromImageUrl(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (Exception e) {
            throw new RuntimeException("Error parsing image URL", e);
        }
    }
}