package com.krukowski.blockchain;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String text;
    private final List<byte[]> signature;
    private final int identifier;
    private final PublicKey publicKey;

    public Transaction(String text) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        this.text = text;
        identifier = Blockchain.getMessageIdentifier();
        signature = MessageSigningService.signMessage(text + identifier);
        publicKey = VerifyMessage.getPublic("KeyPair/publicKey");

    }

    public String getText() {
        return text;
    }

    public List<byte[]> getSignature() {
        return signature;
    }

    public int getIdentifier() {
        return identifier;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "text='" + text + '\'' +
                ", identifier=" + identifier +
                ", publicKey=" + publicKey +
                '}';
    }
}
