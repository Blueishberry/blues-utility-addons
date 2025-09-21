package me.blueishberry.bluesutilityaddons.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BluesUtilityConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "blues-utility-addons.json");

    public boolean toggleSneak = true;
    public boolean quickEquip = false;
    public boolean replier = false;

    public static BluesUtilityConfig INSTANCE = new BluesUtilityConfig();

    public static void load() {
        if(CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, BluesUtilityConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try(FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
