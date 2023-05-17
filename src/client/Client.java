package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;
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
        // String ip = "0.0.0.0"; // inet wifi
        // String ip = "127.0.0.1"; // localhost
        // String ip = "10.20.8.198"; // sauter
        // String ip = "10.20.8.81"; // kenji
        // String ip = "10.20.8.153"; // igor
        // String ip = "10.20.8.77"; // mairon
        // String ip = "10.50.3.13"; // mairon 2
        // String ip = "10.40.11.114"; // edu
        // String ip = "10.20.8.132"; // pedro
        // String ip = "10.20.8.196"; // mitz
        // String ip = "10.40.11.3"; // mitz 2
        // String ip = "10.20.8.93"; // quintero

        // String ip = "26.20.133.105"; // radmin kenji
        // String ip = "26.157.130.119"; // radmin sauter
        // String ip = "26.59.167.57"; // radmin salles
        // String ip = "26.211.0.15"; // radmin sanches
        // String ip = "26.28.97.231"; // radmin quintero

        // String ip = "10.20.8.196"; // teste

        Scanner input = new Scanner(System.in);
        System.out.print("IP do servidor: ");
        String serverHostname = new String(input.nextLine());

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        Gson gson = new Gson();
        String userInput;
        boolean shouldStop = false;
        boolean invalidOperation = false;

        if (args.length > 0)
            serverHostname = args[0];
        System.out.println("Attemping to connect to host " +
                serverHostname);

        try {
            System.out.print("Porta do servidor (int): ");
            echoSocket = new Socket(serverHostname, input.nextInt());
            input.nextLine();
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
                    json.addProperty("senha", CaesarCrypt.hashed(senha));
                    senha = "";

                    System.out.println("\nsending to server...");
                    System.out.println(json + "\n");

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
                    json.addProperty("senha", CaesarCrypt.hashed(senha));
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
                    json.addProperty("senha", CaesarCrypt.hashed(senha));
                    senha = "";

                    System.out.println("\nsending to server...");
                    System.out.println(json + "\n");

                    out.println(json);

                    break;
                }

                case "9": {
                    System.out.println("-------------LOGOUT-------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

                    json.addProperty("id_usuario", userRepository.getId());
                    json.addProperty("token", userRepository.getToken());

                    System.out.println("\nsending to server...");
                    System.out.println(json + "\n");
                    out.println(json);

                    break;
                }

                case "Bye": {
                    shouldStop = true;
                    break;
                }

                default: {
                    System.out.println("Opção inválida!");
                    invalidOperation = true;
                    break;
                }
            }

            if (shouldStop)
                break;

            if (invalidOperation) {
                System.out.println("\n------------------------------------\n");
                invalidOperation = false;
                continue;
            }

            try {
                responseFromServer(in, gson, userInput);
            } catch (JsonSyntaxException e) {
                System.err.println("Erro: Json retornado invalido");
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }

            System.out.println("\n------------------------------------\n");
        }

        out.close();
        in.close();
        teclado.close();
        echoSocket.close();
        input.close();
    }

    private static void responseFromServer(BufferedReader in, Gson gson, String operation) throws IOException {
        JsonObject response;

        if ((response = gson.fromJson(in.readLine(), JsonObject.class)) == null) {
            System.out.println("Json retornado nulo");
            return;
        }

        System.out.println("Json server return: " + response + "\n");

        if (response.get("codigo").getAsInt() == 200) {
            System.out.println("======= Sucesso! =======");
        } else if (response.get("codigo").getAsInt() == 500) {
            System.out.println("======== Falha! ========");
            System.out.println("Mensagem: " + response.get("mensagem").getAsString());
        } else {
            System.out.println("=== Erro Inesperado! ===");
        }
        switch (operation) {
            case "3": {
                try {
                    userRepository.setId(response.get("id_usuario").getAsInt());
                    userRepository.setToken(response.get("token").getAsString());
                    break;
                } catch (NullPointerException e) {
                    System.err.println("Login incompleto");
                    break;
                }
            }
            case "9": {
                // userRepository.setId(0);
                // userRepository.setToken("");
                break;
            }
        }
    }
}
