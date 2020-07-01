package com.krukowski.blockchain;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class MessageSigningService {
    private final List<byte[]> list;

    //The constructor of Message class builds the list that will be written to the file.
    //The list consists of the message and the signature.
    public MessageSigningService(String data, String keyFile) throws Exception {
        list = new ArrayList<>();
        list.add(data.getBytes());
        list.add(sign(data, keyFile));
    }

    //The method that signs the data using the private key that is stored in keyFile path
    public byte[] sign(String data, String keyFile) throws Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(getPrivate(keyFile));
        rsa.update(data.getBytes());
        return rsa.sign();
    }

    //Method to retrieve the Private Key from a file
    public PrivateKey getPrivate(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    //Method to write the List of byte[] to a file
    private List<byte[]> getSignedMessage() {
        return list;
    }

    public static List<byte[]> signMessage(String message) {
        try {
            return new MessageSigningService(message, "KeyPair/privateKey").getSignedMessage();
        } catch (Exception e) {
            System.out.println("Exception 3");
            return null;
        }
        
    }
}
