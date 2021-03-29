package nl.tudelft.oopp.g7.server.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class Config {
    Logger logger = LoggerFactory.getLogger(Config.class);

    public static int RATE_LIMIT = -1;

    /**
     * Config object that contains customizable rules.( such as amount of questions per unit time)
     * @param file config file that will be read at the start of application
     */
    public Config(File file) {
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
