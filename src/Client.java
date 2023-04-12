
import java.io.*;
import java.net.*;
import com.google.gson.Gson;

public class Client {
    public static void main(String[] args) throws IOException {

        String serverHostname = new String("127.0.0.1");

        if (args.length > 0)
            serverHostname = args[0];
        System.out.println("Attemping to connect to host " +
                serverHostname + " on port 10008.");

        Socket echoSocket = null;
        PrintWriter server = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 10008);
            server = new PrintWriter(echoSocket.getOutputStream(), true);
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

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        while (true) {

            System.out.println("Selecione uma opção: ");
            System.out.println("1 - Fazer Login");
            System.out.println("2 - Cadastrar Usuário");
            System.out.println("3 - Fazer Logout");
            System.out.println("\"Bye\" to quit");

            if ((userInput = teclado.readLine()) == null)
                break;

            switch (userInput) {
                case "1": {
                    System.out.println("-------------LOGIN-------------");
                    System.out.println("Email: ");
                    String email = teclado.readLine();
                    System.out.println("Senha: ");
                    Integer senha = Integer.parseInt(teclado.readLine());
                    String token = "1234";

                    Gson gson = new Gson();
                    server.println(gson.toJson(new Login(email, senha, token)));

                    break;
                }
                case "2": {
                    System.out.println("-------------CADASTRO-------------");
                    System.out.println("Nome: ");
                    String nome = teclado.readLine();
                    System.out.println("Email: ");
                    String email = teclado.readLine();
                    System.out.println("Senha: ");
                    Integer senha = Integer.parseInt(teclado.readLine());
                    System.out.println("-------------------------------");

                    Gson gson = new Gson();
                    // saida.println(gson.toJson(new Login(nome, email, senha)));

                    break;
                }
            }
            if (userInput.equals("Bye"))
                break;

            System.out.println("-------------------------------");
            System.out.println("client: " + in.readLine());
        }

        server.close();
        in.close();
        teclado.close();
        echoSocket.close();
    }
}
