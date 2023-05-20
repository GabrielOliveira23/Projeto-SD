package server;

import java.net.*;
import java.util.Scanner;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import entities.User;

public class Server extends Thread {
    protected Socket clientSocket;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        Scanner input = new Scanner(System.in);
        System.out.println("Porta do servidor (int): ");
        int port = input.nextInt();
        input.nextLine();
        input.close();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection on Port: " + port + "...");
                    new Server(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: " + port);
                System.exit(1);
            }
        }
    }

    private Server(Socket clientSoc) {
        clientSocket = clientSoc;
        start();
    }

    public void run() {
        System.out.println("New Communication Thread Started");
        JsonObject json, response = new JsonObject();
        Gson gson = new Gson();
        User userLogin = new User();

        try {
            PrintWriter client = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("\nServer: " + inputLine);

                try {
                    json = gson.fromJson(inputLine, JsonObject.class);
                } catch (JsonSyntaxException e) {
                    response.addProperty("codigo", 500);
                    response.addProperty("mensagem", "Formato de mensagem invalido");
                    client.println(response);
                    run();
                    break;
                } catch (NullPointerException e) {
                    response.addProperty("codigo", 500);
                    response.addProperty("mensagem", "Formato de mensagem invalido");
                    client.println(response);
                    run();
                    break;
                }

                int operation = json.get("id_operacao").getAsInt();

                switch (operation) {
                    case 1: {
                        try {
                            if (json.get("nome").equals(JsonNull.INSTANCE)
                                    || json.get("email").equals(JsonNull.INSTANCE)
                                    || json.get("senha").equals(JsonNull.INSTANCE)) {

                                response = ServerTreatment.jsonError("Dados insuficientes");
                                System.out.println("Enviando p/ cliente: " + response);
                                client.println(response);
                                break;
                            }

                            response = ServerTreatment.userCreate(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        } catch (Exception e) {
                            response = ServerTreatment.jsonError("Erro ao criar usuario");
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        }
                    }

                    case 2: {
                        response.addProperty("codigo", 500);
                        response.addProperty("mensagem", "Operação nao implementada");
                        client.println(response);
                        break;
                    }

                    case 3: {
                        try {
                            if (json.get("email").equals(JsonNull.INSTANCE)
                                    || json.get("senha").equals(JsonNull.INSTANCE)) {

                                response = ServerTreatment.jsonError("Dados insuficientes");
                                System.out.println("Enviando p/ cliente: " + response);
                                client.println(response);
                                break;
                            }

                            response = ServerTreatment.userLogin(userLogin, json);
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        } catch (Exception e) {
                            response = ServerTreatment.jsonError("Erro ao realizar login");
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        }
                    }

                    case 9: {
                        try {
                            if (json.get("id_usuario").equals(JsonNull.INSTANCE)
                                    || json.get("token").equals(JsonNull.INSTANCE)) {

                                response = ServerTreatment.jsonError("Dados insuficientes");
                                System.out.println("Enviando p/ cliente: " + response);
                                client.println(response);
                                break;
                            }

                            response = ServerTreatment.userLogout(userLogin, json);
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        } catch (Exception e) {
                            response = ServerTreatment.jsonError("Erro ao realizar logout");
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        }
                    }

                    default:
                        json.addProperty("codigo", 500);
                        json.addProperty("mensagem", "Operacao nao implementada");
                        client.println(json);
                        break;
                }

                if (inputLine.equals("Bye"))
                    break;
            }

            client.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            // response = ServerTreatment.userLogout(userLogin, json); // logout ao quitar server
            System.err.println("Problem with Communication Server");
        }
    }
}
