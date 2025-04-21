package entity;

public class UserInfo {     //  用户治疗信息实体类
    int userID;
    int userInfoID;
    String work_mode;
    String p12_mode;
    String p34_mode;
    String rf12_mode;
    String rf34_mode;
    String mode_f1;
    String mode_f2;
    String mode_f3;
    String mode_options;
    String work_duration;
    String work_date;
    String body_part;
    String body_part_;
    String machine;
    String pan;
    String R_forehead_current ;
    String L_forehead_current;

    public String getWork_duration() {
        return work_duration;
    }

    public void setWork_duration(String work_duration) {
        this.work_duration = work_duration;
    }

    public String getBody_part_() {
        return body_part_;
    }

    public void setBody_part_(String body_part_) {
        this.body_part_ = body_part_;
    }

    public String getR_forehead_current() {
        return R_forehead_current;
    }

    public void setR_forehead_current(String r_forehead_current) {
        R_forehead_current = r_forehead_current;
    }

    public String getL_forehead_current() {
        return L_forehead_current;
    }

    public void setL_forehead_current(String l_forehead_current) {
        L_forehead_current = l_forehead_current;
    }

    public UserInfo(int userID, int userInfoID, String work_mode, String p12_mode, String p34_mode, String rf12_mode, String rf34_mode, String mode_f1, String mode_f2, String mode_f3, String mode_options, String mode_duration, String work_date, String body_part, String machine, String pan) {
        this.userID = userID;
        this.userInfoID = userInfoID;
        this.work_mode = work_mode;
        this.p12_mode = p12_mode;
        this.p34_mode = p34_mode;
        this.rf12_mode = rf12_mode;
        this.rf34_mode = rf34_mode;
        this.mode_f1 = mode_f1;
        this.mode_f2 = mode_f2;
        this.mode_f3 = mode_f3;
        this.mode_options = mode_options;
        this.work_duration = mode_duration;
        this.work_date = work_date;
        this.body_part = body_part;
        this.machine = machine;
        this.pan = pan;
    }

    public UserInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userID=" + userID +
                ", userInfoID=" + userInfoID +
                ", work_mode='" + work_mode + '\'' +
                ", p12_mode='" + p12_mode + '\'' +
                ", p34_mode='" + p34_mode + '\'' +
                ", rf12_mode='" + rf12_mode + '\'' +
                ", rf34_mode='" + rf34_mode + '\'' +
                ", mode_f1='" + mode_f1 + '\'' +
                ", mode_f2='" + mode_f2 + '\'' +
                ", mode_f3='" + mode_f3 + '\'' +
                ", mode_options='" + mode_options + '\'' +
                ", mode_duration='" + work_duration + '\'' +
                ", work_date='" + work_date + '\'' +
                ", body_part='" + body_part + '\'' +
                ", machine='" + machine + '\'' +
                '}';
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserInfoID() {
        return userInfoID;
    }

    public void setUserInfoID(int userInfoID) {
        this.userInfoID = userInfoID;
    }

    public String getWork_mode() {
        return work_mode;
    }

    public void setWork_mode(String work_mode) {
        this.work_mode = work_mode;
    }

    public String getP12_mode() {
        return p12_mode;
    }

    public void setP12_mode(String p12_mode) {
        this.p12_mode = p12_mode;
    }

    public String getP34_mode() {
        return p34_mode;
    }

    public void setP34_mode(String p34_mode) {
        this.p34_mode = p34_mode;
    }

    public String getRf12_mode() {
        return rf12_mode;
    }

    public void setRf12_mode(String rf12_mode) {
        this.rf12_mode = rf12_mode;
    }

    public String getRf34_mode() {
        return rf34_mode;
    }

    public void setRf34_mode(String rf34_mode) {
        this.rf34_mode = rf34_mode;
    }

    public String getMode_f1() {
        return mode_f1;
    }

    public void setMode_f1(String mode_f1) {
        this.mode_f1 = mode_f1;
    }

    public String getMode_f2() {
        return mode_f2;
    }

    public void setMode_f2(String mode_f2) {
        this.mode_f2 = mode_f2;
    }

    public String getMode_f3() {
        return mode_f3;
    }

    public void setMode_f3(String mode_f3) {
        this.mode_f3 = mode_f3;
    }

    public String getMode_options() {
        return mode_options;
    }

    public void setMode_options(String mode_options) {
        this.mode_options = mode_options;
    }

    public String getMode_duration() {
        return work_duration;
    }

    public void setMode_duration(String mode_duration) {
        this.work_duration = mode_duration;
    }

    public String getWork_date() {
        return work_date;
    }

    public void setWork_date(String work_date) {
        this.work_date = work_date;
    }

    public String getBody_part() {
        return body_part;
    }

    public void setBody_part(String body_part) {
        this.body_part = body_part;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }
    public void setPan (String pan){
        this.pan = pan;
    }
    public String getPan(){
        return pan;
    }
}
