package database;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import config.Database;
import utils.GeneralFunctions;

public class IncidentDB {
    private static MongoDatabase db = Database.connect();
    private static MongoCollection<Document> collection = db.getCollection("incidents");
    private static JsonObject response;
    private static BsonDocument bson;
    private static Gson gson = new Gson();

    public static BsonDocument getIncidentById(int incidentId) {
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            bson = BsonDocument.parse(cursor.next().toJson());
            if (bson.get("id_incidente").asInt32().getValue() == incidentId)
                return bson;
        }
        return null;
    }

    public static int getCount() {
        return (int) collection.countDocuments();
    }

    public static void insertOne(JsonObject json) {
        Document document = Document.parse(json.toString());
        collection.insertOne(document);
    }

    public static JsonObject update(int incidentId, JsonObject json) {
        BsonDocument incident = getIncidentById(incidentId);
        Bson updates = null;
        response = new JsonObject();

        String rodovia = json.get("rodovia").getAsString();
        int tipoIncidente = json.get("tipo_incidente").getAsInt();
        String data = json.get("data").getAsString();
        int km = json.get("km").getAsInt();

        updates = Updates.combine(
                Updates.set("rodovia", rodovia),
                Updates.set("km", km),
                Updates.set("tipo_incidente", tipoIncidente),
                Updates.set("data", data));

        collection.updateMany(incident, updates, new UpdateOptions().upsert(true));

        response.addProperty("codigo", 200);

        return response;
    }

    public static void deleteOne(int incidentId) {
        BsonDocument bson = getIncidentById(incidentId);
        if (bson != null)
            collection.deleteOne(bson);
    }

    public static JsonObject getMany(JsonObject json) {
        response = new JsonObject();
        JsonArray incidents = new JsonArray();

        MongoCursor<Document> cursor = collection.find().iterator();

        try {
            response.addProperty("codigo", 200);

            while (cursor.hasNext()) {
                bson = BsonDocument.parse(cursor.next().toJson());
                if (bson.get("data").asString().getValue().split(" ")[0]
                        .equals(json.get("data").getAsString().split(" ")[0])
                        && bson.get("rodovia").asString().getValue().equals(json.get("rodovia").getAsString())
                        && GeneralFunctions.getPeriod(bson.get("data").asString().getValue()) == json.get("periodo")
                                .getAsInt()) {
                    if (json.has("min_km") && json.has("max_km")) {
                        if ((bson.get("km").asInt32().getValue() >= json.get("min_km").getAsInt()
                                && bson.get("km").asInt32().getValue() <= json.get("max_km").getAsInt()))
                            incidents.add(gson.fromJson(bson.toJson(), JsonObject.class));
                    } else
                        incidents.add(gson.fromJson(bson.toJson(), JsonObject.class));
                }
            }

            System.out.println();
            response.add("lista_incidentes", incidents);
        } catch (Exception e) {
            response = new JsonObject();
            System.out.println("MongoDB error: " + e.getMessage());
            response.addProperty("codigo", 500);
            response.addProperty("mensagem", "Erro ao buscar incidentes");
        } finally {
            cursor.close();
        }

        return response;
    }

    public static JsonObject getManyByUser(int userId) {
        response = new JsonObject();
        JsonArray incidents = new JsonArray();

        MongoCursor<Document> cursor = collection.find().iterator();

        try {
            response.addProperty("codigo", 200);

            while (cursor.hasNext()) {
                bson = BsonDocument.parse(cursor.next().toJson());
                if (!bson.get("id_usuario").isNull())
                    if (bson.get("id_usuario").asInt32().getValue() == userId)
                        incidents.add(gson.fromJson(bson.toJson(), JsonObject.class));
            }

            System.out.println();
            response.add("lista_incidentes", incidents);
        } catch (Exception e) {
            response = new JsonObject();
            System.out.println("MongoDB error: " + e.getMessage());
            response.addProperty("codigo", 500);
            response.addProperty("mensagem", "Erro ao buscar incidentes");
        } finally {
            cursor.close();
        }

        return response;
    }

    public static JsonObject setNullOnIncidents(int userId) {
        JsonObject json = getManyByUser(userId);

        try {
            if (json.get("codigo").getAsInt() == 200) {
                JsonArray incidents = json.get("lista_incidentes").getAsJsonArray();
                for (int i = 0; i < incidents.size(); i++) {
                    JsonObject incident = incidents.get(i).getAsJsonObject();
                    incident.add("id_usuario", null);

                    BsonDocument bsonIncident = getIncidentById(incident.get("id_incidente").getAsInt());

                    String rodovia = incident.get("rodovia").getAsString();
                    int tipoIncidente = incident.get("tipo_incidente").getAsInt();
                    String data = incident.get("data").getAsString();
                    int km = incident.get("km").getAsInt();

                    Bson updates = Updates.combine(
                            Updates.set("rodovia", rodovia),
                            Updates.set("km", km),
                            Updates.set("tipo_incidente", tipoIncidente),
                            Updates.set("id_usuario", null),
                            Updates.set("data", data));

                    collection.updateMany(bsonIncident, updates, new UpdateOptions().upsert(true));
                }
            }
            response = new JsonObject();
            response.addProperty("codigo", 200);
        } catch (Exception e) {
            response.addProperty("codigo", 500);
            response.addProperty("mensagem", "Erro ao setar null em incidentes");
        }

        return response;

    }
}
