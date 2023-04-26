
import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Client {
    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1";
        int port = 24001;
        String serverHostname = new String(ip);

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        JsonObject json = new JsonObject(), response = new JsonObject();
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

            System.out.println("Selecione uma opção: ");
            System.out.println("1 - Cadastrar Usuário");
            System.out.println("2 - Atualizar Cadastro");
            System.out.println("3 - Fazer Login");
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
                    json.addProperty("senha", teclado.readLine());

                    System.out.println("\nsending to server...\n");
                    out.println(json);

                    break;
                }
                case "2": {
                    System.out.println("-------------ATUALIZAR CADASTRO-------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

                    System.out.print("Nome: ");
                    json.addProperty("nome", teclado.readLine());

                    System.out.print("Email: ");
                    json.addProperty("email", teclado.readLine());

                    System.out.print("Senha: ");
                    json.addProperty("senha", teclado.readLine());

                    
                    break;
                }
                case "3": {
                    System.out.println("-------------LOGIN-------------");
                    json.addProperty("id_operacao", Integer.parseInt(userInput));

                    System.out.print("Email: ");
                    json.addProperty("email", teclado.readLine());

                    System.out.print("Senha: ");
                    json.addProperty("senha", teclado.readLine());

                    // gerar token aleatorio
                    json.addProperty("token", "12345");

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

            response = gson.fromJson(in.readLine(), JsonObject.class);
            System.out.println("server return: " + response);

            if (response.get("codigo").getAsInt() == 200) {
                System.out.println("======= Sucesso! =======");
            } else if (response.get("codigo").getAsInt() == 500) {
                System.out.println(response.get("mensagem").getAsString());
            }

            System.out.println("\n------------------------------------\n");
        }

        out.close();
        in.close();
        teclado.close();
        echoSocket.close();
    }
}
