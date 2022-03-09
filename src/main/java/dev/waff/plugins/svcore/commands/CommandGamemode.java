package dev.waff.plugins.svcore.commands;

import dev.waff.plugins.svcore.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * author: waffdev
 * purpose: to house the /gamemode command
 * created: 9/3/22
 */

public class CommandGamemode implements CommandExecutor {

    /*
     command: /gamemode
     usage: /gamemode:gm <survival:creative:adventure:spectator:s:c:a:sp:0:1:2:3> [target]
     permissions:
      - servercore.gamemode.others
      - servercore.gamemode
      - servercore.gamemode.survival
      - servercore.gamemode.creative
      - servercore.gamemode.adventure
      - servercore.gamemode.spectator
     params:
      - sender => command sender
      - cmd => command instance
      - label => command label (ie. /gamemode would be 'gamemode')
      - args[] => command arguments
     */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("servercore.gamemode")) {
                    ChatUtils.sendMessage(p, "You do not have access to that.", 0);
                    return false;
                }
                if (args.length == 0 || args.length > 2) {
                    ChatUtils.sendMessage(p, "Incorrect Usage: /gamemode <survival:creative:adventure:spectator:s:c:a:sp:0:1:2:3> [target]", 1);
                    return false;
                }

                // Handle Gamemode
                if (args.length < 2) { // usage /gamemode (arg0)
                    setGamemode(p, p, args[0]);
                } else { // usage /gamemode (arg0) (arg1)
                    if (!p.hasPermission("servercore.gamemode.others")) {
                        ChatUtils.sendMessage(p, "You are not able to set the gamemode of others.", 0);
                        return false;
                    }

                    Player target = Bukkit.getServer().getPlayer(args[1]);
                    if (target != null){
                        setGamemode(target, p, args[0]);
                    }
                }


            } else {

                if (args.length != 2) { // ensure console isn't attempting to switch their own gamemode
                    ChatUtils.sendMessage(sender, "From console, you can only set an online player's gamemode. You must be in-game to perform the rest of this command.", 0);
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (sender.hasPermission("servercore.gamemode.others")) {
                        setGamemode(target, sender, args[0]);
                    } else {
                        ChatUtils.sendMessage(sender, "You do not have access to change other people's gamemodes.", 0);
                    }
                } else {
                    ChatUtils.sendMessage(sender, "That player is not online.", 0);
                    return false;
                }
            }
        } else {
            if (args.length == 1){ // ie. /gms [player]
                if (sender.hasPermission("servercore.gamemode.others")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (sender instanceof Player){
                        Player p = (Player) sender;
                        if (target != null) {
                            setGamemodeFromAlias(target, p, label);
                        } else {
                            ChatUtils.sendMessage(p, "That player is not online.", 0);
                            return false;
                        }
                    } else {
                        if (target != null){
                            setGamemodeFromAlias(target, sender, label);
                        } else {
                            ChatUtils.sendMessage(sender, "That player is not online.", 0);
                            return false;
                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        ChatUtils.sendMessage((Player) sender, "You do not have access to change other people's gamemodes", 0);
                        return false;
                    } else {
                        ChatUtils.sendMessage(sender, "You do not have access to change other people's gamemodes", 0);
                        return false;
                    }
                }
            } else if (args.length == 0) {
                if (!(sender instanceof Player))
                    return false;

                Player p = (Player) sender;
                setGamemodeFromAlias(p, p, label);
            } else {
                if (sender instanceof Player) {
                    ChatUtils.sendMessage((Player)sender, "Incorrect Usage: /" + label + " [player]", 1);
                    return false;
                } else {
                    ChatUtils.sendMessage(sender, "Incorrect Usage: /" + label + " [player]", 1);
                }
            }
        }

        return false;
    }

    private void setGamemode(Player p, Player sender, String arg){
        switch (arg){
            case "survival":
            case "s":
            case "0":
                if (sender.hasPermission("servercore.gamemode.survival"))
                    p.setGameMode(GameMode.SURVIVAL);
                break;
            case "creatie":
            case "c":
            case "1":
                if (sender.hasPermission("servercore.gamemode.creative"))
                    p.setGameMode(GameMode.CREATIVE);
                break;
            case "adventure":
            case "a":
            case "2":
                if (sender.hasPermission("servercore.gamemode.adventure"))
                    p.setGameMode(GameMode.ADVENTURE);
                break;
            case "spectator":
            case "sp":
            case "3":
                if (sender.hasPermission("servercore.gamemode.specator"))
                    p.setGameMode(GameMode.SPECTATOR);
                break;
        }
        ChatUtils.sendMessage(sender, "Player gamemode updated.", 2);
    }
    private void setGamemode(Player p, CommandSender sender, String arg){
        switch (arg){
            case "survival":
            case "s":
            case "0":
                if (sender.hasPermission("servercore.gamemode.survival"))
                    p.setGameMode(GameMode.SURVIVAL);
                break;
            case "creative":
            case "c":
            case "1":
                if (sender.hasPermission("servercore.gamemode.creative"))
                    p.setGameMode(GameMode.CREATIVE);
                break;
            case "adventure":
            case "a":
            case "2":
                if (sender.hasPermission("servercore.gamemode.adventure"))
                    p.setGameMode(GameMode.ADVENTURE);
                break;
            case "spectator":
            case "sp":
            case "3":
                if (sender.hasPermission("servercore.gamemode.specator"))
                    p.setGameMode(GameMode.SPECTATOR);
                break;
        }
        ChatUtils.sendMessage(sender, "Player gamemode updated.", 2);
    }

    private void setGamemodeFromAlias(Player p, Player sender, String label){
        switch (label){
            case "gms":
                if (sender.hasPermission("servercore.gamemode.survival"))
                    p.setGameMode(GameMode.SURVIVAL);
                break;
            case "gmc":
                if (sender.hasPermission("servercore.gamemode.creative"))
                    p.setGameMode(GameMode.CREATIVE);
                break;
            case "gma":
                if (sender.hasPermission("servercore.gamemode.spectator"))
                    p.setGameMode(GameMode.ADVENTURE);
                break;
            case "gmsp":
                if (sender.hasPermission("servercore.gamemode.specator"))
                    p.setGameMode(GameMode.SPECTATOR);
                break;
        }
        ChatUtils.sendMessage(sender, "Player gamemode updated.", 2);
    }
    private void setGamemodeFromAlias(Player p, CommandSender sender, String label){
        switch (label){
            case "gms":
                if (sender.hasPermission("servercore.gamemode.survival"))
                    p.setGameMode(GameMode.SURVIVAL);
                break;
            case "gmc":
                if (sender.hasPermission("servercore.gamemode.creative"))
                    p.setGameMode(GameMode.CREATIVE);
                break;
            case "gma":
                if (sender.hasPermission("servercore.gamemode.spectator"))
                    p.setGameMode(GameMode.ADVENTURE);
                break;
            case "gmsp":
                if (sender.hasPermission("servercore.gamemode.specator"))
                    p.setGameMode(GameMode.SPECTATOR);
                break;
        }
        ChatUtils.sendMessage(sender, "Player gamemode updated.", 2);
    }
}
