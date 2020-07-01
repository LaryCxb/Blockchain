package com.krukowski.blockchain;

import java.io.*;
import java.util.ArrayList;

public class SerializationUtils {

    public static synchronized void serialize(Object o, String filename) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(filename, true);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        File file = new File(filename);
        if (file.length() == 0) {
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(o);
            } catch (IOException e) {
                System.out.println("IOException during writing object");
            }
        } else {
            try (ObjectOutputStream oos = new AppendingObjectOutputStream(bos)) {
                oos.writeObject(o);
            } catch (IOException e) {
                System.out.println("IOException during writing object");
            }
        }
    }

    public static synchronized ArrayList<Block> deserialize(String filename) throws FileNotFoundException {
        ArrayList<Block> blocks = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            Block block = (Block) ois.readObject();
            try {
                while(block != null) {
                    blocks.add(block);
                    block = (Block) ois.readObject();
                }
            } catch (EOFException e) {
            }
        } catch (IOException e) {
            System.out.println("IOException during deserialization");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException during deserialization");
        }

        return blocks;
    }
}
