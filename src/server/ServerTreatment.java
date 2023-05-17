package server;

import com.google.gson.JsonObject;

import entities.User;

public class ServerTreatment {
    public static JsonObject userCreate(JsonObject json) {
        User user = new User();
        String name = json.get("nome").getAsString();
        String email = json.get("email").getAsString();
        String password = json.get("senha").getAsString();
        return user.create(name, email, password);
    }

    public static JsonObject userLogin(User user, JsonObject json) {
        String email = json.get("email").getAsString();
        String password = json.get("senha").getAsString();
        return user.login(email, password);
    }

    public static JsonObject userLogout(User user, JsonObject json) {
        int userId = json.get("id_usuario").getAsInt();
        String token = json.get("token").getAsString();
        return user.logout(userId, token);
    }

    public static JsonObject jsonError(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("codigo", 500);
        json.addProperty("mensagem", message);
        return json;
    }
}
