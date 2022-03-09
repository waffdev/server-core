package dev.waff.plugins.svcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * author: waffdev
 * purpose: plugin-wide chat utilities
 * created: 9/3/22
 */

public class ChatUtils {

    private static final String gen_prefix = ChatColor.translateAlternateColorCodes('&', "&7[&bSC&7]&r");

    /*
     TYPE A - sendSenderMessage
     Sends a message to the "sender" (CommandSender)

     params:
      - sender => message target
      - message => message to send
      - type => message type (0 = DANGER, 1 = WARNING, 2 = SUCCESS, 3 = NEUTRAL, 4 = GENERIC PREFIXED)

     TYPE B - sendPlayerMessage
     Sends a message to the "player" (Player)

     params:
      - player => message target
      - message => message to send
      - type => message type (0 = DANGER, 1 = WARNING, 2 = SUCCESS, 3 = NEUTRAL, 4 = GENERIC PREFIXED)

     */

    // in both methods, case 3 is redundant as it's echoed in the default case so no need to implement it (case 3 was literally just msgPrefix = "")
    public static void sendMessage(CommandSender sender, String message, int type){
        String msgPrefix = "";

        switch(type){
            case 0:
                msgPrefix = "&c&l[!] &c";
                break;
            case 1:
                msgPrefix = "&e&l[?] &e";
                break;
            case 2:
                msgPrefix = "&a&l[✓] &a";
                break;
            case 4:
                msgPrefix = gen_prefix + " ";
                break;
            default:
                msgPrefix = "";
                break;
        }

        sMsg(sender, msgPrefix + message);
    }
    public static void sendMessage(Player sender, String message, int type){
        String msgPrefix = "";

        switch(type){
            case 0:
                msgPrefix = "&c&l[!] &c";
                break;
            case 1:
                msgPrefix = "&e&l[?] &e";
                break;
            case 2:
                msgPrefix = "&a&l[✓] &a";
                break;
            case 4:
                msgPrefix = gen_prefix + " ";
                break;
            default:
                msgPrefix = "";
                break;
        }

        sMsg(sender, msgPrefix + message);
    }

    private static void sMsg(CommandSender sender, String message){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
