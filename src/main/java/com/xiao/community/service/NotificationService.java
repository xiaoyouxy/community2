package com.xiao.community.service;

import com.xiao.community.advice.CustomizeErrorCode;
import com.xiao.community.advice.CustomizeException;
import com.xiao.community.domain.Notification;
import com.xiao.community.domain.NotificationExample;
import com.xiao.community.domain.User;
import com.xiao.community.dto.NotificationDTO;
import com.xiao.community.dto.PaginationDTO;
import com.xiao.community.enums.NotificationEnum;
import com.xiao.community.enums.NotificationStatusEnum;
import com.xiao.community.mapper.NotificationMapper;
import com.xiao.community.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO find(Long userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>(); //分页对象

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount =(int) notificationMapper.countByExample(notificationExample);
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

        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc"); //按创建时间倒序排序
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        if (notifications.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }


    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FATL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus()); //设为已读标记 1
        notificationMapper.updateByPrimaryKey(notification); //修改数据库

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));

        return notificationDTO;
    }
}
