package tdp.bikum.myapplication.models;

public class User {
    private String email;
    private String password;
    private String fullname; // Thêm trường mới
    private String username; // Thêm trường mới
    private String otp; // Thêm trường otp

    // Constructor với email, password, fullname, username và otp
    public User(String email, String password, String fullname, String username, String otp) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.username = username;
        this.otp = otp;
    }

    // Constructor chỉ với email và password (nếu cần)
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter và Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}