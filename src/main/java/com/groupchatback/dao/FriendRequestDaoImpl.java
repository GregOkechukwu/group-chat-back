package com.groupchatback.dao;

import com.groupchatback.dao.interfaces.FriendRequestDao;
import com.groupchatback.dao.interfaces.UserDao;
import com.groupchatback.entity.FriendRequest;
import com.groupchatback.entity.User;
import com.groupchatback.util.DaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;

@Repository
public class FriendRequestDaoImpl extends DaoUtil implements FriendRequestDao {

    @Autowired
    private UserDao userDao;

    @Override
    public FriendRequest findFriendRequest(EntityManager em, String friendRequestId) throws BadRequestException {
        FriendRequest friendRequest = em.find(FriendRequest.class, friendRequestId);

        if (friendRequest == null) {
            throw new BadRequestException("friend request " + friendRequestId + " does not exist");
        }

        return friendRequest;
    }

    @Override
    public void saveFriendRequests(String senderId, List<String> usersToAdd, Date dateSent) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        User sender = this.userDao.findUser(em, senderId);
        int n = usersToAdd.size();

        for (int i = 0; i < n; i++) {
            User recipient = this.userDao.findUser(em, usersToAdd.get(i));
            FriendRequest friendRequest = new FriendRequest(sender, recipient, dateSent);

            em.persist(friendRequest);
        }

        em.getTransaction().commit();
    }

    @Override
    public List<Object[]> getFriendRequests(String userId, boolean getReceivedRequests) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String getReceivedFriendRequestsQuery = "SELECT friendrequest.id, friendrequest.datesent, sender.id, sender.username, sender.firstname, sender.lastname, sender.isonline, sender.hasprofilepic FROM FriendRequest friendrequest " +
                "INNER JOIN friendrequest.sender sender " +
                "INNER JOIN friendrequest.recipient recipient " +
                "WHERE recipient.id = ?1";

        String getSentFriendRequestsQuery = "SELECT friendrequest.id, friendrequest.datesent, recipient.id, recipient.username, recipient.firstname, recipient.lastname, recipient.isonline, recipient.hasprofilepic FROM FriendRequest friendrequest " +
                "INNER JOIN friendrequest.sender sender " +
                "INNER JOIN friendrequest.recipient recipient " +
                "WHERE sender.id = ?1";

        List<?> records = em.createQuery(getReceivedRequests ? getReceivedFriendRequestsQuery : getSentFriendRequestsQuery)
                .setParameter(1, userId)
                .getResultList();

        return getCastedList(records);
    }

    @Override
    public void deleteFriendRequest(String friendRequestId) throws BadRequestException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        FriendRequest friendRequest = this.findFriendRequest(em, friendRequestId);

        em.remove(friendRequest);
        em.getTransaction().commit();
    }

}
