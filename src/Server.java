import java.net.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import entities.User;

public class Server extends Thread {
    protected Socket clientSocket;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        int port = 24001;

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

                json = gson.fromJson(inputLine, JsonObject.class);
                int operation = json.get("id_operacao").getAsInt();

                switch (operation) {
                    case 1: {
                        User user = new User();
                        String name = json.get("nome").getAsString();
                        String email = json.get("email").getAsString();
                        String password = json.get("senha").getAsString();
                        response = user.create(name, email, password);

                        client.println(response);
                        break;
                    }

                    case 2: {
                        response.addProperty("codigo", 500);
                        response.addProperty("mensagem", "Operação não implementada");
                        client.println(response);
                        response = null;
                        break;
                    }

                    case 3: {
                        String email = json.get("email").getAsString();
                        String password = json.get("senha").getAsString();
                        response = userLogin.login(email, password);
                        client.println(response);
                        break;
                    }

                    case 9: {
                        int userId = json.get("id_usuario").getAsInt();
                        String token = json.get("token").getAsString();
                        response = userLogin.logout(userId, token);
                        client.println(response);
                        break;
                    }

                    default:
                        client.println("internal error");
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
