package net.nipa0711.triplebackend.service;

import java.util.List;

import net.nipa0711.triplebackend.model.History;
import net.nipa0711.triplebackend.model.Photo;
import net.nipa0711.triplebackend.model.Review;
import net.nipa0711.triplebackend.model.ReviewRequest;
import net.nipa0711.triplebackend.model.User;

public interface ReviewService {

    // About Review.

    String processReview(ReviewRequest reviewRequest);

    String createReview(ReviewRequest reviewRequest);

    int findMyReviewIndex(String userId, String placeId);

    int isReviewRegistered(String userId, String placeId);

    int addedReviewCount(String placeId);

    int getFirstReview(String placeId);    

    String updateReview(ReviewRequest reviewRequest);

    String deleteReview(ReviewRequest reviewRequest);

    Review getMyReview(int reviewIdx);

    // About User.

    int addUser(User user);

    User getUser(String userId);

    int findUserPoint(String userId);

    // About Photo.

    int isPhotoExist(String photoId);

    boolean uploadPhoto(Photo photo);

    int deletePhoto(String userId, String placeId);

    int deletePhotoByPhotoId(String photoId);

    List<String> getPhotoList(String userId, String placeId);

    // About history.

    int createHistory(History history);

    List<History> getHistory(String userId);
}