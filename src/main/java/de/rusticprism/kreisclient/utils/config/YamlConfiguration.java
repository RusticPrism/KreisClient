package de.rusticprism.kreisclient.utils.config;

import java.io.File;
import java.io.IOException;

public class YamlConfiguration extends FileConfiguration{

    public YamlConfiguration(File file) throws IOException {
        super(file);
    }
}
