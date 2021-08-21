package net.taccy.spleef;

import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.commands.SpleefCommand;
import net.taccy.spleef.guisystem.GUIListener;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.powerup.PowerupBlock;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.MessagesUtil;
import net.taccy.spleef.utils.SoundUtil;
import net.taccy.spleef.listeners.*;
import net.taccy.spleef.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.HashMap;

public class Spleef extends JavaPlugin {

    public static ArrayList<SpleefArena> arenas = new ArrayList<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public GameManager gm = new GameManager(this);
    public ArenaManager am = new ArenaManager(this);
    public ConfigurationManager cm = new ConfigurationManager(this);
    public ArenaSetupWizardManager aswm = new ArenaSetupWizardManager(this);
    public ScoreboardManager sm = new ScoreboardManager(this);
    public TimingsManager tm = new TimingsManager(this);
    public MessagesUtil mu = new MessagesUtil();
    public PowerupManager pm = new PowerupManager(this);
    public EventManager em = new EventManager();
    public LauncherManager lm = new LauncherManager(this);

    public SoundUtil su = new SoundUtil(this);

    @Override
    public void onEnable() {
        cm.loadArenas();
        // cm.saveConfig();
        gm.makeGames();

        // save default config
        saveDefaultConfig();

        // registrations
        registerCommands();
        registerListeners();
        registerRunnables();

    }

    @Override
    public void onDisable() {
        cm.saveAllArenas();

        // clear scoreboard's on disable
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                if (Colorize.stripColor(p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName()).contains("spleef")) {
                    sm.clearBoard(p);
                }
            }
        }

        for (SpleefArena a : arenas) {
            for (PowerupBlock pb : a.getGame().getPowerups()) {
                pb.despawn();
            }
        }

    }

    private void registerCommands() {
        getCommand("spleef").setExecutor(new SpleefCommand(this));
        getCommand("spleef").setTabCompleter(new SpleefCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GUIListener(), this);

        getServer().getPluginManager().registerEvents(new AntiGriefListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new RideablePearlListener(this), this);
        getServer().getPluginManager().registerEvents(new TNTListener(this), this);

        getServer().getPluginManager().registerEvents(aswm, this);
        getServer().getPluginManager().registerEvents(lm, this);
    }

    private void registerRunnables() {
        tm.runTaskTimer(this, 0, 20);
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        if (playerMenuUtilityMap.containsKey(p)) { return playerMenuUtilityMap.get(p); }

        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(p);
        playerMenuUtilityMap.put(p, playerMenuUtility);
        return playerMenuUtility;
    }

}
