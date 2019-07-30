package com.xiao.community.service;

import com.xiao.community.advice.CustomizeErrorCode;
import com.xiao.community.advice.CustomizeException;
import com.xiao.community.domain.Question;
import com.xiao.community.domain.QuestionExample;
import com.xiao.community.domain.User;
import com.xiao.community.dto.PaginationDTO;
import com.xiao.community.dto.QuestionDTO;
import com.xiao.community.mapper.QuestionExtMapper;
import com.xiao.community.mapper.QuestionMapper;
import com.xiao.community.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebRequestDataBinder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询所有问题
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO findAll(Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO(); //分页对象
        Integer totalCount =(int) questionMapper.countByExample(new QuestionExample());

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


        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc"); //按时间倒序排列

        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));

        for(Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //快速把question赋值给questionDTO
            questionDTO.setUser(user);
            dtos.add(questionDTO);
        }
        paginationDTO.setData(dtos);
        return paginationDTO;
    }

    /**
     * 查询账户发布的所有问题
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO findAllById(Long userId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO(); //分页对象

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount =(int) questionMapper.countByExample(questionExample);
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

        QuestionExample example =  new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        List<QuestionDTO> dtos = new ArrayList<>(); //存放QuestionDTO的集合
        for(Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator()); //根据用户id找到发布问题的用户
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //快速把question赋值给questionDTOid
            questionDTO.setUser(user);
            dtos.add(questionDTO);
        }
        paginationDTO.setData(dtos);
        return paginationDTO;
    }

    /**
     * 查询一个问题的全部信息
     * @param id 问题
     * @return
     */
    public QuestionDTO findById(Long id){
        QuestionDTO questionDTO =new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        if (null == question){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 保存或者更新 问题
     * @param question
     */
    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (i !=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    /**
     * 增加阅读数
     * @param id
     */
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        //得到该问题的标签，并差分
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        //根据问题标签查出所有具有这些标签的问题
        List<Question> questions = questionExtMapper.selectRelated(question);
        //把question转化为questiDTO
        List<QuestionDTO> questionDTOS = questions.stream().map(p -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(p,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
