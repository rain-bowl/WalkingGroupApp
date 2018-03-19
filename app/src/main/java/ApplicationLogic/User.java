package ApplicationLogic;

import org.json.JSONArray;
import org.json.JSONObject;


//This class will store all information regarding the currently logged in user. It will be filled up on login
// or when a user creates a new account.
public class User {
    int ID;
    String emailAddress;
    String name;
    Object birthyear;
    Object birthmonth;
    Object userAddress;
    Object cellPhoneNumber;
    Object homePhoneNumber;
    Object grade;
    Object teacherName;
    Object emergencyContactInfoInstruction;
    JSONArray monitoredByUsers;
    JSONArray monitorsOtherUsers;
    JSONArray memberOfGroups;
    JSONArray leaderOfGroups;
    JSONObject lastGpsLocation;
    Object userHref;

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

    public Object getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(Object birthyear) {
        this.birthyear = birthyear;
    }

    public Object getBirthmonth() {
        return birthmonth;
    }

    public void setBirthmonth(Object birthmonth) {
        this.birthmonth = birthmonth;
    }

    public Object getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Object userAddress) {
        this.userAddress = userAddress;
    }

    public Object getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(Object cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public Object getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(Object homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public Object getGrade() {
        return grade;
    }

    public void setGrade(Object grade) {
        this.grade = grade;
    }

    public Object getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(Object teacherName) {
        this.teacherName = teacherName;
    }

    public Object getEmergencyContactInfoInstruction() {
        return emergencyContactInfoInstruction;
    }

    public void setEmergencyContactInfoInstruction(Object emergencyContactInfoInstruction) {
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

    public Object getUserHref() {
        return userHref;
    }

    public void setUserHref(Object userHref) {
        this.userHref = userHref;
    }
}


