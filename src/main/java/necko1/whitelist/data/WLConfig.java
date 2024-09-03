package necko1.whitelist.data;

import necko1.whitelist.Whitelist;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WLConfig {

    public File file;
    public FileConfiguration config;
    public FileConfiguration defaultConfig;

    public void init() {
        this.file = new File(Whitelist.getInstance().getDataFolder(), "config.yml");

        try {
            if (!this.file.exists()) {
                Whitelist.getInstance().getDataFolder().mkdirs();
            }
            if (this.file.createNewFile()) this.copyDefaultConfig();
            else this.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getDefaultConfig() {
        InputStream inputStream = Whitelist.getInstance().getResource("config.yml");
        assert inputStream != null;
        return YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public void copyDefaultConfig() {
        this.config = this.getDefaultConfig();
        this.save();
    }

    public void save() {
        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Component getString(String path) {
        try {
            String text = this.config.getString(path);
            assert text != null;
            return Whitelist.miniMessage.deserialize(text);
        } catch (NullPointerException | IllegalArgumentException e) {
            Bukkit.getLogger().warning("Could not find path " + path + " in config.yml");
            return Whitelist.miniMessage.deserialize(Objects.requireNonNull(this.getDefaultConfig().getString(path)));
        }
    }

    public List<String> getStringList(String path) {
        try {
            List<String> text = this.config.getStringList(path);
            List<String> translated = new ArrayList<>();
            for (String s : text) {
                translated.add(Whitelist.serializer.serialize(Whitelist.miniMessage.deserialize(s)));
            }
            return translated;
        } catch (NullPointerException | IllegalArgumentException e) {
            Bukkit.getLogger().warning("Could not find path " + path + " in config.yml");
            return getDefaultConfig().getStringList(path);
        }
    }

}
