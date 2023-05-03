package utils;

import java.util.Scanner;

public class CriptoCesar {

    public static String encrypt(String msg, int chave) {
        char ascii;
        char x, y;
        
        while (chave >= 26) {//chave tem que ter o tamanho do alfabeto
            chave = chave - 26;
        }
        for (int i = 0; i < msg.length(); i++) {
         //Tratamento Letras minusculas  
            if (msg.charAt(i) >= 97 && msg.charAt(i) <= 122) {//letrans minusculas de acordo com a tabela ASCII
                if ((int) (msg.charAt(i) + chave) > 122) {
                    x = (char) (msg.charAt(i) + chave);
                    y = (char) (x - 122);
                    ascii = (char) (96 + y);
                    System.out.print(ascii + " ");
                } else {
                    ascii = (char) (msg.charAt(i) + chave);
                    System.out.print(ascii + " ");

                }
            }
	//Tratamento Letras mausculas
            if (msg.charAt(i) >= 65 && msg.charAt(i) <= 90) {
                if (msg.charAt(i) + chave > 90) {
                    x = (char) (msg.charAt(i) + chave);
                    y = (char) (x - 90);
                    ascii = (char) (64 + y);
                    System.out.print(ascii + " ");
                } else {
                    ascii = (char) (msg.charAt(i) + chave);
                    System.out.print(ascii + " ");
                }
            }
        }
    }
}
