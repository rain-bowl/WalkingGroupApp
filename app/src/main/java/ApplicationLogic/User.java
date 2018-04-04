package ApplicationLogic;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZonedDateTime;


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
    JSONArray unreadMessages;
    JSONArray readMessages;
    JSONObject userJsonInformation;


    //Takes in json object and sets the user fields according to the information recieved.
    // The information here is used throughout the entire application.
     public void setJsonObject(JSONObject retrievedInfo){

        try {
            userJsonInformation = new JSONObject(retrievedInfo.toString());
            ID = retrievedInfo.getInt("id");
            emailAddress = retrievedInfo.getString("email");
            name = retrievedInfo.getString("name");
            birthyear = retrievedInfo.getInt("birthYear");
            birthmonth = retrievedInfo.getInt("birthMonth");
            userAddress = retrievedInfo.getString("address");
            cellPhoneNumber = retrievedInfo.getString("cellPhone");
            homePhoneNumber = retrievedInfo.getString("homePhone");
            grade = retrievedInfo.getString("grade");
            teacherName = retrievedInfo.getString("teacherName");
            emergencyContactInfoInstruction = retrievedInfo.getString("emergencyContactInfo");
            monitoredByUsers = retrievedInfo.getJSONArray("monitoredByUsers");
            monitorsOtherUsers = retrievedInfo.getJSONArray("monitorsUsers");
            memberOfGroups = retrievedInfo.getJSONArray("memberOfGroups");
            leaderOfGroups = retrievedInfo.getJSONArray("leadsGroups");
            lastGpsLocation = retrievedInfo.getJSONObject("lastGpsLocation");
            unreadMessages = retrievedInfo.getJSONArray("unreadMessages");
            readMessages = retrievedInfo.getJSONArray("readMessages");
            userHref = retrievedInfo.getString("href");
        }
        catch (Exception e){

        }
    }

    //Simply returns the Json object which contains the user information to be used.
     public JSONObject returnJsonUserInfo(){
        return this.userJsonInformation;
    }



    //Setters and getters, might have to be removeed at a later point since they are currently
    //useless due to the singleton not providing access to them. Additionally, simply sending
    //the entire JSONObject will give us all the information required.
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

    public void setLastGpsLocation(JSONObject lastKnownLocation) {
        this.lastGpsLocation = lastKnownLocation;
    }

    public String getUserHref() {
        return userHref;
    }

    public void setUserHref(String userHref) {
        this.userHref = userHref;
    }




}


