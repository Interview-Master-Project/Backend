package com.interview_master.application;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.interview_master.common.constant.Constant.DIRECTORY_SEPARATOR;
import static com.interview_master.common.constant.Constant.IMAGE_DIRECTORY_NAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class NcpImageService {

    @Value("${cloud.ncp.bucketname}")
    private String bucketName;

    private final AmazonS3Client objectStorageClient;

    public String uploadImage(MultipartFile image) {
        return upload(image);
    }


    public String upload(MultipartFile file) {
        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            throw new ApiException(ErrorCode.EMPTY_FILE_EXCEPTION);
        }

        validateImageFileExtension(file.getOriginalFilename());

        String fileName = IMAGE_DIRECTORY_NAME + DIRECTORY_SEPARATOR + UUID.randomUUID().toString().substring(0, 13) + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            try (InputStream inputStream = file.getInputStream()) {
                PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                objectStorageClient.putObject(request);
                log.info("Image {} has been uploaded.", fileName);
            }
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Error uploading file to NCloud Storage", e);
        } catch (Exception e) {
            log.error("Error uploading file to NCloud Storage", e);
        }

        return objectStorageClient.getUrl(bucketName, fileName).toString();
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

    public void deleteImageFromBucket(String imgUrl) {
        log.info("imgUrl = {}", imgUrl);
        String filename = getKeyFromImageUrl(imgUrl);
        log.info("Deleting image {} from the bucket.", filename);
        try {
            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, filename);
            objectStorageClient.deleteObject(request);
            log.info("Image {} has been deleted.", filename);
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