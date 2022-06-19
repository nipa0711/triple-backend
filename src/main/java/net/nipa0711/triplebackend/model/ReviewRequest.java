package net.nipa0711.triplebackend.model;

import java.util.List;

import lombok.Data;

@Data
public class ReviewRequest {
    private String type; // 미리 정의된 string 값을 가지고 있습니다. 리뷰 이벤트의 경우 "REVIEW" 로 옵니다.
    private String action; // 리뷰 생성 이벤트의 경우 "ADD" , 수정 이벤트는 "MOD" , 삭제 이벤트는 "DELETE" 값을 가지고 있습니다.
    private String reviewId; // UUID 포맷의 review id입니다. 어떤 리뷰에 대한 이벤트인지 가리키는 값입니다.
    private String content; // 리뷰의 내용입니다.
    private List<String> attachedPhotoIds; // 리뷰에 첨부된 이미지들의 id 배열입니다.
    private String userId; // 리뷰의 작성자 id입니다.
    private String placeId; // 리뷰가 작성된 장소의 id입니다.
}
