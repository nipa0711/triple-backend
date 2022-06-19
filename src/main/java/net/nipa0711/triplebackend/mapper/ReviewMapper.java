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
    @Insert("INSERT INTO photos (photoId, userId, placeId) VALUES (#{photoId}, #{userId}, #{placeId})")
    boolean uploadPhoto(final Photo photo);

    @Insert("INSERT INTO reviews (userId, reviewId, content, placeId, isPhotoExist) VALUES(#{userId},#{reviewId},#{content},#{placeId},#{isPhotoExist})")
    @Options(useGeneratedKeys = true, keyProperty = "reviewIdx")
    int createReview(final Review review);

    @Update("UPDATE reviews SET content = #{content}, isPhotoExist = #{isPhotoExist} WHERE userId = #{userId} AND placeId = #{placeId}")
    int updateReview(final Review review);

    @Delete("DELETE FROM reviews WHERE reviewIdx = #{reviewIdx}")
    int deleteReview(int reviewIdx);

    @Select("SELECT * FROM reviews WHERE reviewIdx = #{reviewIdx}")
    Review getMyReview(int reviewIdx);

    @Select("SELECT COUNT(*) FROM reviews WHERE userId = #{userId} AND placeId = #{placeId}")
    int isReviewRegistered(@Param("userId")String userId, @Param("placeId")String placeId);

    @Select("SELECT COUNT(*) FROM reviews WHERE placeId = #{placeId}")
    int addedReviewCount(@Param("placeId")String placeId);

    @Select("SELECT COUNT(*) FROM photos WHERE photoId = #{photoId}")
    int isPhotoExist(@Param("photoId")String photoId);

    @Select("SELECT photoId FROM photos WHERE userId = #{userId} AND placeId = #{placeId} ORDER BY registerTime ASC")
    List<String> getPhotoList(@Param("userId")String userId, @Param("placeId")String placeId);

    @Insert("INSERT INTO histories (userId, point, content) VALUES (#{userId}, #{point}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    int createHistory(final History history);

    @Select("SELECT reviewIdx FROM reviews WHERE userId = #{userId} AND placeId = #{placeId}")
    int findMyReviewIndex(String userId, String placeId);

    @Delete("DELETE FROM photos WHERE userId = #{userId} AND placeId = #{placeId}")
    int deletePhoto(String userId, String placeId);

    @Delete("DELETE FROM photos WHERE photoId = #{photoId}")
    int deletePhotoByPhotoId(String photoId);

    @Select("SELECT reviewIdx FROM reviews WHERE placeId = #{placeId} ORDER BY createdTime ASC LIMIT 1")
    int getFirstReview(String placeId);

    @Select("SELECT * FROM histories WHERE userId = #{userId} ORDER BY time ASC")
    List<History> getHistory(String userId);
}