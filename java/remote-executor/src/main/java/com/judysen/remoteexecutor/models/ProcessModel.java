package com.judysen.remoteexecutor.models;

public class ProcessModel {
    private String body;
    private String recipient;
    private String sender;
    private String subject;
    private String isHtmlBody;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIsHtmlBody() {
        return isHtmlBody;
    }

    public void setIsHtmlBody(String isHtmlBody) {
        this.isHtmlBody = isHtmlBody;
    }
}
