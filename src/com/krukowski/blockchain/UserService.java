package com.krukowski.blockchain;

import java.util.Scanner;

public class UserService implements Runnable {

    @Override
    public void run() {
        while (Blockchain.getCurrentBlocks().size() < Blockchain.NUMBER_OF_BLOCKS_TO_PRINT) {
            try {
                write();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void write() throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (Blockchain.getCurrentBlocks().size() < Blockchain.NUMBER_OF_BLOCKS_TO_PRINT) {
            Transaction transaction = new Transaction(scanner.nextLine());
            Blockchain.saveTransaction(transaction);
        }

    }

}
