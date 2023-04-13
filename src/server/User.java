package server;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class User {
    private String id;
    private String email;
    private String token;
    private int senha;

    public static JsonObject login(JsonObject data){
        boolean fakeLogin = true;
        
        JsonObject json = new JsonObject();

        

        Gson gson = new Gson();

        // parse the response
        if (fakeLogin) {
            json = gson.toJson();
        }

        return json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
