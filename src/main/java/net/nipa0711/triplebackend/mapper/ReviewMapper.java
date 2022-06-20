package net.nipa0711.triplebackend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import net.nipa0711.triplebackend.model.History;
import net.nipa0711.triplebackend.model.Photo;
import net.nipa0711.triplebackend.model.Review;

@Mapper
public interface ReviewMapper {
    @Insert("INSERT INTO photos (photoId, reviewId) VALUES (#{photoId}, #{reviewId})")
    boolean uploadPhoto(final Photo photo);

    @Insert("INSERT INTO reviews (reviewId, content, userId, placeId, isPhotoExist) VALUES(#{reviewId},#{content},#{userId},#{placeId},#{isPhotoExist})")
    int createReview(final Review review);

    @Update("UPDATE reviews SET content = #{content}, isPhotoExist = #{isPhotoExist} WHERE reviewId = #{reviewId}")
    int updateReview(final Review review);

    @Delete("DELETE FROM reviews WHERE reviewId = #{reviewId}")
    int deleteReview(String reviewId);

    @Select("SELECT * FROM reviews WHERE reviewId = #{reviewId}")
    Review getMyReview(String reviewId);

    @Select("SELECT COUNT(*) FROM reviews WHERE reviewId = #{reviewId}")
    int isReviewRegistered(@Param("reviewId")String reviewId);

    @Select("SELECT COUNT(*) FROM reviews WHERE placeId = #{placeId}")
    int addedReviewCount(@Param("placeId")String placeId);

    @Select("SELECT COUNT(*) FROM photos WHERE photoId = #{photoId}")
    int isPhotoExist(@Param("photoId")String photoId);

    @Select("SELECT photoId FROM photos WHERE reviewId = #{reviewId} ORDER BY registerTime ASC")
    List<String> getPhotoList(@Param("reviewId")String reviewId);

    @Insert("INSERT INTO histories (userId, point, content) VALUES (#{userId}, #{point}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    int createHistory(final History history);

    @Delete("DELETE FROM photos WHERE reviewId = #{reviewId}")
    int deletePhotosFromReview(String reviewId);

    @Delete("DELETE FROM photos WHERE photoId = #{photoId}")
    int deletePhotoByPhotoId(String photoId);

    @Select("SELECT reviewId FROM reviews WHERE placeId = #{placeId} ORDER BY createdTime ASC LIMIT 1")
    String getFirstReview(String placeId);

    @Select("SELECT * FROM histories WHERE userId = #{userId} ORDER BY time ASC")
    List<History> getHistory(String userId);
}