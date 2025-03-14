package server;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import entities.Incident;
import entities.User;

public class ServerTreatment {
    private static User user;
    private static Incident incident;

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
        user.setToken(json.get("token").getAsString());

        return user.update(json.get("token").getAsString(), json.get("id_usuario").getAsInt());
    }

    public static JsonObject userLogin(JsonObject json) {
        user = new User();

        user.setEmail(json.get("email").getAsString());
        user.setPassword(json.get("senha").getAsString());

        return user.login();
    }

    public static JsonObject userDelete(JsonObject json) {
        user = new User();

        user.setEmail(json.get("email").getAsString());
        user.setPassword(json.get("senha").getAsString());
        user.setToken(json.get("token").getAsString());

        return user.delete(json.get("token").getAsString(), json.get("id_usuario").getAsInt());
    }

    public static JsonObject reportIncident(JsonObject json) {
        incident = new Incident();

        incident.setKm(json.get("km").getAsInt());
        incident.setIncidentType(json.get("tipo_incidente").getAsInt());
        incident.setDate(json.get("data").getAsString());
        incident.setHighway(json.get("rodovia").getAsString());

        return incident.report(json.get("id_usuario").getAsInt(), json.get("token").getAsString());
    }

    public static JsonObject getIncidents(JsonObject json) {
        incident = new Incident();

        incident.setDate(json.get("data").getAsString());
        incident.setHighway(json.get("rodovia").getAsString());
        incident.setPeriod(json.get("periodo").getAsInt());
        if (json.has("faixa_km"))
            if (!json.get("faixa_km").equals(JsonNull.INSTANCE) && !json.get("faixa_km").getAsString().equals(""))
                incident.setHighwayLane(json.get("faixa_km").getAsString());

        return incident.getIncidents();
    }

    public static JsonObject getUserIncidents(JsonObject json) {
        return new Incident().getUserIncidents(json.get("id_usuario").getAsInt(), json.get("token").getAsString());
    }

    public static JsonObject updateIncident(JsonObject json) {
        incident = new Incident();

        incident.setKm(json.get("km").getAsInt());
        incident.setIncidentType(json.get("tipo_incidente").getAsInt());
        incident.setDate(json.get("data").getAsString());
        incident.setHighway(json.get("rodovia").getAsString());

        return incident.updateIncident(json.get("id_usuario").getAsInt(), json.get("id_incidente").getAsInt(),
                json.get("token").getAsString());
    }

    public static JsonObject userLogout(JsonObject json) {
        return new User().logout(json.get("id_usuario").getAsInt(), json.get("token").getAsString());
    }

    public static JsonObject removeIncident(JsonObject json) {
        return new Incident().removeIncident(json.get("id_usuario").getAsInt(), json.get("token").getAsString(),
                json.get("id_incidente").getAsInt());
    }

    public static JsonObject jsonError(String message) {
        JsonObject json = new JsonObject();

        json.addProperty("codigo", 500);
        json.addProperty("mensagem", message);

        return json;
    }
}
