package config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Database {
    private static MongoDatabase db;
    private static MongoClient connection;

    public static MongoDatabase connect() {
        try {
            connection = new MongoClient("localhost", 27017);
            db = connection.getDatabase("projeto-sd");
            // System.out.println("--- Connected to MongoDB ---");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return db;
    }
}
