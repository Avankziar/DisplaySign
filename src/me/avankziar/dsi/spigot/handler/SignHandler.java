package me.avankziar.dsi.spigot.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.recipe.CookingBookCategory;

import me.avankziar.dsi.spigot.DSI;

public class SignHandler 
{
	public static List<String> getDisplaySignVerification(String path)
	{
		return DSI.getPlugin().getYamlHandler().getConfig().getStringList(path+".Line0");
	}
	
	public static String isDisplaySign(Sign sign, Material mat)
	{
		String a = sign.getSide(Side.FRONT).getLine(0);
		String b = sign.getSide(Side.BACK).getLine(0);
		for(String s : getDisplaySignVerification(getCamleCase(mat)))
		{
			if(a.contains(s) || b.contains(s))
			{
				return a;
			}
		}
		return null;
	}
	
	private static String getCamleCase(Material mat)
	{
		String a = String.valueOf(mat.toString().charAt(0));
		String b = mat.toString().replace("_", "").substring(1).toLowerCase();
		return a+b;
	}
	
	private static String getPermission(Material mat, String path)
	{
		return DSI.getPlugin().getYamlHandler().getConfig().getString(getCamleCase(mat)+"."+path+".Permission");
	}
	
	public static void setSign(Player player, Block b, Block signblock)
	{
		BlockState blockstate = signblock.getState();
		if(!(blockstate instanceof Sign))
		{
			return;
		}
		Sign sign = (Sign) blockstate;
		Material mat = null;
		String path = null;
		switch(b.getType())
		{
		default:
			break;
		case CHEST:
			mat = Material.CHEST;
			path = isDisplaySign(sign, mat);
			if(path == null)
			{
				return;
			}
			path = path.replace("[", "").replace("]", "");
			setChestDisplay(player, path, b, sign);
			break;
		case FURNACE:
		case BLAST_FURNACE:
		case SMOKER:
			mat = Material.FURNACE;
			path = isDisplaySign(sign, mat);
			if(path == null)
			{
				return;
			}
			path = path.replace("[", "").replace("]", "");
			setFurnaceDisplay(player, path, b, sign);
			break;
		case BEEHIVE:
		case BEE_NEST:
			mat = Material.BEEHIVE;
			path = isDisplaySign(sign, mat);
			if(path == null)
			{
				return;
			}
			path = path.replace("[", "").replace("]", "");
			setBeehiveDisplay(player, path, b, sign);
			break;
		case BREWING_STAND:
			mat = Material.BREWING_STAND;
			path = isDisplaySign(sign, mat);
			if(path == null)
			{
				return;
			}
			path = path.replace("[", "").replace("]", "");
			setBrewingStandDisplay(player, path, b, sign);
			break;
		}
	}
	
	private static boolean checkPermission(Player player, String path, Material mat, Sign sign)
	{
		if(player != null)
		{
			String perm = getPermission(mat, path);
			if(!perm.equals("null"))
			{
				if(!player.hasPermission(perm))
				{
					sign.getSide(Side.FRONT).setLine(0, "");
					sign.getSide(Side.FRONT).setLine(1, "");
					sign.getSide(Side.FRONT).setLine(2, "");
					sign.getSide(Side.FRONT).setLine(3, "");
					if(DSI.getPlugin().getYamlHandler().getConfig().getBoolean(getCamleCase(mat)+".WriteOnBack"))
					{
						sign.getSide(Side.BACK).setLine(0, "");
						sign.getSide(Side.BACK).setLine(1, "");
						sign.getSide(Side.BACK).setLine(2, "");
						sign.getSide(Side.BACK).setLine(3, "");
					}
					sign.setWaxed(true);
					player.sendMessage(DSI.getPlugin().getYamlHandler().getLang().getString("NoPermission"));
					return false;
				}
			}
		}
		return true;
	}
	
