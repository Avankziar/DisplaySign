package me.avankziar.dsi.spigot.handler;

import java.util.UUID;

import org.bukkit.Bukkit;

import me.avankziar.dsi.spigot.DSI;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;

public class EconomyHandler 
{
	public static String format(double d)
	{
		if(DSI.getPlugin().getIFHEco() != null)
		{
			EconomyCurrency ec = DSI.getPlugin().getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL);
			return DSI.getPlugin().getIFHEco().format(d, ec);
		}
		if(DSI.getPlugin().getVaultEco() != null)
		{
			return String.valueOf(d) + " " + DSI.getPlugin().getVaultEco().currencyNamePlural();
		}
		return "MISSING ECONOMY";
	}
	
	public static boolean hasBalance(UUID uuid, double d)
	{
		if(DSI.getPlugin().getIFHEco() != null)
		{
			Account ac = DSI.getPlugin().getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN,
					DSI.getPlugin().getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
			return ac != null ? ac.getBalance() >= d : false;
		}
		if(DSI.getPlugin().getVaultEco() != null)
		{
			return DSI.getPlugin().getVaultEco().has(Bukkit.getOfflinePlayer(uuid), d);
		}
		return false;
	}
	
	public static void withdraw(UUID uuid, double d, String category, String comment)
	{
		if(DSI.getPlugin().getIFHEco() != null)
		{
			Account ac = DSI.getPlugin().getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN,
					DSI.getPlugin().getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
			DSI.getPlugin().getIFHEco().withdraw(ac, d, OrdererType.PLAYER, uuid.toString(),
					category, comment);
		}
		if(DSI.getPlugin().getVaultEco() != null)
		{
			DSI.getPlugin().getVaultEco().withdrawPlayer(Bukkit.getPlayer(uuid), d);
		}
	}
	
	public static void deposit(UUID uuid, double d, String category, String comment)
	{
		if(DSI.getPlugin().getIFHEco() != null)
		{
			Account ac = DSI.getPlugin().getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN,
					DSI.getPlugin().getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
			DSI.getPlugin().getIFHEco().deposit(ac, d, OrdererType.PLAYER, uuid.toString(),
					category, comment);
		}
		if(DSI.getPlugin().getVaultEco() != null)
		{
			DSI.getPlugin().getVaultEco().withdrawPlayer(Bukkit.getPlayer(uuid), d);
		}
	}
}