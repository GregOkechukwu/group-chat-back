package com.groupchatback.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Invite")
public class Invite {

    private String id;
    private Conversation conversation;
    private User sender;
    private User recipient;
    private Date datesent;

    public Invite() {}

    public Invite(Conversation conversation, User sender, User recipient, Date datesent) {
        setConversation(conversation);
        setSender(sender);
        setRecipient(recipient);
        setDatesent(datesent);
    }

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(nullable = false)
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = com.groupchatback.entity.Conversation.class)
    @JoinColumn(name = "conversation")
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
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
