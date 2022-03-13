package dev.waff.plugins.svcore.commands;

import dev.waff.plugins.svcore.main.Main;
import dev.waff.plugins.svcore.utils.ChatUtils;
import dev.waff.plugins.svcore.utils.TypeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CommandHelp implements CommandExecutor {

    private Main mainClass;
    private int helpType;
    private boolean pluginHelpAllowed;

    public CommandHelp(Main mainClass){
        this.mainClass = mainClass;
        this.helpType = mainClass.getCustomConfig().getInt("help-type");
        this.pluginHelpAllowed = mainClass.getCustomConfig().getBoolean("allow-plugin-help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (label.equalsIgnoreCase("help")){
            if (sender instanceof Player player){
                if (player.hasPermission("servercore.help")) {

                    HashMap<String, String> cmdList = getCommandList(); // map: command, description
                    if (helpType == 0) {
                        switch (args.length) {
                            case 0:
                                sendHelpMessage(player, 0, cmdList);
                                break;
                            case 1:
                                if (TypeUtils.isStringParsable(args[0]) && (cmdList.size() / 5) > Integer.parseInt(args[0]))
                                    sendHelpMessage(player, (Integer.parseInt(args[0]) - 1), cmdList);
                                else
                                    ChatUtils.sendMessage(player, "That is not a valid page", 1);
                                break;
                        }

                        if (args.length > 1)
                            ChatUtils.sendMessage(player, "Incorrect Usage: /help [page]", 1);
                    } else {
                        List<String> lines = mainClass.getCustomConfig().getStringList("help-messages");
                        for (String line : lines) {
                            ChatUtils.sendMessage(player, line, 3);
                        }
                    }
                } else {
                    ChatUtils.sendMessage(player, "You do not have access to that.", 0);
                }
            }
        } else if (label.equalsIgnoreCase("pluginhelp")){
            if (sender instanceof Player player){
                if (pluginHelpAllowed) { // set in config.yml
                    if (player.hasPermission("servercore.pluginhelp")) {

                        if (args.length < 1 || args.length > 2){
                            ChatUtils.sendMessage(player, "Incorrect Usage: /pluginhelp <plugin> [page]", 1);
                            return false;
                        }

                        String pluginName = replaceAlias(args[0]); // replace any alias the player may have typed (ie. Multiverse instead of Multiverse-Core) as per the definition in config.yml
                        HashMap<String, String> cmdList = getPluginCommandList(pluginName); // map: command, description

                        if (cmdList.isEmpty()) {
                            ChatUtils.sendMessage(player, "That plugin does not exist. Ensure the plugin name you have typed is exactly correct.", 1);
                            return false;
                        }

                        switch (args.length) {
                            case 1:
                                sendPluginHelp(player, 0, cmdList, pluginName);
                                break;
                            case 2:
                                if (TypeUtils.isStringParsable(args[1]) && (cmdList.size() / 5) > Integer.parseInt(args[1]))
                                    sendPluginHelp(player, (Integer.parseInt(args[1]) - 1), cmdList, pluginName);
                                else
                                    ChatUtils.sendMessage(player, "That is not a valid page", 1);
                                break;
                        }
                    } else {
                        ChatUtils.sendMessage(player, "You do not have access to that.", 0);
                    }
                } else {
                    ChatUtils.sendMessage(player, "That command is not available for use on this server.", 0); // if help type is different, /pluignhelp will not be available
                }
            }
        }
        return false;
    }

    private HashMap<String, String> getCommandList() {
        HashMap<String, String> cmdMap = new HashMap<>();
        for (Plugin pl : mainClass.getServer().getPluginManager().getPlugins()){
            pl.getDescription().getCommands().keySet().forEach(cmd -> cmdMap.put(cmd, pl.getServer().getPluginCommand(cmd).getDescription()));
        }

        return cmdMap;
    }
    private HashMap<String, String> getPluginCommandList(String plName) {
        HashMap<String, String> cmdMap = new HashMap<>();
        Plugin pl = mainClass.getServer().getPluginManager().getPlugin(plName);
        if (pl != null)
            pl.getDescription().getCommands().keySet().forEach(cmd -> cmdMap.put(cmd, pl.getServer().getPluginCommand(cmd).getDescription()));

        return cmdMap;
    }

    private void sendHelpMessage(Player player, int page, HashMap<String,String> cmdList) {
        ChatUtils.sendMessage(player, "&eBelow is a list of commands to use. [Page " + (page + 1) + "]", 4);
        final int startIndex = page * 6; // list 5 on each page, starting page is 0. therefore, starting page would be 0 * 6 (start at 0), second page would be 1 * 6 (start at 6 after listing 5 on the prev. page), etc. etc.
        int index = 0;

        for (String cmd : cmdList.keySet()) {
            if (index >= startIndex + 5) {
                break;
            }

            if (index >= startIndex){
                ChatUtils.sendMessage(player, String.format("&6/%s &7- &b%s", cmd, cmdList.get(cmd)), 3);
            }

            index++;
        }
    }
    private void sendPluginHelp(Player player, int page, HashMap<String,String> cmdList, String plName) {
        ChatUtils.sendMessage(player, String.format("&eBelow is a list of commands for &6%s &e[Page &6%d&e]", plName, page + 1), 4);
        final int startIndex = page * 6; // list 5 on each page, starting page is 0. therefore, starting page would be 0 * 6 (start at 0), second page would be 1 * 6 (start at 6 after listing 5 on the prev. page), etc. etc.
        int index = 0;

        for (String cmd : cmdList.keySet()) {
            if (index >= startIndex + 5) {
                break;
            }

            if (index >= startIndex){
                ChatUtils.sendMessage(player, String.format("&6/%s &7- &b%s", cmd, cmdList.get(cmd)), 3);
            }

            index++;
        }
    }

    // To replace any entered string with the listed config alias.
    private String replaceAlias(String input){
        for (String statement : mainClass.getCustomConfig().getStringList("plugin-help-aliases")) {
            String[] details = statement.split(",");
            if (input.equalsIgnoreCase(details[0])) {
                return details[1];
            }
        }

        return input;
    }

}
