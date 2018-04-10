package ApplicationLogic;

/**
 * Class which holds the information for a single user which is part of the leader board. It contains:
 *      -Points
 *      -First name
 *      -Last name
 */
public class LeaderboardMember {
    int userPoints;
    String firstName;
    String lastName;


    public void setUserPoints(int points){
        this.userPoints = points;
    }

    public int getUserPoints(){
        return this.userPoints;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
