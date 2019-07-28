package com.xiao.community.mapper;

import com.xiao.community.domain.Question;
import com.xiao.community.domain.QuestionExample;
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
}