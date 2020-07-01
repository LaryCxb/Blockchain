package com.krukowski.blockchain;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printHelloMessage();
        scanner.next();

        Thread[] miners = new Thread[10];
        Thread userService = new Thread(new UserService());
        GenerateKeys.generate();

        File file = new File("Blocks.txt");
        if (file.exists()) {
            file.delete();
        }

        userService.start();

        employMiners(miners);

        try {
            userService.join();
        } catch (InterruptedException e) {
            System.out.println("User Thread interrupted");
        }
        dismissMiners(miners);
    }

    private static void printHelloMessage() {
        System.out.println("Welcome in blockchain generator.\n" +
                "It allows to transfer Virtual Coins (VC) between users.\n" +
                "Each user gets 100 VC at start, and Miners get additional 100 VC\n" +
                "for finding the block.\n" +
                "You can add transactions data to blockchain,\n" +
                "by writing transaction orders in following form:\n" +
                "<NameOfSender> sends <Number> VC to <NameOfReceiver>\n" +
                "\nBlock will be printed, once it is found, even if no transactions are ordered.\n" +
                "\nTo start generator type anything and press Enter.\n" +
                "To exit program type exit and press Enter.\n");
    }

    private static void employMiners(Thread[] miners) {
        for (int i = 0; i < miners.length; i++) {
            miners[i] = new Thread(new Miner(), "miner" + i);
            Blockchain.users.add(new Person(miners[i].getName()));
            miners[i].start();
        }
    }

    private static void dismissMiners(Thread[] miners) {
        Arrays.stream(miners).forEach(miner -> {
            try {
                miner.join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException!");
            }
        });
    }
}

