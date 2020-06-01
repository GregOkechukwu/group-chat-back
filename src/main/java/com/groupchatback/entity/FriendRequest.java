package com.groupchatback.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FriendRequest")
public class FriendRequest {

    private String id;
    private User sender;
    private User recipient;
    private Date datesent;

    public FriendRequest() {}

    public FriendRequest(User sender, User recipient, Date datesent) {
        setSender(sender);
        setRecipient(recipient);
        setDatesent(datesent);
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

    @ManyToOne(targetEntity = com.groupchatback.entity.User.class)
    @JoinColumn(name = "sender")
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @ManyToOne(targetEntity = com.groupchatback.entity.User.class)
    @JoinColumn(name = "recipient")
    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @Column(name = "datesent")
    public Date getDatesent() {
        return datesent;
    }

    public void setDatesent(Date datesent) {
        this.datesent = datesent;
    }
}
