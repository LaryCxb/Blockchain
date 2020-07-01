package com.krukowski.blockchain;

public class Person {
    private final String name;
    private int balance;

    public Person(String name) {
        this.name = name;
        this.balance = 100;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
