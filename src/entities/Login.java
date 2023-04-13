package entities;
public class Login {
    private String email;
    private int senha;
    private String token;

    public Login() {}

    public Login(String email, int senha, String token) {
        this.email = email;
        this.senha = senha;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
