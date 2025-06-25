package de.mcjunky33.linkacc.commands;



import de.mcjunky33.linkacc.LinkAcc;

import de.mcjunky33.linkacc.managers.LinkManager;

import de.mcjunky33.linkacc.managers.LangManager;

import de.mcjunky33.linkacc.managers.PlayerDataManager;

import org.bukkit.Bukkit;

import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;

import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;



import java.util.UUID;



public class LinkCommand implements CommandExecutor {



    private final LinkAcc plugin;

    private final LinkManager linkManager;

    private final LangManager langManager;

    private final PlayerDataManager playerDataManager;



    public LinkCommand(LinkAcc plugin) {

        this.plugin = plugin;

        this.linkManager = plugin.getLinkManager();

        this.langManager = plugin.getLangManager();

        this.playerDataManager = plugin.getPlayerDataManager();

    }



    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        if (!(sender instanceof Player)) {

            sender.sendMessage("§cNur Spieler können diesen Befehl ausführen.");

            return true;

        }



        Player player = (Player) sender;

        UUID playerUUID = player.getUniqueId();



        if (args.length == 0) {

            sendHelp(player);

            return true;

        }



        String sub = args[0].toLowerCase();



        switch (sub) {

            case "link":

                if (args.length < 2) {

                    player.sendMessage(langManager.getString("error.no_target", player));

                    return true;

                }

                handleLinkRequest(player, args[1]);

                break;



            case "accept":

                handleAccept(player);

                break;



            case "deny":

                handleDeny(player);

                break;



            case "unlink":

                handleUnlink(player);

                break;



            case "block":

                if (args.length < 2) {

                    player.sendMessage(langManager.getString("error.no_target", player));

                    return true;

                }

                handleBlock(player, args[1]);

                break;



            case "unblock":

                if (args.length < 2) {

                    player.sendMessage(langManager.getString("error.no_target", player));

                    return true;

                }

                handleUnblock(player, args[1]);

                break;



            case "list":

                handleList(player);

                break;



            case "swap":

                handleSwap(player);

                break;



            default:

                player.sendMessage(langManager.getString("error.unknown_command", player));

                break;

        }



