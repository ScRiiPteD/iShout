package me.gaioli.plugins.ishout;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Ishout extends JavaPlugin implements CommandExecutor{
    private Logger log;
    private PluginDescriptionFile description;
    private String prefix;
    private String version;
    public int currentVer = 1;
    public int currentSubVer = 0;
    public Map<Player, Boolean> hasShouted = new HashMap<Player, Boolean>();
    
    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        log = Logger.getLogger("Minecraft");
        description = getDescription();
        version = description.getVersion();
        prefix = "["+description.getName()+"] ";
        log("Loading iShout v"+version);
        log("Initializing Commands...");
        
    }
    
    public void log(String message){
        log.info(prefix+message);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        Player player  = (Player)sender;
        Server server = Bukkit.getServer();
        if (cmd.getName().equalsIgnoreCase("ishout")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.LIGHT_PURPLE+"====iShout====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"==== v"+version+" ====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"================");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"Commands: ");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/shout - Shout to the server");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/ishout [argument] (Type /ishout help arguments for arguments)");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/ishout help - Shows this page");
            } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.LIGHT_PURPLE+"====iShout====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"==== v"+version+" ====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"================");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"Commands: ");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/shout - Shout to the server");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/ishout [argument] (Type /ishout help arguments for arguments)");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/ishout help - Shows this page");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("arguments")) {
                player.sendMessage(ChatColor.LIGHT_PURPLE+"====iShout====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"==== v"+version+" ====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"================");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/ishout on - Allow shouting (Req. Perm)");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"/ishout off - Turns off shouting (Req. Perm)");
            } else {
                player.sendMessage(ChatColor.LIGHT_PURPLE+"====iShout====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"==== v"+version+" ====");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"================");
                player.sendMessage("");
                player.sendMessage("");
                player.sendMessage(ChatColor.RED+"Unknown argument! Type /ishout help for commands!");
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("shout")) {
            String msg = "";
            for (String part : args) {
                if (msg != "") msg += " ";
                    msg += part;
	    }
            
            shout (player, msg);
            return true;
        }
        return false;
    }
    
    public void shout(Player player, String msg) {
        Server server = Bukkit.getServer();
        if (msg.isEmpty()) {
            player.sendMessage(ChatColor.RED+"You must type a message to shout!");
            return;
        }
        if (!hasShouted.containsKey(player) && player.hasPermission("dc.shout")) {
            if (player.hasPermission("dc.color")) {
                server.broadcastMessage(ChatColor.RED+"[S]"+ChatColor.WHITE+player.getDisplayName()+ChatColor.GRAY+": "+replaceColors(msg));
                hasShouted.put(player, true);
                setTimer(player);
            } else {
                server.broadcastMessage(ChatColor.RED+"[S]"+ChatColor.WHITE+player.getDisplayName()+ChatColor.GRAY+": "+msg);
                hasShouted.put(player, true);
                setTimer(player);
            }
        } else if (player.hasPermission("dc.shout.bypass")) {
            if (player.hasPermission("dc.color")) {
                server.broadcastMessage(ChatColor.RED+"[S]"+ChatColor.WHITE+player.getDisplayName()+ChatColor.GRAY+": "+replaceColors(msg));
            } else {
                server.broadcastMessage(ChatColor.RED+"[S]"+ChatColor.WHITE+player.getDisplayName()+ChatColor.GRAY+": "+msg);
            }
        } else if (hasShouted.containsKey(player)) {
            player.sendMessage(ChatColor.RED+"Sorry you must wait 1 minute from when you last shouted to shout!");
        } else {
            if (player.hasPermission("dc.color")) {
                server.broadcastMessage(ChatColor.RED+"[S]"+ChatColor.WHITE+player.getDisplayName()+ChatColor.GRAY+": "+replaceColors(msg));
                hasShouted.put(player, true);
            } else {
                server.broadcastMessage(ChatColor.RED+"[S]"+ChatColor.WHITE+player.getDisplayName()+ChatColor.GRAY+": "+msg);
                hasShouted.put(player, true);
            }
        }
    }
    
    public void setTimer(final Player player) {
        Server server = Bukkit.getServer();
        server.getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
            public void run() {
                hasShouted.remove(player);
            }
        }, 1200L);
    }
    
    private String replaceColors (String message) {
	return message.replaceAll("&([o-9a-z])", "\u00A7$1");
    }
}

