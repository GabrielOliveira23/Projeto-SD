package database;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

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

    public static void insertUser(JsonObject json) {
        Document document = Document.parse(json.toString());
        collection.insertOne(document);
    }

    public static int getCountUsers() {
        return (int) collection.countDocuments();
    }

    public static boolean authUser(String email, String senha) {
        // autentica se usuario existe e confirma email e senha
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            json = gson.fromJson(cursor.next().toJson(), JsonObject.class);
            if (json.get("email").getAsString().equals(email)) {
                String pass = json.get("senha").getAsString();
                if (BCrypt.checkpw(senha, pass))
                    return true;
                
                break;
            }
        }

        return false;
    }

    public static boolean getUserByEmail(String email) {
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            json = gson.fromJson(cursor.next().toJson(), JsonObject.class);
            if (json.get("email").getAsString().equals(email)) {
                System.out.println("Usuário encontrado!");
                return true;
            }
        }
        return false;
    }
}
