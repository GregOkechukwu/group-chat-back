package com.groupchatback.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "UserFriend")
public class UserFriend {

    private String id;
    private User user;
    private User friend;
    private Date dateadded;

    public UserFriend() {}

    public UserFriend(User user, User friend, Date dateadded) {
        setUser(user);
        setFriend(friend);
        setDateadded(dateadded);
    }

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(unique = true, nullable = false)
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = com.groupchatback.entity.User.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "myuser")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne(targetEntity = com.groupchatback.entity.User.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "friend")
    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    @Column(name = "dateadded")
    public Date getDateadded() {
        return dateadded;
    }

    public void setDateadded(Date dateadded) {
        this.dateadded = dateadded;
    }
}
