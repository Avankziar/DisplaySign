package me.avankziar.dsi.spigot.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityEnterBlockEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.dsi.spigot.DSI;
import me.avankziar.dsi.spigot.handler.SignHandler;
import me.avankziar.ifh.spigot.event.inventory.InventoryPostUpdateEvent;

public class BlockListener implements Listener
{
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(!(event.getBlock().getBlockData() instanceof WallSign))
		{
			return;
		}
		final WallSign wallsign = (WallSign) event.getBlock().getBlockData();
		final Block block = event.getBlock().getRelative(wallsign.getFacing().getOppositeFace());
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				SignHandler.setSign(event.getPlayer(), block, event.getBlock());
			}
		}.runTaskLater(DSI.getPlugin(), 1);
		
	}
	
	private void searchSign(final Location l, Player player)
	{
		int minx = l.getBlockX() - 1;
		int miny = l.getBlockY() - 1;
		int minz = l.getBlockZ() - 1;
		int maxx = l.getBlockX() + 1;
		int maxy = l.getBlockY() + 1;
		int maxz = l.getBlockZ() + 1;
		for(int x = minx; x <= maxx; x++)
		{
			for(int y = miny; y <= maxy; y++)
			{
				for(int z = minz; z <= maxz; z++)
				{
					Block b = l.getBlock();
					SignHandler.setSign(player, b, l.getWorld().getBlockAt(x, y, z));
				}
			}
		}
	}
	
	@EventHandler
	public void onInventarClose(InventoryCloseEvent event)
	{
		if(event.getInventory().getType() == InventoryType.PLAYER)
		{
			return;
		}
		Location l = event.getInventory().getLocation();
		if(l == null)
		{
			return;
		}
		searchSign(l, (Player) event.getPlayer());
	}
	
	@EventHandler
	public void onBlockCookEvent(BlockCookEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		searchSign(event.getBlock().getLocation(), null);
	}
	
	@EventHandler
	public void onFurnaceBurnEvent(FurnaceBurnEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		searchSign(event.getBlock().getLocation(), null);
	}
	
	@EventHandler
	public void onEntityEnterBlock(EntityEnterBlockEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		searchSign(event.getBlock().getLocation(), null);
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(event.getSpawnReason() != SpawnReason.BEEHIVE)
		{
			return;
		}
		searchSign(event.getLocation(), null);
	}
	
	/*@EventHandler
	public void onServerCommand(ServerCommandEvent event) 
	{
		if(event.isCancelled())
		{
			return;
		}
        if(!(event.getSender() instanceof BlockCommandSender))
        {
        	return;
        }
        BlockCommandSender bcs = (BlockCommandSender) event.getSender();
        searchSign(bcs.getBlock().getLocation(), null);
    }*/
	
	@EventHandler
	public void onInventoryPostUpdateEvent(InventoryPostUpdateEvent event)
	{
		if(event.getInventory() == null || event.getInventory().getLocation() == null)
		{
			return;
		}
		searchSign(event.getInventory().getLocation(), null);
	}
	
	@EventHandler
	public void onBrewingStartEvent(BrewingStartEvent event)
	{
		searchSign(event.getBlock().getLocation(), null);
	}
	
	@EventHandler
	public void onBrewingStartEvent(BrewingStandFuelEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		searchSign(event.getBlock().getLocation(), null);
	}
	
	@EventHandler
	public void onBrewEvent(BrewEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		searchSign(event.getBlock().getLocation(), null);
	}
}