package ancsum.com.inclass06;

import java.io.Serializable;

public class Threads implements Serializable{
    private String userFname;
    private String userLname;
    private String userID;
    private String threadID;
    private String title;
    private String createdAt;

    public Threads(String userFname, String userLname, String userID, String threadID, String title, String createdAt) {
        this.userFname = userFname;
        this.userLname = userLname;
        this.userID = userID;
        this.threadID = threadID;
        this.title = title;
        this.createdAt = createdAt;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getThreadID() {
        return threadID;
    }

    public void setThreadID(String threadID) {
        this.threadID = threadID;
    }

    @Override
    public String toString() {
        return "Threads{" +
                "userFname='" + userFname + '\'' +
                ", userLname='" + userLname + '\'' +
                ", userID='" + userID + '\'' +
                ", threadID='" + threadID + '\'' +
                ", title='" + title + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
