package server;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import entities.Incident;
import entities.User;

public class ServerTreatment {
    private static User user;

    public static JsonObject userCreate(JsonObject json) {
        user = new User();

        user.setName(json.get("nome").getAsString());
        user.setEmail(json.get("email").getAsString());
        user.setPassword(json.get("senha").getAsString());

        return user.register();
    }

    public static JsonObject userUpdate(JsonObject json) {
        user = new User();

        user.setName(json.get("nome").getAsString());
        user.setEmail(json.get("email").getAsString());
        user.setPassword(json.get("senha").getAsString());

        return user.update(json.get("token").getAsString(), json.get("id_usuario").getAsInt());
    }

    public static JsonObject userLogin(JsonObject json) {
        user = new User();

        user.setEmail(json.get("email").getAsString());
        user.setPassword(json.get("senha").getAsString());

        return user.login();
    }

    public static JsonObject reportIncident(JsonObject json) {
        Incident incident = new Incident();

        incident.setKm(json.get("km").getAsInt());
        incident.setIncidentType(json.get("tipo_incidente").getAsInt());
        incident.setDate(json.get("data").getAsString());
        incident.setHighway(json.get("rodovia").getAsString());

        return incident.report(json.get("id_usuario").getAsInt(), json.get("token").getAsString());
    }

    public static JsonObject getIncidents(JsonObject json) {
        Incident incident = new Incident();

        incident.setDate(json.get("data").getAsString());
        incident.setHighway(json.get("rodovia").getAsString());
        incident.setPeriod(json.get("periodo").getAsInt());
        if (json.has("faixa_km"))
            if (!json.get("faixa_km").equals(JsonNull.INSTANCE))
                incident.setHighwayLane(json.get("faixa_km").getAsString());

        return incident.getIncidents();
    }

    public static JsonObject userLogout(JsonObject json) {
        return user.logout(json.get("id_usuario").getAsInt(), json.get("token").getAsString());
    }

    public static JsonObject jsonError(String message) {
        JsonObject json = new JsonObject();

        json.addProperty("codigo", 500);
        json.addProperty("mensagem", message);

        return json;
    }
}
