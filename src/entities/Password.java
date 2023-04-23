package entities;

public class Password {
    private String password;
    private String salt;

    public Password(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return this.salt;
    }
}
