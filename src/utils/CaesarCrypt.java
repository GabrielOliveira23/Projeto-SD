package utils;

public class CaesarCrypt {
    public static String encrypt(String pass, int key) {
        int i, n = pass.length();
        String encripted = "";

        for (i = 0; i < n; i++) {
            encripted = encripted + (char) (pass.charAt(i) + key);
        }

        return (encripted);
    }
}
