package com.example.tfg.poo;

public class Message {

    private String id;
    private String idSend;
    private String idRecive;
    private String idChat;
    private String message;
    private long time;

    public Message() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSend() {
        return idSend;
    }

    public void setIdSend(String idSend) {
        this.idSend = idSend;
    }

    public String getIdRecive() {
        return idRecive;
    }

    public void setIdRecive(String idRecive) {
        this.idRecive = idRecive;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Message(String id, String idSend, String idRecive, String idChat, String message, long time) {
        this.id = id;
        this.idSend = idSend;
        this.idRecive = idRecive;
        this.idChat = idChat;
        this.message = message;
        this.time = time;


    }
}
