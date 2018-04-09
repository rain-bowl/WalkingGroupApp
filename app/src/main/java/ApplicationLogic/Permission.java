package ApplicationLogic;

//Class holds the information for a single permission.
public class Permission {
    String status;                  //Status of permission(Accepted, Denied, Pending)
    String message;                 //Message of request(what the request is about)
    int permissionId;               //id of the request stored here

    public Permission(String status, String message, int id){
        this.status = status;
        this.message = message;
        this.permissionId = id;
    }

    //no setter methods for this class. An instance is only initialized with values when it is instantiated.
    public int getPermissionId() {
        return permissionId;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStatus() {
        return this.status;
    }
}
