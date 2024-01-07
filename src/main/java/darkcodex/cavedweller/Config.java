package darkcodex.cavedweller;

import java.util.Properties;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
/**
 * The Config class manages a set of configuration properties.
 * It provides methods to load and save the configuration,
 * and to get and set individual properties.
 * 
 * It automaticaly loads the default configuration file, and if present, overrides the default values.
 * You must manually call saveConfig() to save the configuration to the file when you are done with changes.
 */
public class Config {

    private static Config instance = null;
    private Properties config;
    private String configFilePath;
    private String defaultConfigFilePath = "cave-dweller.properties";

    //Settings and their defaults. If property is present in config file, it will override the default value.
    private static String[] dwellers;
	private static boolean sunlightBurns = true;
	private static float sunlightBurn = 1;
	private static int sunlightBurnDuration = 1;
	private static boolean villagersFearPlayers = true;
	private static int villagerFearLevel = 0;
	private static int logFrameCount = 30;
    
    private Config() {
        this.configFilePath = defaultConfigFilePath;
        this.config = new Properties();
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        // Lazy initialization (If needed, create the instance
        // print a message saying that the instance is being created
        // and then return the instance
        System.out.println("Getting instance of Config");  
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }


    public void loadConfig() throws IOException {
        File configFile = new File(configFilePath);
        if (configFile.exists() && !configFile.isDirectory()) {
            FileInputStream input = new FileInputStream(configFilePath);
            config.load(input);
            input.close();

            dwellers = config.getProperty("dwellers").split(",");
            sunlightBurns = Boolean.parseBoolean(config.getProperty("sunlightBurns"));
            sunlightBurn = Float.parseFloat(config.getProperty("sunlightBurn"));
            sunlightBurnDuration = Integer.parseInt(config.getProperty("sunlightBurnDuration"));
            villagersFearPlayers = Boolean.parseBoolean(config.getProperty("villagersFearPlayers"));
            villagerFearLevel = Integer.parseInt(config.getProperty("villagerFearLevel"));
            logFrameCount = Integer.parseInt(config.getProperty("logFrameCount"));

        } else {
            System.out.println("Config file not found, using default configuration where defined."); 
        }
    }

    public void saveConfig() {
        try {
            config.setProperty("dwellers", String.join(",", dwellers));
            config.setProperty("sunlightBurns", Boolean.toString(sunlightBurns));
            config.setProperty("sunlightBurn", Float.toString(sunlightBurn));
            config.setProperty("sunlightBurnDuration", Integer.toString(sunlightBurnDuration));
            config.setProperty("villagersFearPlayers", Boolean.toString(villagersFearPlayers));
            config.setProperty("villagerFearLevel", Integer.toString(villagerFearLevel));
            config.setProperty("logFrameCount", Integer.toString(logFrameCount));

            FileOutputStream output = new FileOutputStream(configFilePath);
            config.store(output, null);
            output.close();
        } catch (IOException e) {
            // Handle the exception
            System.err.println("Failed to save config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Getters and setters for each property

    public static String[] getDwellers() {
        return dwellers != null ? dwellers : new String[0]; //Return empty array if null
    }
    
    public static void setDwellers(String[] dwellers) {
        Config.dwellers = dwellers;
        getInstance().saveConfig();
    }
    
    public static boolean getSunlightBurns() {
        return sunlightBurns;
    }
    
    public static void setSunlightBurns(boolean sunlightBurns)  {
        Config.sunlightBurns = sunlightBurns;
        getInstance().saveConfig();
    }
    
    public static float getSunlightBurn() {
        return sunlightBurn;
    }
    
    public static void setSunlightBurn(float sunlightBurn)  {
        Config.sunlightBurn = sunlightBurn;
        getInstance().saveConfig();
    }
    
    public static int getSunlightBurnDuration() {
        return sunlightBurnDuration;
    }
    
    public static void setSunlightBurnDuration(int sunlightBurnDuration)  {
        Config.sunlightBurnDuration = sunlightBurnDuration;
        getInstance().saveConfig();
    }
    
    public static boolean getVillagersFearPlayers() {
        return villagersFearPlayers;
    }
    
    public static void setVillagersFearPlayers(boolean villagersFearPlayers)  {
        Config.villagersFearPlayers = villagersFearPlayers;
        getInstance().saveConfig();
    }
    
    public static int getVillagerFearLevel() {
        return villagerFearLevel;
    }
    
    public static void setVillagerFearLevel(int villagerFearLevel)  {
        Config.villagerFearLevel = villagerFearLevel;
        getInstance().saveConfig();
    }
    
    public static int getLogFrameCount() {
        return logFrameCount;
    }
    
    public static void setLogFrameCount(int logFrameCount)  {
        Config.logFrameCount = logFrameCount;
        getInstance().saveConfig();
    }

    
}