	private static void setChestDisplay(Player player, String path, Block block, Sign sign)
	{
		if(!checkPermission(player, path, Material.CHEST, sign))
		{
			return;
		}
		HashMap<Material, Integer> count = new HashMap<>();
		if(!(block.getState() instanceof Chest))
		{
			return;
		}
		Chest chest = (Chest) block.getState();
		int amountofitems = 0;
		if(chest.getInventory() instanceof DoubleChestInventory)
		{
			DoubleChestInventory dci = (DoubleChestInventory) chest.getInventory();
			for(ItemStack is : dci.getStorageContents())
			{
				if(is == null || is.getType() == Material.AIR)
				{
					continue;
				}
				int amount = is.getAmount();
				if(count.containsKey(is.getType()))
				{
					amount = amount + count.get(is.getType());
				}
				count.put(is.getType(), amount);
				amountofitems += amount;
			}
		} else
		{
			for(ItemStack is : chest.getInventory().getStorageContents())
			{
				if(is == null || is.getType() == Material.AIR)
				{
					continue;
				}
				int amount = is.getAmount();
				if(count.containsKey(is.getType()))
				{
					amount = amount + count.get(is.getType());
				}
				count.put(is.getType(), amount);
				amountofitems += amount;
			}
		}
		List<Map.Entry<Material, Integer>> top6 = count.entrySet().stream()
	            .sorted(Map.Entry.<Material, Integer>comparingByValue().reversed()) // Nach Werten absteigend sortieren
	            .limit(6) // Nur die obersten 3 Elemente nehmen
	            .collect(Collectors.toList()); // In eine Liste sammeln
		Material mat1 = top6.size() >= 1 ? top6.get(0).getKey() : null;
		int i1 = top6.size() >= 1 ? top6.get(0).getValue() : 0;
		Material mat2 = top6.size() >= 2 ? top6.get(1).getKey() : null;
		int i2 = top6.size() >= 2 ? top6.get(1).getValue() : 0;
		Material mat3 = top6.size() >= 3 ? top6.get(2).getKey() : null;
		int i3 = top6.size() >= 3 ? top6.get(2).getValue() : 0;
		Material mat4 = top6.size() >= 4 ? top6.get(3).getKey() : null;
		int i4 = top6.size() >= 4 ? top6.get(3).getValue() : 0;
		Material mat5 = top6.size() >= 5 ? top6.get(4).getKey() : null;
		int i5 = top6.size() >= 5 ? top6.get(4).getValue() : 0;
		Material mat6 = top6.size() >= 6 ? top6.get(5).getKey() : null;
		int i6 = top6.size() >= 6 ? top6.get(5).getValue() : 0;
		String s1 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest."+path+".Line1")
				.replace("%amounttotal%", String.valueOf(amountofitems))
				.replace("%amountofitems%", String.valueOf(count.size()))
				.replace("%mat1%", mat1 != null ? mat1.toString() : "")
				.replace("%amount1%", i1 > 0 ? String.valueOf(i1) : "")
				.replace("%mat2%", mat2 != null ? mat2.toString() : "")
				.replace("%amount2%", i2 > 0 ? String.valueOf(i2) : "")
				.replace("%mat3%", mat3 != null ? mat3.toString() : "")
				.replace("%amount3%", i3 > 0 ? String.valueOf(i3) : "")
				.replace("%mat4%", mat4 != null ? mat4.toString() : "")
				.replace("%amount4%", i4 > 0 ? String.valueOf(i4) : "")
				.replace("%mat5%", mat5 != null ? mat5.toString() : "")
				.replace("%amount5%", i5 > 0 ? String.valueOf(i5) : "")
				.replace("%mat6%", mat6 != null ? mat6.toString() : "")
				.replace("%amount6%", i6 > 0 ? String.valueOf(i6) : "");
		s1 = s1.substring(0, s1.length() >= 15 ? 15 : s1.length());
		String s2 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest."+path+".Line2")
				.replace("%amounttotal%", String.valueOf(amountofitems))
				.replace("%amountofitems%", String.valueOf(count.size()))
				.replace("%mat1%", mat1 != null ? mat1.toString() : "")
				.replace("%amount1%", i1 > 0 ? String.valueOf(i1) : "")
				.replace("%mat2%", mat2 != null ? mat2.toString() : "")
				.replace("%amount2%", i2 > 0 ? String.valueOf(i2) : "")
				.replace("%mat3%", mat3 != null ? mat3.toString() : "")
				.replace("%amount3%", i3 > 0 ? String.valueOf(i3) : "")
				.replace("%mat4%", mat4 != null ? mat4.toString() : "")
				.replace("%amount4%", i4 > 0 ? String.valueOf(i4) : "")
				.replace("%mat5%", mat5 != null ? mat5.toString() : "")
				.replace("%amount5%", i5 > 0 ? String.valueOf(i5) : "")
				.replace("%mat6%", mat6 != null ? mat6.toString() : "")
				.replace("%amount6%", i6 > 0 ? String.valueOf(i6) : "");
		s2 = s2.substring(0, s2.length() >= 15 ? 15 : s2.length());
		String s3 = DSI.getPlugin().getYamlHandler().getConfig().getString("Chest."+path+".Line3")
				.replace("%amounttotal%", String.valueOf(amountofitems))
				.replace("%amountofitems%", String.valueOf(count.size()))
				.replace("%mat1%", mat1 != null ? mat1.toString() : "")
				.replace("%amount1%", i1 > 0 ? String.valueOf(i1) : "")
				.replace("%mat2%", mat2 != null ? mat2.toString() : "")
				.replace("%amount2%", i2 > 0 ? String.valueOf(i2) : "")
				.replace("%mat3%", mat3 != null ? mat3.toString() : "")
				.replace("%amount3%", i3 > 0 ? String.valueOf(i3) : "")
				.replace("%mat4%", mat4 != null ? mat4.toString() : "")
				.replace("%amount4%", i4 > 0 ? String.valueOf(i4) : "")
				.replace("%mat5%", mat5 != null ? mat5.toString() : "")
				.replace("%amount5%", i5 > 0 ? String.valueOf(i5) : "")
				.replace("%mat6%", mat6 != null ? mat6.toString() : "")
				.replace("%amount6%", i6 > 0 ? String.valueOf(i6) : "");
		s3 = s3.substring(0, s3.length() >= 15 ? 15 : s3.length());
		sign.getSide(Side.FRONT).setLine(1, s1);
		sign.getSide(Side.FRONT).setLine(2, s2);
		sign.getSide(Side.FRONT).setLine(3, s3);
		if(DSI.getPlugin().getYamlHandler().getConfig().getBoolean(getCamleCase(block.getType())+".WriteOnBack"))
		{
			sign.getSide(Side.BACK).setLine(0, sign.getSide(Side.FRONT).getLine(0));
			sign.getSide(Side.BACK).setLine(1, s1);
			sign.getSide(Side.BACK).setLine(2, s2);
			sign.getSide(Side.BACK).setLine(3, s3);
		}
		sign.setWaxed(true);
		sign.update();
	}
	
