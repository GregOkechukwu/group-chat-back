package com.groupchatback.dao;

import com.groupchatback.dao.interfaces.UserDao;
import com.groupchatback.entity.UserFriend;
import com.groupchatback.entity.User;
import com.groupchatback.util.DaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl extends DaoUtil implements UserDao {

    private User createUser(String userId, String username, String firstName, String lastName, String email, Date dateCreated) {
        User user = new User();

        user.setId(userId);
        user.setUsername(username);
        user.setFirstname(firstName != null ? firstName : null);
        user.setLastname(lastName != null ? lastName : null);
        user.setEmail(email != null ? email : null);
        user.setDatecreated(dateCreated != null ? dateCreated : null);

        return user;
    }

    @Override
    public User findUser(EntityManager em, String userId) throws BadRequestException {
        User user = em.find(User.class, userId);

        if (user == null) {
            throw new BadRequestException("user " + userId + " does not exist");
        }

        return user;
    }

    @Override
    public UserFriend findUserFriend(EntityManager em, String userFriendId) throws BadRequestException {
        UserFriend userFriend = em.find(UserFriend.class, userFriendId);

        if (userFriend == null) {
            throw new BadRequestException("user friend record " + userFriendId + " does not exist");
        }

        return userFriend;
    }

    @Override
    public Object[] getUser(String userId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT user.id, user.username, user.firstname, user.lastname FROM User user " +
                "WHERE user.id = ?1 ";

        Object result = em.createQuery(query)
                .setParameter(1 , userId)
                .getSingleResult();

        return (Object[])result;
    }

    @Override
    public Object[] getFullName(String userId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT DISTINCT user.firstname, user.lastname FROM User user " +
                "WHERE user.id = ?1";

        Object result = em.createQuery(query)
                .setParameter(1 , userId)
                .getSingleResult();

        return (Object[])result;
    }

    @Override
    public List<Object[]> getAllUsersIncludingFriends(String userId, String usernameSubstr, String firstNameSubstr, String lastNameSubstr) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        boolean hasWhereClause = usernameSubstr != null || firstNameSubstr != null || lastNameSubstr != null;

        String query = "SELECT DISTINCT user.id, user.username, user.firstname, user.lastname, user.isonline, user.hasprofilepic " +
                "FROM User user " +
                "WHERE user.id != ?1 ";

        if (hasWhereClause) {
            String whereClause = usernameSubstr != null ? "user.username LIKE ?2" : firstNameSubstr != null ? "user.firstname LIKE ?2" : "user.lastname LIKE ?2";
            query += "AND " + whereClause;
        }

        Query queryObj = em.createQuery(query).setParameter(1 , userId);

        if (hasWhereClause) {
            String attribute = usernameSubstr != null ? usernameSubstr : firstNameSubstr != null ? firstNameSubstr : lastNameSubstr;
            queryObj.setParameter(2, attribute + "%");
        }

        List<?> records = queryObj.getResultList();
        return getCastedList(records);
    }

    /**
     *  Get all users except:
     *      the current user's friends
     *      users whom the current user has sent a friend request to
     *      users whom the current user has received a friend request from
     * **/
    @Override
    public List<Object[]> getAllUsersExcludingFriends(String userId, String usernameSubstr, String firstNameSubstr, String lastNameSubstr) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        boolean hasWhereClause = usernameSubstr != null || firstNameSubstr != null || lastNameSubstr != null;

        String subQueryA = "(" +
                    "SELECT friend.id FROM UserFriend user_friend " +
                    "INNER JOIN user_friend.friend friend " +
                    "INNER JOIN user_friend.user u " +
                    "WHERE u.id = ?1 " +
                    "AND friend.id = user.id" +
                ") ";

        String subQueryB = "(" +
                    "SELECT recipient.id FROM FriendRequest friend_request " +
                    "INNER JOIN friend_request.sender sender " +
                    "INNER JOIN friend_request.recipient recipient " +
                    "WHERE sender.id = ?1 " +
                    "AND recipient.id = user.id" +
                ") ";

        String subQueryC = "(" +
                    "SELECT sender.id FROM FriendRequest friend_request " +
                    "INNER JOIN friend_request.sender sender " +
                    "INNER JOIN friend_request.recipient recipient " +
                    "WHERE sender.id = user.id " +
                    "AND recipient.id = ?1 " +
                ") ";

        String query = "SELECT DISTINCT user.id, user.username, user.firstname, user.lastname, user.isonline, user.hasprofilepic FROM User user " +
                "WHERE NOT EXISTS " + subQueryA +
                "AND NOT EXISTS " + subQueryB +
                "AND NOT EXISTS " + subQueryC +
                "AND user.id != ?1 ";

        if (hasWhereClause) {
            String whereClause = usernameSubstr != null ? "user.username LIKE ?2" : firstNameSubstr != null ? "user.firstname LIKE ?2" : "user.lastname LIKE ?2";
            query += "AND " + whereClause;
        }

        Query queryObj = em.createQuery(query).setParameter(1 , userId);

        if (hasWhereClause) {
            String attribute = usernameSubstr != null ? usernameSubstr : firstNameSubstr != null ? firstNameSubstr : lastNameSubstr;
            queryObj.setParameter(2, attribute + "%");
        }

        List<?> records = queryObj.getResultList();
        return getCastedList(records);
    }

    @Override
    public List<Object[]> getAllFriends(String userId, String usernameSubstr, String firstNameSubstr, String lastNameSubstr) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        boolean hasWhereClause = usernameSubstr != null || firstNameSubstr != null || lastNameSubstr != null;

        String query = "SELECT DISTINCT friend.id, friend.username, friend.firstname, friend.lastname, friend.isonline, friend.hasprofilepic, user_friend.dateadded " +
                "FROM UserFriend user_friend " +
                "INNER JOIN user_friend.friend friend " +
                "INNER JOIN user_friend.user user " +
                "WHERE user.id = ?1 ";

        if (hasWhereClause) {
            String whereClause = usernameSubstr != null ? "friend.username LIKE ?2" : firstNameSubstr != null ? "friend.firstname LIKE ?2" : "friend.lastname LIKE ?2";
            query += "AND " + whereClause;
        }

        Query queryObj = em.createQuery(query).setParameter(1 , userId);

        if (hasWhereClause) {
            String attribute = usernameSubstr != null ? usernameSubstr : firstNameSubstr != null ? firstNameSubstr : lastNameSubstr;
            queryObj.setParameter(2, attribute + "%");
        }

        List<?> records = queryObj.getResultList();
        return getCastedList(records);
    }

    @Override
    public List<Object[]> getUsersInConversation(String conversationId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT user.id, user.username, user.firstname, user.lastname, user.isonline, user.hasprofilepic, conversation_member.inchat, conversation_member.ishost " +
                "FROM ConversationMember conversation_member " +
                "INNER JOIN conversation_member.member user " +
                "INNER JOIN conversation_member.conversation conversation " +
                "WHERE conversation.id = ?1 " +
                "ORDER BY (case when conversation_member.ishost = true then 1 else 2 end)";

        List<?> records = em.createQuery(query).setParameter(1, conversationId).getResultList();
        return getCastedList(records);
    }

    @Override
    public void saveUser(String userId, String firstName, String lastName, String email, Date dateCreated) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        User user = createUser(userId, userId, firstName, lastName, email, dateCreated);
        user.setHasprofilepic(false);
        user.setIsonline(false);

        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public long getEmailCount(String email) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT COUNT(*) FROM User user WHERE user.email = ?1";
        Object result = em.createQuery(query)
                .setParameter(1, email)
                .getSingleResult();

        return (long)result;
    }

    @Override
    public long getUsernameCount(String username) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT COUNT(*) FROM User user WHERE user.username = ?1";
        Object result = em.createQuery(query)
                .setParameter(1, username)
                .getSingleResult();

        return (long)result;
    }

    @Override
    public void deleteUser(String userId) throws BadRequestException {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        em.remove(this.findUser(em, userId));
        em.getTransaction().commit();
    }

    @Override
    public boolean hasProfilePic(String userId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT user.hasprofilepic FROM User user WHERE user.id = ?1";
        Object result = em.createQuery(query)
                .setParameter(1, userId)
                .getSingleResult();

        return (boolean)result;
    }

    @Override
    public void updateUser(String userId, Map<String, Object> attributes) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        User user = em.find(User.class, userId);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String attribute = entry.getKey();
            Object value = entry.getValue();

            if (StringUtils.equals(attribute, "preferred_username")) {
                user.setUsername((String)value);
            }

            if (StringUtils.equals(attribute, "email")) {
                user.setEmail((String)value);
            }

            if (StringUtils.equals(attribute, "isOnline")) {
                user.setIsonline((boolean)value);
            }

            if (StringUtils.equals(attribute, "hasProfilePic")) {
                user.setHasprofilepic((boolean)value);
            }

            if (StringUtils.equals(attribute, "name")) {
                String[] fullName = ((String)value).split(" ");
                user.setFirstname(fullName[0]);
                user.setLastname(fullName[1]);
            }
        }

        em.getTransaction().commit();
    }

    @Override
    public void addFriend(String userId, String friendId, Date dateAdded) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        User user = this.findUser(em, userId);
        User newFriend = this.findUser(em, friendId);

        em.persist(new UserFriend(user, newFriend, dateAdded));
        em.persist(new UserFriend(newFriend, user, dateAdded));

        em.getTransaction().commit();
    }

    @Override
    public void removeFriend(String userId, String friendId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();

        String query = "SELECT userfriend.id FROM UserFriend user_friend " +
                "INNER JOIN user_friend.user user " +
                "INNER JOIN user_friend.friend friend " +
                "WHERE user.id = ?1 AND friend.id = ?2";

        String userFriendIdA = (String)em.createQuery(query).setParameter(1, userId).setParameter(2, friendId).getSingleResult();
        String userFriendIdB = (String)em.createQuery(query).setParameter(2, userId).setParameter(1, friendId).getSingleResult();

        em.getTransaction().begin();

        UserFriend userFriendA = this.findUserFriend(em, userFriendIdA);
        UserFriend userFriendB = this.findUserFriend(em, userFriendIdB);

        em.remove(userFriendA);
        em.remove(userFriendB);

        em.getTransaction().commit();
    }
}
