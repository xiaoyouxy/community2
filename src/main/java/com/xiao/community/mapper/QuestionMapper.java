package com.xiao.community.mapper;

import com.xiao.community.domain.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    /**
     * 保存一个问题
     * @param question
     */
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    /**
     * 查询问题
     * @param offset 开始下标
     * @param size 一次查多少个
     * @return 问题集合
     */
    @Select("select * from question limit #{offset},#{size}")
    List<Question> findAll(@Param("offset") Integer offset, @Param("size") Integer size);

    /**
     * 查询问题总数
     * @return 问题总数
     */
    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> findAllById(@Param("userId") Integer userId,@Param("offset") Integer offset, @Param("size") Integer size);

    /**
     * 查询一个用户发布问题的数量
     * @param userId 用户id
     * @return 问题数量
     */
    @Select("select count(1) from question where creator=#{userId}")
    Integer countById(@Param("userId") Integer userId);

    /**
     * 根据问题的id查询相应的问题
     * @param id2 问题的id
     * @return
     */
    @Select("select * from question where id = #{id2}")
    Question findById(@Param("id2") Integer id2);

    /**
     * 更新
     * @param question
     */
    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag} where id =#{id}")
    void update(Question question);
}
