package com.philderbeast.prisonpicks; 

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before; 

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack; 
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static org.mockito.Mockito.* ;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PluginDescriptionFile.class)
public class EventTest {

    @Mock PlayerInventory inventory;
    @Mock private Player player;
    @Mock private CommandSender commandSender;
    @Mock private Server mockServer;
    @Mock private PluginManager pluginManager;
    @Mock private ItemFactory itemFactory;
    @Mock private ItemMeta itemMeta;
    @Mock private PlayerInventory playerInventory;
    @Mock private ItemStack tool;
    
    private ArrayList<String> lore;
    private PrisonPicks pp;

    public static final File pluginDirectory = new File("build/libs");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize the Mock server.
        JavaPluginLoader mockPluginLoader = new JavaPluginLoader(mockServer);
        Whitebox.setInternalState(mockPluginLoader, "server", mockServer);
        when(mockServer.getName()).thenReturn("TestBukkit");
        Logger.getLogger("Minecraft").setParent(TestUtil.logger);
        when(mockServer.getLogger()).thenReturn(TestUtil.logger);

         // Return a fake PDF file.
        PluginDescriptionFile pdf = PowerMockito.spy(new PluginDescriptionFile("PrisonPicks", "1.3.4-Test",
                "com.philderbeast.prisonpicks.PrisonPicks"));
        when(pdf.getAuthors()).thenReturn(new ArrayList<String>());

        //add my plugin object
        pp = PowerMockito.spy(new PrisonPicks(mockPluginLoader, pdf, pluginDirectory, new File(pluginDirectory, "testPluginFile")));

        //Tool Stubbs
        doReturn(0).when(tool).getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        doReturn(Material.DIAMOND_PICKAXE).when(tool).getType();
        doReturn((short) 0).when(tool).getDurability(); 
        doReturn(true).when(tool).hasItemMeta();
        doReturn(itemMeta).when(tool).getItemMeta();

        doReturn(tool).when(inventory).getItemInMainHand(); 
        doReturn(inventory).when(player).getInventory(); 

        doReturn(true).when(tool).hasItemMeta();
        doReturn(itemMeta).when(tool).getItemMeta();
        when(tool.getDurability()).thenReturn((short) 5);

        lore = new ArrayList<String>();
        lore.add(ChatColor.GOLD + "Explosive I");
        doReturn(lore).when(itemMeta).getLore();
         doReturn(true).when(itemMeta).hasLore();

    }

    @Test
    public void repairTest()
    {
        PlayerInteractEvent pie = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR , tool, null, null);
        Events e = new Events(pp);
        e.onPlayerInteract(pie);
        verify(tool).setDurability((short) 0 );
    }
    
}