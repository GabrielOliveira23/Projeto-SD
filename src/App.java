import java.util.Scanner;

import utils.CaesarCrypt;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Scanner input = new Scanner(System.in);
        
        System.out.println(CaesarCrypt.encrypt(input.nextLine()));
        System.out.println(CaesarCrypt.hashed(input.nextLine()));

        input.close();
    }
}
