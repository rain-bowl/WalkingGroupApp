package ApplicationLogic;

import org.json.JSONArray;
import org.json.JSONObject;


//This class will store all information regarding the currently logged in user. It will be filled up on login
// or when a user creates a new account.
public class User {
    int ID;
    String emailAddress;
    String name;
    int birthyear;
    int birthmonth;
    String userAddress;
    String cellPhoneNumber;
    String homePhoneNumber;
    String grade;
    String teacherName;
    String emergencyContactInfoInstruction;
    JSONArray monitoredByUsers;
    JSONArray monitorsOtherUsers;
    JSONArray memberOfGroups;
    JSONArray leaderOfGroups;
    JSONObject lastGpsLocation;
    String userHref;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public int getBirthmonth() {
        return birthmonth;
    }

    public void setBirthmonth(int birthmonth) {
        this.birthmonth = birthmonth;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getEmergencyContactInfoInstruction() {
        return emergencyContactInfoInstruction;
    }

    public void setEmergencyContactInfoInstruction(String emergencyContactInfoInstruction) {
        this.emergencyContactInfoInstruction = emergencyContactInfoInstruction;
    }

    public JSONArray getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public void setMonitoredByUsers(JSONArray monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public JSONArray getMonitorsOtherUsers() {
        return monitorsOtherUsers;
    }

    public void setMonitorsOtherUsers(JSONArray monitorsOtherUsers) {
        this.monitorsOtherUsers = monitorsOtherUsers;
    }

    public JSONArray getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(JSONArray memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public JSONArray getLeaderOfGroups() {
        return leaderOfGroups;
    }

    public void setLeaderOfGroups(JSONArray leaderOfGroups) {
        this.leaderOfGroups = leaderOfGroups;
    }

    public JSONObject getLastGpsLocation() {
        return lastGpsLocation;
    }

    public void setLastGpsLocation(JSONObject lastGpsLocation) {
        this.lastGpsLocation = lastGpsLocation;
    }

    public String getUserHref() {
        return userHref;
    }

    public void setUserHref(String userHref) {
        this.userHref = userHref;
    }
}


