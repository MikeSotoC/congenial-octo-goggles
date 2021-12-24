package me.chocolf.moneyfrommobs.listener;

import java.util.List;
import java.util.UUID;

import me.chocolf.moneyfrommobs.util.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Hopper;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import me.chocolf.moneyfrommobs.MoneyFromMobs;
import me.chocolf.moneyfrommobs.manager.PickUpManager;
import org.bukkit.persistence.PersistentDataType;

public class PickUpListeners implements Listener{

	private final MoneyFromMobs plugin;
	
	public PickUpListeners(MoneyFromMobs plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		PickUpManager pickUpManager = plugin.getPickUpManager();
		// gets item picked up
		Item item = e.getItem();
		ItemStack itemStack = item.getItemStack();
		// return if item picked up isn't money
		if (!pickUpManager.isMoneyPickedUp(itemStack)) return;
		
		e.setCancelled(true);
		// Stop mobs from picking up money
		if (e.getEntity() instanceof Player ) {
			Player p = (Player) e.getEntity();
			// returns if player doesn't have permission to pickup money
			if ( !p.hasPermission("MoneyFromMobs.use") ) return;
			
			List<String> itemLore = itemStack.getItemMeta().getLore();
			
			if (pickUpManager.shouldOnlyKillerPickUpMoney() && itemLore.size() > 2 && !itemLore.get(2).equals(p.getName()) )
				return;
		    
		    double amount = Double.parseDouble(itemLore.get(1));
		    pickUpManager.giveMoney(amount, p);
		    item.remove();
		}
	}
}
