package database;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import config.Database;

public class UserDB {
    private static MongoDatabase db = Database.connect();
    private static MongoCollection<Document> collection = db.getCollection("users");

    public static boolean getUserById(int userId) {
        // procura no banco de dados o usuário com o id passado
        MongoCursor<Document> cursor = collection.find().iterator();

        cursor.forEachRemaining(document -> {
            System.out.println(document.toJson());
        });

        return false;
    }
}
