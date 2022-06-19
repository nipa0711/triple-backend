package net.nipa0711.triplebackend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import net.nipa0711.triplebackend.model.User;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    List<User> findAll();

    @Insert("INSERT INTO users(userId) VALUES(#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(final User user);    

    @Update("UPDATE users SET totalPoint = #{totalPoint} WHERE userId = #{userId}")
    void updateUserPoint(@Param("totalPoint") int totalPoint, @Param("userId") String userId);

    @Select("SELECT * FROM users WHERE userId = #{userId}")
    User getUser(@Param("userId") String userId);

    @Select("SELECT totalPoint FROM users WHERE userId = #{userId}")
    int findUserPoint(@Param("userId") String userId);
}