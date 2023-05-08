package utils;

public class CaesarCrypt {
        public static String encrypt(String pass) {
        int i;
        int key = pass.length();
        String encripted = "";

        for (i = 0; i < key; i++) {
            encripted = encripted + (char) (pass.charAt(i) + key);
        }

        return (encripted);
    }

    public static String hashed(String pswd) {
    	
    	String hashed = "";
    	
        for (int i = 0; i < pswd.length(); i++) {
            char c = pswd.charAt(i);
            int asciiValue = (int) c;
            int novoAsciiValue = asciiValue + pswd.length();
            if (novoAsciiValue > 127) {
                novoAsciiValue = novoAsciiValue - 127 + 32;
            }
            char novoCaractere = (char) novoAsciiValue;
            hashed += novoCaractere;
        }
        return hashed;
    }
}
