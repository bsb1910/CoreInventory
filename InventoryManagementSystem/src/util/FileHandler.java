package util;

import java.io.*;

public class FileHandler {

    public static void saveToFile(Object data, String path) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(path))) {

            oos.writeObject(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadFromFile(String path) {

        File file = new File(path);
        if (!file.exists()) return null;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(path))) {

            return ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}