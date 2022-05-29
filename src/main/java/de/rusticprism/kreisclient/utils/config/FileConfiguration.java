package de.rusticprism.kreisclient.utils.config;

import de.rusticprism.kreisclient.KreisClient;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class FileConfiguration {

    private final File file;
    private final Map<String, String> fileconfig;
    public FileConfiguration(File file) {
        File file1 = new File(FabricLoader.getInstance().getConfigDir() + "/KreisClient/" + file);
        if(!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                KreisClient.LOGGER.warn("Couldn't create " + file + " because of " + e.getCause() + "!");
            }
        }

        this.file = file;
        this.fileconfig = new HashMap<>();

        //Set Map
        StringBuilder builder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Path.of(file.getPath()))) {
            stream.forEach(s -> builder.append(s).append(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] array = builder.toString().split(" ");
        for(String str : array) {
            String[] array1 = str.split(":");
            fileconfig.put(array1[0],array1[1]);
        }
    }

    public void set(String path, Object value) {
        fileconfig.put(path,String.valueOf(value));
        }
    public String get(@NotNull String path) {
        if(fileconfig.get(path) == null) {
            return null;
        }
        return fileconfig.get(path);
    }
    public void save() {
        try (Writer writer = new FileWriter(file)){
          for(String key : fileconfig.keySet()) {
              writer.write(key + ":" + fileconfig.get(key) + "\n");
          }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getString(@NotNull String path) {
        return String.valueOf(get(path));
    }
    public int getInt(@NotNull String path) {
        return Integer.parseInt(get(path));
    }
    public boolean getBoolean(@NotNull String path) {
        return Boolean.parseBoolean(get(path));
    }
    public float getFloat(@NotNull String path) {
        System.out.println(get(path));
        return Float.parseFloat(get(path));
    }
    public double getDouble(@NotNull String path) {
        return Double.parseDouble(get(path));
    }
}
