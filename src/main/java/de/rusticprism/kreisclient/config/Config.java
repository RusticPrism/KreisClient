package de.rusticprism.kreisclient.config;

import de.rusticprism.kreisclient.KreisClient;
import io.netty.util.internal.ObjectUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

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
    protected static ClassLoader getClassLoader() {
        return KreisClient.class.getClassLoader();
    }

    public static void loadAll() {
        load("prefix.txt","Prefix","!");
        load("Blockcounter.txt","Enabled","false");
        load("Narrator.txt","Enabled","false");
        load("TestMod.txt","Enabled","false");
    }
    public static void load(String filename,String path,String value) {

        // read config from file
        try {
            File config = new File(FabricLoader.getInstance().getConfigDir() + "/KreisClient/" + filename);
            if(!config.exists()) {
                config.getParentFile().mkdirs();
                config.createNewFile();
                set(filename,path,value);
                set(filename,"Xpos","1");
                set(filename,"Ypos","1");
            }
            // if we get through the whole config, it's good to go
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String filename, String path, Object value) {
        JSONObject setting;
        try {
            setting = new JSONObject(new String(Files.readAllBytes(Path.of(getConfigPath(filename)))));
        } catch (JSONException | IOException e) {
            Klammern(filename);
            set(filename,path,value);
            return;
        }


        JSONObject object = new JSONObject();
        JSONObject finalSetting = setting;
        setting.keySet().forEach(key -> object.put(key, finalSetting.get(key)));
        object.put(path,value);
        try {
            Files.write(OtherUtil.getPath(getConfigPath(filename)),object.toString().getBytes());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    public static String get(String filename, Object path) {
        JSONObject setting;
        try {
            setting = new JSONObject(new String(Files.readAllBytes(OtherUtil.getPath(getConfigPath(filename)))));

        } catch (JSONException | IOException e) {
            return null;
        }
        setting.get(path.toString());
        return setting.getString(path.toString());
    }
    public static String getConfigPath(String filename) {
        return FabricLoader.getInstance().getConfigDir() + "/KreisClient/" +filename;
    }
    public static void Klammern(String filename) {
        File file = new File(getConfigPath(filename));
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("{ }");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
