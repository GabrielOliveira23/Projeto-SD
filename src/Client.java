
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import utils.CaesarCrypt;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        new Client();
        // String ip = "10.20.8.198"; // sauter
        // String ip = "10.20.8.81"; // lucas
        String ip = "127.0.0.1"; // localhost
        int port = 24001;
        String serverHostname = new String(ip);

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        JsonObject response = new JsonObject();
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
            System.out.println("12 - Fazer Logout");
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
                    json.addProperty("senha", CaesarCrypt.encrypt(senha, senha.length()));
                    senha = "";

                    System.out.println("\nsending to server...\n");
                    out.println(json);

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
                    json.addProperty("senha", CaesarCrypt.encrypt(senha, senha.length()));
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
                    json.addProperty("senha", CaesarCrypt.encrypt(senha, senha.length()));
                    senha = "";

                    System.out.println("\nsending to server...\n");
                    out.println(json);

                    break;
                }

                case "12": {
                    System.out.println("-------------LOGOUT-------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

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

            response = sendToServer(in, gson);
            System.out.println("\n------------------------------------\n");
        }

        out.close();
        in.close();
        teclado.close();
        echoSocket.close();
    }

    private static JsonObject sendToServer(BufferedReader in, Gson gson) throws IOException {
        JsonObject response;
        response = gson.fromJson(in.readLine(), JsonObject.class);
        System.out.println("server return: " + response);

        if (response.get("codigo").getAsInt() == 200) {
            System.out.println("======= Sucesso! =======");
        } else if (response.get("codigo").getAsInt() == 500) {
            System.out.println(response.get("mensagem").getAsString());
        }

        return response;
    }
}
