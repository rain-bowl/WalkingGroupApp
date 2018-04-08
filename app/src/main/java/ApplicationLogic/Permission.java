package ApplicationLogic;

//Class holds the information for a single permission.
public class Permission {
    String status;
    String message;
    int permissionId;

    public Permission(String status, String message, int id){
        this.status = status;
        this.message = message;
        this.permissionId = id;
    }

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
