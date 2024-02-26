package com.prgrms.zzalmyu.domain.image.presentation.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImageUploadRequestDto {
    private String title;
    private List<Long> tagIdList;
    private MultipartFile file;
}
