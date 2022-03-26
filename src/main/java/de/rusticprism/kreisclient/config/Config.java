package de.rusticprism.kreisclient.config;

import de.rusticprism.kreisclient.KreisClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Config {
    public static int data;
    public static InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }
            FabricLoader.getInstance().getConfigDir();
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }
    protected static ClassLoader getClassLoader() {
        return KreisClient.class.getClassLoader();
    }

    public static Object get(File file) {
        File dir = file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                KreisClient.LOGGER.warn("Couldn't create Parent File");
            }
        } else if (!dir.isDirectory()) {
            KreisClient.LOGGER.warn("Parent File isn't a directory!");
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
                set(file,"+");
            } catch (IOException e) {
                KreisClient.LOGGER.warn("Couldn't create " + file.getName());
            }
        }
        try {
            FileReader reader = new FileReader(file);
               data = reader.read();
                reader.close();
        } catch (FileNotFoundException e) {
            KreisClient.LOGGER.warn("Couldnt read " + file.getName() + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (char)data;
    }
        public static void set(File file,String value) {
        File dir = file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                KreisClient.LOGGER.warn("Couldn't create Parent File");
            }
        } else if (!dir.isDirectory()) {
            KreisClient.LOGGER.warn("Parent File isn't a directory!");
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(value);
            writer.flush();
        } catch (IOException e) {
            KreisClient.LOGGER.warn("Couldn't write File!");
        }
    }

}
