package me.avankziar.dsi.spigot.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Crafter;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.recipe.CookingBookCategory;

import me.avankziar.dsi.spigot.DSI;

public class SignHandler 
{
	public static String getDisplaySignVerification()
	{
		return DSI.getPlugin().getYamlHandler().getConfig().getString("");
	}
	
	public static boolean isDisplaySign(Sign sign)
	{
		return sign.getSide(Side.FRONT).getLine(0).contains(getDisplaySignVerification());
	}
	
	public static void setSign(Block b, Block block)
	{
		BlockState blockstate = block.getState();
		if(!(blockstate instanceof Sign))
		{
			return;
		}
		Sign sign = (Sign) blockstate;
		if(!isDisplaySign(sign))
		{
			return;
		}
		switch(b.getType())
		{
		default: break;
		case CHEST:
			setChestDisplay(block, sign);
			break;
		case FURNACE:
			setFurnaceDisplay(block, sign);
			break;
		case BEACON:
		case BEEHIVE:
		case COMMAND_BLOCK:
		case JUKEBOX:			
		}
	}
	
	private static void setChestDisplay(Block block, Sign sign)
	{
		HashMap<Material, Integer> count = new HashMap<>();
		if(!(block.getState() instanceof Chest))
		{
			return;
		}
		Chest chest = (Chest) block.getState();
		if(chest.getInventory() instanceof DoubleChestInventory)
		{
			DoubleChestInventory dci = (DoubleChestInventory) chest.getInventory();
			for(ItemStack is : dci.getStorageContents())
			{
				int amount = is.getAmount();
				if(count.containsKey(is.getType()))
				{
					amount =+ count.get(is.getType());
				}
				count.put(is.getType(), amount);
			}
		} else
		{
			for(ItemStack is : chest.getInventory().getStorageContents())
			{
				int amount = is.getAmount();
				if(count.containsKey(is.getType()))
				{
					amount =+ count.get(is.getType());
				}
				count.put(is.getType(), amount);
			}
		}
		List<Map.Entry<Material, Integer>> top3 = count.entrySet().stream()
	            .sorted(Map.Entry.<Material, Integer>comparingByValue().reversed()) // Nach Werten absteigend sortieren
	            .limit(3) // Nur die obersten 3 Elemente nehmen
	            .collect(Collectors.toList()); // In eine Liste sammeln
		Material mat1 = top3.size() >= 1 ? top3.get(0).getKey() : null;
		int i1 = top3.size() >= 1 ? top3.get(0).getValue() : 0;
		Material mat2 = top3.size() >= 2 ? top3.get(1).getKey() : null;
		int i2 = top3.size() >= 2 ? top3.get(1).getValue() : 0;
		Material mat3 = top3.size() >= 3 ? top3.get(2).getKey() : null;
		int i3 = top3.size() >= 3 ? top3.get(2).getValue() : 0;
		String s1 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest.Line1")
				.replace("%mat1%", mat1 != null ? mat1.toString() : "")
				.replace("%amount1%", i1 > 0 ? String.valueOf(i1) : "")
				.replace("%mat2%", mat2 != null ? mat2.toString() : "")
				.replace("%amount2%", i2 > 0 ? String.valueOf(i2) : "")
				.replace("%mat3%", mat3 != null ? mat3.toString() : "")
				.replace("%amount3%", i2 > 0 ? String.valueOf(i3) : "");
		String s2 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest.Line2")
				.replace("%mat1%", mat1 != null ? mat1.toString() : "")
				.replace("%amount1%", i1 > 0 ? String.valueOf(i1) : "")
				.replace("%mat2%", mat2 != null ? mat2.toString() : "")
				.replace("%amount2%", i2 > 0 ? String.valueOf(i2) : "")
				.replace("%mat3%", mat3 != null ? mat3.toString() : "")
				.replace("%amount3%", i2 > 0 ? String.valueOf(i3) : "");
		String s3 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest.Line3")
				.replace("%mat1%", mat1 != null ? mat1.toString() : "")
				.replace("%amount1%", i1 > 0 ? String.valueOf(i1) : "")
				.replace("%mat2%", mat2 != null ? mat2.toString() : "")
				.replace("%amount2%", i2 > 0 ? String.valueOf(i2) : "")
				.replace("%mat3%", mat3 != null ? mat3.toString() : "")
				.replace("%amount3%", i2 > 0 ? String.valueOf(i3) : "");
		sign.getSide(Side.FRONT).setLine(1, s1);
		sign.getSide(Side.FRONT).setLine(2, s2);
		sign.getSide(Side.FRONT).setLine(3, s3);
		if(DSI.getPlugin().getYamlHandler().getConfig().getBoolean("Chest.WriteOnBack"))
		{
			sign.getSide(Side.BACK).setLine(0, sign.getSide(Side.FRONT).getLine(0));
			sign.getSide(Side.BACK).setLine(1, s1);
			sign.getSide(Side.BACK).setLine(2, s2);
			sign.getSide(Side.BACK).setLine(3, s3);
		}
		sign.setWaxed(true);
	}
	
	private static void setFurnaceDisplay(Block block, Sign sign)
	{
		if(!(block.getState() instanceof Furnace))
		{
			return;
		}
		Furnace furnace = (Furnace) block.getState();
		Map<CookingRecipe<?>, Integer> map = furnace.getRecipesUsed();
		for(Entry<CookingRecipe<?>, Integer> e : map.entrySet())
		{
			CookingBookCategory cbc = e.getKey().getCategory();
			Material mat = e.getKey().getResult().getType();
			int i = e.getValue();
			String s1 = DSI.getPlugin().getYamlHandler().getConfig().getString("Furnace.Line1")
					.replace("%mat%", mat != null ? mat.toString() : "")
					.replace("%amount%", i > 0 ? String.valueOf(i) : "")
					.replace("%cookingbookcategory%", cbc != null ? cbc.toString() : "");
			String s2 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest.Line2")
					.replace("%mat%", mat != null ? mat.toString() : "")
					.replace("%amount%", i > 0 ? String.valueOf(i) : "")
					.replace("%cookingbookcategory%", cbc != null ? cbc.toString() : "");
			String s3 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest.Line3")
					.replace("%mat%", mat != null ? mat.toString() : "")
					.replace("%amount%", i > 0 ? String.valueOf(i) : "")
					.replace("%cookingbookcategory%", cbc != null ? cbc.toString() : "");
			sign.getSide(Side.FRONT).setLine(1, s1);
			sign.getSide(Side.FRONT).setLine(2, s2);
			sign.getSide(Side.FRONT).setLine(3, s3);
			sign.setWaxed(true);
		}
	}
	
	private static void setHopperDisplay(Block block, Sign sign)
	{
		if(!(block.getState() instanceof Crafter))
		{
			return;
		}
		Beacon beacon = (Beacon) block.getState();
		
	}
}