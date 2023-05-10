package entities;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonObject;

import database.UserDB;

public class User {
    private int id;
    private String email;
    private String token;
    private String password;
    private boolean fakeLogin = false;

    public User() {
        this.email = "";
        this.token = "";
        this.password = null;
        this.id = -1;
    }

    public JsonObject login(String email, String password) {
        JsonObject json = new JsonObject();
        printLogin(email, password);
        if (this.fakeLogin) {
            json.addProperty("codigo", 200);
            return json;
        }

        json = dataVerifyLogin(email, password);

        return json;
    }

    public JsonObject create(String nome, String email, String senha) {
        JsonObject json = new JsonObject();
        printRegister(nome, email, senha);
        if (this.fakeLogin) {
            json.addProperty("codigo", 200);
            return json;
        }

        json = dataVerifyCreate(nome, email, senha);

        if (json.get("codigo").getAsInt() == 200) {
            JsonObject user = new JsonObject();
            user.addProperty("id_usuario", this.createId());
            user.addProperty("nome", nome);
            user.addProperty("email", email);
            managePassword(senha, user);
            user.add("token", null);

            UserDB.insertUser(user);
        }

        return json;
    }

    public JsonObject logout(int idUsuario, String token) {
        JsonObject json = new JsonObject();

        if (token.equals(this.getToken())) {
            if (UserDB.updateToken(idUsuario, null)) {
                json.addProperty("codigo", 200);
                this.setToken(null);
            } else {
                json.addProperty("codigo", 500);
                json.addProperty("mensagem", "Id nao encontrado");
            }
        } else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Token invalido");
        }

        return json;
    }

    private void managePassword(String password, JsonObject user) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        user.addProperty("senha", hashed);
    }

    private boolean nameChecker(String name) {
        if (name.length() >= 3 && name.length() <= 32 && !name.matches("[0-9]+"))
            return true;

        return false;
    }

    private boolean emailChecker(String email, boolean isRegister) {
        if (isRegister) {
            // verificar se o email já existe no banco de dados
            if (UserDB.getUserByEmail(email) != null) {
                System.out.println("Email ja cadastrado!");
                return false;
            }
        }

        if (email.contains("@") && email.length() >= 16 // && email.contains(".")
                && email.length() <= 50) {
            return true;
        }

        return false;
    }

    private boolean passwordChecker(String password) {
        if (password.length() >= 8 && password.length() <= 32)
            return true;

        return false;
    }

    private JsonObject dataVerifyCreate(String nome, String email, String senha) {
        JsonObject json = new JsonObject();
        if (emailChecker(email, true))
            if (nameChecker(nome))
                if (passwordChecker(senha))
                    json.addProperty("codigo", 200);
                else {
                    json.addProperty("codigo", 500);
                    json.addProperty("mensagem", "Senha invalida");
                }
            else {
                json.addProperty("codigo", 500);
                json.addProperty("mensagem", "Nome invalido");
            }
        else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email invalido ou ja cadastrado");
        }

        return json;
    }

    private JsonObject dataVerifyLogin(String email, String senha) {
        JsonObject json = new JsonObject();

        if (emailChecker(email, false)) {
            if (passwordChecker(senha)) {
                if (UserDB.authUser(email, senha)) {
                    json.addProperty("codigo", 200);
                    json.addProperty("token", this.generateToken());
                    json.addProperty("id_usuario", this.getId(email));

                    this.setToken(json.get("token").getAsString());
                    this.setId(json.get("id_usuario").getAsInt());

                    UserDB.updateToken(this.getId(), this.getToken());

                } else {
                    System.out.println("--------- email ou senha incorreto ---------");
                    json.addProperty("codigo", 500);
                    json.addProperty("mensagem", "Email ou senha incorreto");
                }
            } else {
                System.out.println("--------- senha invalida ---------");
                json.addProperty("codigo", 500);
                json.addProperty("mensagem", "Email ou senha invalido");
            }
        } else {
            System.out.println("--------- email invalido ---------");
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Email ou senha invalido");
        }

        return json;
    }

    private String generateToken() {
        String token = BCrypt.hashpw(this.email, BCrypt.gensalt());
        return token;
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
        System.out.println();
    }

    public int createId() {
        return UserDB.getCountUsers() + 1;
    }

    public int getId(String email) {
        return UserDB.getUserByEmail(email).get("id_usuario").getAsInt();
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

    public boolean isFakeLogin() {
        return fakeLogin;
    }

    public void setFakeLogin(boolean fakeLogin) {
        this.fakeLogin = fakeLogin;
    }

}
