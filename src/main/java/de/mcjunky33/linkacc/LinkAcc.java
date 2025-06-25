package de.mcjunky33.linkacc;



import de.mcjunky33.linkacc.commands.LinkCommand;

import de.mcjunky33.linkacc.listeners.PlayerJoinListener;

import de.mcjunky33.linkacc.managers.LangManager;

import de.mcjunky33.linkacc.managers.LinkManager;

import de.mcjunky33.linkacc.managers.PlayerDataManager;

import org.bukkit.plugin.java.JavaPlugin;



public class LinkAcc extends JavaPlugin {



    private LangManager langManager;

    private LinkManager linkManager;

    private PlayerDataManager playerDataManager;



    @Override

    public void onEnable() {

        saveDefaultConfig(); // Erstellt automatisch die config.yml



        this.langManager = new LangManager(this);

        langManager.createLangFiles();

        langManager.loadLangFiles();



        this.linkManager = new LinkManager(this);

        this.playerDataManager = new PlayerDataManager(this);



        // Lade gespeicherte Verlinkungen

        linkManager.loadLinks();



        // Command + Listener registrieren

        getCommand("link").setExecutor(new LinkCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);



        getLogger().info("LinkAcc erfolgreich aktiviert.");

    }



    @Override

    public void onDisable() {

        linkManager.saveLinks();

        getLogger().info("LinkAcc wurde deaktiviert und Links gespeichert.");

    }



    public LangManager getLangManager() {

        return langManager;

    }



    public LinkManager getLinkManager() {

        return linkManager;

    }



    public PlayerDataManager getPlayerDataManager() {

        return playerDataManager;

    }

}
