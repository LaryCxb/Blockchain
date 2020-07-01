package com.krukowski.blockchain;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Blockchain {

    public static final int NUMBER_OF_BLOCKS_TO_PRINT = 5;
    private static ArrayList<Block> currentBlocks = new ArrayList<>();
    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static int messageIdentifier = 0;
    private static int maximumIdentifier = 0;
    public static final Set<Person> users = new HashSet<>();

    public static synchronized ArrayList<Block> fetchBlocks() {
        return readBlocksFromFile("Blocks.txt");
    }

    private static synchronized ArrayList<Block> readBlocksFromFile(String filename) {
        File file = new File(filename);
        ArrayList<Block> blocks = new ArrayList<>();
        if (file.exists()) {
            blocks = readBlockchain(file);

            if (!isBlockchainValid(blocks)) {
                blocks = new ArrayList<>();
            }
        }
        return blocks;
    }

    private static synchronized ArrayList<Block> readBlockchain(File file) {
        ArrayList<Block> blocks = new ArrayList<>();
        try {
            blocks = SerializationUtils.deserialize(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blocks;
    }

    public static synchronized boolean isBlockchainValid(List<Block> blocks) {
        for (int i = 1; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (!(block.hashFunction(true).equals(block.getHash())
                    && isPreviousHashCorrect(blocks, i)
                    && isPreviousIdentifierSmaller(blocks, i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPreviousIdentifierSmaller(List<Block> blocks, int i) {
        if (i > 0) {
            for (Transaction transaction : blocks.get(i).getTransactions()) {
                if(transaction.getIdentifier() <= blocks.get(i - 1).getTransactions().stream()
                        .map(Transaction::getIdentifier).max(Comparator.naturalOrder()).orElse(0)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static synchronized boolean isPreviousHashCorrect(List<Block> blocks, int i) {
        if (i > 0) {
            return blocks.get(i).getPreviousHash().equals(blocks.get(i - 1).getHash());
        }
        return false;
    }

    public static synchronized boolean addBlockIfValid(Block block) {
        ArrayList<Block> blocks = fetchBlocks();
        blocks.add(block);
        if (isBlockchainValid(blocks)) {
            addBlock(block);
            return true;
        }
        return false;
    }

    private static synchronized void addBlock(Block block) {
        saveBlockToFile(block);
        currentBlocks = fetchBlocks();
        printBlock(block);

        if (!block.getTransactions().isEmpty()) {
            maximumIdentifier = block.getTransactions().get(block.getTransactions().size() - 1).getIdentifier();
        }
        transactions = transactions.stream()
                .filter(transaction ->
                        transaction.getIdentifier() > maximumIdentifier)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static synchronized void saveBlockToFile(Block block) {
        String filename = "Blocks.txt";
        try {
            SerializationUtils.serialize(block, filename);
        } catch (IOException e) {
            System.out.println("An exception occurs during block serialization");
        }
    }

    public static void printBlocks(List<Block> blocks, int numberOfBlocksToPrint) {
        for (int i = 0; i < numberOfBlocksToPrint; i++) {
            printBlock(blocks.get(i));
        }
    }

    private synchronized static void printBlock(Block block) {
        System.out.println("Block:");
        System.out.println("Created by miner: " + block.getMinerName());
        System.out.println(block.getMinerName() + " gets 100 VC");
        System.out.println("Id: " + block.getId());
        System.out.println("Timestamp: " + block.getTimeStamp());
        System.out.println("Magic number: " + block.getMagicNumber());
        System.out.println("Hash of the previous block: \n" + block.getPreviousHash());
        System.out.println("Hash of the block: \n" + block.getHash());
        System.out.print("Block data: ");
        printMessages(block);
        System.out.println("Block was generating for " + block.getGenerationTime() + " seconds");
        System.out.println(getDifficultyChangeInfo(block));
        System.out.println();
    }

    private static void printMessages(Block block) {
        if (block.getTransactions().isEmpty()) {
            System.out.println("No transactions");
        } else {
            System.out.println();
            block.getTransactions().forEach(transaction -> System.out.println(transaction.getText()));
        }
    }

    private static String getDifficultyChangeInfo(Block block) {
        if (block.getGenerationTime() < 5) {
            return "N was increased to " + (block.getDifficultyNumber() + 1);
        } else if (block.getGenerationTime() > 20) {
            return "N was decreased by 1";
        } else {
            return "N stays the same";
        }
    }

    public static void saveTransaction(Transaction transaction) throws Exception {
        if (VerifyMessage.verify(transaction)) {
            if(makeTransaction(transaction)) {
                transactions.add(transaction);
            }
        }
    }

    private static boolean makeTransaction(Transaction transaction) {
        if (transaction.getText().matches("\\w+ sends \\d+ VC to \\w+")) {
            String[] transactionAttributes = transaction.getText().split(" ");
            Person sender = getPersonByName(transactionAttributes[0]);
            Person receiver = getPersonByName(transactionAttributes[5]);
            addUserIfAbsent(sender);
            addUserIfAbsent(receiver);
            return setBalancesIfPossible(sender, receiver, Integer.parseInt(transactionAttributes[2]));
        }
        return false;
    }

    private static boolean setBalancesIfPossible(Person sender, Person receiver, int value) {
        if (sender.getBalance() - value >= 0) {
            sender.setBalance(sender.getBalance() - value);
            receiver.setBalance(receiver.getBalance() + value);
            return true;
        }
        return false;
    }

    private static void addUserIfAbsent(Person user) {
        users.add(user);
    }

    private static Person getPersonByName(String name) {
        return users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElse(new Person(name));
    }

    public static synchronized List<Transaction> getTransactions() {
        return transactions;
    }

    public static synchronized ArrayList<Block> getCurrentBlocks() {
        return currentBlocks;
    }

    public static int getMessageIdentifier() {
        messageIdentifier++;
        return messageIdentifier;
    }

    public static int getMaximumIdentifier() {
        return maximumIdentifier;
    }
}
