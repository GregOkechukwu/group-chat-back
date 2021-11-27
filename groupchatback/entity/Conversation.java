package com.groupchatback.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Conversation")
public class Conversation {

    private String id;
    private String name;
    private Date datecreated;
    private User host;
    private List<Invite> invites;
    private List<ConversationMember> members;
    private List<Message> messages;

    public Conversation() {
    }

    public Conversation(String name, User host, Date datecreated) {
        setName(name);
        setHost(host);
        setDatecreated(datecreated);
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

    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "datecreated")
    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    @ManyToOne(targetEntity = com.groupchatback.entity.User.class)
    @JoinColumn(name = "host")
    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.Invite.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "conversation")
    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.ConversationMember.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "conversation")
    public List<ConversationMember> getMembers() {
        return members;
    }

    public void setMembers(List<ConversationMember> members) {
        this.members = members;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.Message.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "conversation")
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
