package me.sergivb01.kothscheduler.commands;

import me.sergivb01.kothscheduler.KothScheduler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KothSchedulerCommand implements CommandExecutor{
    private KothScheduler plugin;

    public KothSchedulerCommand(KothScheduler plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(args.length <= 0){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThis server is using &a&lKothScheduler &7(" + plugin.getDescription().getVersion() + ") &edeveloped by &a&lsergivb01 &7(twitter.com/sergivb01)"));
            return false;
        }

        switch (args[0].toLowerCase()){
            case "list":
                if(!existsKoth()){
                    sender.sendMessage(ChatColor.RED + "There are no koths.");
                    return false;
                }
                sender.sendMessage(ChatColor.YELLOW + "Current loaded koths: " + ChatColor.GRAY + plugin.getKoths().toString().replace("[", "").replace("]", ""));
                break;
            case "next":
                if(!existsKoth()){
                    sender.sendMessage(ChatColor.RED + "There are no koths.");
                    return false;
                }
                sender.sendMessage(ChatColor.YELLOW + "Next Koth: " + ChatColor.GREEN + plugin.getNextGame());
                break;
            case "shuffle":
                if(!existsKoth()){
                    sender.sendMessage(ChatColor.RED + "There are no koths.");
                    return false;
                }
                plugin.shuffleGames();
                sender.sendMessage(ChatColor.YELLOW + "Koth list has been shuffled.");
                break;
            case "rotate":
                if(!existsKoth()){
                    sender.sendMessage(ChatColor.RED + "There are no koths.");
                    return false;
                }
                plugin.rotateGames();
                sender.sendMessage(ChatColor.YELLOW + "Koths have been rotated!");
                break;
            case "start":
                if(!existsKoth()){
                    sender.sendMessage(ChatColor.RED + "There are no koths.");
                    return false;
                }
                plugin.startNewKoth(15);
                sender.sendMessage(ChatColor.YELLOW + "Starting new koth in 15 seconds. " + ChatColor.GRAY + "(Command used for debugging/testing - use API instead)");
                break;
            case "reload":
                plugin.reloadConfig();
                plugin.getKoths().clear();
                plugin.getKoths().addAll(plugin.getConfig().getStringList("koth-list"));
                sender.sendMessage(ChatColor.YELLOW + "Koth list has been " + ChatColor.GREEN + "reloaded" + ChatColor.YELLOW + ".");
                break;
            default:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThis server is using &a&lKothScheduler &7(" + plugin.getDescription().getVersion() + ") &edeveloped by &a&lsergivb01 &7(twitter.com/sergivb01)"));
                break;
        }

        return true;
    }

    private boolean existsKoth(){ return !plugin.getKoths().isEmpty(); }


}
