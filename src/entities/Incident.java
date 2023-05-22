package entities;

import com.google.gson.JsonObject;

import database.IncidentDB;
import utils.DataVerify;

public class Incident {
    private int id;
    private int userId;
    private int km;
    private int incidentType;
    private String date;
    private String highway;
    private String token;

    public Incident() {
        this.id = -1;
        this.userId = -1;
        this.km = -1;
        this.incidentType = -1;
        this.date = "";
        this.highway = "";
        this.token = "";
    }

    public JsonObject create(int userId, String token) {
        JsonObject json = new JsonObject();
        // printLogin();

        this.userId = userId;
        
        if ((json = DataVerify.reportIncident(this)).get("codigo").getAsInt() == 200) {
            JsonObject incident = new JsonObject();

            incident.addProperty("id_usuario", userId);
            incident.addProperty("token", token);
            incident.addProperty("data", this.date);
            incident.addProperty("rodovia", this.highway);
            incident.addProperty("km", this.km);
            incident.addProperty("tipo_incidente", this.incidentType);

            IncidentDB.insertOne(incident);
        }

        return json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public int getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(int tipoIncidente) {
        this.incidentType = tipoIncidente;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public String getHighway() {
        return highway;
    }

    public void setHighway(String rodovia) {
        this.highway = rodovia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
