package com.krukowski.blockchain;

import java.util.ArrayList;

public class Miner implements Runnable {

    private String name;

    @Override
    public void run() {
        name = Thread.currentThread().getName();
        while (Blockchain.getCurrentBlocks().size() < Blockchain.NUMBER_OF_BLOCKS_TO_PRINT) {
            mine();
        }
    }

    public void mine() {
        ArrayList<Block> blocks = Blockchain.getCurrentBlocks();
        Block block;
        if (blocks.isEmpty()) {
            block = createFirstBlock();
        } else {
            block = createNextBlock(blocks);
        }
        if (block.getHash() != null && Blockchain.addBlockIfValid(block)) {
            rewardMiner();
        }
    }

    private void rewardMiner() {
        Person person = Blockchain.users.stream()
                .filter(user -> user.getName().equals(name)).findFirst().orElse(new Person(name));
        person.setBalance(person.getBalance() + 100);
    }

    private Block createNextBlock(ArrayList<Block> blocks) {
        Block previousBlock = blocks.get(blocks.size() - 1);
        int difficultyNumber = calculateDifficultyNumber(previousBlock);
        return createBlock(previousBlock, difficultyNumber);
    }

    private Block createFirstBlock() {
        return createBlock(null, 3);
    }


    private int calculateDifficultyNumber(Block previousBlock) {
        if (previousBlock.getGenerationTime() < 5) {
            return previousBlock.getDifficultyNumber() + 1;
        } else if (previousBlock.getGenerationTime() > 20) {
            return previousBlock.getDifficultyNumber() - 1;
        } else {
            return previousBlock.getDifficultyNumber();
        }
    }

    private Block createBlock(Block previousBlock, int difficultyNumber) {
        if (previousBlock == null) {
            return new Block(1, "0", difficultyNumber,
                    name);
        } else {
            return new Block(previousBlock.getId() + 1, previousBlock.getHash(),
                    difficultyNumber, name);
        }
    }

}
