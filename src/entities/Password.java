package entities;

import org.mindrot.jbcrypt.BCrypt;

public class Password {
    private String password;
    private String salt;

    public Password(String password) {
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, this.salt);
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return this.salt;
    }
}
