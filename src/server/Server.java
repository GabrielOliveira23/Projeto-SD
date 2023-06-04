package server;

import java.net.*;
import java.util.Scanner;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import utils.JsonVerify;

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
                        if (JsonVerify.register(json)) {
                            response = ServerTreatment.userCreate(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 2: {
                        if (JsonVerify.updateUser(json)) {
                            response = ServerTreatment.userUpdate(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 3: {
                        if (JsonVerify.login(json)) {
                            response = ServerTreatment.userLogin(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 4: {
                        if (JsonVerify.reportIncident(json)) {
                            response = ServerTreatment.reportIncident(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 5: {
                        if (JsonVerify.getIncidents(json)) {
                            response = ServerTreatment.getIncidents(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 6: {
                        if (JsonVerify.getUserIncidents(json)) {
                            response = ServerTreatment.getUserIncidents(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 7: {
                        if (JsonVerify.removeIncident(json)) {
                            response = ServerTreatment.removeIncident(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 9: {
                        if (JsonVerify.hasTokenAndId(json)) {
                            response = ServerTreatment.userLogout(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    case 10: {
                        // if (JsonVerify.hasTokenAndId(json)) {
                        //     response = ServerTreatment.userDelete(json);
                        //     System.out.println("Enviando p/ cliente: " + response);
                        //     System.out.println(
                        //             "\n================================================================================");
                        //     client.println(response);
                        //     break;
                        // }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    default:
                        json.addProperty("codigo", 500);
                        json.addProperty("mensagem", "Operacao nao implementada");
                        System.out.println(
                                "\n================================================================================");
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
            System.err.println("Cliente desconectado");
        }
    }

    private JsonObject sendInvalidDataError(PrintWriter client) {
        JsonObject response;
        response = ServerTreatment.jsonError("Dados insuficientes");
        System.out.println("Enviando p/ cliente: " + response);
        client.println(response);
        return response;
    }
}
