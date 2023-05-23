package entities;

import com.google.gson.JsonObject;

import database.IncidentDB;
import utils.DataVerify;

public class Incident {
    private int id;
    private int userId;
    private int km;
    private int incidentType;
    private int period;
    private String date;
    private String highway;
    private String token;
    private String highwayLane;

    public Incident() {
        this.id = -1;
        this.userId = -1;
        this.km = -1;
        this.incidentType = -1;
        this.period = -1;
        this.date = "";
        this.highway = "";
        this.token = "";
        this.highwayLane = "";
    }

    public JsonObject create(int userId, String token) {
        JsonObject json = new JsonObject();
        printCreate();

        this.userId = userId;

        if ((json = DataVerify.reportIncident(this)).get("codigo").getAsInt() == 200) {
            JsonObject incident = new JsonObject();

            incident.addProperty("id_usuario", userId);
            incident.addProperty("token", token);
            incident.addProperty("data", this.getDate());
            incident.addProperty("rodovia", this.getHighway());
            incident.addProperty("km", this.getKm());
            incident.addProperty("tipo_incidente", this.getIncidentType());

            IncidentDB.insertOne(incident);
        }

        return json;
    }

    public JsonObject getIncidents() {
        JsonObject json = new JsonObject();
        printGetIncidents();

        if ((json = DataVerify.getIncidents(this)).get("codigo").getAsInt() == 200) {
            JsonObject incident = new JsonObject();

            incident.addProperty("data", this.getParsedDate());
            incident.addProperty("rodovia", this.getHighway());
            incident.addProperty("periodo", this.getPeriod());

            if (this.getHighwayLane() != "") {
                incident.addProperty("min_km", this.getMinKm());
                incident.addProperty("max_km", this.getMaxKm());
            }

            json = IncidentDB.getMany(incident);
        }

        return json;
    }

    private void printCreate() {
        System.out.println("Executando operacao de criacao de incidente...");
        System.out.println("data: " + this.getDate());
        System.out.println("rodovia: " + this.getHighway());
        System.out.println("km: " + this.getKm());
        System.out.println("tipo_incidente: " + this.getIncidentType());
        System.out.println();
    }

    private void printGetIncidents() {
        System.out.println("Executando operacao de listagem de incidentes...");
        System.out.println("rodovia: " + this.getHighway());
        System.out.println("data: " + this.getDate());
        System.out.println("faixa de km: " + this.getHighwayLane());
        System.out.println("periodo: " + this.getPeriod());
        System.out.println();
    }

    private String getMinKm() {
        return this.getHighwayLane().split("-")[0];
    }

    private String getMaxKm() {
        return this.getHighwayLane().split("-")[1];
    }

    private String getParsedDate() {
        return this.getDate().split(" ")[0];
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int periodo) {
        this.period = periodo;
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

    public String getHighwayLane() {
        return highwayLane;
    }

    public void setHighwayLane(String highwayLane) {
        this.highwayLane = highwayLane;
    }

}
