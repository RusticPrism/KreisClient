package de.rusticprism.kreisclient.config;

import de.rusticprism.kreisclient.KreisClient;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OtherUtil {
    private final static String WINDOWS_INVALID_PATH = "c:\\windows\\system32\\";
    public static Path getPath(String path) {
        Path result = Paths.get(path);
        // special logic to prevent trying to access system32
        if (result.toAbsolutePath().toString().toLowerCase().startsWith(WINDOWS_INVALID_PATH)) {
            try {
                result = Paths.get(new File(KreisClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath() + File.separator + path);
            } catch (URISyntaxException ex) {
            }
        }
        return result;
    }
}
