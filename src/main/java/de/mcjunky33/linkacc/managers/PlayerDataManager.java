package de.mcjunky33.linkacc.managers;

import de.mcjunky33.linkacc.LinkAcc;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class PlayerDataManager {

    private final LinkAcc plugin;

    public PlayerDataManager(LinkAcc plugin) {
        this.plugin = plugin;
    }

    public boolean switchPlayerData(UUID uuid1, UUID uuid2) {
        World world = Bukkit.getWorlds().get(0);
        File playerDataFolder = new File(world.getWorldFolder(), "playerdata");
        File statsFolder = new File(world.getWorldFolder(), "stats");
        File advancementsFolder = new File(world.getWorldFolder(), "advancements");

        File file1 = new File(playerDataFolder, uuid1.toString() + ".dat");
        File file2 = new File(playerDataFolder, uuid2.toString() + ".dat");

        File stats1 = new File(statsFolder, uuid1.toString() + ".json");
        File stats2 = new File(statsFolder, uuid2.toString() + ".json");

        File adv1 = new File(advancementsFolder, uuid1.toString() + ".json");
        File adv2 = new File(advancementsFolder, uuid2.toString() + ".json");

        if (!file1.exists() || !file2.exists()) {
            plugin.getLogger().warning(plugin.getLangManager().getString("log.playerdata_file_not_found"));
            return false;
        }

        Player p1 = Bukkit.getPlayer(uuid1);
        Player p2 = Bukkit.getPlayer(uuid2);

        if (p1 != null) {
            p1.saveData();
            p1.kickPlayer(plugin.getLangManager().getString("kick.playerdata_switching"));
        }
        if (p2 != null) {
            p2.saveData();
            p2.kickPlayer(plugin.getLangManager().getString("kick.playerdata_switching"));
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                // Temp Dateien
                File tempPlayerData = new File(playerDataFolder, "temp_" + uuid1 + ".dat");
                File tempStats = new File(statsFolder, "temp_" + uuid1 + ".json");
                File tempAdv = new File(advancementsFolder, "temp_" + uuid1 + ".json");

                // PlayerData tauschen
                Files.move(file1.toPath(), tempPlayerData.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.move(file2.toPath(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.move(tempPlayerData.toPath(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Stats tauschen, wenn beide existieren
                if (stats1.exists() && stats2.exists()) {
                    Files.move(stats1.toPath(), tempStats.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.move(stats2.toPath(), stats1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.move(tempStats.toPath(), stats2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                // Advancements tauschen, wenn beide existieren
                if (adv1.exists() && adv2.exists()) {
                    Files.move(adv1.toPath(), tempAdv.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.move(adv2.toPath(), adv1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.move(tempAdv.toPath(), adv2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                // Log-Meldung mit Platzhaltern für UUIDs
                plugin.getLogger().info(plugin.getLangManager().getString("log.playerdata_swapped", uuid1.toString(), uuid2.toString()));

            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().warning(plugin.getLangManager().getString("log.playerdata_swap_error"));
            }
        }, 20L); // 1 Sekunde Verzögerung

        return true;
    }
}
