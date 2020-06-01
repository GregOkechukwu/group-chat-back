package com.groupchatback.dao;

import com.groupchatback.dao.interfaces.ConversationDao;
import com.groupchatback.dao.interfaces.UserDao;
import com.groupchatback.entity.Conversation;
import com.groupchatback.entity.ConversationMember;
import com.groupchatback.entity.Invite;
import com.groupchatback.entity.User;
import com.groupchatback.util.DaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.ws.rs.BadRequestException;
import javax.persistence.EntityManager;
import java.util.*;

@Repository
public class ConversationDaoImpl extends DaoUtil implements ConversationDao {

    @Autowired
    private UserDao userDao;

    @Override
    public Conversation findConversation(EntityManager em, String conversationId) throws BadRequestException {
        Conversation conversation = em.find(Conversation.class, conversationId);

        if (conversation == null) {
            throw new BadRequestException("conversation " + conversationId + " does not exist");
        }

        return conversation;
    }

    @Override
    public ConversationMember findConversationMember(EntityManager em, String conversationMemberId) throws BadRequestException {
        ConversationMember conversationMember = em.find(ConversationMember.class, conversationMemberId);

        if (conversationMember == null) {
            throw new BadRequestException("conversation member " + conversationMemberId + " does not exist");
        }

        return conversationMember;
    }

    @Override
    public List<Object[]> getConversations(String userId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String subQuery = "(SELECT COUNT(*) FROM Conversation c LEFT JOIN c.members WHERE c.name = conversation.name GROUP BY c.id)";

        String query = "SELECT conversation.id, conversation.name, host.username, " + subQuery + " as member_count FROM Conversation conversation " +
                "INNER JOIN conversation.members conversation_member " +
                "INNER JOIN conversation_member.member user " +
                "INNER JOIN conversation.host host " +
                "WHERE user.id = ?1 " +
                "GROUP BY conversation.id, host.id";

         List<?> records = em.createQuery(query)
                .setParameter(1 , userId)
                .getResultList();

         return getCastedList(records);
    }
    @Override
    public List<Object[]> getUsersInConversationExceptHost(String conversationId, String hostId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT user.id, user.username, user.firstname, user.lastname, user.hasprofilepic, user.isonline FROM Conversation conversation " +
                "INNER JOIN conversation.members conversation_member " +
                "INNER JOIN conversation_member.member user " +
                "WHERE conversation.id = ?1 AND user.id != ?2";

        List<?> records =  em.createQuery(query)
                .setParameter(1, conversationId)
                .setParameter(2, hostId)
                .getResultList();

        return getCastedList(records);
    }

    @Override
    public Object[] getHostOfConversation(String conversationId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT host.id, host.username, host.firstname, host.lastname, host.hasprofilepic, host.isonline FROM Conversation conversation " +
                "INNER JOIN conversation.host host " +
                "WHERE conversation.id = ?1";

        Object result =  em.createQuery(query)
                .setParameter(1, conversationId)
                .getSingleResult();

        return (Object[])result;
    }

    @Override
    public Date getDateJoined(EntityManager em, String conversationId, String userId) {
        String query = "SELECT conversation_member.datejoined FROM ConversationMember conversation_member " +
                "INNER JOIN conversation_member.member user " +
                "INNER JOIN conversation_member.conversation conversation " +
                "WHERE user.id = ?1 AND conversation.id = ?2";

        Object result =  em.createQuery(query)
                .setParameter(1, userId)
                .setParameter(2, conversationId)
                .getSingleResult();

        return (Date)result;
    }

    @Override
    public void saveConversationAndInviteUsers(String hostId, String conversationName, String[] invitedUsers, Date dateCreated) throws BadRequestException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        User hostUser = this.userDao.findUser(em, hostId);
        Conversation conversation = new Conversation(conversationName, hostUser, dateCreated);

        List<Invite> invites = new ArrayList<>();
        int n = invitedUsers.length;

        for (int i = 0; i < n; i++) {
            User recipient = this.userDao.findUser(em, invitedUsers[i]);
            Invite invite = new Invite(conversation, hostUser, recipient, dateCreated);

            invites.add(invite);
            em.persist(invite);
        }

        ConversationMember hostConversationMember = new ConversationMember(hostUser, conversation, dateCreated, false, true);

        conversation.setInvites(invites);
        hostUser.getHostconversations().add(hostConversationMember);

        em.persist(hostConversationMember);
        em.persist(hostUser);
        em.getTransaction().commit();
    }

    @Override
    public long getConversationNameCount(String conversationName) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT COUNT(*) from Conversation conversation WHERE conversation.name = ?1";
        Object result = em.createQuery(query)
                .setParameter(1, conversationName)
                .getSingleResult();

        return (long)result;
    }

    @Override
    public void joinConversationAsNewMember(String userId, String conversationId, Date dateJoined) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        User user = this.userDao.findUser(em, userId);
        Conversation conversation = this.findConversation(em, conversationId);

        em.persist(new ConversationMember(user, conversation, dateJoined, false, false));
        em.getTransaction().commit();
    }

    @Override
    public void updateInChatStatus(String userId, String conversationId, boolean inChat) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT conversation_member.id FROM ConversationMember conversation_member " +
                "INNER JOIN conversation_member.member user " +
                "INNER JOIN conversation_member.conversation conversation " +
                "WHERE user.id = ?1 AND conversation.id = ?2";

        String conversationMemberId = (String)em.createQuery(query).setParameter(1, userId).setParameter(2, conversationId).getSingleResult();

        em.getTransaction().begin();
        ConversationMember conversationMember = this.findConversationMember(em, conversationMemberId);
        conversationMember.setInchat(inChat);

        em.persist(conversationMember);
        em.getTransaction().commit();
    }

    @Override
    public void leaveConversation(String userId, String conversationId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT conversation_member.id FROM ConversationMember conversation_member " +
                "INNER JOIN conversation_member.member user " +
                "INNER JOIN conversation_member.conversation conversation " +
                "WHERE user.id = ?1 AND conversation.id = ?2";

        String conversationMemberId = (String)em.createQuery(query).setParameter(1, userId).setParameter(2, conversationId).getSingleResult();

        em.getTransaction().begin();
        ConversationMember conversationMember = this.findConversationMember(em, conversationMemberId);

        em.remove(conversationMember);
        em.getTransaction().commit();
    }
}
