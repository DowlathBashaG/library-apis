package libraryapi.apigee.user;

/**
 * @Author Dowlath
 * @create 5/23/2020 12:38 PM
 */
public enum Role {

    ADMIN("Admin"),
    USER("User");

    private String roleName;

    Role(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName(){
        return roleName;
    }
    public void setRoleName(String roleName){
        this.roleName = roleName;
    }
}
