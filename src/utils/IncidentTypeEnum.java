package utils;

public enum IncidentTypeEnum {
    VENTO(1),
    CHUVA(2),
    NEBLINA(3),
    NEVE(4),
    GELO_NA_PISTA(5),
    GRANIZO(6),
    TRANSITO_PARADO(7),
    FILAS_DE_TRANSITO(8),
    TRANSITO_LENTO(9),
    ACIDENTE_DESCONHECIDO(10),
    INCIDENTE_DESCONHECIDO(11),
    TRABALHOS_NA_ESTRADA(12),
    VIA_INTERDITADA(13),
    PISTA_INTERDITADA(14);

    private int numero;

    private IncidentTypeEnum(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public static String getDescription(int incident) {
        for(IncidentTypeEnum incidentType : IncidentTypeEnum.values()) {
            if(incidentType.getNumero() == incident)
                return incidentType.name().replace("_", " ");
        }
        return "";
    }

    public static int getEnum(String incident) {
        for(IncidentTypeEnum incidentType : IncidentTypeEnum.values()) {
            if(incidentType.name().equals(incident.replace(" ", "_").toUpperCase()))
                return incidentType.getNumero();
        }
        return -1;
    }
}
