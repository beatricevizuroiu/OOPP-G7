package nl.tudelft.oopp.g7.server.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Config {
    Logger logger = LoggerFactory.getLogger(Config.class);

    public static int RATE_LIMIT = -1;
    private final File file;

    /**
     * Constructor for the Config class.
     * @param file The file to load as the config.
     */
    public Config(File file) {
        this.file = file;
        loadConfig();
    }

    /**
     * Load the config from disk into the static variables in this class.
     */
    private void loadConfig() {
        try {
            if (file == null)
                throw new IllegalStateException("Config file is not set.");

            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (!file.exists())
                Util.exportResource("/config.yml", file);

            Yaml yaml = new Yaml();

            try (InputStream is = new FileInputStream(file)) {
                Map data = yaml.load(is);

                RATE_LIMIT = (Integer) data.get("slow-mode");

            }
        } catch (IOException e) {
            logger.error("An error occurred while loading the config", e);
        }
    }
}
