package ancsum.com.inclass06;

import java.io.Serializable;

public class UserDetails implements Serializable {
    private String token;
    private String userID;
    private String userEmail;
    private String userFname;
    private String userLname;

    public UserDetails(String token, String userID, String userEmail, String userFname, String userLname) {
        this.token = token;
        this.userID = userID;
        this.userEmail = userEmail;
        this.userFname = userFname;
        this.userLname = userLname;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    @Override
    public String toString() {
        return "UserDetails{" +
                "token='" + token + '\'' +
                ", userID='" + userID + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userFname='" + userFname + '\'' +
                ", userLname='" + userLname + '\'' +
                '}';
    }
}
