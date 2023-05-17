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
        System.out.println("Porta do servidor: ");
        int port = input.nextInt();
        input.nextLine();
        input.close();

        try {
            // InetAddress addr = InetAddress.getByName("0.0.0.0"); //
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
                }

                // if (json.get("id_operacao").equals(JsonNull.INSTANCE)) {
                //     response.addProperty("codigo", 500);
                //     response.addProperty("mensagem", "Operacao nao especificada");
                //     client.println(response);
                //     run();
                //     break;
                // }
                
                int operation = json.get("id_operacao").getAsInt();

                switch (operation) {
                    case 1: {
                        if (json.get("nome").equals(JsonNull.INSTANCE)
                                || json.get("email").equals(JsonNull.INSTANCE)
                                || json.get("senha").equals(JsonNull.INSTANCE)) {
                            response.addProperty("codigo", 500);
                            response.addProperty("mensagem", "Dados insuficientes");
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        }

                        User user = new User();
                        String name = json.get("nome").getAsString();
                        String email = json.get("email").getAsString();
                        String password = json.get("senha").getAsString();
                        response = user.create(name, email, password);
                        System.out.println("Enviando p/ cliente: " + response);
                        client.println(response);
                        break;
                    }

                    case 2: {
                        response.addProperty("codigo", 500);
                        response.addProperty("mensagem", "Operação nao implementada");
                        client.println(response);
                        response = null;
                        break;
                    }

                    case 3: {
                        if (json.get("email").equals(JsonNull.INSTANCE)
                                || json.get("senha").equals(JsonNull.INSTANCE)) {
                            System.out.println("Algum dos campos nulos.");
                            response.addProperty("codigo", 500);
                            response.addProperty("mensagem", "Dados insuficientes");
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        }

                        String email = json.get("email").getAsString();
                        String password = json.get("senha").getAsString();
                        response = userLogin.login(email, password);
                        System.out.println("Enviando p/ cliente: " + response);
                        client.println(response);
                        break;
                    }

                    case 9: {
                        if (json.get("id_usuario").equals(JsonNull.INSTANCE)
                                || json.get("token").equals(JsonNull.INSTANCE)) {
                            System.out.println("Algum dos campos nulos.");
                            response.addProperty("codigo", 500);
                            response.addProperty("mensagem", "Dados insuficientes");
                            System.out.println("Enviando p/ cliente: " + response);
                            client.println(response);
                            break;
                        }

                        int userId = json.get("id_usuario").getAsInt();
                        String token = json.get("token").getAsString();
                        response = userLogin.logout(userId, token);
                        System.out.println("Enviando p/ cliente: " + response);
                        client.println(response);
                        break;
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
            System.err.println("Problem with Communication Server");
        }
    }
}
