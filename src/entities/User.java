package entities;

import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class User {
    private int id;
    private String email = "";
    private String token;
    private int senha = -23;
    private boolean fakeLogin = false;

    public User() {
        Random random = new Random();
        this.id = random.nextInt(1000);
    }

    public JsonObject login(String email, int senha) {
        JsonObject json = new JsonObject();

        System.out.println("Executando Login...");
        System.out.println("Email: " + email);
        System.out.println("Senha: " + senha);
        System.out.println("ID: " + this.id);
        System.out.println();

        // parse the response
        if (this.fakeLogin) {
            json.addProperty("codigo", 200);
            json.addProperty("token", this.token);
            json.addProperty("id_usuario", this.id);
        } else if (this.email.equals(email) && this.senha == senha) {
            json.addProperty("codigo", 200);
            json.addProperty("token", this.token);
        } else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email ou senha incorretos");
        }

        return json;
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

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public boolean isFakeLogin() {
        return fakeLogin;
    }

    public void setFakeLogin(boolean fakeLogin) {
        this.fakeLogin = fakeLogin;
    }

}
