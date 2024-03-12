package com.prgrms.zzalmyu.domain.chat.presentation.dto.res;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportDetailResponse;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatImageResponse {

    private String email;

    private String image;

    private String nickname;

    public static ChatImageResponse of(String email, String image, String nickname) {
        return new ChatImageResponse(email, image, nickname);
    }
}
