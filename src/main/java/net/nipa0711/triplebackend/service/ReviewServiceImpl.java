package net.nipa0711.triplebackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.nipa0711.triplebackend.mapper.ReviewMapper;
import net.nipa0711.triplebackend.mapper.UserMapper;
import net.nipa0711.triplebackend.model.History;
import net.nipa0711.triplebackend.model.Photo;
import net.nipa0711.triplebackend.model.Review;
import net.nipa0711.triplebackend.model.ReviewRequest;
import net.nipa0711.triplebackend.model.User;

@Transactional(rollbackFor = { Exception.class })
@Service
public class ReviewServiceImpl implements ReviewService {

    private UserMapper userMapper;
    private ReviewMapper reviewMapper;

    public ReviewServiceImpl(UserMapper userMapper, ReviewMapper reviewMapper) {
        this.userMapper = userMapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public String processReview(ReviewRequest reviewRequest) {
        String type = reviewRequest.getType();
        if (type == null || "".equals(type)) {
            return "type value can not be null or empty";
        }
        if (!"REVIEW".equals(type)) {
            return "Not review event.";
        }
        String action = reviewRequest.getAction();
        if (action == null || "".equals(action)) {
            return "action value can not be null or empty";
        }

        String result = "Action is wrong.";
        switch (action) {
            case "ADD":
                System.out.println("ADD");
                result = createReview(reviewRequest);
                break;
            case "MOD":
                System.out.println("MOD");
                result = updateReview(reviewRequest);
                break;
            case "DELETE":
                System.out.println("DELETE");
                result = deleteReview(reviewRequest);
                break;
            default:
                System.out.println("Wrong action detected");
                break;
        }
        return result;
    }

    @Override
    public String createReview(ReviewRequest reviewRequest) {
        String reviewId = reviewRequest.getReviewId();
        if (reviewId == null || "".equals(reviewId)) {
            return "Failed : reviewId is null or empty";
        }
        String userId = reviewRequest.getUserId();
        if (userId == null || "".equals(userId)) {
            return "Failed : userId is null or empty";
        }
        String placeId = reviewRequest.getPlaceId();
        if (placeId == null || "".equals(placeId)) {
            return "Failed : placeId is null or empty";
        }
        String content = reviewRequest.getContent();
        if (content == null || "".equals(content)) {
            return "Failed : Content is null or empty";
        }

        String resultMsg = "Unknown Error!";

        try {
            int currentPoint = 0;
            int newPoint = 0;
            User user = getUser(userId);
            System.out.println("user : " + user);
            if (user == null) {
                // Newbie
                user = new User();
                user.setUserId(userId);
                int id = addUser(user);
                System.out.println("id : " + id);
            } else {
                // Registered user.
                currentPoint = findUserPoint(userId);
            }
            System.out.println("currentPoint : " + currentPoint);

            // If review is exist.
            if (isReviewRegistered(reviewId) != 0) {
                resultMsg = "Try to add duplicated review.";
                System.out.println("resultMsg : " + resultMsg);
                throw new RuntimeException();
            }

            Review review = new Review();
            review.setUserId(userId);
            review.setReviewId(reviewRequest.getReviewId());
            review.setPlaceId(placeId);
            review.setContent(content);
            List<String> photos = reviewRequest.getAttachedPhotoIds();
            if (photos == null || photos.size() == 0) {
                review.setPhotoExist(false);
            } else {
                // If photo is existed.
                review.setPhotoExist(true);
                // Pre-test for duplicate photo is existed.
                for (String photoId : photos) {
                    if (isPhotoExist(photoId) == 1) {
                        resultMsg = "Try to add duplicate photo";
                        System.out.println("resultMsg : " + resultMsg);
                        throw new RuntimeException();
                    }
                }
            }

            int createdResult = reviewMapper.createReview(review);
            System.out.println("createdResult : " + createdResult);

            if (review.isPhotoExist()) {
                // Add photos.
                Photo photo = new Photo();
                photo.setReviewId(reviewId);
                for (String photoId : photos) {
                    photo.setPhotoId(photoId);
                    uploadPhoto(photo);
                }
                // Point for add photo.
                newPoint++;
            }

            // Point for add review.
            newPoint++;

            History history = new History();
            history.setUserId(userId);
            String reviewContent = "Added new review about : " + placeId + " reviewId : " + reviewId;
            if (newPoint == 1) {
                currentPoint += newPoint;
                reviewContent = reviewContent + " with text only. Point is " + currentPoint;
            } else if (newPoint == 2) {
                currentPoint += newPoint;
                reviewContent = reviewContent + " with photo. Point is " + currentPoint;
            }
            history.setContent(reviewContent);
            history.setPoint(currentPoint);
            int historyIdx = createHistory(history);
            System.out.println("historyIdx : " + historyIdx);

            int reviewCount = addedReviewCount(placeId);
            if (reviewCount == 1) {
                // Added first.
                currentPoint++;
                history.setPoint(currentPoint);
                history.setContent("First review about : " + placeId + " bonus. Point is " + currentPoint);
                createHistory(history);
            }
            userMapper.updateUserPoint(currentPoint, userId);

        } catch (Exception e) {
            e.printStackTrace();
            return resultMsg;
        }

        return "Success";
    }

    @Override
    public String updateReview(ReviewRequest reviewRequest) {
        String reviewId = reviewRequest.getReviewId();
        if (reviewId == null || "".equals(reviewId)) {
            return "Failed : reviewId is null or empty";
        }
        String userId = reviewRequest.getUserId();
        if (userId == null || "".equals(userId)) {
            return "Failed : userId is null or empty";
        }
        String placeId = reviewRequest.getPlaceId();
        if (placeId == null || "".equals(placeId)) {
            return "Failed : placeId is null or empty";
        }
        String content = reviewRequest.getContent();
        if (content == null || "".equals(content)) {
            return "Failed : Content is null or empty";
        }

        String resultMsg = "Unknown Error!";
        try {
            // Get my review.
            Review review = getMyReview(reviewId);
            if (review == null) {
                resultMsg = "Can't get my review.";
                System.out.println("resultMsg : " + resultMsg);
                throw new RuntimeException();
            }

            if (!userId.equals(review.getUserId())) {
                resultMsg = "Can't modify different user's review.";
                System.out.println("resultMsg : " + resultMsg);
                throw new RuntimeException();
            }
            System.out.println("my registered review : " + review);

            int newPoint = 0;
            String historyComment = "";

            List<String> attachedPhotos = reviewRequest.getAttachedPhotoIds();
            List<String> registeredPhotos = getPhotoList(reviewId);
            if (!review.isPhotoExist() && attachedPhotos != null && !attachedPhotos.isEmpty()) {
                // Register new photos.
                for (String photoId : attachedPhotos) {
                    Photo photo = new Photo();
                    photo.setReviewId(reviewId);
                    photo.setPhotoId(photoId);
                    uploadPhoto(photo);
                }
                historyComment = "Uploaded photo! Place : " + placeId + " reviewId : " + reviewId;
                review.setPhotoExist(true);
                newPoint++;
            } else if (review.isPhotoExist()) {
                // Photo is registered in db.
                if (attachedPhotos == null || attachedPhotos.isEmpty()) {
                    // Remove photos from db.
                    int deletePhotoResult = deletePhotosFromReview(reviewId);
                    System.out.println("deletePhotoResult : " + deletePhotoResult);
                    historyComment = "Delete photo from review! Place : " + placeId + " reviewId : " + reviewId;

                    review.setPhotoExist(false);
                    newPoint--;
                } else if (attachedPhotos.equals(registeredPhotos)) {
                    System.out.println("Photo don't need to change");
                } else {
                    // Compare photo list.
                    // First need to deep copy.
                    List<String> needToRemovePhotos = new ArrayList<>();
                    for (String photoId : registeredPhotos) {
                        needToRemovePhotos.add(photoId);
                    }
                    List<String> needToAddPhotos = new ArrayList<>();
                    for (String photoId : attachedPhotos) {
                        needToAddPhotos.add(photoId);
                    }
                    needToRemovePhotos.removeAll(attachedPhotos);
                    needToAddPhotos.removeAll(registeredPhotos);
                    System.out.println("needToRemovePhotos : " + needToRemovePhotos.toString());
                    System.out.println("needToAddPhotos : " + needToAddPhotos.toString());

                    // Delete photos from db.
                    for (String photoId : needToRemovePhotos) {
                        deletePhotoByPhotoId(photoId);
                    }

                    // Register new photos to DB.
                    for (String photoId : needToAddPhotos) {
                        Photo photo = new Photo();
                        photo.setReviewId(reviewId);
                        photo.setPhotoId(photoId);
                        uploadPhoto(photo);
                    }
                }
            }

            review.setContent(content);
            reviewMapper.updateReview(review);

            if (newPoint != 0) {
                System.out.println("newPoint : " + newPoint);
                History history = new History();
                history.setContent(historyComment);
                User user = getUser(userId);
                int totalPoint = user.getTotalPoint();
                System.out.println("totalPoint : " + totalPoint);
                totalPoint = totalPoint + newPoint;
                System.out.println("updatedPoint : " + totalPoint);
                history.setPoint(totalPoint);
                history.setUserId(userId);
                createHistory(history);
                userMapper.updateUserPoint(totalPoint, userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return resultMsg;
        }

        return "Success";
    }

    @Override
    public String deleteReview(ReviewRequest reviewRequest) {
        String reviewId = reviewRequest.getReviewId();
        if (reviewId == null || "".equals(reviewId)) {
            return "Failed : reviewId is null or empty";
        }
        String userId = reviewRequest.getUserId();
        if (userId == null || "".equals(userId)) {
            return "Failed : userId is null or empty";
        }
        String placeId = reviewRequest.getPlaceId();
        if (placeId == null || "".equals(placeId)) {
            return "Failed : placeId is null or empty";
        }

        String resultMsg = "Unknown Error!";

        try {
            int minusPoint = 0;

            // Get my review.
            Review review = getMyReview(reviewId);
            if (review == null) {
                resultMsg = "Can't get my review";
                System.out.println("resultMsg : " + resultMsg);
                throw new RuntimeException();
            }

            // Remove registed photos.
            if (review.isPhotoExist()) {
                int deletePhotoResult = deletePhotosFromReview(reviewId);
                System.out.println("deletePhotoResult : " + deletePhotoResult);
                minusPoint++;
            }

            // Check registered review is first review.
            String getFirstReviewId = getFirstReview(placeId);
            if (reviewId.equals(getFirstReviewId)) {
                minusPoint++;
            }

            // Delete review.
            int deleteReviewResult = reviewMapper.deleteReview(reviewId);
            System.out.println("deleteReviewResult : " + deleteReviewResult);

            int count = isReviewRegistered(reviewId);
            if (count != 0) {
                resultMsg = "Review was not deleted";
                System.out.println("resultMsg : " + resultMsg);
                throw new RuntimeException();
            }
            minusPoint++;
            User user = getUser(userId);
            if (user == null) {
                resultMsg = "Can not find proper user. userId : " + userId;
                System.out.println("resultMsg : " + resultMsg);
                throw new RuntimeException();
            }

            // Calculation point.
            int totalPoint = user.getTotalPoint();
            totalPoint = totalPoint - minusPoint;
            user.setTotalPoint(totalPoint);
            userMapper.updateUserPoint(totalPoint, userId);

            // Create point history.
            String hitoryContent = "Deleted review : " + reviewId + " Point is " + totalPoint;
            History history = new History();
            history.setContent(hitoryContent);
            history.setPoint(totalPoint);
            history.setUserId(userId);
            createHistory(history);
        } catch (Exception e) {
            e.printStackTrace();
            return resultMsg;
        }

        return "Success";
    }

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public User getUser(String userId) {
        return userMapper.getUser(userId);
    }

    @Override
    public int findUserPoint(String userId) {
        return userMapper.findUserPoint(userId);
    }

    @Override
    public boolean uploadPhoto(Photo photo) {
        return reviewMapper.uploadPhoto(photo);
    }

    @Override
    public int deletePhotosFromReview(String reviewId) {
        return reviewMapper.deletePhotosFromReview(reviewId);
    }

    @Override
    public int isReviewRegistered(String reviewId) {
        return reviewMapper.isReviewRegistered(reviewId);
    }

    @Override
    public int isPhotoExist(String photoId) {
        return reviewMapper.isPhotoExist(photoId);
    }

    @Override
    public int createHistory(History history) {
        return reviewMapper.createHistory(history);
    }

    @Override
    public int addedReviewCount(String placeId) {
        return reviewMapper.addedReviewCount(placeId);
    }

    @Override
    public String getFirstReview(String placeId) {
        return reviewMapper.getFirstReview(placeId);
    }

    @Override
    public Review getMyReview(String reviewId) {
        return reviewMapper.getMyReview(reviewId);
    }

    @Override
    public List<String> getPhotoList(String reviewId) {
        return reviewMapper.getPhotoList(reviewId);
    }

    @Override
    public int deletePhotoByPhotoId(String photoId) {
        return reviewMapper.deletePhotoByPhotoId(photoId);
    }

    @Override
    public List<History> getHistory(String userId) {
        return reviewMapper.getHistory(userId);
    }

}
