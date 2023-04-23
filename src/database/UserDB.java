package database;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import config.Database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserDB {
    private static MongoDatabase db = Database.connect();
    private static MongoCollection<Document> collection = db.getCollection("users");
    private static JsonObject json = new JsonObject();
    private static Gson gson = new Gson();

    public static boolean getUserById(int userId) {
        // procura no banco de dados o usuário com o id passado
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            json = gson.fromJson(cursor.next().toJson(), JsonObject.class);
            if (json.get("id_usuario").getAsInt() == userId) {
                System.out.println("Usuário encontrado!");
                return true;
            }
        }
        return false;
    }
}
