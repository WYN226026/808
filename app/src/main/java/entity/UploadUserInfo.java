package entity;

public class UploadUserInfo {   // 用户治疗信息 痕迹表的实体类。 表中的字段时这两个 ： userID  和userInfoID
    String userID;
    String userInfoID;

    public UploadUserInfo(String userID, String userInfoID) {
        this.userID = userID;
        this.userInfoID = userInfoID;
    }

    public UploadUserInfo() {
    }

    @Override
    public String toString() {
        return "UploadUserInfo{" +
                "userID='" + userID + '\'' +
                ", userInfoID='" + userInfoID + '\'' +
                '}';
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserInfoID() {
        return userInfoID;
    }

    public void setUserInfoID(String userInfoID) {
        this.userInfoID = userInfoID;
    }
}
