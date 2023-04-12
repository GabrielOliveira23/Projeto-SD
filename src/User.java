public class User {
    private String email;
    private int senha;
    private String token;
    private String id;

    public User(String email, int senha, String token) {
        this.email = email;
        this.senha = senha;
        this.token = token;
    }
}
