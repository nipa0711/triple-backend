package net.nipa0711.triplebackend.model;

import java.util.Date;

import lombok.Data;

@Data
public class Review {
    private int reviewIdx;
    private String reviewId; // UUID 포맷의 review id입니다. 어떤 리뷰에 대한 이벤트인지 가리키는 값입니다.
    private String content; // 리뷰의 내용입니다.
    private String userId; // 리뷰의 작성자 id입니다.
    private String placeId; // 리뷰가 작성된 장소의 id입니다.
    private boolean isPhotoExist;
    private Date createdTime;
}
