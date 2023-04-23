package entities;

import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonObject;

import database.UserDB;

public class User {
    private int id;
    private String email;
    private String token;
    private Password password;
    private boolean fakeLogin = false;

    public User() {
        this.email = "gabriel@gmail.com";

        String salt = BCrypt.gensalt();
        this.password = new Password(BCrypt.hashpw("tictac123", salt), salt);

        Random random = new Random();
        this.id = random.nextInt(1000);
    }

    public JsonObject login(String email, String password) {
        // System.out.println(UserDB.getUserById(1)); // aplicar isto quanod precisar usar find
        JsonObject json = new JsonObject();
        printLogin(email, password);
        if (this.fakeLogin) {
            json.addProperty("codigo", 200);
            return json;
        }

        json = dataVerify(email, password);

        return json;
    }

    private JsonObject dataVerify(String email, String senha) {
        JsonObject json = new JsonObject();

        if (email == null || !email.contains("@") || !email.contains(".") || email.length() <= 16
                || email.length() >= 50) {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email ou senha inválido");
        } else if (senha == null || senha.length() <= 8 || senha.length() >= 32) {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email ou senha inválido");
        } else if (this.email.equals(email)
                && this.password.getPassword().equals(BCrypt.hashpw(senha, this.password.getSalt()))) {
            json.addProperty("codigo", 200);
        } else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email ou senha incorreto");
        }

        return json;
    }

    private void printLogin(String email, String senha) {
        System.out.println("Executando Login...");
        System.out.println("Email: " + email);
        System.out.println("Senha: " + senha);
        System.out.println("ID: " + this.id);
        System.out.println();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password senha) {
        this.password = senha;
    }

    public boolean isFakeLogin() {
        return fakeLogin;
    }

    public void setFakeLogin(boolean fakeLogin) {
        this.fakeLogin = fakeLogin;
    }

}
