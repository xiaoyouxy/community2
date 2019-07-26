package com.xiao.community.mapper;

import com.xiao.community.domain.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void creat(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> findAll(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> findUserById(@Param("userId") Integer userId,@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer countById(@Param("userId") Integer userId);
}