	private static void setFurnaceDisplay(Player player, String path, Block block, Sign sign)
	{
		if(!checkPermission(player, path, Material.FURNACE, sign))
		{
			DSI.log.info("0");
			return;
		}
		if(!(block.getState() instanceof Furnace))
		{
			DSI.log.info("1");
			return;
		}
		Furnace furnace = (Furnace) block.getState();
		Map<CookingRecipe<?>, Integer> map = furnace.getRecipesUsed();
		if(map.isEmpty())
		{
			String s1 = DSI.getPlugin().getYamlHandler().getConfig().getString("Furnace."+path+".Line1")
					.replace("%resultmat%", "")
					.replace("%amount%", "")
					.replace("%cookingbookcategory%", "")
					.replace("%burntime%", String.valueOf(furnace.getBurnTime()/20));
			s1 = s1.substring(0, s1.length() >= 15 ? 15 : s1.length());
			String s2 = DSI.getPlugin().getYamlHandler().getConfig().getString("Furnace."+path+".Line2")
					.replace("%resultmat%", "")
					.replace("%amount%", "")
					.replace("%cookingbookcategory%", "")
					.replace("%burntime%", String.valueOf(furnace.getBurnTime()/20));
			s2 = s2.substring(0, s2.length() >= 15 ? 15 : s2.length());
			String s3 = DSI.getPlugin().getYamlHandler().getConfig().getString("Furnace."+path+".Line3")
					.replace("%resultmat%", "")
					.replace("%amount%", "")
					.replace("%cookingbookcategory%", "")
					.replace("%burntime%", String.valueOf(furnace.getBurnTime()/20));
			s3 = s3.substring(0, s3.length() >= 15 ? 15 : s3.length());
			sign.getSide(Side.FRONT).setLine(1, s1);
			sign.getSide(Side.FRONT).setLine(2, s2);
			sign.getSide(Side.FRONT).setLine(3, s3);
			if(DSI.getPlugin().getYamlHandler().getConfig().getBoolean(getCamleCase(block.getType())+".WriteOnBack"))
			{
				sign.getSide(Side.BACK).setLine(0, sign.getSide(Side.FRONT).getLine(0));
				sign.getSide(Side.BACK).setLine(1, s1);
				sign.getSide(Side.BACK).setLine(2, s2);
				sign.getSide(Side.BACK).setLine(3, s3);
			}
			sign.setWaxed(true);
			sign.update();
		} else
		{
			for(Entry<CookingRecipe<?>, Integer> e : map.entrySet())
			{
				CookingBookCategory cbc = e.getKey().getCategory();
				Material mat = e.getKey().getResult().getType();
				int i = e.getValue();
				String s1 = DSI.getPlugin().getYamlHandler().getConfig().getString("Furnace."+path+".Line1")
						.replace("%resultmat%", mat != null ? mat.toString() : "")
						.replace("%amount%", i > 0 ? String.valueOf(i) : "")
						.replace("%cookingbookcategory%", cbc != null ? cbc.toString() : "")
						.replace("%burntime%", String.valueOf(furnace.getBurnTime()/20));
				s1 = s1.substring(0, s1.length() >= 15 ? 15 : s1.length());
				String s2 = DSI.getPlugin().getYamlHandler().getConfig().getString("Furnace."+path+".Line2")
						.replace("%mat%", mat != null ? mat.toString() : "")
						.replace("%amount%", i > 0 ? String.valueOf(i) : "")
						.replace("%cookingbookcategory%", cbc != null ? cbc.toString() : "")
						.replace("%burntime%", String.valueOf(furnace.getBurnTime()/20));
				s2 = s2.substring(0, s2.length() >= 15 ? 15 : s2.length());
				String s3 = DSI.getPlugin().getYamlHandler().getConfig().getString("Furnace."+path+".Line3")
						.replace("%mat%", mat != null ? mat.toString() : "")
						.replace("%amount%", i > 0 ? String.valueOf(i) : "")
						.replace("%cookingbookcategory%", cbc != null ? cbc.toString() : "")
						.replace("%burntime%", String.valueOf(furnace.getBurnTime()/20));
				s3 = s3.substring(0, s3.length() >= 15 ? 15 : s3.length());
				sign.getSide(Side.FRONT).setLine(1, s1);
				sign.getSide(Side.FRONT).setLine(2, s2);
				sign.getSide(Side.FRONT).setLine(3, s3);
				if(DSI.getPlugin().getYamlHandler().getConfig().getBoolean(getCamleCase(block.getType())+".WriteOnBack"))
				{
					sign.getSide(Side.BACK).setLine(0, sign.getSide(Side.FRONT).getLine(0));
					sign.getSide(Side.BACK).setLine(1, s1);
					sign.getSide(Side.BACK).setLine(2, s2);
					sign.getSide(Side.BACK).setLine(3, s3);
				}
				sign.setWaxed(true);
				sign.update();
			}
		}
		
	}
	
