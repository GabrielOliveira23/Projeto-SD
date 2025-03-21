package entities;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonObject;

import database.IncidentDB;
import database.UserDB;
import utils.DataVerify;

public class User {
    private int id;
    private String name;
    private String email;
    private String token;
    private String password;

    public User() {
        this.name = "";
        this.email = "";
        this.token = "";
        this.password = null;
        this.id = -1;
    }

    public JsonObject login() {
        JsonObject json = new JsonObject();
        printLogin(email, password);

        json = DataVerify.login(this, this.getEmail(), this.getPassword());

        return json;
    }

    public JsonObject register() {
        JsonObject json = new JsonObject();
        printRegister(this.getName(), this.getEmail(), this.getPassword());

        json = DataVerify.registerUser(this.getName(), this.getEmail(), this.getPassword());

        if (json.get("codigo").getAsInt() == 200) {
            JsonObject user = new JsonObject();
            user.addProperty("id_usuario", this.createId());
            user.addProperty("nome", this.getName());
            user.addProperty("email", this.getEmail());
            managePassword(this.getPassword(), user);
            user.add("token", null);

            UserDB.insertOne(user);
        }

        return json;
    }

    public JsonObject update(String token, int idUsuario) {
        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        printUpdate();

        user.addProperty("nome", this.getName());
        user.addProperty("email", this.getEmail());
        managePassword(this.getPassword(), user);
        user.addProperty("token", token);

        if ((json = UserDB.isLogged(idUsuario, token)).get("codigo").getAsInt() == 200)
            if ((json = DataVerify.updateUser(this)).get("codigo").getAsInt() == 200)
                json = UserDB.update(idUsuario, user);

        return json;
    }

    public JsonObject logout(int userId, String token) {
        JsonObject json = new JsonObject();

        if ((json = UserDB.isLogged(userId, token)).get("codigo").getAsInt() == 200)
            if ((UserDB.updateToken(userId, null)) != null) {
                json = new JsonObject();
                json.addProperty("codigo", 500);
                json.addProperty("mensagem", "logout nao realizado");
            }

        return json;
    }

    public JsonObject getLoggedUsers() {
        return UserDB.getAllLogged();
    }

    public JsonObject delete(String token, int userId) {
        JsonObject json = new JsonObject();

        if (UserDB.auth(email, password)) {
            if ((json = UserDB.isLogged(userId, token)).get("codigo").getAsInt() == 200)
                if ((json = DataVerify.deleteUser(this)).get("codigo").getAsInt() == 200)
                    if ((json = IncidentDB.setNullOnIncidents(userId)).get("codigo").getAsInt() == 200)
                        json = UserDB.delete(userId);
        } else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email ou senha incorretos");
        }

        return json;
    }

    private void managePassword(String password, JsonObject user) {
        if (password == null || password.isEmpty())
            return;

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        user.addProperty("senha", hashed);
    }

    private void printRegister(String nome, String email, String senha) {
        System.out.println("Executando Registro...");
        System.out.println("Nome: " + nome);
        System.out.println("Email: " + email);
        System.out.println("Senha: " + senha);
        System.out.println();
    }

    private void printUpdate() {
        System.out.println("Executando Atualizacao...");
        System.out.println("Nome: " + this.getName());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Senha: " + this.getPassword());
        System.out.println();
    }

    private void printLogin(String email, String senha) {
        System.out.println("Executando Login...");
        System.out.println("Email: " + email);
        System.out.println("Senha: " + senha);
        System.out.println();
    }

    public int createId() {
        return UserDB.getCount() + 1;
    }

    public int getId(String email) {
        return UserDB.getByEmail(email).get("id_usuario").getAsInt();
    }

    public int getId() {
        return this.id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String senha) {
        this.password = senha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
