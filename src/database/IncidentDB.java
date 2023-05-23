package database;

import org.bson.BsonDocument;
import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


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
            if (bson.get("id_incidente").asInt32().getValue() == incidentId) {
                return bson;
            }
        }
        return null;
    }

    public static void insertOne(JsonObject json) {
        Document document = Document.parse(json.toString());
        collection.insertOne(document);
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

            if (response.get("lista_incidentes").getAsJsonArray().size() == 0) {
                response = new JsonObject();
                response.addProperty("codigo", 200);
                response.addProperty("mensagem", "Nenhum incidente encontrado");
            }
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
}
