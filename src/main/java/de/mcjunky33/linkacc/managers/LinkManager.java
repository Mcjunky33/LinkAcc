package de.mcjunky33.linkacc.managers;

import de.mcjunky33.linkacc.LinkAcc;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LinkManager {

    private final LinkAcc plugin;
    private final Map<UUID, UUID> pendingRequests = new HashMap<>();   // Ziel → Anfrager
    private final Map<UUID, UUID> activeLinks     = new HashMap<>();   // Spieler → verlinkter Spieler
    private final Map<UUID, Set<UUID>> blocked     = new HashMap<>();   // Spieler → blockierte Spieler
    private final Set<UUID> blockAll               = new HashSet<>();   // Spieler, die alle blocken

    private final File linkFile;
    private final YamlConfiguration cfg;

    public LinkManager(LinkAcc plugin) {
        this.plugin = plugin;
        this.linkFile = new File(plugin.getDataFolder(), "links.yml");
        this.cfg = YamlConfiguration.loadConfiguration(linkFile);
    }

    // ─── Persistent Save/Load ───────────────────────────────────────────────────

    public void loadLinks() {
        pendingRequests.clear();
        activeLinks.clear();
        if (!linkFile.exists()) return;
        if (cfg.contains("active")) {
            for (String k : cfg.getConfigurationSection("active").getKeys(false)) {
                UUID from = UUID.fromString(k);
                UUID to   = UUID.fromString(cfg.getString("active." + k));
                activeLinks.put(from, to);
            }
        }
    }

    public void saveLinks() {
        cfg.set("active", null);
        for (Map.Entry<UUID, UUID> e : activeLinks.entrySet()) {
            cfg.set("active." + e.getKey().toString(), e.getValue().toString());
        }
        try {
            cfg.save(linkFile);
        } catch (IOException ex) {
            plugin.getLogger().severe("Konnte links.yml nicht speichern!");
            ex.printStackTrace();
        }
    }

    // ─── Anfrage-Handling ───────────────────────────────────────────────────────

    public boolean hasPendingRequest(UUID target) {
        return pendingRequests.containsKey(target);
    }

    public boolean sendLinkRequest(UUID requester, UUID target) {
        if (pendingRequests.containsKey(target)) {
            return false; // Anfrage existiert schon
        }
        pendingRequests.put(target, requester);
        return true;
    }

    public UUID getPendingRequest(UUID target) {
        return pendingRequests.get(target);
    }

    public void removePendingRequest(UUID target) {
        pendingRequests.remove(target);
    }

    // ─── Verlinkung ─────────────────────────────────────────────────────────────

    public void linkPlayers(UUID a, UUID b) {
        activeLinks.put(a, b);
        activeLinks.put(b, a);
        pendingRequests.remove(b);
        saveLinks();
    }

    public boolean isLinked(UUID who) {
        return activeLinks.containsKey(who);
    }

    public UUID getLinkedUUID(UUID who) {
        return activeLinks.get(who);
    }

    public boolean unlinkPlayers(UUID who) {
        if (!activeLinks.containsKey(who)) {
            return false; // nicht verlinkt
        }
        UUID other = activeLinks.remove(who);
        if (other != null) {
            activeLinks.remove(other);
        }
        saveLinks();
        return true;
    }

    // ─── Blockieren ────────────────────────────────────────────────────────────

    public void blockAll(UUID who) {
        blockAll.add(who);
    }

    public void unblockAll(UUID who) {
        blockAll.remove(who);
    }

    public void blockPlayer(UUID who, UUID toBlock) {
        blocked.computeIfAbsent(who, k -> new HashSet<>()).add(toBlock);
    }

    public void unblockPlayer(UUID who, UUID toUnblock) {
        if (blocked.containsKey(who)) {
            blocked.get(who).remove(toUnblock);
            if (blocked.get(who).isEmpty()) blocked.remove(who);
        }
    }

    public boolean isBlocked(UUID who, UUID byPlayer) {
        if (blockAll.contains(who)) return true;
        return blocked.containsKey(who) && blocked.get(who).contains(byPlayer);
    }
}
