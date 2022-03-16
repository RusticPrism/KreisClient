package de.rusticprism.kreisclient.config;

import de.rusticprism.kreisclient.KreisClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.MonitorInfo;
import java.lang.module.Configuration;
import java.net.URL;
import java.net.URLConnection;

public class Config {
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
    protected static final ClassLoader getClassLoader() {
        return KreisClient.class.getClassLoader();
    }

    public static Object get(String filename,String path) {
        File file = new File(FabricLoader.getInstance().getConfigDir() +filename + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                KreisClient.LOGGER.warn("Couldnt create " + filename + " !");
                return null;
            }
        }
        return file;
    }
}
