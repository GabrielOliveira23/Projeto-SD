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
        json.addProperty("id_operacao", 3);
        json.addProperty("email", email);
        json.addProperty("senha", password);

        System.out.println("\nsending to server...");
        System.out.println(json + "\n");

        response = null;
        out.println(json);

        try {
            response = gson.fromJson(in.readLine(), JsonObject.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        json = null;
        return response;
    }

    public static JsonObject logout(String token, int idUsuario) {
        json = new JsonObject();
        json.addProperty("id_operacao", 9);
        json.addProperty("token", token);
        json.addProperty("id_usuario", idUsuario);

        System.out.println("\nsending to server...");
        System.out.println(json + "\n");

        response = null;
        out.println(json);

        try {
            response = gson.fromJson(in.readLine(), JsonObject.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        json = null;
        return response;
    }
}
