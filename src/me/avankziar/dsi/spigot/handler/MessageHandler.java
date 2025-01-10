package me.avankziar.dsi.spigot.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.dsi.general.assistance.ChatApi;
import me.avankziar.dsi.spigot.DSI;

public class MessageHandler 
{
	public static void sendMessage(CommandSender sender, String...array)
	{
		Arrays.asList(array).stream().forEach(x -> sender.spigot().sendMessage(ChatApi.tl(x)));
	}
	
	public static void sendMessage(UUID uuid, String...array)
	{
		Player player = Bukkit.getPlayer(uuid);
		if(player != null)
		{
			Arrays.asList(array).stream().forEach(x -> player.spigot().sendMessage(ChatApi.tl(x)));
			return;
		}
		if(DSI.getPlugin().getMtV() == null)
		{
			return;
		}
		DSI.getPlugin().getMtV().sendMessage(uuid, array);
	}
	
	public static void sendMessage(Collection<UUID> uuids, String...array)
	{
		uuids.stream().forEach(x -> sendMessage(x, array));
	}
	
	public static void sendMessage(String...array)
	{
		if(DSI.getPlugin().getMtV() != null)
		{
			DSI.getPlugin().getMtV().sendMessage(array);
			return;
		} else
		{
			Bukkit.getOnlinePlayers().stream().forEach(x -> sendMessage(x.getUniqueId(), array));
			return;
		}
	}
}