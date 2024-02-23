package com.prgrms.zzalmyu.domain.report.application;

import static org.assertj.core.api.Assertions.*;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ImageChatCountRepository;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.report.exception.ReportException;
import com.prgrms.zzalmyu.domain.report.infrastructure.ReportRepository;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportDetailResponse;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportResponse;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageChatCountRepository imageChatCountRepository;

    User kazuha = User.builder()
            .email("kazuha@naver.com")
            .nickname("카즈하")
            .role(Role.USER)
            .build();

    User shogun = User.builder()
            .email("raiden@naver.com")
            .nickname("라이덴")
            .role(Role.USER)
            .build();

    User nahida = User.builder()
            .email("nahida@naver.com")
            .nickname("나히다")
            .role(Role.USER)
            .build();

    User miko = User.builder()
            .email("miko@naver.com")
            .nickname("야에 미코")
            .role(Role.USER)
            .build();

    ImageChatCount imageChatCount = new ImageChatCount();

    Image image;

    @BeforeEach
    public void beforeEach() {
        imageChatCountRepository.save(imageChatCount);
        kazuha = userRepository.save(kazuha);
        shogun = userRepository.save(shogun);
        nahida = userRepository.save(nahida);
        miko = userRepository.save(miko);
        image = Image.builder()
                .title("title1")
                .imageChatCount(imageChatCount)
                .path(
                        "https://i.namu.wiki/i/UQar4WhQAWFiJUmHBAbNlCSoqK-noyPt6tbI0DK6pDFbks3bhZdJehgSW3S50RMQgSzuzEx7ArHcg_ztlDYXwQ.webp")
                .userId(kazuha.getId())
                .s3Key("key")
                .build();

        image = imageRepository.save(image);
    }

    @AfterEach
    public void afterEach() {
        imageChatCountRepository.deleteAll();
        reportRepository.deleteAll();
        userRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    @DisplayName("이미지를 신고할 수 있다.")
    public void reportImage() {
        reportService.reportImage(shogun.getId(), image.getId());

        List<Report> reports = reportRepository.findAll();
        Report report = reports.get(0);

        assertThat(report.getReportUserId()).isEqualTo(shogun.getId());
        assertThat(report.getImageId()).isEqualTo(image.getId());
    }

    @Test
    @DisplayName("신고 상세 내역을 조회할 수 있다.")
    public void getReportDetailOverThree() {
        reportService.reportImage(shogun.getId(), image.getId());
        reportService.reportImage(miko.getId(), image.getId());
        reportService.reportImage(nahida.getId(), image.getId());

        List<ReportDetailResponse> reportDetail = reportService.getReportDetail(image.getId());

        assertThat(reportDetail.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("신고 3번 이상인 이미지를 삭제할 수 있다.")
    public void deleteReportedImageMoreThanThree() {
        reportService.reportImage(shogun.getId(), image.getId());
        reportService.reportImage(miko.getId(), image.getId());
        reportService.reportImage(nahida.getId(), image.getId());

        // TODO: s3 연결 완료되면 수정하기
        assertThatThrownBy(() -> reportService.deleteReportedImage(image.getId()))
                .isInstanceOf(AmazonS3Exception.class);
    }

    @Test
    @DisplayName("신고 3번 미만인 이미지는 삭제할 수 없다.")
    public void deleteReportedImageBelowThree() {
        reportService.reportImage(shogun.getId(), image.getId());
        reportService.reportImage(miko.getId(), image.getId());

        assertThatThrownBy(() -> reportService.deleteReportedImage(image.getId()))
                .isInstanceOf(ReportException.class);
    }

    @Test
    @DisplayName("3번 이상 신고된 내역 리스트를 조회할 수 있다.")
    public void getReportsMoreThanThree() {
        reportService.reportImage(shogun.getId(), image.getId());
        reportService.reportImage(miko.getId(), image.getId());
        reportService.reportImage(nahida.getId(), image.getId());
        Pageable pageable = PageRequest.of(0, 10);
        List<ReportResponse> reports = reportService.getReports(pageable);

        assertThat(reports.size()).isEqualTo(1);
        assertThat(reports.get(0).getReportCount()).isEqualTo(3);
        assertThat(reports.get(0).getTags().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("3번 미만 신고된 내역은 리스트에 포함되지 않는다.")
    public void getReportsBelowThree() {
        reportService.reportImage(shogun.getId(), image.getId());
        reportService.reportImage(miko.getId(), image.getId());
        Pageable pageable = PageRequest.of(0, 10);

        List<ReportResponse> reports = reportService.getReports(pageable);

        assertThat(reports.size()).isEqualTo(0);
    }
}