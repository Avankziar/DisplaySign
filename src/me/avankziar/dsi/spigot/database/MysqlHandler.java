package me.avankziar.dsi.spigot.database;

import me.avankziar.dsi.general.database.MysqlBaseHandler;
import me.avankziar.dsi.spigot.DSI;

public class MysqlHandler extends MysqlBaseHandler
{	
	public MysqlHandler(DSI plugin)
	{
		super(plugin.getLogger(), plugin.getMysqlSetup());
	}
}
