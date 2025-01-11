package me.avankziar.dsi.spigot.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityEnterBlockEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.ServerCommandEvent;

import me.avankziar.dsi.spigot.handler.SignHandler;

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
		WallSign wallsign = (WallSign) event.getBlock().getBlockData();
		Block block = event.getBlock().getRelative(wallsign.getFacing().getOppositeFace());
		SignHandler.setSign(block, event.getBlock());
	}
	
	private void searchSign(Location l)
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
				for(int z = minz; x <= maxz; z++)
				{
					SignHandler.setSign(l.getBlock(), l.getWorld().getBlockAt(x, y, z));
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
		searchSign(l);
	}
	
	@EventHandler
	public void onBlockCookEvent(BlockCookEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		searchSign(event.getBlock().getLocation());
	}
	
	@EventHandler
	public void onEntityEnterBlock(EntityEnterBlockEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		searchSign(event.getBlock().getLocation());
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
		searchSign(event.getLocation());
	}
	
	@EventHandler
	public void CommandBlock(ServerCommandEvent event) 
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
        searchSign(bcs.getBlock().getLocation());
    }
}