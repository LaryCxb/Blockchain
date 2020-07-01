package com.krukowski.blockchain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class VerifyMessage {

    //Method for signature verification that initializes with the Public Key,
    //updates the data to be verified and then verifies them using the signature
    private static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(data);

        return sig.verify(signature);
    }

    //Method to retrieve the Public Key from a file
    public static PublicKey getPublic(String filename) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static boolean verify(Transaction transaction) throws Exception {
        return verifySignature(transaction.getSignature().get(0),
                transaction.getSignature().get(1), transaction.getPublicKey());
    }
}