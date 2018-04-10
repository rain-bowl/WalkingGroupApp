package ApplicationLogic;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZonedDateTime;


/**
 * Class which stores the user profile. Contains fields representing all of the information needed throughout the app
 * To make things easier, we simply store the JSON object containing the user information and then retrieve the entire
 * object when we need it since most of the time we are modifying the entire set of data nd need to send it ot the server
 */
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
    JSONArray pendingPermRequests;


    /**
     * Unpackages the users information from the provided JSON object and loads the local instances with it
     * @param retrievedInfo             JSON object containing the users profile
     */
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
            pendingPermRequests = retrievedInfo.getJSONArray("pendingPermissionRequests");
        }
        catch (Exception e){

        }
    }

    /**
     * Returns the JSON object which we store as the users profile
     * @return
     */
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


