package utils;

import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonObject;

import database.UserDB;
import entities.Incident;
import entities.User;

public class DataVerify {
    private String regex;
    private static String listError = "";

    public static boolean name(String name) {
        if (name.length() >= 3 && name.length() <= 32 && !name.matches("[0-9]+"))
            return true;

        return false;
    }

    public static boolean email(String email, boolean isRegister) {
        if (isRegister) {
            // verificar se o email já existe no banco de dados
            if (UserDB.getByEmail(email) != null) {
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

    public static boolean password(String password) {
        if (password.length() >= 8 && password.length() <= 32)
            return true;

        return false;
    }

    public static JsonObject register(String nome, String email, String senha) {
        JsonObject json = new JsonObject();
        if (email(email, true))
            if (name(nome))
                if (password(senha))
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

    public static JsonObject update(User user) {
        JsonObject json = new JsonObject();

        if (user.getToken() != null) {
            if (user.getName() != "" && name(user.getName()))
                json.addProperty("codigo", 200);
            if (user.getEmail() != "" && email(user.getEmail(), false))
                json.addProperty("codigo", 200);
            if (user.getPassword() != "" && password(user.getPassword()))
                json.addProperty("codigo", 200);

            if (!json.has("codigo")) {
                json.addProperty("codigo", 500);
                json.addProperty("mensagem", "Token invalido");
            }
        } else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Token invalido");
        }

        return json;
    }

    public static JsonObject login(User user, String email, String senha) {
        JsonObject json = new JsonObject();

        if (DataVerify.email(email, false)) {
            if (DataVerify.password(senha)) {
                if (UserDB.auth(email, senha)) {
                    json.addProperty("codigo", 200);
                    json.addProperty("token", generateToken());
                    json.addProperty("id_usuario", user.getId(email));

                    user.setToken(json.get("token").getAsString());
                    user.setId(json.get("id_usuario").getAsInt());

                    UserDB.updateToken(user.getId(), user.getToken());

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

    public static String generateToken() {
        String token = BCrypt.hashpw("alguma criptografia", BCrypt.gensalt());
        return token;
    }

    public static JsonObject reportIncident(Incident incident) {
        JsonObject json = new JsonObject();
        if (date(incident.getDate())
                && highway(incident.getHighway())
                && km(incident.getKm())
                && incidentType(incident.getIncidentType()))
            json.addProperty("codigo", 200);
        else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", "Erro nos campos: " + listError);
        }
        return json;
    }

    public static boolean date(String date) {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        if (Pattern.matches(regex, date))
            return true;
        listError.concat(" data");
        return false;
    }

    private static boolean highway(String highway) {
        String regex = "^[A-Za-z]{2}-\\d{3}$";
        if (Pattern.matches(regex, highway))
            return true;
        listError.concat(" rodovia");
        return false;
    }

    private static boolean km(int km) {
        String regex = "^[0-9]{1,3}$";
        if (Pattern.matches(regex, String.valueOf(km)))
            return true;
        listError.concat(" km");
        return false;
    }

    private static boolean incidentType(int type) {
        String regex = "^[1-5]$";
        return Pattern.matches(regex, String.valueOf(type));
    }
}
