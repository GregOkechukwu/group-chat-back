package com.groupchatback.service;

import com.groupchatback.config.LocalMetaData;
import com.groupchatback.dao.interfaces.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private LocalMetaData localBean;

    public String createMessage(String conversationId, String content, Date dateSent) {
        String decodedUserId = this.localBean.getDecodedUserId();
       return this.messageDao.createMessage(decodedUserId, conversationId, content, dateSent);
    }

    public List<Object[]> getUserMessages(String conversationId) {
        return this.messageDao.getUserMessages(conversationId);
    }
}
