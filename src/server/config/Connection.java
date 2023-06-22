package server.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import server.ServerTreatment;
import utils.JsonVerify;

public class Connection extends Thread {
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private boolean isRunning = true;

    public Connection() {
        this.serverSocket = null;
    }

    private Connection(Socket client) {
        this.clientSocket = client;
        start();
    }

    public void connect(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado na porta " + port);

            while (true) {
                Socket client = null;

                client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());

                new Connection(client);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao iniciar o servidor: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar o servidor\n" + e.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
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

            while ((inputLine = in.readLine()) != null && this.isRunning) {
                System.out.println("\nServer: " + inputLine);

                try {
                    json = gson.fromJson(inputLine, JsonObject.class);
                } catch (JsonSyntaxException e) {
                    response.addProperty("codigo", 500);
                    response.addProperty("mensagem", "Formato de mensagem invalido");
                    client.println(response);
                    break;
                } catch (NullPointerException e) {
                    response.addProperty("codigo", 500);
                    response.addProperty("mensagem", "Formato de mensagem invalido");
                    client.println(response);
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
                        if (JsonVerify.hasTokenAndId(json)) {
                            response = ServerTreatment.updateIncident(json);
                            System.out.println("Enviando p/ cliente: " + response);
                            System.out.println(
                                    "\n================================================================================");
                            client.println(response);
                            break;
                        }

                        response = sendInvalidDataError(client);
                        break;
                    }

                    default:
                        json = new JsonObject();
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
        } catch (SocketException e) {
            System.err.println("Cliente " + this.clientSocket.getInetAddress().getHostAddress() + " desconectado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject sendInvalidDataError(PrintWriter client) {
        JsonObject response;
        response = ServerTreatment.jsonError("Dados insuficientes");
        System.out.println("Enviando p/ cliente: " + response);
        client.println(response);
        return response;
    }

    public void stopServer() {
        this.isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
