package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.dreu.potionshrines.PotionShrines.LOGGER;
import static com.dreu.potionshrines.PotionShrines.MODID;

public class PSConfig {

    static Map.Entry<Config, String> getConfigOrDefault(String name, String defaultConfig) {
        try {Files.createDirectories(Path.of("config/" + MODID));} catch (Exception ignored) {}
        return Map.entry(new TomlParser().parse(Path.of("config/" + MODID + "/"+ name +".toml").toAbsolutePath(),
                ((path, configFormat) -> {
                    FileWriter writer = new FileWriter(path.toFile().getAbsolutePath());
                    writer.write(defaultConfig);
                    writer.close();
                    return true;})), "config/" + MODID + "/"+ name +".toml");
    }
    static int getIntOrDefault(String key, Map.Entry<Config, String> config, Config defaultConfig) {
        try {
            if ((config.getKey().get(key) == null)) {
                LOGGER.error("Key [" + key + "] is missing from Config: " + config.getValue());
                return defaultConfig.get(key);
            }
            return config.getKey().get(key);
        } catch (Exception e) {
            LOGGER.error("Value for [" + key + "] is an invalid type in Config: " + config.getValue());
            return defaultConfig.get(key);
        }
    }
    static boolean getBooleanOrDefault(String key, Map.Entry<Config, String> config, Config defaultConfig) {
        try {
            if ((config.getKey().get(key) == null)) {
                LOGGER.error("Key [" + key + "] is missing from Config: " + config.getValue());
                return defaultConfig.get(key);
            }
            return config.getKey().get(key);
        } catch (Exception e) {
            LOGGER.error("Value for [" + key + "] is an invalid type in Config: " + config.getValue());
            return defaultConfig.get(key);
        }
    }
}
