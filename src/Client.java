
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import entities.User;
import utils.CaesarCrypt;

public class Client {
    private static User userRepository = new User();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        new Client();
        // String ip = "127.0.0.1"; // localhost
        // String ip = "10.20.8.198"; // sauter
        // String ip = "10.20.8.81"; // kenji
        // String ip = "10.20.8.153"; // igor
        // String ip = "10.20.8.77"; // mairon

        String ip = "26.20.133.105"; // radmin kenji
        // String ip = "26.157.130.119"; // radmin sauter
        // String ip = "26.59.167.57"; // radmin salles

        int port = 24001;
        String serverHostname = new String(ip);

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        Gson gson = new Gson();
        String userInput;
        boolean shouldStop = false;

        if (args.length > 0)
            serverHostname = args[0];
        System.out.println("Attemping to connect to host " +
                serverHostname + ":" + port);

        try {
            echoSocket = new Socket(serverHostname, port);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHostname);
            System.exit(1);
        }

        while (!shouldStop) {
            JsonObject json = new JsonObject();
            System.out.println("Selecione uma opção: ");
            System.out.println("1 - Cadastrar Usuário");
            System.out.println("2 - Atualizar Cadastro");
            System.out.println("3 - Fazer Login");
            System.out.println("9 - Fazer Logout");
            System.out.println("\"Bye\" to quit");

            if ((userInput = teclado.readLine()) == null)
                break;

            switch (userInput) {
                case "1": {
                    System.out.println("-------------CADASTRO-------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

                    System.out.print("Nome: ");
                    json.addProperty("nome", teclado.readLine());

                    System.out.print("Email: ");
                    json.addProperty("email", teclado.readLine());

                    System.out.print("Senha: ");
                    String senha = teclado.readLine();
                    json.addProperty("senha", CaesarCrypt.encrypt(senha));
                    senha = "";

                    System.out.println("\nsending to server...\n");
                    out.println(json);

                    // out.println((String)null);
                    break;
                }
                case "2": {
                    System.out.println("-------------ATUALIZAR CADASTRO-------------");
                    System.out.println("--------------NOT IMPLEMENTED---------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

                    System.out.print("Nome: ");
                    json.addProperty("nome", teclado.readLine());

                    System.out.print("Email: ");
                    json.addProperty("email", teclado.readLine());

                    System.out.print("Senha: ");
                    String senha = teclado.readLine();
                    json.addProperty("senha", CaesarCrypt.encrypt(senha));
                    senha = "";

                    break;
                }

                case "3": {
                    System.out.println("-------------LOGIN-------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

                    System.out.print("Email: ");
                    json.addProperty("email", teclado.readLine());

                    System.out.print("Senha: ");
                    String senha = teclado.readLine();
                    json.addProperty("senha", CaesarCrypt.encrypt(senha));
                    senha = "";

                    System.out.println("\nsending to server...\n");

                    out.println(json);

                    break;
                }

                case "9": {
                    System.out.println("-------------LOGOUT-------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

                    json.addProperty("id_usuario", userRepository.getId());
                    json.addProperty("token", userRepository.getToken());

                    System.out.println("\nsending to server...\n");
                    out.println(json);

                    break;
                }

                case "Bye": {
                    shouldStop = true;
                    break;
                }

                default: {
                    System.out.println("Opção inválida!");
                    break;
                }
            }

            if (shouldStop)
                break;

            sendToServer(in, gson, userInput);
            System.out.println("\n------------------------------------\n");
        }

        out.close();
        in.close();
        teclado.close();
        echoSocket.close();
    }

    private static void sendToServer(BufferedReader in, Gson gson, String operation) throws IOException {
        JsonObject response;
        try {
            response = gson.fromJson(in.readLine(), JsonObject.class);
            System.out.println("server return: " + response);

            switch (operation) {
                case "3": {
                    try {
                        userRepository.setId(response.get("id_usuario").getAsInt());
                        userRepository.setToken(response.get("token").getAsString());
                        break;
                    } catch (NullPointerException e) {
                        System.out.println("Erro ao fazer login, retorno nulo!");
                        break;
                    }
                }
            }

            if (response.get("codigo").getAsInt() == 200) {
                System.out.println("======= Sucesso! =======");
            } else if (response.get("codigo").getAsInt() == 500) {
                System.out.println(response.get("mensagem").getAsString());
            } else {
                System.out.println("=== Erro Inesperado! ===");
            }
        } catch (NullPointerException e) {
            System.err.println("Erro: Retorno nulo!");
        } catch (JsonSyntaxException e) {
            System.err.println("Erro: Retorno inválido!");
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
