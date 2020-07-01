package com.krukowski.blockchain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class UserService implements Runnable {

    public static boolean stopMining = false;

    @Override
    public void run() {
        while (write()) {
        }
        stopMining = true;
    }

    private boolean write() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("exit")) {
            return false;
        }
        saveTransaction(input);

        return true;
    }

    private void saveTransaction(String input) {
        try {
            Blockchain.saveTransaction(makeTransaction(input));
        } catch (Exception e) {
            System.out.println("Exception occurred during saving transaction.");
        }

    }

    private Transaction makeTransaction(String input) {
        try {
            return new Transaction(input);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
            System.out.println("Exception occurred during making new transaction.");
        }
        return null;
    }

}
