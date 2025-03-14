package config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import java.util.logging.Level;

public class Database {
    private static MongoDatabase db;
    private static MongoClient connection;

    public static MongoDatabase connect() {
        try {
            connection = new MongoClient("localhost", 27017);
            db = connection.getDatabase("projeto-sd");
            Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
            mongoLogger.setLevel(Level.OFF); // e.g. or Log.WARNING, etc.
            // System.out.println("--- Connected to MongoDB ---");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar com Banco de Dados", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: " + e.getMessage());
        }
        return db;
    }
}
