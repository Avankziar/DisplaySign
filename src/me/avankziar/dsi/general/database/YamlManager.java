package me.avankziar.dsi.general.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import me.avankziar.dsi.general.database.Language.ISO639_2B;

public class YamlManager
{	
	public enum Type
	{
		BUNGEE, SPIGOT, VELO;
	}
	
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private Type type;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> mvelanguageKeys = new LinkedHashMap<>();
	/*
	 * Here are mutiplefiles in one "double" map. The first String key is the filename
	 * So all filename muss be predefine. For example in the config.
	 */
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> guisKeys = new LinkedHashMap<>();
	
	public YamlManager(Type type)
	{
		this.type = type;
		initConfig();
		initLanguage();
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getModifierValueEntryLanguageKey()
	{
		return mvelanguageKeys;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Language>> getGUIKey()
	{
		return guisKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInputBukkit(org.bukkit.configuration.file.YamlConfiguration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(key.startsWith("#"))
		{
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no aktual key are present
				return;
			}
			if(yml.getComments(k) != null && !yml.getComments(k).isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.setComments(k, Arrays.asList(s));
				}
			} else
			{
				List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
				ArrayList<String> stringList = new ArrayList<>();
				if(list instanceof List<?>)
				{
					for(Object o : list)
					{
						if(o instanceof String)
						{
							stringList.add(((String) o).replace("\r\n", ""));
						}
					}
				}
				yml.setComments(k, (List<String>) stringList);
			}
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	public void setFileInput(dev.dejvokep.boostedyaml.YamlDocument yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType) throws org.spongepowered.configurate.serialize.SerializationException
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(key.startsWith("#"))
		{
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no actual key are present
				return;
			}
			if(yml.getBlock(k) == null)
			{
				return;
			}
			if(yml.getBlock(k).getComments() != null && !yml.getBlock(k).getComments().isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.getBlock(k).setComments(Arrays.asList(s));
				}
			} else
			{
				List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
				ArrayList<String> stringList = new ArrayList<>();
				if(list instanceof List<?>)
				{
					for(Object o : list)
					{
						if(o instanceof String)
						{
							stringList.add(((String) o).replace("\r\n", ""));
						}
					}
				}
				yml.getBlock(k).setComments((List<String>) stringList);
			}
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, convertMiniMessageToBungee(((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "")));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(convertMiniMessageToBungee(((String) o).replace("\r\n", "")));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	private String convertMiniMessageToBungee(String s)
	{
		if(type != Type.BUNGEE)
		{
			//If Server is not Bungee, there is no need to convert.
			return s;
		}
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(c == '<' && i+1 < s.length())
			{
				char cc = s.charAt(i+1);
				if(cc == '#' && i+8 < s.length())
				{
					//Hexcolors
					//     i12345678
					//f.e. <#00FF00>
					String rc = s.substring(i, i+8);
					b.append(rc.replace("<#", "&#").replace(">", ""));
					i += 8;
				} else
				{
					//Normal Colors
					String r = null;
					StringBuilder sub = new StringBuilder();
					sub.append(c).append(cc);
					i++;
					for(int j = i+1; j < s.length(); j++)
					{
						i++;
						char jc = s.charAt(j);
						if(jc == '>')
						{
							sub.append(jc);
							switch(sub.toString())
							{
							case "</color>":
							case "</black>":
							case "</dark_blue>":
							case "</dark_green>":
							case "</dark_aqua>":
							case "</dark_red>":
							case "</dark_purple>":
							case "</gold>":
							case "</gray>":
							case "</dark_gray>":
							case "</blue>":
							case "</green>":
							case "</aqua>":
							case "</red>":
							case "</light_purple>":
							case "</yellow>":
							case "</white>":
							case "</obf>":
							case "</obfuscated>":
							case "</b>":
							case "</bold>":
							case "</st>":
							case "</strikethrough>":
							case "</u>":
							case "</underlined>":
							case "</i>":
							case "</em>":
							case "</italic>":
								r = "";
								break;
							case "<black>":
								r = "&0";
								break;
							case "<dark_blue>":
								r = "&1";
								break;
							case "<dark_green>":
								r = "&2";
								break;
							case "<dark_aqua>":
								r = "&3";
								break;
							case "<dark_red>":
								r = "&4";
								break;
							case "<dark_purple>":
								r = "&5";
								break;
							case "<gold>":
								r = "&6";
								break;
							case "<gray>":
								r = "&7";
								break;
							case "<dark_gray>":
								r = "&8";
								break;
							case "<blue>":
								r = "&9";
								break;
							case "<green>":
								r = "&a";
								break;
							case "<aqua>":
								r = "&b";
								break;
							case "<red>":
								r = "&c";
								break;
							case "<light_purple>":
								r = "&d";
								break;
							case "<yellow>":
								r = "&e";
								break;
							case "<white>":
								r = "&f";
								break;
							case "<obf>":
							case "<obfuscated>":
								r = "&k";
								break;
							case "<b>":
							case "<bold>":
								r = "&l";
								break;
							case "<st>":
							case "<strikethrough>":
								r = "&m";
								break;
							case "<u>":
							case "<underlined>":
								r = "&n";
								break;
							case "<i>":
							case "<em>":
							case "<italic>":
								r = "&o";
								break;
							case "<reset>":
								r = "&r";
								break;
							case "<newline>":
								r = "~!~";
								break;
							}
							b.append(r);
							break;
						} else
						{
							//Search for the color.
							sub.append(jc);
						}
					}
				}
			} else
			{
				b.append(c);
			}
		}
		return b.toString();
	}
	
	private void addComments(LinkedHashMap<String, Language> mapKeys, String path, Object[] o)
	{
		mapKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, o));
	}
	
	private void addConfig(String path, Object[] c, Object[] o)
	{
		configKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configKeys, "#"+path, o);
	}
	
	public void initConfig() //INFO:Config
	{
		addConfig("useIFHAdministration",
				new Object[] {
				true},
				new Object[] {
				"Boolean um auf das IFH Interface Administration zugreifen soll.",
				"Wenn 'true' eingegeben ist, aber IFH Administration ist nicht vorhanden, so werden automatisch die eigenen Configwerte genommen.",
				"Boolean to access the IFH Interface Administration.",
				"If 'true' is entered, but IFH Administration is not available, the own config values are automatically used."});
		addConfig("IFHAdministrationPath", 
				new Object[] {
				"dsi"},
				new Object[] {
				"",
				"Diese Funktion sorgt dafür, dass das Plugin auf das IFH Interface Administration zugreifen kann.",
				"Das IFH Interface Administration ist eine Zentrale für die Daten von Sprache, Servername und Mysqldaten.",
				"Diese Zentralisierung erlaubt für einfache Änderung/Anpassungen genau dieser Daten.",
				"Sollte das Plugin darauf zugreifen, werden die Werte in der eigenen Config dafür ignoriert.",
				"",
				"This function ensures that the plugin can access the IFH Interface Administration.",
				"The IFH Interface Administration is a central point for the language, server name and mysql data.",
				"This centralization allows for simple changes/adjustments to precisely this data.",
				"If the plugin accesses it, the values in its own config are ignored."});
		addConfig("Language",
				new Object[] {
				"ENG"},
				new Object[] {
				"",
				"Die eingestellte Sprache. Von Haus aus sind 'ENG=Englisch' und 'GER=Deutsch' mit dabei.",
				"Falls andere Sprachen gewünsch sind, kann man unter den folgenden Links nachschauen, welchs Kürzel für welche Sprache gedacht ist.",
				"Siehe hier nach, sowie den Link, welche dort auch für Wikipedia steht.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java",
				"",
				"The set language. By default, ENG=English and GER=German are included.",
				"If other languages are required, you can check the following links to see which abbreviation is intended for which language.",
				"See here, as well as the link, which is also there for Wikipedia.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java"});		
		addConfig("Chest.Line0",
				new Object[] {
				"[DSIChest]",
				"[DSIChest2]",
				"[DSIChest3]"},
				new Object[] {
				"",
				"Die Möglichen Variabeln um für die Kisten die Replace Linien zu erhalten.",
				"Dabei ist immer die Variable minus [ und ] gemeint, welches den Pfad deklariert.",
				"Pfade können unbegrenzt hinzugefügt werden. Die Standartpfade werden aber immer bleiben, auch wenn diese gelöscht werden.",
				"Jedoch müssen diese nicht benutzt werden.",
				"",
				"The possible variables to obtain the replace lines for the crates.",
				"This always refers to the variable minus [ and ], which declares the path.",
				"Paths can be added indefinitely. However, the default paths will always remain, even if they are deleted.",
				"However, these do not have to be used."});
		addConfig("Chest.WriteOnBack",
				new Object[] {
				false},
				new Object[] {
				"",
				"Wenn 'true', dann wird alles auf beiden Seiten des Schildes geschrieben.",
				"",
				"If 'true', then everything is written on both sides of the sign."});
		addConfig("Chest.DSIChest.Permission",
				new Object[] {
				"dsi.chest.dsi"},
				new Object[] {
				"",
				"Die Permission um an einer Kiste die Display auf Schilder einzurichten.",
				"'null' zum deaktivieren.",
				"",
				"The permission to set up the display on signs on a box.",
				"'null' to deactivate."});
		addConfig("Chest.DSIChest.Line1",
				new Object[] {
				"1) %amount1% %mat1%"},
				new Object[] {
				"",
				"Die Möglichen Variabeln um für die Kisten die Replace Linien zu erhalten.",
				"Dabei ist immer die Variable minus [ und ] gemeint, welches den Pfad deklariert.",
				"%mat1% für das Item als Material welches am meisten vorhanden ist.",
				"%amount1% für das Item als Anzahl welches am meisten vorhanden ist.",
				"%mat2% für das Item als Material welches am zweit meisten vorhanden ist.",
				"%amount2% für das Item als Anzahl welches am zweit meisten vorhanden ist.",
				"%mat3% für das Item als Material welches am dritt meisten vorhanden ist.",
				"%amount3% für das Item als Anzahl welches am dritt meisten vorhanden ist.",
				"%mat4% für das Item als Material welches am viert meisten vorhanden ist.",
				"%amount4% für das Item als Anzahl welches am viert meisten vorhanden ist.",
				"%mat5% für das Item als Material welches am fünft meisten vorhanden ist.",
				"%amount5% für das Item als Anzahl welches am fünft meisten vorhanden ist.",
				"%mat6% für das Item als Material welches am sechst meisten vorhanden ist.",
				"%amount6% für das Item als Anzahl welches am sechst meisten vorhanden ist.",
				"%amounttotal% für die totale Anzahl an Items unabhängig vom Type in der Kiste.",
				"%amountofitems% für die Anzahl, wieviel verschiedene Itemtypen in der Kiste sind.",
				"",
				"The possible variables to obtain the replace lines for the crates.",
				"This always refers to the variable minus [ and ], which declares the path.",
				"%mat1% for the item as material which is most available.",
				"%amount1% for the item with the highest count.",
				"%mat2% for the item as the second most available material.",
				"%amount2% for the item with the second highest count.",
				"%mat3% for the item as material which is present the third most.",
				"%amount3% for the item with the third highest count.",
				"%mat4% for the item as the fourth most available material.",
				"%amount4% for the item with the fourth highest count.",
				"%mat5% for the item as material which is available the fifth most.",
				"%amount5% for the item with the fifth highest number.",
				"%mat6% for the item as material which is present the sixth most.",
				"%amount6% for the item with the sixth highest count.",
				"%amounttotal% for the total number of items regardless of the type in the chest.",
				"%amountofitems% for the number of different item types in the chest."});
		configKeys.put("Chest.DSIChest.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"2) %amount2% %mat2%"}));
		configKeys.put("Chest.DSIChest.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"3) %amount3% %mat3%"}));
		configKeys.put("Chest.DSIChest2.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.chest.dis2"}));
		configKeys.put("Chest.DSIChest2.Line1", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"4) %amount4% %mat4%"}));
		configKeys.put("Chest.DSIChest2.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"5) %amount5% %mat5%"}));
		configKeys.put("Chest.DSIChest2.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"6) %amount6% %mat6%"}));
		configKeys.put("Chest.DSIChest3.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.chest.dis3"}));
		configKeys.put("Chest.DSIChest3.Line1", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Total: %amounttotal%"}));
		configKeys.put("Chest.DSIChest3.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Itemstyps: %amountofitems%"}));
		configKeys.put("Chest.DSIChest3.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				""}));
		//--------------------------
		configKeys.put("Furnace.Line0", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"[DSIFurnace]",
				"[DSIFurnace2]"}));
		configKeys.put("Furnace.WriteOnBack", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Furnace.DSIFurnace.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.furnace.dsi"}));
		addConfig("Furnace.DSIFurnace.Line1",
				new Object[] {
				"Res: %resultmat%"},
				new Object[] {
				"",
				"Listet auf, was in der 2 Linie geschrieben wird (Linie 1 bzw. 0 im Code ist für die Schildzuweisung dar)",
				"Mögliche Replacer sind:",
				"%resultmat% das Item als Material, welches als Resultat aus dem Ofen kommen wird.",
				"%amount% die Anzahl an Items welche schon aus dem Ofen vom jetztigen Rezept gekommen sind.",
				"%cookingbookcategory% die Kategorie des Kochbuches zu dem dieses Rezept gehört.",
				"%burntime% welche als Sekunden dargestellte Zeit wie lange der Ofen noch brennt. Ohne mögliche wartenden Brennstoff dazuzuzählen.",
				"",
				"Lists what is written in the 2 line (line 1 or 0 in the code is for the sign assignment)",
				"Possible replacers are:",
				"%resultmat% das Item als Material, welches als Resultat aus dem Ofen kommen wird.",
				"%amount% the number of items that have already come out of the oven from the current recipe.",
				"%cookingbookcategory% the category of the cookbook to which this recipe belongs.",
				"%burntime% which is the time in seconds how long the stove is still burning. Without counting possible waiting fuel."});
		configKeys.put("Furnace.DSIFurnace.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"x%amount%"}));
		configKeys.put("Furnace.DSIFurnace.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"BurnTime: %burntime%s"}));
		configKeys.put("Furnace.DSIFurnace2.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.furnace.dsi2"}));
		configKeys.put("Furnace.DSIFurnace2.Line1", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"%amount%x"}));
		configKeys.put("Furnace.DSIFurnace2.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"%resultmat%"}));
		configKeys.put("Furnace.DSIFurnace2.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				""}));
		//--------------------------
		configKeys.put("Beehive.Line0", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"[DSIBeehive]",
				"[DSIBeehive2]"}));
		configKeys.put("Beehive.WriteOnBack", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Beehive.DSIBeehive.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.beehive.dsi"}));
		addConfig("Beehive.DSIBeehive.Line1",
				new Object[] {
				"Bees: %entities%"},
				new Object[] {
				"",
				"Listet auf, was in der 2 Linie geschrieben wird (Linie 1 bzw. 0 im Code ist für die Schildzuweisung dar)",
				"Mögliche Replacer sind:",
				"%entities%, die Anzahl an Bienen im Nest.",
				"%honeylevel%, das Honiglevel des Nest.",
				"",
				"Lists what is written in the 2 line (line 1 or 0 in the code is for the sign assignment)",
				"Possible replacers are:",
				"%entities%, the number of bees in the nest.",
				"%honeylevel%, the honey level of the nest."});
		configKeys.put("Beehive.DSIBeehive.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Honey: %honeylevel%"}));
		configKeys.put("Beehive.DSIBeehive.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				""}));
		configKeys.put("Beehive.DSIBeehive2.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.beehive.dsi2"}));
		configKeys.put("Beehive.DSIBeehive2.Line1", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Bees: %entities%"}));
		configKeys.put("Beehive.DSIBeehive2.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"-------------------"}));
		configKeys.put("Beehive.DSIBeehive2.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Honey: %honeylevel%"}));
		//--------------------------
		configKeys.put("Brewingstand.Line0", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"[DSIBrewing]",
				"[DSIBrewing2]"}));
		configKeys.put("Brewingstand.WriteOnBack", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Brewingstand.DSIBrewing.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.brewingstand.dsi"}));
		addConfig("Brewingstand.DSIBrewing.Line1",
				new Object[] {
				"Fuellev: %fuellevel%"},
				new Object[] {
				"",
				"Listet auf, was in der 2 Linie geschrieben wird (Linie 1 bzw. 0 im Code ist für die Schildzuweisung dar)",
				"Mögliche Replacer sind:",
				"%fuellevel%, listet das Fuellevel des Braustand auf.",
				"%ingredient%, listet das Item auf, welches momentan als Zutat gilt.",
				"",
				"Lists what is written in the 2 line (line 1 or 0 in the code is for the sign assignment)",
				"Possible replacers are:",
				"%fuellevel%, lists the filling level of the brewing state.",
				"%ingredient%, lists the item that is currently considered an ingredient."});
		configKeys.put("Brewingstand.DSIBrewing.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"-------------------"}));
		configKeys.put("Brewingstand.DSIBrewing.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Ingd: %ingredient%"}));
		configKeys.put("Brewingstand.DSIBrewing2.Permission", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dsi.beehive.dsi2"}));
		configKeys.put("Brewingstand.DSIBrewing2.Line1", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Not your Brewstand!"}));
		configKeys.put("Brewingstand.DSIBrewing2.Line2", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"-------------------"}));
		configKeys.put("Brewingstand.DSIBrewing2.Line3", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Shush away!"}));
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"&cYour input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast dafür keine Rechte!",
						"&cYou dont not have the rights!"}));
		languageKeys.put("NoPlayerExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler existiert nicht!",
						"&cThe player does not exist!"}));
		languageKeys.put("NoNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine ganze Zahl sein.",
						"&cThe argument &f%value% &must be an integer."}));
		languageKeys.put("NoDouble",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine Gleitpunktzahl sein!",
						"&cThe argument &f%value% &must be a floating point number!"}));
		languageKeys.put("IsNegativ",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine positive Zahl sein!",
						"&cThe argument &f%value% &must be a positive number!"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick mich!",
						"&eClick me!"}));
		languageKeys.put("Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6BungeeTeleportManager&7]&e=====",
						"&e=====&7[&6BungeeTeleportManager&7]&e====="}));
		languageKeys.put("Next", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put("Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put("IsTrue", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&a✔",
						"&a✔"}));
		languageKeys.put("IsFalse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c✖",
						"&c✖"}));
	}
}