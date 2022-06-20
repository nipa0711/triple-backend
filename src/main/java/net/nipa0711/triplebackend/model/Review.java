package net.nipa0711.triplebackend.model;

import java.util.Date;

import lombok.Data;

@Data
public class Review {
    private String reviewId; // UUID 포맷의 review id입니다. 각각의 리뷰에 대한 고유 값 입니다.
    private String content; // 리뷰의 내용입니다.
    private String userId; // 리뷰의 작성자 id입니다.
    private String placeId; // 리뷰가 작성된 장소의 id입니다.
    private boolean isPhotoExist;
    private Date createdTime;
}
