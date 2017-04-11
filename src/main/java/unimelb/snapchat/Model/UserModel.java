package unimelb.snapchat.Model;

public class UserModel {

    public String userid;
    public String username;
    public String address;
    public String phone;
    public String profileImageUri;
    public String state;
    public String email;

    public UserModel(String userid, String username, String address, String phone, String profileImageUri, String state, String email) {
        this.userid = userid;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.profileImageUri = profileImageUri;
        this.state = state;
        this.email = email;
    }

    public UserModel() {}
}