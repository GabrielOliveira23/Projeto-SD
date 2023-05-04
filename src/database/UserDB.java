package database;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.internal.validator.UpdateFieldNameValidator;

import config.Database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserDB {
    private static MongoDatabase db = Database.connect();
    private static MongoCollection<Document> collection = db.getCollection("users");
    private static JsonObject json = new JsonObject();
    private static BsonDocument bson = new BsonDocument();
    private static Gson gson = new Gson();

    public static BsonDocument getUserById(int userId) {
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            bson = BsonDocument.parse(cursor.next().toJson());
            if (bson.get("id_usuario").asInt32().getValue() == userId) {
                System.out.println("Usuário encontrado!");
                return bson;
            }
        }
        return null;
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

    public static JsonObject getUserByEmail(String email) {
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            json = gson.fromJson(cursor.next().toJson(), JsonObject.class);
            if (json.get("email").getAsString().equals(email))
                return json;
        }
        return null;
    }

    public static boolean updateToken(int idUser, String token) {
        Bson updates = Updates.set("token", token);
        BsonDocument user = getUserById(idUser);

        collection.updateOne(user, updates, new UpdateOptions().upsert(true));

        return false;
    }
}