	private static void setBeehiveDisplay(Player player, String path, Block block, Sign sign)
	{
		if(!checkPermission(player, path, Material.BEEHIVE, sign))
		{
			return;
		}
		if(!(block.getState() instanceof Beehive))
		{
			return;
		}
		Beehive beehive = (Beehive) block.getState();
		if(!(block.getBlockData() instanceof org.bukkit.block.data.type.Beehive))
		{
			return;
		}
		org.bukkit.block.data.type.Beehive beehiveBD = (org.bukkit.block.data.type.Beehive) block.getBlockData();
		String s1 = DSI.getPlugin().getYamlHandler().getConfig().getString("Beehive."+path+".Line1")
				.replace("%entities%", String.valueOf(beehive.getEntityCount()))
				.replace("%honeylevel%", String.valueOf(beehiveBD.getHoneyLevel()));
		String s2 = DSI.getPlugin().getYamlHandler().getConfig().getString("Beehive."+path+".Line2")
				.replace("%entities%", String.valueOf(beehive.getEntityCount()))
				.replace("%honeylevel%", String.valueOf(beehiveBD.getHoneyLevel()));
		String s3 = DSI.getPlugin().getYamlHandler().getConfig().getString("Beehive."+path+".Line3")
				.replace("%entities%", String.valueOf(beehive.getEntityCount()))
				.replace("%honeylevel%", String.valueOf(beehiveBD.getHoneyLevel()));
		sign.getSide(Side.FRONT).setLine(1, s1);
		sign.getSide(Side.FRONT).setLine(2, s2);
		sign.getSide(Side.FRONT).setLine(3, s3);
		if(DSI.getPlugin().getYamlHandler().getConfig().getBoolean(getCamleCase(block.getType())+".WriteOnBack"))
		{
			sign.getSide(Side.BACK).setLine(0, sign.getSide(Side.FRONT).getLine(0));
			sign.getSide(Side.BACK).setLine(1, s1);
			sign.getSide(Side.BACK).setLine(2, s2);
			sign.getSide(Side.BACK).setLine(3, s3);
		}
		sign.setWaxed(true);
		sign.update();
	}
	
