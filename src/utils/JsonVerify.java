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
            if (hasTokenAndId(json))
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
            if (hasTokenAndId(json))
                if (json.has("data") && json.has("rodovia")
                        && json.has("km") && json.has("tipo_incidente"))
                    if (!json.get("data").equals(JsonNull.INSTANCE)
                            && !json.get("rodovia").equals(JsonNull.INSTANCE)
                            && !json.get("km").equals(JsonNull.INSTANCE)
                            && !json.get("tipo_incidente").equals(JsonNull.INSTANCE))
                        return true;
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de reportar incidente");
        }
        return false;
    }

    public static boolean getIncidents(JsonObject json) {
        try {
            if (json.has("data") && json.has("rodovia")
                    && json.has("periodo"))
                if (!json.get("data").equals(JsonNull.INSTANCE)
                        && !json.get("rodovia").equals(JsonNull.INSTANCE)
                        && !json.get("periodo").equals(JsonNull.INSTANCE))
                    return true;
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de obter incidentes");
        }
        return false;
    }

    public static boolean getUserIncidents(JsonObject json) {
        try {
            if (hasTokenAndId(json))
                return true;
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de obter incidentes de usuário");
        }
        return false;
    }

    public static boolean removeIncident(JsonObject json) {
        try {
            if (hasTokenAndId(json))
                if (json.has("id_incidente"))
                    if (!json.get("id_incidente").equals(JsonNull.INSTANCE))
                        return true;
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de remover incidente");
        }
        return false;
    }

    public static boolean hasTokenAndId(JsonObject json) {
        try {
            if (json.has("token") && json.has("id_usuario")) {
                if (!json.get("token").equals(JsonNull.INSTANCE)
                        && !json.get("id_usuario").equals(JsonNull.INSTANCE))
                    return true;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar dados de usuário, id ou token invalido");
        }
        return false;
    }

}
