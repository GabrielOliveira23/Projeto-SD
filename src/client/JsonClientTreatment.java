package client;

import com.google.gson.JsonObject;

public class JsonClientTreatment {
	public static boolean responseTreatment(JsonObject response) {
		if (response.get("codigo").getAsInt() == 200) {
			return true;
		} else if (response.get("codigo").getAsInt() == 500) {
			System.out.println("Erro ao pegar lista de incidentes");
			System.out.println(response.get("mensagem").getAsString());
			return false;
		} else {
			System.out.println("Codigo retornado invalido");
			return false;
		}
	}
}
