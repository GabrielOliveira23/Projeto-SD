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

import config.Database;
import utils.DataVerify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserDB {
    private static MongoDatabase db = Database.connect();
    private static MongoCollection<Document> collection = db.getCollection("users");
    private static JsonObject response;
    private static BsonDocument bson;
    private static Gson gson = new Gson();

    public static BsonDocument getUserById(int userId) {
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            bson = BsonDocument.parse(cursor.next().toJson());
            if (bson.get("id_usuario").asInt32().getValue() == userId) {
                return bson;
            }
        }
        return null;
    }

    public static void insertOne(JsonObject json) {
        Document document = Document.parse(json.toString());
        collection.insertOne(document);
    }

    public static int getCount() {
        return (int) collection.countDocuments();
    }

    public static boolean auth(String email, String senha) {
        response = new JsonObject();
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            response = gson.fromJson(cursor.next().toJson(), JsonObject.class);
            if (response.get("email").getAsString().equals(email)) {
                String pass = response.get("senha").getAsString();
                if (BCrypt.checkpw(senha, pass))
                    return true;

                break;
            }
        }

        return false;
    }

    public static JsonObject isLogged(int userId, String token) {
        BsonDocument user = getUserById(userId);
        response = new JsonObject();

        if (user == null) {
            response.addProperty("codigo", 500);
            response.addProperty("mensagem", "Usuario nao encontrado");
        } else if (user.get("token").asString().getValue().equals(token)) {
            response.addProperty("codigo", 200);
        } else {
            response.addProperty("codigo", 500);
            response.addProperty("mensagem", "Token invalido");
        }
        return response;
    }

    public static JsonObject getByEmail(String email) {
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            response = gson.fromJson(cursor.next().toJson(), JsonObject.class);
            if (response.get("email").getAsString().equals(email))
                return response;
        }
        return null;
    }

    public static JsonObject update(int userId, JsonObject json) {
        BsonDocument user = getUserById(userId);
        Bson updates = null;
        response = new JsonObject();

        try {
            if (!json.get("nome").getAsString().equals("")) {
                if (!json.get("email").getAsString().equals("")) {
                    if (json.has("senha"))
                        updates = Updates.combine(
                                Updates.set("nome", json.get("nome").getAsString()),
                                Updates.set("email", json.get("email").getAsString()),
                                Updates.set("senha", json.get("senha").getAsString()));
                    else
                        updates = Updates.combine(
                                Updates.set("nome", json.get("nome").getAsString()),
                                Updates.set("email", json.get("email").getAsString()));
                } else
                    updates = Updates.set("nome", json.get("nome").getAsString());
            } else if (!json.get("email").getAsString().equals("")) {
                if (json.has("senha"))
                    updates = Updates.combine(
                            Updates.set("email", json.get("email").getAsString()),
                            Updates.set("senha", json.get("senha").getAsString()));
                else
                    updates = Updates.set("email", json.get("email").getAsString());
            } else if (json.has("senha"))
                updates = Updates.set("senha", json.get("senha").getAsString());

            if (updates == null) {
                response.addProperty("codigo", 500);
                response.addProperty("mensagem", "Nenhum dado foi alterado");
                return response;
            }

            collection.updateMany(user, updates, new UpdateOptions().upsert(true));
            String newToken = updateToken(userId, DataVerify.generateToken());

            response.addProperty("codigo", 200);
            response.addProperty("token", newToken);

            return response;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário (MongoDB): " + e.getMessage());
            response = new JsonObject();
            response.addProperty("codigo", 500);
            response.addProperty("mensagem", "Erro ao atualizar usuário");
            return response;
        }
    }

    public static String updateToken(int idUser, String token) {
        Bson updates = Updates.set("token", token);
        BsonDocument user = getUserById(idUser);

        try {
            collection.updateOne(user, updates, new UpdateOptions().upsert(true));
            return token;
        } catch (Exception e) {
            return null;
        }
    }
}
