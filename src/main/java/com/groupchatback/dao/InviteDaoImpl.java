package com.groupchatback.dao;

import com.groupchatback.dao.interfaces.InviteDao;
import com.groupchatback.entity.Invite;
import com.groupchatback.util.DaoUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import java.util.List;

@Repository
public class InviteDaoImpl extends DaoUtil implements InviteDao {

    @Override
    public Invite findInvite(EntityManager em, String inviteId) throws BadRequestException {
        Invite invite = em.find(Invite.class, inviteId);

        if (invite == null) {
            throw new BadRequestException("invite " + inviteId + " does not exist");
        }

        return invite;
    }

    @Override
    public List<Object[]> getInvites(String userId, boolean getReceivedInvites) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String getReceivedInvitesQuery = "SELECT DISTINCT invite.id, conversation.id, conversation.name, sender.id, sender.username, sender.firstname, sender.lastname, sender.isonline, sender.hasprofilepic, invite.datesent FROM Invite invite " +
                "INNER JOIN invite.conversation conversation " +
                "INNER JOIN invite.sender sender " +
                "INNER JOIN invite.recipient recipient " +
                "WHERE recipient.id = ?1";

        String getSentInvitesQuery = "SELECT DISTINCT invite.id, conversation.id, conversation.name, recipient.id, recipient.username, recipient.firstname, recipient.lastname, recipient.isonline, recipient.hasprofilepic, invite.datesent FROM Invite invite " +
                "INNER JOIN invite.conversation conversation " +
                "INNER JOIN invite.sender sender " +
                "INNER JOIN invite.recipient recipient " +
                "WHERE sender.id = ?1";

        List<?> records = em.createQuery(getReceivedInvites ? getReceivedInvitesQuery : getSentInvitesQuery)
                .setParameter(1, userId)
                .getResultList();

        return getCastedList(records);
    }

    @Override
    public void deleteInvite(String inviteId) throws BadRequestException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Invite invite = this.findInvite(em, inviteId);

        em.remove(invite);
        em.getTransaction().commit();
    }

    @Override
    public long getReceivedInvitesCount(String userId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT COUNT(*) FROM Invite invite " +
                "INNER JOIN invite.recipient recipient " +
                "WHERE recipient.id = ?1";

        Object result = em.createQuery(query)
                .setParameter(1, userId)
                .getSingleResult();

        return (long)result;
    }
}


