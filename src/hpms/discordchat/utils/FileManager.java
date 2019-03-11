package hpms.discordchat.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FileManager {
	
	private static String PATH;
	
	private static FileConfiguration storageFile;
	private static FileConfiguration prefixFile;
	private static FileConfiguration configFile;
	
	//Error somewhere here
	public static void initFileManager(JavaPlugin plugin) {
		plugin.getDataFolder().mkdir();
		PATH = plugin.getDataFolder().getPath();
		File storage = new File(PATH,"storage.yml");
		File prefix = new File(PATH,"prefix.yml");
		File config = new File(PATH,"config.yml");
		createNewFile(config);
		createNewFile(storage);
		createNewFile(prefix);
		storageFile = YamlConfiguration.loadConfiguration(storage);
		prefixFile = YamlConfiguration.loadConfiguration(prefix);
		configFile = plugin.getConfig();
		if(storageFile.getConfigurationSection("storage") == null) {
			storageFile.createSection("storage");
			saveConfiguration("storage");
		}
		if(prefixFile.getConfigurationSection("prefix") == null) {
			prefixFile.createSection("prefix");
			saveConfiguration("prefix");
		}
	}
	
	public static void saveConfiguration(String file) {
		if(file.equalsIgnoreCase("storage")) {
			saveFile(storageFile,new File(PATH,"storage.yml"));
		}
		else if(file.equalsIgnoreCase("prefix")) {
			saveFile(prefixFile,new File(PATH,"prefix.yml"));
		}
		else if(file.equalsIgnoreCase("config")) {
			saveFile(configFile,new File(PATH,"config.yml"));
		}
	}
	
	public static ConfigurationSection getConfigurationSection(String file,String section) {
		if(file.equalsIgnoreCase("storage")) {
			return storageFile.getConfigurationSection(section);
		}
		else if(file.equalsIgnoreCase("prefix")) {
			return prefixFile.getConfigurationSection(section);
		}
		else if(file.equalsIgnoreCase("config")) {
			return configFile.getConfigurationSection(section);
		}
		return null;
	}
	
	public static FileConfiguration getConfig() {
		return configFile;
	}
	
	private static void saveFile(FileConfiguration fileConfig,File file) {
		try {
			fileConfig.save(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createNewFile(File file) {
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
