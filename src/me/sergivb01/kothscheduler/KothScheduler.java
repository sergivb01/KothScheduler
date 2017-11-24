package me.sergivb01.kothscheduler;

import lombok.Getter;
import lombok.Setter;
import me.sergivb01.kothscheduler.commands.KothSchedulerCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 *  KothScheduler Plugin - Developed by sergivb01
 *  Licensed under MIT License (https://github.com/sergivb01/KothScheduler/blob/master/LICENSE.md)
 *
 *  Twitter: twitter.com/sergivb01
 *  Contact: hi@sergivb01.me
 */

public class KothScheduler extends JavaPlugin{
    @Getter private static KothScheduler instance;
    @Getter private List<String> koths = new ArrayList<>();
    @Getter @Setter private long NEXT_KOTH = -1L;

    public void onEnable(){
        instance = this;

        final File configFile = new File(this.getDataFolder() + "/config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
        this.getConfig().options().copyDefaults(true);

        getCommand("kothscheduler").setExecutor(new KothSchedulerCommand(this));

        Map<String, Map<String, Object>> map = getDescription().getCommands();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            PluginCommand command = getCommand(entry.getKey());
            command.setPermission("kothscheduler.command." + entry.getKey());
            command.setPermissionMessage(ChatColor.translateAlternateColorCodes('&', "&e&l⚠ &cYou do not have permissions to execute this command."));
        }

        koths.addAll(getConfig().getStringList("koth-list"));
        this.getLogger().info("Loaded " + koths.size() + " koths! (" + koths.toString().replace("[", "").replace("]", "") + ")");
    }

    public void onDisable(){
        instance = null;
    }

    public void startNewKoth(int seconds){
        this.getLogger().info("Starting koth in " + seconds + " seconds. (" + getNextGame() + ")");
        NEXT_KOTH = System.currentTimeMillis() + (seconds * 1000);

        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(this, ()->{
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.getConfig().getString("start-koth-command").replace("%KOTH_NAME%", getNextGame()));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("start-koth-message").replace("%KOTH_NAME%", getNextGame())));

            NEXT_KOTH = -1;
        }, seconds * 20L);

    }

    public void rotateGames(){
        this.getLogger().info("Game list was rotated.");
        Collections.rotate(koths, -1);
    }

    public String getNextGame(){
        return koths.get(0);
    }


    /*
     *  In case you want to automatically register Koth games
     *  you may use this method. Do not forget to import the required
     *  packages and add your HCF plugin as a library.
     */

    /*private void registerFromFactions(){
        koths.clear();
        for(Faction faction  : getFactionManager().getFactions()){
            if(faction instanceof EventFaction){
                if(faction instanceof KothFaction) {
                    koths.add(faction.getName());
                    this.getLogger().info("Registered " + faction.getName() + " in eventGames list.");
                }
            }
        }
    }*/

}
