package com.prgrms.zzalmyu.domain.image.application;

import com.amazonaws.services.cloudformation.model.AlreadyExistsException;
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
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final ImageChatCountRepository imageChatCountRepository;
    private final ImageRepository imageRepository;

    private static final String dirName = "upload";

    @Value("${s3.bucket}")
    private String bucket;

    public AwsS3ResponseDto upload(User user, MultipartFile multipartFile) throws IOException {
        File file = convertMultipartFileToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

        return upload(user, file);
    }

    private AwsS3ResponseDto upload(User user, File file) {
        String key = makeFileName(user, file);
        String path = putS3(file, key);
        removeFile(file);

        ImageChatCount imageChatCount = new ImageChatCount();
        imageChatCountRepository.save(imageChatCount);
        Image image = Image.builder()
                .key(key)
                .path(path)
                .imageChatCount(imageChatCount)
                .userId(user.getId())
                .build();
        imageRepository.save(image);

        AwsS3 awsS3 = AwsS3.builder()
                .key(key)
                .path(path)
                .build();
        return awsS3.convertResponseDto(image.getId());
    }

    private String makeFileName(User user, File file) {
        return dirName + "/" + user.getEmail() + file.getName();
    }

    private String putS3(File uploadFile, String fileName) {
        // 이미 s3에 존재하는 파일 이름이라면 업로드 못하게 방지
        if (getS3(bucket, fileName) != null) {
            throw new AlreadyExistsException("이미 존재하는 파일 이름입니다.");
        }
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return getS3(fileName);
    }

    public String getS3(String fileName) {
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


    public void remove(User user, AwsS3RequestDto awsS3RequestDto) {
        Image image = imageRepository.findById(awsS3RequestDto.getImageId()).orElseThrow(() -> new NoSuchElementException("해당하는 이미지가 존재하지 않습니다."));
        if (!amazonS3.doesObjectExist(bucket, image.getKey())) {
            throw new AmazonS3Exception("Object " + image.getKey() + " does not exist!");
        }
        if (!image.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("본인이 등록한 이미지만 삭제할 수 있습니다.");
        }
        amazonS3.deleteObject(bucket, image.getKey());
        imageRepository.delete(image);
    }
}
