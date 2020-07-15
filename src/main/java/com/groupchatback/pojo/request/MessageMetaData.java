package com.groupchatback.pojo.request;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessageMetaData {
    private String messageId;
    private String messageContent;
    private Date dateSent;

    public MessageMetaData(String messageId, String messageContent, Date dateSent) {
        setMessageId(messageId);
        setMessageContent(messageContent);
        setDateSent(dateSent);
    }

    @JsonProperty(value = "messageId")
    public String getMessageId() {
        return messageId;
    }

    @JsonProperty(value = "messageId")
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @JsonProperty(value = "messageContent")
    public String getMessageContent() {
        return messageContent;
    }

    @JsonProperty(value = "messageContent")
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @JsonProperty(value = "dateSent")
    public Date getDateSent() {
        return dateSent;
    }

    @JsonProperty(value = "dateSent")
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
