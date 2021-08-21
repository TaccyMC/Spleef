package net.taccy.spleef.managers;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigurationManager {

    private Spleef pl;
    private final FileConfiguration arenasConfiguration;
    private final File arenasFile;

    public ConfigurationManager(Spleef pl) {
        this.pl = pl;

        this.arenasFile = new File(pl.getDataFolder(), "arenas.yml");
        this.arenasConfiguration = new YamlConfiguration();

        // loading config could throw errors so try catch
        try {
            this.arenasConfiguration.load(this.arenasFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadArenas() {
        for (String arenaName : arenasConfiguration.getKeys(false)) {
            ConfigurationSection section = arenasConfiguration.getConfigurationSection(arenaName);
            SpleefArena arena = new SpleefArena();
            arena.setName(arenaName);
            arena.setDisplayName(section.getString("display-name"));
            arena.setDisabled(section.getBoolean("disabled"));
            arena.setLobby(locationFrom(section.getConfigurationSection("lobby")));
            arena.setCenter(locationFrom(section.getConfigurationSection("center")));
            /*
            ConfigurationSection spawnsSection = section.getConfigurationSection("spawns");
            for (String spawn : spawnsSection.getKeys(false)) {
                Location spawnLoc = locationFrom(spawnsSection.getConfigurationSection(spawn));
                arena.getSpawns().add(spawnLoc);
            }
             */
            Spleef.arenas.add(arena);
        }
    }

    public void saveArena(SpleefArena arena) throws IOException {
        if (arenasConfiguration.isConfigurationSection(arena.getName())) {
            arenasConfiguration.set(arena.getName(), null);
        }

        ConfigurationSection arenaSection = arenasConfiguration.createSection(arena.getName());
        arenaSection.set("display-name", arena.getDisplayName());
        arenaSection.set("disabled", arena.isDisabled());
        writeLocation(arena.getLobby(), arenaSection.createSection("lobby"));
        writeLocation(arena.getCenter(), arenaSection.createSection("center"));
        /*
        ConfigurationSection spawns = arenaSection.createSection("spawns");
        int i = 1;
        for (Location loc : arena.getSpawns()) {
            ConfigurationSection spawnSection = spawns.createSection(String.valueOf(i));
            writeLocation(loc, spawnSection);
            i++;
        }
         */
        saveConfig();
    }

    public void deleteArena(SpleefArena arena) {
        if (arenasConfiguration.isConfigurationSection(arena.getName())) {
            arenasConfiguration.set(arena.getName(), null);
        }
        saveConfig();
    }

    public void saveAllArenas() {
        for (SpleefArena a : Spleef.arenas) {
            try {
                saveArena(a);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bukkit.getLogger().log(Level.INFO, a.getName());
        }
    }

    public void writeLocation(Location loc, ConfigurationSection section) {
        section.set("world", loc.getWorld().getName());
        section.set("x", loc.getX());
        section.set("y", loc.getY());
        section.set("z", loc.getZ());
        section.set("yaw", loc.getYaw());
        section.set("pitch", loc.getPitch());
    }

    public Location locationFrom(ConfigurationSection section) {
        return new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"), section.getDouble("y"), section.getDouble("z"),
                (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }

    public void saveConfig() {
        try {
            arenasConfiguration.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
