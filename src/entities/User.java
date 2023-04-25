package entities;

import com.google.gson.JsonObject;

import database.UserDB;

public class User {
    private int id;
    private String email;
    private String token;
    private Password password;
    private boolean fakeLogin = false;

    public User() {
        this.email = "";
        this.token = "";
        this.password = null;
        this.id = UserDB.getCountUsers() + 1;
    }

    public JsonObject login(String email, String password) {
        // System.out.println(UserDB.getUserById(1)); // aplicar isto quanod precisar
        // usar find
        JsonObject json = new JsonObject();
        printLogin(email, password);
        if (this.fakeLogin) {
            json.addProperty("codigo", 200);
            return json;
        }

        json = dataVerify(email, password);

        return json;
    }

    public JsonObject create(String nome, String email, String senha) {
        JsonObject json = new JsonObject();
        printRegister(nome, email, senha);
        if (this.fakeLogin) {
            json.addProperty("codigo", 200);
            return json;
        }

        json = dataVerify(nome, email, senha);

        if (json.get("codigo").getAsInt() == 200) {
            JsonObject user = new JsonObject();
            user.addProperty("id_usuario", this.id);
            user.addProperty("nome", nome);
            user.addProperty("email", email);
            managePassword(senha, user);
            user.addProperty("token", "abc123abcabc"); // gerar token depois

            UserDB.insertUser(user);
        }

        return json;
    }

    private void managePassword(String senha, JsonObject user) {
        Password password = new Password(senha);
        JsonObject passwordJson = new JsonObject();

        passwordJson.addProperty("password", password.getPassword());
        passwordJson.addProperty("salt", password.getSalt());

        user.add("senha", passwordJson);
    }

    private JsonObject dataVerify(String nome, String email, String senha) {
        JsonObject json = new JsonObject();

        if (nome.length() <= 3 || nome.length() >= 32 || nome.matches("[0-9]+")) {
            System.out.println("-------- Erro no nome --------");
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Nome inválido");
        } else if (!email.contains("@") || !email.contains(".") || email.length() <= 16
                || email.length() >= 50) {
            System.out.println("-------- Erro no email --------");
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email inválido");
        } else if (senha.length() < 8 || senha.length() > 32) {
            System.out.println("-------- Erro na senha --------");
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Senha inválido");
        } else {
            json.addProperty("codigo", 200);
        }

        return json;
    }

    private JsonObject dataVerify(String email, String senha) {
        JsonObject json = new JsonObject();

        if (email.contains("@") && email.contains(".") && email.length() >= 16 || email.length() <= 50) {
            if (senha.length() >= 8 && senha.length() <= 32) {
                if (UserDB.authUser(email, senha)) {
                    json.addProperty("codigo", 200);
                } else {
                    System.out.println("--------- email ou senha incorreto ---------");
                    json.addProperty("codigo", 500);
                    json.addProperty("mensagem", "Email ou senha incorreto");
                }
            } else {
                System.out.println("--------- senha inválida ---------");
                json.addProperty("codigo", 500);
                json.addProperty("mensagem", "Email ou senha inválido");
            }
        } else {
            System.out.println("--------- email inválido ---------");
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email ou senha inválido");
        }

        return json;
    }

    private void printRegister(String nome, String email, String senha) {
        System.out.println("Executando Registro...");
        System.out.println("Nome: " + nome);
        System.out.println("Email: " + email);
        System.out.println("Senha: " + senha);
        System.out.println("ID: " + this.id);
        System.out.println();
    }

    private void printLogin(String email, String senha) {
        System.out.println("Executando Login...");
        System.out.println("Email: " + email);
        System.out.println("Senha: " + senha);
        System.out.println("ID: " + this.id);
        System.out.println();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password senha) {
        this.password = senha;
    }

    public boolean isFakeLogin() {
        return fakeLogin;
    }

    public void setFakeLogin(boolean fakeLogin) {
        this.fakeLogin = fakeLogin;
    }

}
