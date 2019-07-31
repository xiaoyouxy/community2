package com.xiao.community.mapper;

import com.xiao.community.domain.Question;
import com.xiao.community.domain.QuestionExample;
import com.xiao.community.dto.QuestionQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {

    /**
     * 增加阅读数
     * @param record
     * @return
     */
    int incView(Question record);

    /**
     * 增加回复数
     * @param record
     * @return
     */
    int incCommentCount(Question record);

    List<Question> selectRelated(Question record);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}