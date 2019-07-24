package com.xiao.community.service;

import com.xiao.community.domain.Question;
import com.xiao.community.domain.User;
import com.xiao.community.dto.QuestionDTO;
import com.xiao.community.mapper.QuestionMapper;
import com.xiao.community.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public List<QuestionDTO> findAll() {
        List<QuestionDTO> dtos = new ArrayList<>();
        List<Question> questions = questionMapper.findAll();
        for(Question question : questions){
            User user = userMapper.findUserByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //快速把question赋值给questionDTO
            questionDTO.setUser(user);
            dtos.add(questionDTO);
        }
        return dtos;
    }
}
