package com.prgrms.zzalmyu.domain.image.application;

public interface ImageService {

    /**
     * 짤의 상세 페이지를 볼 수 있다.
     */
    void getImageDetail();

    /**
     * 로그인 한 유저는 짤의 좋아요를 누를 수 있다.
     */
    void likeImage();

    /**
     * 좋아요한 짤 조회
     */
    void getLikeImages();

    /**
     * 업로드한 짤 조회
     */
    void getUploadImages();

    /**
     * 짤 업로드
     * 인자 : imageUrl, List<Tag>
     */
    void uploadImage();


    /**
     * 업로드한 사진 삭제
     * 인자 : 사진 id (단건)
     * 로직: 해당 사진이 해당 유저가 업로드한 사진인지 체크
     */
    void deleteUploadImages();

    /**
     * 신고가 3번 누적된 사진 삭제
     * hard delete 사용
     * 관리자 권한 여부 체크
     */
    void deleteReportImage();
}
