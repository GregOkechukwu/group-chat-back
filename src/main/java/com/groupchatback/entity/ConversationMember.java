package com.groupchatback.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ConversationMember")
public class ConversationMember {

    private String id;
    private User member;
    private Conversation conversation;
    private Date datejoined;
    private boolean inchat;
    private boolean ishost;

    public ConversationMember() {}

    public ConversationMember(User member, Conversation conversation, Date datejoined, boolean inchat, boolean ishost) {
        setMember(member);
        setConversation(conversation);
        setDatejoined(datejoined);
        setInchat(inchat);
        setIshost(ishost);
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

    @ManyToOne(targetEntity = com.groupchatback.entity.User.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "member")
    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    @ManyToOne(targetEntity = com.groupchatback.entity.Conversation.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "conversation")
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Column(name = "datejoined", unique = false, nullable = false)
    public Date getDatejoined() {
        return datejoined;
    }

    public void setDatejoined(Date dateJoined) {
        this.datejoined = dateJoined;
    }

    @Column(name = "inchat", unique = false, nullable = false)
    public boolean isInchat() {
        return inchat;
    }

    public void setInchat(boolean inchat) {
        this.inchat = inchat;
    }

    @Column(name = "ishost", unique = false, nullable = false)
    public boolean isIshost() {
        return ishost;
    }

    public void setIshost(boolean ishost) {
        this.ishost = ishost;
    }
}
