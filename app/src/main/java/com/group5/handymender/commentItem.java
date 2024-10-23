package com.group5.handymender;

public class commentItem {
    String name, LName, email, commentDescription, date, documentId ;

    public commentItem() {
    }

    public commentItem(String date, String commentDescription, String documentId, String email, String LName, String name) {
        this.date = date;
        this.commentDescription = commentDescription;
        this.documentId = documentId;
        this.email = email;
        this.LName = LName;
        this.name = name;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {this.commentDescription = commentDescription;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
