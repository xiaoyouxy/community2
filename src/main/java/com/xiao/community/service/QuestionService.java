package com.xiao.community.service;

import com.xiao.community.domain.Question;
import com.xiao.community.domain.User;
import com.xiao.community.dto.PaginationDTO;
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

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    public PaginationDTO findAll(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO(); //分页对象
        Integer totalCount = questionMapper.count();

        Integer totalPage; //总页数

        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        //判断页面是否越界
        if(page < 1) {
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        Integer offset =size * (page - 1);
        List<QuestionDTO> dtos = new ArrayList<>(); //存放QuestionDTO的集合
        List<Question> questions = questionMapper.findAll(offset,size);

        for(Question question : questions){
            User user = userMapper.findUserByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //快速把question赋值给questionDTO
            questionDTO.setUser(user);
            dtos.add(questionDTO);
        }
        paginationDTO.setQuestions(dtos);
        return paginationDTO;
    }

    public PaginationDTO findAllById(Integer UserId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO(); //分页对象
        Integer totalCount = questionMapper.countById(UserId);

        Integer totalPage;

        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        //判断页面是否越界
        if(page < 1) {
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        Integer offset =size * (page - 1);
        List<QuestionDTO> dtos = new ArrayList<>(); //存放QuestionDTO的集合
        List<Question> questions = questionMapper.findAllById(UserId, offset, size);

        for(Question question : questions){
            User user = userMapper.findUserByID(question.getCreator()); //根据用户id找到发布问题的用户
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //快速把question赋值给questionDTO
            questionDTO.setUser(user);
            dtos.add(questionDTO);
        }
        paginationDTO.setQuestions(dtos);
        return paginationDTO;
    }

    public QuestionDTO findById(Integer id){
        QuestionDTO questionDTO =new QuestionDTO();
        Question question = questionMapper.findById(id);
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findUserByID(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
