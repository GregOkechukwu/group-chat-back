package com.groupchatback.dao.interfaces;

import java.util.Date;
import java.util.List;

public interface MessageDao {
    String createMessage(String userId, String conversationId, String content, Date dateSent);
    List<Object[]> getUserMessages(String conversationId);
}
