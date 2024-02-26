package com.prgrms.zzalmyu.domain.image.application;

import com.amazonaws.services.cloudformation.model.AlreadyExistsException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.image.domain.entity.AwsS3;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    private static final String dirName = "upload";

    @Value("${s3.bucket}")
    private String bucket;

    public AwsS3 upload(User user, MultipartFile multipartFile) throws IOException {
        File file = convertMultipartFileToProgressiveJPEG(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

        return upload(user, file);
    }

    private AwsS3 upload(User user, File file) {
        String key = makeFileName(user, file);
        String path = putS3(file, key);
        removeFile(file);
        return AwsS3.builder()
                .key(key)
                .path(path)
                .build();
    }

    private String makeFileName(User user, File file) {
        return dirName + "/" + user.getEmail() + file.getName();
    }

    private String putS3(File uploadFile, String fileName) {
        // 이미 s3에 존재하는 파일 이름이라면 업로드 못하게 방지
        if (isS3Exists(fileName)) {
            throw new AlreadyExistsException(ErrorCode.IMAGE_ALREADY_EXISTS.getMessage());
        }
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return getS3(fileName);
    }

    public String getS3(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public boolean isS3Exists(String fileName) {
        try {
            amazonS3.getObject(bucket, fileName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void removeFile(File file) {
        file.delete();
    }

    private Optional<File> convertMultipartFileToProgressiveJPEG(MultipartFile multipartFile) throws IOException {
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        File tempFile = File.createTempFile("temp_image", ".jpg");
        try (FileImageOutputStream imageOutputStream = new FileImageOutputStream(tempFile)) {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (writers.hasNext()) {
                ImageWriter writer = writers.next();
                writer.setOutput(imageOutputStream);
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
                writer.write(null, new IIOImage(image, null, null), param);
                writer.dispose();
            }
        }
        return Optional.of(tempFile);
    }

    public void remove(Image image) {
        if (!amazonS3.doesObjectExist(bucket, image.getS3Key())) {
            throw new AmazonS3Exception("Object " + image.getS3Key() + " does not exist!");
        }
        amazonS3.deleteObject(bucket, image.getS3Key());
    }
}
