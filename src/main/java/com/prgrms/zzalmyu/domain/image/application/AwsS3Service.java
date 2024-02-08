package com.prgrms.zzalmyu.domain.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ImageChatCountRepository;
import com.prgrms.zzalmyu.domain.image.domain.entity.AwsS3;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3RequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final ImageChatCountRepository imageChatCountRepository;
    private final ImageRepository imageRepository;

    @Value("${s3.bucket}")
    private String bucket;

    public AwsS3ResponseDto upload(MultipartFile multipartFile, String dirName) throws IOException {
        File file = convertMultipartFileToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

        return upload(file, dirName);
    }

    private AwsS3ResponseDto upload(File file, String dirName) {
        String key = randomFileName(file, dirName);
        String path = putS3(file, key);
        removeFile(file);

        ImageChatCount imageChatCount = new ImageChatCount();
        imageChatCountRepository.save(imageChatCount);
        Image image = Image.builder()
                .url(key)
                .imageChatCount(imageChatCount)
                .userId(1L) // 수정해야함.
                .build();
        imageRepository.save(image);

        AwsS3 awsS3 = AwsS3.builder()
                .key(key)
                .path(path)
                .build();
        return awsS3.convertResponseDto(image.getId());
    }

    private String randomFileName(File file, String dirName) {
        return dirName + "/" + UUID.randomUUID() + file.getName();
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return getS3(bucket, fileName);
    }

    private String getS3(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File file) {
        file.delete();
    }

    public Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return Optional.empty();
        }

        File file = new File(System.getProperty("user.dir") + "/" + fileName);
        if (!file.exists()) {
            if (file.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(multipartFile.getBytes());
                }
                return Optional.of(file);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }


    public void remove(AwsS3RequestDto awsS3RequestDto) {
        Image image = imageRepository.findById(awsS3RequestDto.getImageId()).orElseThrow(() -> new NoSuchElementException("해당하는 이미지가 존재하지 않습니다."));
        if (!amazonS3.doesObjectExist(bucket, image.getUrl())) {
            throw new AmazonS3Exception("Object " + image.getUrl() + " does not exist!");
        }
        amazonS3.deleteObject(bucket, image.getUrl());
        imageRepository.delete(image);
    }
}
