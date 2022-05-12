package de.rusticprism.kreisclient.utils.config;

import de.rusticprism.kreisclient.config.OtherUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public abstract class FileConfiguration {

    private File file;
    private final Map<String, Object> fileconfig;
    public FileConfiguration(File file) throws IOException {
        this.file = file;
        this.fileconfig = new HashMap<>();
        String str = new String(Files.readAllBytes(OtherUtil.getPath(file.getPath())));
    }
    public void set(Object path, Object value) throws IOException {
        Files.write(OtherUtil.getPath(file.getPath()), (path + ":" + "\n" + value).getBytes());
        System.out.println(1);
    }
}
