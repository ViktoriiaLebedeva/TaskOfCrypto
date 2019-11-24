package task_of_crypto;

import java.util.Scanner;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter key");
        String key = in.next();
        System.out.println("Enter text");
        String text = in.next();
        Gost gost = new Gost();
        String encryptedText = gost.encryption(text,key);
        System.out.println("Encrypted text: " + encryptedText);
        System.out.println("Decrypted text: " + gost.decryption(encryptedText,key));

    }
}
