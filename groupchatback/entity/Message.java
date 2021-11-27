package com.groupchatback.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Message")
public class Message {

    private String id;
    private String content;
    private User sender;
    private Conversation conversation;
    private Date datesent;

    public Message() {
    }

    public Message(User sender, Conversation conversation, String content, Date datesent) {
       setSender(sender);
       setConversation(conversation);
       setContent(content);
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

    @ManyToOne(targetEntity = com.groupchatback.entity.User.class)
    @JoinColumn(name = "sender")
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @ManyToOne(targetEntity = com.groupchatback.entity.Conversation.class)
    @JoinColumn(name = "conversation")
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "datesent")
    public Date getDatesent() {
        return datesent;
    }

    public void setDatesent(Date datesent) {
        this.datesent = datesent;
    }
}