	private static void setBrewingStandDisplay(Player player, String path, Block block, Sign sign)
	{
		if(!checkPermission(player, path, Material.BREWING_STAND, sign))
		{
			return;
		}
		if(!(block.getState() instanceof BrewingStand))
		{
			return;
		}
		BrewingStand brewingstand = (BrewingStand) block.getState();
		BrewerInventory bi = brewingstand.getInventory();
		String s1 = DSI.getPlugin().getYamlHandler().getConfig().getString("Brewingstand."+path+".Line1")
				.replace("%fuellevel%", String.valueOf(brewingstand.getFuelLevel()))
				.replace("%ingredient%", bi.getIngredient() != null ? bi.getIngredient().getType().toString() : "");
		String s2 = DSI.getPlugin().getYamlHandler().getConfig().getString("Brewingstand."+path+".Line2")
				.replace("%fuellevel%", String.valueOf(brewingstand.getFuelLevel()))
				.replace("%ingredient%", bi.getIngredient() != null ? bi.getIngredient().getType().toString() : "");
		String s3 = DSI.getPlugin().getYamlHandler().getConfig().getString("Brewingstand."+path+".Line3")
				.replace("%fuellevel%", String.valueOf(brewingstand.getFuelLevel()))
				.replace("%ingredient%", bi.getIngredient() != null ? bi.getIngredient().getType().toString() : "");
		sign.getSide(Side.FRONT).setLine(1, s1);
		sign.getSide(Side.FRONT).setLine(2, s2);
		sign.getSide(Side.FRONT).setLine(3, s3);
		if(DSI.getPlugin().getYamlHandler().getConfig().getBoolean(getCamleCase(block.getType())+".WriteOnBack"))
		{
			sign.getSide(Side.BACK).setLine(0, sign.getSide(Side.FRONT).getLine(0));
			sign.getSide(Side.BACK).setLine(1, s1);
			sign.getSide(Side.BACK).setLine(2, s2);
			sign.getSide(Side.BACK).setLine(3, s3);
		}
		sign.setWaxed(true);
		sign.update();
	}
}