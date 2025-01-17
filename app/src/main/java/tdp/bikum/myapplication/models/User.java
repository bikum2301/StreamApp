package tdp.bikum.myapplication.models;

public class User {
    private String email;
    private String password;
    private String otp; // Thêm trường otp

    // Constructor với email, password và otp
    public User(String email, String password, String otp) {
        this.email = email;
        this.password = password;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}