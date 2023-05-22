package server;

import com.google.gson.JsonObject;

import entities.User;

public class ServerTreatment {
    private static User user;

    public static JsonObject userCreate(JsonObject json) {
        User user = new User();
        String name = json.get("nome").getAsString();
        String email = json.get("email").getAsString();
        String password = json.get("senha").getAsString();
        return user.create(name, email, password);
    }

    public static JsonObject userUpdate(JsonObject json) {
        User user = new User();
        user.setName(json.get("nome").getAsString());
        user.setEmail(json.get("email").getAsString());
        user.setPassword(json.get("senha").getAsString());

        return user.update(json.get("token").getAsString(), json.get("id_usuario").getAsInt());
    }

    public static JsonObject userLogin(JsonObject json) {
        User user = new User();
        String email = json.get("email").getAsString();
        String password = json.get("senha").getAsString();
        return user.login(email, password);
    }

    public static JsonObject userLogout(JsonObject json) {
        User user = new User();
        user.setId(json.get("id_usuario").getAsInt());
        user.setToken(json.get("token").getAsString());
        return user.logout();
    }

    public static JsonObject jsonError(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("codigo", 500);
        json.addProperty("mensagem", message);
        return json;
    }
}