        return true;

    }



    private void sendHelp(Player player) {

        player.sendMessage(langManager.getString("help.header", player));

        player.sendMessage(langManager.getString("help.link", player));

        player.sendMessage(langManager.getString("help.accept", player));

        player.sendMessage(langManager.getString("help.deny", player));

        player.sendMessage(langManager.getString("help.unlink", player));

        player.sendMessage(langManager.getString("help.block", player));

        player.sendMessage(langManager.getString("help.unblock", player));

        player.sendMessage(langManager.getString("help.list", player));

        player.sendMessage(langManager.getString("help.swap", player));

        player.sendMessage(langManager.getString("help.credits", player));

    }



    private void handleLinkRequest(Player requester, String targetName) {

        UUID requesterUUID = requester.getUniqueId();



        if (targetName.equalsIgnoreCase(requester.getName())) {

            requester.sendMessage(langManager.getString("error.cannot_link_self", requester));

            return;

        }



        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {

            requester.sendMessage(langManager.getString("error.target_not_online", requester));

            return;

        }



        UUID targetUUID = target.getUniqueId();



        if (linkManager.isBlocked(targetUUID, requesterUUID)) {

            requester.sendMessage(langManager.getString("error.blocked", requester));

            return;

        }



        if (linkManager.isLinked(requesterUUID)) {

            requester.sendMessage(langManager.getString("error.already_linked", requester));

            return;

        }



        if (linkManager.hasPendingRequest(targetUUID)) {

            requester.sendMessage(langManager.getString("error.target_has_pending", requester));

            return;

        }



        linkManager.sendLinkRequest(requesterUUID, targetUUID);

        requester.sendMessage(langManager.getString("link.request_sent", requester).replace("{player}", targetName));

        target.sendMessage(langManager.getString("link.request_received", target).replace("{player}", requester.getName()));

    }



    private void handleAccept(Player player) {

        UUID playerUUID = player.getUniqueId();



        UUID requesterUUID = linkManager.getPendingRequest(playerUUID);

        if (requesterUUID == null) {

            player.sendMessage(langManager.getString("error.no_pending_request", player));

            return;

        }



        if (linkManager.isLinked(playerUUID)) {

            player.sendMessage(langManager.getString("error.already_linked", player));

            return;

        }



        linkManager.linkPlayers(requesterUUID, playerUUID);



        Player requester = Bukkit.getPlayer(requesterUUID);

        if (requester != null) {

            requester.sendMessage(langManager.getString("link.request_accepted_requester", requester).replace("{player}", player.getName()));

        }

        player.sendMessage(langManager.getString("link.request_accepted_target", player).replace("{player}", requester != null ? requester.getName() : "unknown"));



        linkManager.removePendingRequest(playerUUID);

    }



    private void handleDeny(Player player) {

        UUID playerUUID = player.getUniqueId();



        UUID requesterUUID = linkManager.getPendingRequest(playerUUID);

        if (requesterUUID == null) {

            player.sendMessage(langManager.getString("error.no_pending_request", player));

            return;

        }



        Player requester = Bukkit.getPlayer(requesterUUID);

        if (requester != null) {

            requester.sendMessage(langManager.getString("link.request_denied_requester", requester).replace("{player}", player.getName()));

        }

        player.sendMessage(langManager.getString("link.request_denied_target", player));



        linkManager.removePendingRequest(playerUUID);

    }



    private void handleUnlink(Player player) {

        UUID playerUUID = player.getUniqueId();



        if (!linkManager.isLinked(playerUUID)) {

            player.sendMessage(langManager.getString("error.not_linked", player));

            return;

        }



        UUID linkedUUID = linkManager.getLinkedUUID(playerUUID);

        linkManager.unlinkPlayers(playerUUID);



        player.sendMessage(langManager.getString("unlink.success", player));

        Player linkedPlayer = Bukkit.getPlayer(linkedUUID);

        if (linkedPlayer != null) {

            linkedPlayer.sendMessage(langManager.getString("unlink.linked_player_notified", linkedPlayer));

        }

    }



    private void handleBlock(Player player, String target) {

        UUID playerUUID = player.getUniqueId();



        if (target.equalsIgnoreCase("all")) {

            linkManager.blockAll(playerUUID);

            player.sendMessage(langManager.getString("block.all", player));

        } else {

            Player targetPlayer = Bukkit.getPlayerExact(target);

            if (targetPlayer == null) {

                player.sendMessage(langManager.getString("block.notfound", player));

                return;

            }

            linkManager.blockPlayer(playerUUID, targetPlayer.getUniqueId());

            player.sendMessage(langManager.getString("block.success", player).replace("{player}", target));

        }

    }



    private void handleUnblock(Player player, String target) {

        UUID playerUUID = player.getUniqueId();



        if (target.equalsIgnoreCase("all")) {

            linkManager.unblockAll(playerUUID);

            player.sendMessage(langManager.getString("unblock.all", player));

        } else {

            Player targetPlayer = Bukkit.getPlayerExact(target);

            if (targetPlayer == null) {

                player.sendMessage(langManager.getString("unblock.notfound", player));

                return;

            }

            linkManager.unblockPlayer(playerUUID, targetPlayer.getUniqueId());

            player.sendMessage(langManager.getString("unblock.success", player).replace("{player}", target));

        }

    }



    private void handleList(Player player) {

        UUID playerUUID = player.getUniqueId();



        if (!linkManager.isLinked(playerUUID)) {

            player.sendMessage(langManager.getString("list.no_links", player));

            return;

        }



        UUID linkedUUID = linkManager.getLinkedUUID(playerUUID);

        Player linkedPlayer = Bukkit.getPlayer(linkedUUID);



        if (linkedPlayer != null) {

            player.sendMessage(langManager.getString("list.linked_with_online", player).replace("{player}", linkedPlayer.getName()));

        } else {

            player.sendMessage(langManager.getString("list.linked_with_offline", player).replace("{uuid}", linkedUUID.toString()));

        }

    }



    private void handleSwap(Player player) {

        UUID playerUUID = player.getUniqueId();



        if (!linkManager.isLinked(playerUUID)) {

            player.sendMessage(langManager.getString("error.not_linked", player));

            return;

        }



        UUID linkedUUID = linkManager.getLinkedUUID(playerUUID);



        // Hier wird erwartet, dass PlayerDataManager diese Methode implementiert hat

        if (playerDataManager.switchPlayerData(playerUUID, linkedUUID)) {

            player.sendMessage(langManager.getString("swap.success", player).replace("{player}", Bukkit.getOfflinePlayer(linkedUUID).getName()));

        } else {

            player.sendMessage(langManager.getString("swap.fail", player));

        }

    }

                                  }
