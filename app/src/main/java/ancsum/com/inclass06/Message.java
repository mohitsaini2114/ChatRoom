package ancsum.com.inclass06;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {

    @SerializedName("user_fname")
    private String userFname;

    @SerializedName("user_lname")
    private String userLname;

    @SerializedName("user_id")
    private String userID;

    @SerializedName("id")
    private String messageID;

    @SerializedName("message")
    private String messageString;

    @SerializedName("created_at")
    private String createdAtTime;

    public Message(String userFname, String userLname, String userID, String messageID, String messageString, String createdAtTime) {
        this.userFname = userFname;
        this.userLname = userLname;
        this.userID = userID;
        this.messageID = messageID;
        this.messageString = messageString;
        this.createdAtTime = createdAtTime;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public String getCreatedAtTime() {
        return createdAtTime;
    }

    public void setCreatedAtTime(String createdAtTime) {
        this.createdAtTime = createdAtTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userFname='" + userFname + '\'' +
                ", userLname='" + userLname + '\'' +
                ", userID='" + userID + '\'' +
                ", messageID='" + messageID + '\'' +
                ", messageString='" + messageString + '\'' +
                ", createdAtTime='" + createdAtTime + '\'' +
                '}';
    }
}
