package utils;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class JsonVerify {
    public static boolean register(JsonObject json) {
        try {
            if (json.has("nome") && json.has("email") && json.has("senha")) {
                if (!json.get("nome").equals(JsonNull.INSTANCE)
                        && !json.get("email").equals(JsonNull.INSTANCE)
                        && !json.get("senha").equals(JsonNull.INSTANCE))
                    return true;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de registro");
        }
        return false;
    }

    public static boolean updateUser(JsonObject json) {
        try {
            if (tokenAndId(json))
                if (json.has("nome") || json.has("email") || json.has("senha"))
                    if (!json.get("nome").equals(JsonNull.INSTANCE)
                            || !json.get("email").equals(JsonNull.INSTANCE)
                            || !json.get("senha").equals(JsonNull.INSTANCE))
                        return true;
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de atualização de usuário");
        }
        return false;
    }

    public static boolean login(JsonObject json) {
        try {
            if (json.has("email") && json.has("senha")) {
                if (!json.get("email").equals(JsonNull.INSTANCE)
                        && !json.get("senha").equals(JsonNull.INSTANCE))
                    return true;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de login");
        }
        return false;
    }

    public static boolean reportIncident(JsonObject json) {
        try {
            // validar dados de report de incidente
            if (tokenAndId(json))
                return true;
        } catch(Exception e) {
            System.out.println("Erro ao verificar dados de reportar incidente");
        }
        return false;
    }

    public static boolean tokenAndId(JsonObject json) {
        try {
            if (json.has("token") && json.has("id_usuario")) {
                if (!json.get("token").equals(JsonNull.INSTANCE)
                        && !json.get("id_usuario").equals(JsonNull.INSTANCE))
                    return true;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de logout");
        }
        return false;
    }

}
