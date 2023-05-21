package client;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import entities.User;

public class ConnectionLogic {
    private static User user;
    private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static Gson gson = new Gson();
    private static JsonObject response, json;

    public static boolean connect(String serverIp, int serverPort) {
        try {
            socket = new Socket(serverIp, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverIp);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverIp);
            e.printStackTrace();
        }
        return false;
    }

    public static JsonObject login(String email, String password) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 3);
        json.addProperty("email", email);
        json.addProperty("senha", password);

        sendToServer();

        return getResponse();
    }

    public static JsonObject logout(String token, int idUsuario) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 9);
        json.addProperty("token", token);
        json.addProperty("id_usuario", idUsuario);

        sendToServer();

        return getResponse();
    }

    public static JsonObject updateUser(User user) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 2);
        json.addProperty("nome", user.getName());
        json.addProperty("email", user.getEmail());
        json.addProperty("senha", user.getPassword());
        json.addProperty("token", user.getToken());
        json.addProperty("id_usuario", user.getId());

        sendToServer();

        return getResponse();
    }

    private static void sendToServer() {
        System.out.println("\nsending to server...");
        System.out.println(json + "\n");
        out.println(json);
    }

    private static JsonObject getResponse() {
        try {
            response = gson.fromJson(in.readLine(), JsonObject.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
