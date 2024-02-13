package com.prgrms.zzalmyu.domain.report.application;

import static org.assertj.core.api.Assertions.*;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ImageChatCountRepository;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.report.infrastructure.ReportRepository;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserJPARepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserJPARepository userJPARepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageChatCountRepository imageChatCountRepository;

    User user1 = User.builder()
        .email("kazuha@naver.com")
        .nickname("카즈하")
        .role(Role.USER)
        .build();

    User user2 = User.builder()
        .email("raiden@naver.com")
        .nickname("라이덴")
        .role(Role.USER)
        .build();

    User saved1;
    User saved2;
    ImageChatCount imageChatCount = new ImageChatCount();

    @BeforeEach
    public void beforeEach() {
        imageChatCountRepository.save(imageChatCount);

        saved1 = userJPARepository.save(user1);
        saved2 = userJPARepository.save(user2);
    }

    @AfterEach
    public void afterEach() {
        reportRepository.deleteAll();
    }

    @Test
    @DisplayName("이미지를 신고할 수 있다.")
    public void reportImage() {
        Image image = Image.builder()
            .imageChatCount(imageChatCount)
            .path("https://i.namu.wiki/i/UQar4WhQAWFiJUmHBAbNlCSoqK-noyPt6tbI0DK6pDFbks3bhZdJehgSW3S50RMQgSzuzEx7ArHcg_ztlDYXwQ.webp")
            .userId(saved1.getId())
            .build();
        image = imageRepository.save(image);
        reportService.reportImage(user2.getId(), image.getId());

        List<Report> reports = reportRepository.findAll();
        Report report = reports.get(0);

        assertThat(report.getReportUserId()).isEqualTo(user2.getId());
        assertThat(report.getImageId()).isEqualTo(image.getId());
    }
}