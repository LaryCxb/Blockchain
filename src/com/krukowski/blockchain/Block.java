package com.krukowski.blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long id;
    private final long timeStamp;
    private final String previousHash;
    private final String hash;
    private int magicNumber;
    private final int generationTime;
    private final int difficultyNumber;
    private final String minerName;
    private final List<Transaction> transactions;
    private final int numberOfBlocksAtStart;

// -----------------Constructors-----------------

    public Block(long id, String previousHash, int difficultyNumber, String minerName) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.magicNumber = 0;
        this.previousHash = previousHash;
        this.difficultyNumber = difficultyNumber;
        this.minerName = minerName;
        this.transactions = List.copyOf(Blockchain.getTransactions()
                .stream()
                .filter(transaction -> transaction.getIdentifier() > Blockchain.getMaximumIdentifier())
                .collect(Collectors.toList()));
        numberOfBlocksAtStart = Blockchain.getCurrentBlocks().size();
        hash = hashFunction(false);
        generationTime = (int) (new Date().getTime() - timeStamp) / 1000;
    }

// -----------------Methods-----------------

    public String hashFunction(boolean checking) {
        String hashPoW = StringUtil.applySha256(toString());
        return checking ? hashPoW : findProperHash(hashPoW);
    }

    private String findProperHash(String hashPoW) {
        String properHash = "0{" + difficultyNumber + "}[^0]*";
        while (!hashPoW.matches(properHash) && numberOfBlocksAtStart == Blockchain.getCurrentBlocks().size()) {
            magicNumber = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
            hashPoW = StringUtil.applySha256(toString());
        }
        return numberOfBlocksAtStart != Blockchain.getCurrentBlocks().size() ? null : hashPoW;
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", timeStamp=" + timeStamp +
                ", previousHash='" + previousHash + '\'' +
                ", magicNumber=" + magicNumber +
                ", difficultyNumber=" + difficultyNumber +
                ", minerId='" + minerName + '\'' +
                ", messages=" + transactions.toString() +
                ", numberOfBlocksAtStart=" + numberOfBlocksAtStart +
                '}';
    }

    // -----------------Getters&Setters-----------------

    public long getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public int getGenerationTime() {
        return generationTime;
    }

    public int getDifficultyNumber() {
        return difficultyNumber;
    }

    public String getMinerName() {
        return minerName;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
