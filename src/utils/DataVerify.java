package utils;

import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonObject;

import database.UserDB;
import entities.Incident;
import entities.User;

public class DataVerify {
    private static String regex;
    private static String listError = "Erro nos campos:";

    public static boolean name(String name) {
        if (name.length() >= 3 && name.length() <= 32 && !name.matches("[0-9]+"))
            return true;

        return false;
    }

    public static boolean email(String email, boolean isRegister) {
        if (isRegister) {
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

    public static JsonObject registerUser(String nome, String email, String senha) {
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

    public static JsonObject updateUser(User user) {
        JsonObject json = new JsonObject();

        if (!user.getToken().isEmpty())
            if (user.getName() != "" && name(user.getName()))
                if (user.getEmail() != "" && email(user.getEmail(), false))
                    if (user.getPassword() != "" && password(user.getPassword())) {
                        json.addProperty("codigo", 200);
                        return json;
                    } else
                        json.addProperty("mensagem", "Senha invalida");
                else
                    json.addProperty("mensagem", "Email invalido");
            else
                json.addProperty("mensagem", "Nome invalido");
        else
            json.addProperty("mensagem", "Token invalido");

        json.addProperty("codigo", 500);
        return json;
    }

    public static JsonObject deleteUser(User user) {
        JsonObject json = new JsonObject();

        if (!user.getToken().isEmpty())
            if (user.getEmail() != "" && email(user.getEmail(), false))
                if (user.getPassword() != "" && password(user.getPassword())) {
                    json.addProperty("codigo", 200);
                    return json;
                } else
                    json.addProperty("mensagem", "Senha invalida");
            else
                json.addProperty("mensagem", "Email invalido");
        else
            json.addProperty("mensagem", "Token invalido");

        json.addProperty("codigo", 500);
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

    public static boolean highway(String highway) {
        regex = "^[A-Za-z]{2}-\\d{3}$";
        if (Pattern.matches(regex, highway))
            return true;
        listError = listError.concat(" rodovia");
        return false;
    }

    public static boolean km(int km) {
        regex = "^[0-9]{1,3}$";
        if (Pattern.matches(regex, String.valueOf(km)))
            return true;
        listError = listError.concat(" km");
        return false;
    }

    public static boolean highwayLane(String highwayLane) {
        if (highwayLane.equals(""))
            return true;

        regex = "^[0-9]{1,3}-[0-9]{1,3}$";
        if (Pattern.matches(regex, highwayLane))
            return true;

        listError = listError.concat(" faixaKm");
        return false;
    }

    public static boolean period(int period) {
        regex = "^[1-4]$";
        if (Pattern.matches(regex, String.valueOf(period)))
            return true;
        listError = listError.concat(" periodo");
        return false;
    }

    public static JsonObject getIncidents(Incident incident) {
        JsonObject json = new JsonObject();
        if (date(incident.getDate())
                && highway(incident.getHighway())
                && highwayLane(incident.getHighwayLane())
                && period(incident.getPeriod()))
            json.addProperty("codigo", 200);
        else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", listError);
        }
        return json;
    }

    public static JsonObject updateIncident(Incident incident) {
        JsonObject json = new JsonObject();
        if (date(incident.getDate())
                && highway(incident.getHighway())
                && km(incident.getKm())
                && incidentType(incident.getIncidentType()))
            json.addProperty("codigo", 200);
        else {
            json.addProperty("codigo", 500);
            json.addProperty("mensagem", listError);
        }
        return json;
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
            json.addProperty("mensagem", listError);
        }
        return json;
    }

    public static boolean hour(String hour) {
        if (Integer.parseInt(hour) >= 0 && Integer.parseInt(hour) <= 23)
            return true;
        return false;
    }

    public static boolean minute(String minute) {
        if (Integer.parseInt(minute) >= 0 && Integer.parseInt(minute) <= 59)
            return true;
        return false;
    }

    public static String generateToken() {
        String token = BCrypt.hashpw("alguma criptografia", BCrypt.gensalt());
        return token;
    }

    private static boolean incidentType(int type) {
        regex = "^[0-9]{1,2}$";
        if (Pattern.matches(regex, String.valueOf(type)))
            return true;
        listError = listError.concat(" tipoIncidente");
        return false;
    }

    private static boolean date(String date) {
        regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        if (Pattern.matches(regex, date))
            return true;
        listError = listError.concat(" data");
        return false;
    }
}
