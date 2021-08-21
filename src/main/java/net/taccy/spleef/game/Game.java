package net.taccy.spleef.game;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.event.Event;
import net.taccy.spleef.game.states.CountdownGameState;
import net.taccy.spleef.game.states.EndGameState;
import net.taccy.spleef.game.states.LobbyGameState;
import net.taccy.spleef.powerup.PowerupBlock;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.GenUtils;
import net.taccy.spleef.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.logging.Level;


public class Game {

    public Game(Spleef pl) {
        this.pl = pl;
        setGameState(new LobbyGameState(this, pl));
    }

    private Spleef pl;
    private SpleefArena arena;
    private GameState state;
    private Integer timeLeft = 480;
    private Boolean paused = false;
    private Integer eventIndex = -1;
    private ArrayList<SpleefPlayer> players = new ArrayList<>();
    private List<PowerupBlock> powerups = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private HashMap<SpleefPlayer, Integer> deathTimes = new HashMap<>();

    // todo: death manager instead of hashmap?

    public void setGameState(GameState state) {
        if (this.state != null) {
            this.state.onDisable(pl);
        }
        this.state = state;
        state.onEnable(pl);
        updateScoreboards();

        if (arena != null) {
            Bukkit.getLogger().log(Level.INFO, "[LAUNCHIFY STATE UPDATE] Game in " + arena.getName() + " changed to state " + state.getType().name());
        }
    }

