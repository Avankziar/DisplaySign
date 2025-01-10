package me.avankziar.dsi.spigot.handler;

import me.avankziar.dsi.spigot.DSI;

public class ConfigHandler
{	
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType()
	{
		String s = DSI.getPlugin().getYamlHandler().getConfig().getString("Mechanic.CountPerm", "HIGHEST");
		CountType ct;
		try
		{
			ct = CountType.valueOf(s);
		} catch (Exception e)
		{
			ct = CountType.HIGHEST;
		}
		return ct;
	}
	
	public boolean isMechanicModifierEnabled()
	{
		return DSI.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.Modifier", false);
	}
	
	public boolean isMechanicValueEntryEnabled()
	{
		return DSI.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.ValueEntry", false);
	}
}