package de.mcjunky33.linkacc.managers;



import de.mcjunky33.linkacc.LinkAcc;

import org.bukkit.ChatColor;

import org.bukkit.entity.Player;



import java.io.File;

import java.io.IOException;

import java.io.InputStream;

import java.nio.file.Files;

import java.util.Properties;



public class LangManager {



    private final LinkAcc plugin;

    private Properties currentLang;



    public LangManager(LinkAcc plugin) {

        this.plugin = plugin;

    }



    public void createLangFiles() {

        try {

            saveResourceIfNotExists("lang/de_de.lang");

            saveResourceIfNotExists("lang/en_us.lang");

        } catch (IOException e) {

            plugin.getLogger().severe("Failed to create lang files!");

            e.printStackTrace();

        }

    }



    private void saveResourceIfNotExists(String resourcePath) throws IOException {

        File langFile = new File(plugin.getDataFolder(), resourcePath);

        if (!langFile.exists()) {

            langFile.getParentFile().mkdirs();

            try (InputStream in = plugin.getResource(resourcePath)) {

                if (in == null) {

                    plugin.getLogger().warning("Language resource not found: " + resourcePath);

                    return;

                }

                Files.copy(in, langFile.toPath());

            }

        }

    }



    public void loadLangFiles() {

        String lang = plugin.getConfig().getString("language", "en_us").toLowerCase();

        String langFileName = "lang/" + lang + ".lang";



        File langFile = new File(plugin.getDataFolder(), langFileName);

        if (!langFile.exists()) {

            plugin.getLogger().warning("Lang file not found: " + langFileName + " - falling back to en_us.lang");

            langFileName = "lang/en_us.lang";

            langFile = new File(plugin.getDataFolder(), langFileName);

            if (!langFile.exists()) {

                plugin.getLogger().severe("Fallback lang file en_us.lang not found!");

                return;

            }

        }



        try {

            Properties props = new Properties();

            try (InputStream in = Files.newInputStream(langFile.toPath())) {

                props.load(in);

            }

            currentLang = props;

            plugin.getLogger().info("Loaded language file: " + langFileName);

        } catch (IOException e) {

            plugin.getLogger().severe("Failed to load lang file: " + langFileName);

            e.printStackTrace();

        }

    }



    // Alte Methoden (für Kompatibilität)



    public String getString(String key, Player player) {

        if (currentLang == null) {

            return "LangManager not loaded!";

        }

        String value = currentLang.getProperty(key, "Missing language key: " + key);

        value = ChatColor.translateAlternateColorCodes('&', value);

        return value;

    }



    public String getString(String key) {

        return getString(key, (Player) null);

    }



    // Neue Methode mit Platzhalter-Ersetzung

    public String getString(String key, String... params) {

        if (currentLang == null) {

            return "LangManager not loaded!";

        }

        String value = currentLang.getProperty(key, "Missing language key: " + key);



        // Ersetze Platzhalter {0}, {1}, ... mit den params

        for (int i = 0; i < params.length; i++) {

            value = value.replace("{" + i + "}", params[i]);

        }



        value = ChatColor.translateAlternateColorCodes('&', value);

        return value;

    }

}
