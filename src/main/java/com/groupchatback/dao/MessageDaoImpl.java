package com.groupchatback.dao;

import com.groupchatback.dao.interfaces.ConversationDao;
import com.groupchatback.dao.interfaces.MessageDao;
import com.groupchatback.dao.interfaces.UserDao;
import com.groupchatback.entity.Conversation;
import com.groupchatback.entity.Message;
import com.groupchatback.entity.User;
import com.groupchatback.util.DaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Repository
public class MessageDaoImpl extends DaoUtil implements MessageDao {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ConversationDao conversationDao;

    @Override
    public String createMessage(String userId, String conversationId, String content, Date dateSent) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        User user = this.userDao.findUser(em, userId);
        Conversation conversation = this.conversationDao.findConversation(em, conversationId);
        Message message = new Message(user, conversation, content, dateSent);

        em.persist(message);
        em.getTransaction().commit();

        return message.getId();
    }

    @Override
    public List<Object[]> getUserMessages(String conversationId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT DISTINCT sender.id, sender.username, sender.firstname, sender.lastname, sender.isonline, sender.hasprofilepic, message.id, message.content, message.datesent " +
                "FROM Message message " +
                "INNER JOIN message.sender sender " +
                "INNER JOIN message.conversation conversation " +
                "WHERE conversation.id = ?1 " +
                "ORDER BY message.datesent ASC";

        List<?> records = em.createQuery(query).setParameter(1, conversationId).getResultList();
        return getCastedList(records);
    }
}
