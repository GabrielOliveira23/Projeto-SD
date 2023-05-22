package database;

import org.bson.BsonDocument;
import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import config.Database;

public class IncidentDB {
    private static MongoDatabase db = Database.connect();
    private static MongoCollection<Document> collection = db.getCollection("incidents");
    private static JsonObject response;
    private static BsonDocument bson;
    private static Gson gson = new Gson();
    
    public static void insertOne(JsonObject json) {
        Document document = Document.parse(json.toString());
        collection.insertOne(document);
    }
}
