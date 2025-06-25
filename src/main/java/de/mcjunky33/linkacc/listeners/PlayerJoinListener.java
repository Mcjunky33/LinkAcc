package de.mcjunky33.linkacc.listeners;



import de.mcjunky33.linkacc.LinkAcc;

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;



import java.util.UUID;



public class PlayerJoinListener implements Listener {



    private final LinkAcc plugin;



    public PlayerJoinListener(LinkAcc plugin) {

        this.plugin = plugin;

    }



    @EventHandler

    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        UUID playerUUID = player.getUniqueId();



        if (plugin.getLinkManager().isLinked(playerUUID)) {

            UUID linkedUUID = plugin.getLinkManager().getLinkedUUID(playerUUID);

            Player linkedPlayer = Bukkit.getPlayer(linkedUUID);



            String linkedName = (linkedPlayer != null) ? linkedPlayer.getName() : linkedUUID.toString();

            String message = plugin.getLangManager().getString("join.linked", player)

                    .replace("{player}", linkedName);



            player.sendMessage(message);

        }

    }

}