    public void setArena(SpleefArena arena) {
        this.arena = arena;
    }
    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }
    public void setPaused(Boolean paused) {
        this.paused = paused;
    }
    public void setEventIndex(Integer eventIndex) {
        this.eventIndex = eventIndex;
    }

    public SpleefArena getArena() {
        return arena;
    }
    public GameState getState() {
        return state;
    }
    public Integer getTimeLeft() {
        return timeLeft;
    }
    public Boolean isPaused() {
        return paused;
    }
    public ArrayList<SpleefPlayer> getPlayers() {
        return players;
    }
    public Integer getEventIndex() {
        return eventIndex;
    }

    public void addPlayer(Player p) {
        SpleefPlayer lp = new SpleefPlayer();
        lp.setPlayer(p);

        players.add(lp);
        p.teleport(arena.getLobby());
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(Colorize.color("&fYou have joined &d" + arena.getDisplayName())));
        preparePlayer(p);
        clearEffects(p);
        pl.su.success2(p.getLocation());
        broadcastMessage("&a&l✓ &7" + p.getName() + " &fhas joined!");

        if (state.getType() == GameStateType.ACTIVE) {
            p.teleport(arena.getCenter());
            giveItems(p);
        }

        updateScoreboards();

        if (players.size() >= 2) {
            if (state.getType() == GameStateType.WAITING) {
                setGameState(new CountdownGameState(this, pl, 15, "as &6player count met.")); // TIME FOR COUNTDOWN HERE
            }
        }

    }

    public void removePlayer(Player p) {
        deathTimes.remove(getSpleefPlayerFromPlayer(p));
        players.remove(getSpleefPlayerFromPlayer(p));
        if (state.getType() != GameStateType.END) {
            broadcastMessage("&c&l✗ &7" + p.getName() + " &fhas left!");
            p.sendMessage(Colorize.color("&9⚐ &7You have &9left the game!"));
        }
        pl.sm.clearBoard(p);
        preparePlayer(p);
        clearEffects(p);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(Colorize.color("&fYou have been removed from &d" + arena.getDisplayName())));
        if (!p.getName().equalsIgnoreCase("ThatsFinn") && !p.getName().equalsIgnoreCase("dragondomenic")) {
            pl.getServer().dispatchCommand(p, "hub");
        }
        updateScoreboards();
        if (players.size() == 0) {
            setGameState(new LobbyGameState(this, pl));
            return;
        }

        if (players.size() < 2) {
            if (state.getType() == GameStateType.COUNTDOWN) {
                setGameState(new LobbyGameState(this, pl));
                return;
            }
            if (state.getType() == GameStateType.ACTIVE) {
                setGameState(new EndGameState(this, pl));
            }
        }
    }

    public void tickEvent() {
        if (!paused) {
            state.handleTick();
            if (timeLeft > 0) {
                timeLeft--;
            }
        }
        updateScoreboards();
    }

    public void pause() {
        paused = true;
        broadcastTitle("&6&lGame Paused!", "&fThe game has been &6paused &fby an admin!");
        broadcastMessage("&6⛏ &fGame has been &6paused by an admin!");
        updateScoreboards();
    }

    public void resume() {
        paused = false;
        broadcastTitle("&a&lGame Resumed!", "&fThe game has been &aresumed &fby an admin!");
        broadcastMessage("&6⛏ &fGame has been &6resumed by an admin!");
        updateScoreboards();
    }

    public void kill(SpleefPlayer target, SpleefPlayer killer, DeathReason reason) { // todo: KILL MESSAGES (from UltiBow basically)
        preparePlayer(target.getPlayer());

        target.setAlive(false);
        target.setDeaths(target.getDeaths() + 1);
        target.getPlayer().setGameMode(GameMode.SPECTATOR);
        target.setLastHitBy(null);
        deathTimes.put(target, timeLeft);

        if (killer != null) {
            upgradeKnockback(killer);
        }

        String message = "";

        switch (reason) {
            case NONE:
                message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.NONE);

                List<SpleefPlayer> alive = new ArrayList<>();
                for (SpleefPlayer bp : players) {
                    if (bp.isAlive()) {
                        alive.add(bp);
                    }
                }

                if (alive.isEmpty()) {
                    target.getPlayer().teleport(arena.getLobby());
                } else {
                    Random rand = new Random();;
                    SpleefPlayer random = alive.get(rand.nextInt(alive.size()));
                    target.getPlayer().teleport(random.getPlayer().getLocation());
                }

                break;
            case PLAYER:
                message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.PLAYER) + "&f" + killer.getPlayer().getName();
                killer.setKills(killer.getKills() + 1);
                break;
            case KNOCK_VOID:
                message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.KNOCK_VOID) + "&f" + killer.getPlayer().getName();
                killer.setKills(killer.getKills() + 1);
                break;
            case EXPLOSION:
                if (killer == null) {
                    message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.EXPLOSION);
                    break;
                } else {
                    message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.EXPLOSION_PLAYER) + "&f" + killer.getPlayer().getName();
                    killer.setKills(killer.getKills() + 1);
                    break;
                }
        }

        broadcastMessage(Colorize.color(message));
        pl.su.error(target.getPlayer().getLocation());
        target.getPlayer().sendMessage(Colorize.color("&a⚝&f You have been &aeliminated!"));
        updateScoreboards();
    }

    public void respawn(SpleefPlayer lp) {
        preparePlayer(lp.getPlayer());
        giveItems(lp.getPlayer());
        lp.setAlive(true);
        lp.getPlayer().teleport(arena.getCenter());
        updateScoreboards();
    }

    public SpleefPlayer getSpleefPlayerFromPlayer(Player p) {
        for (SpleefPlayer bp : players) {
            if (bp.getPlayer() == p) {
                return bp;
            }
        }
        return null;
    }

    public SpleefPlayer getPlayerMostKills() {
        Map<SpleefPlayer, Integer> kills = new HashMap<>();

        for (SpleefPlayer bp : players) {
            kills.put(bp, bp.getKills());
        }

        if (kills.size() == 0) {
            return null;
        }

        Integer collectiveKillCount = 0;

        for (SpleefPlayer bp : players) {
            collectiveKillCount = collectiveKillCount + bp.getKills();
        }

        if (collectiveKillCount == 0) {
            return null;
        }

        return (SpleefPlayer) GenUtils.sortByValue(kills).keySet().toArray()[kills.size() - 1];
    }

    public void preparePlayer(Player p) {
        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().clear();
        p.setFireTicks(0);
        p.setExp(0);
        p.setHealth(20);
        p.setFoodLevel(20);
    }

    public void clearEffects(Player p) {
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }

    public void giveItems(Player p) {
        Inventory inv = p.getInventory();
        inv.setItem(0, new ItemBuilder(Material.DIAMOND_SHOVEL)
                .setName(Colorize.color("&a&lInsta-Shovel"))
                .setLore(Arrays.asList("&fUse this shovel to break snow blocks!"))
                .setUnbreakable()
                .addFlags(Arrays.asList(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE))
                .addEnchant(Enchantment.DIG_SPEED, 100)
                .toItemStack());

        /*
        Integer rodSlot = 1;

        if (eventIndex != -1 && events.size() > eventIndex) {
            Event e = events.get(eventIndex);
            if (e instanceof RodOnlyEvent) {
                if (e.isActive()) {
                    rodSlot = 0;
                }
            }
        }
        */

        inv.setItem(2, new ItemBuilder(Material.BLAZE_ROD)
                .setName(Colorize.color("&6&lKnockback Rod"))
                .setLore(Arrays.asList("&fWhat else is there to say?"))
                .setInfinityDurability()
                .addFlags(Arrays.asList(ItemFlag.HIDE_ATTRIBUTES))
                .addEnchant(Enchantment.KNOCKBACK, 2)
                .toItemStack());
    }

    public void updateScoreboards() {
        for (SpleefPlayer bp : players) {
            pl.sm.createBoard(bp.getPlayer());
        }
    }

    public List<PowerupBlock> getPowerups() {
        return powerups;
    }
    public HashMap<SpleefPlayer, Integer> getDeathTimes() {
        return deathTimes;
    }

    public void upgradeKnockback(SpleefPlayer player) {
        Inventory inv = player.getPlayer().getInventory();

        ItemStack rod = inv.getItem(1);
        if (rod != null) {
            if (rod.getEnchantments().containsKey(Enchantment.KNOCKBACK)) {
                int level = rod.getEnchantmentLevel(Enchantment.KNOCKBACK);

                if (level < 7) {
                    ItemMeta meta = rod.getItemMeta();
                    meta.addEnchant(Enchantment.KNOCKBACK, level + 1, true);
                    rod.setItemMeta(meta);
                }
            }
        }
    }

    public void resetData() {
        timeLeft = 480;
        paused = false;
        eventIndex = -1;
        players.clear();
        deathTimes.clear();
        powerups.clear();
        events.clear();
    }

    public List<Event> getEvents() {
        return events;
    }

    public Spleef getPlugin() {
        return pl;
    }

    public void broadcastMessage(String message) {
        for (SpleefPlayer bp : players) {
            bp.getPlayer().sendMessage(Colorize.color(message));
        }
    }
    public void broadcastTitle(String title, String subtitle) {
        for (SpleefPlayer bp : players) {
            bp.getPlayer().sendTitle(Colorize.color(title), Colorize.color(subtitle), 5, 60, 10);
            pl.su.selected(bp.getPlayer().getLocation());
        }
    }
    public void broadcastActionbar(String message) {
        for (SpleefPlayer bp : players) {
            bp.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Colorize.color(message)));
        }
    }

}
