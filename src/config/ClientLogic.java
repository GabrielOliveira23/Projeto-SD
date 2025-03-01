package config;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import entities.User;

public class ClientLogic {
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
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverIp);
        }
        return false;
    }

    public static JsonObject register(String name, String email, String password) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 1);
        json.addProperty("nome", name);
        json.addProperty("email", email);
        json.addProperty("senha", password);

        sendToServer();

        return getResponse();
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

    public static JsonObject reportIncident(String token, int idUsuario, String data, String rodovia, int km,
            int tipoIncidente) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 4);
        json.addProperty("token", token);
        json.addProperty("id_usuario", idUsuario);
        json.addProperty("data", data);
        json.addProperty("rodovia", rodovia);
        json.addProperty("km", km);
        json.addProperty("tipo_incidente", tipoIncidente);

        sendToServer();

        return getResponse();
    }

    public static JsonObject getIncidents(String token, int idUsuario, String rodovia,
            String data, String faixaKm, int periodo) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 5);
        json.addProperty("rodovia", rodovia);
        json.addProperty("data", data);
        json.addProperty("faixa_km", faixaKm);
        json.addProperty("periodo", periodo);

        sendToServer();

        return getResponse();
    }

    public static JsonObject getUserIncidents(String token, int idUsuario) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 6);
        json.addProperty("token", token);
        json.addProperty("id_usuario", idUsuario);

        sendToServer();

        return getResponse();
    }

    public static JsonObject deleteIncident(String token, int idUsuario, int idIncidente) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 7);
        json.addProperty("token", token);
        json.addProperty("id_usuario", idUsuario);
        json.addProperty("id_incidente", idIncidente);

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

    public static JsonObject updateIncident(String token, int idUsuario, int idIncidente, String data, String rodovia,
            int km, int tipoIncidente) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 10);
        json.addProperty("token", token);
        json.addProperty("id_usuario", idUsuario);
        json.addProperty("id_incidente", idIncidente);
        json.addProperty("data", data);
        json.addProperty("rodovia", rodovia);
        json.addProperty("km", km);
        json.addProperty("tipo_incidente", tipoIncidente);

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

    public static JsonObject deleteUser(User user) {
        json = new JsonObject();
        response = new JsonObject();

        json.addProperty("id_operacao", 8);
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
