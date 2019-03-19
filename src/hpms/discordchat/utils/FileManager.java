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
	private static FileConfiguration roleFile;
	private static FileConfiguration configFile;
	
	public static void initFileManager(JavaPlugin plugin) {
		plugin.getDataFolder().mkdir();
		PATH = plugin.getDataFolder().getPath();
		File storage = new File(PATH,"storage.yml");
		File role = new File(PATH,"role.yml");
		File config = new File(PATH,"config.yml");
		createNewFile(config);
		createNewFile(storage);
		createNewFile(role);
		storageFile = YamlConfiguration.loadConfiguration(storage);
		roleFile = YamlConfiguration.loadConfiguration(role);
		configFile = plugin.getConfig();
		if(storageFile.getConfigurationSection("storage") == null) {
			storageFile.createSection("storage");
			saveConfiguration("storage");
		}
		if(roleFile.getConfigurationSection("role") == null) {
			roleFile.createSection("role");
			saveConfiguration("role");
		}
	}
	
	public static void saveConfiguration(String file) {
		if(file.equalsIgnoreCase("storage")) {
			saveFile(storageFile,new File(PATH,"storage.yml"));
		}
		else if(file.equalsIgnoreCase("role")) {
			saveFile(roleFile,new File(PATH,"role.yml"));
		}
		else if(file.equalsIgnoreCase("config")) {
			saveFile(configFile,new File(PATH,"config.yml"));
		}
	}
	
	public static ConfigurationSection getConfigurationSection(String file,String section) {
		if(file.equalsIgnoreCase("storage")) {
			return storageFile.getConfigurationSection(section);
		}
		else if(file.equalsIgnoreCase("role")) {
			return roleFile.getConfigurationSection(section);
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
	
	public static File createNewFile(String name) {
		File file = new File(PATH,name);
		createNewFile(file);
		return file;
	}
	
	public static YamlConfiguration getYamlConfiguration(String name) {
		YamlConfiguration file = YamlConfiguration.loadConfiguration(createNewFile(name));
		return file;
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
