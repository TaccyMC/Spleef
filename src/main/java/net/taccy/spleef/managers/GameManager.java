package net.taccy.spleef.managers;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.GameStateType;
import net.taccy.spleef.game.SpleefPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private Spleef pl;

    public GameManager(Spleef pl) {
        this.pl = pl;
    }

    // generates games for all arenas that don't currently have games
    public void makeGames() {
        for (SpleefArena arena : Spleef.arenas) {
            if (arena.getGame() == null) {
                Game game = new Game(pl);
                arena.setGame(game);
                game.setArena(arena);
            }
        }
    }

    public Game getGameFromPlayer(Player p) {
        for (SpleefArena arena : Spleef.arenas) {
            for (SpleefPlayer bp : arena.getGame().getPlayers()) {
                if (bp.getPlayer() == p) {
                    return arena.getGame();
                }
            }
        }
        return null;
    }

    public SpleefPlayer getLaunchPlayerFromPlayer(Player p) {
        for (SpleefArena arena : Spleef.arenas) {
            for (SpleefPlayer bp : arena.getGame().getPlayers()) {
                if (bp.getPlayer() == p) {
                    return bp;
                }
            }
        }
        return null;
    }

    public Game getBestGame() {
        Map<Game, Integer> availableGames = new HashMap<>();

        for (SpleefArena a : Spleef.arenas) {
            if (a.getGame().getState().getType() == GameStateType.WAITING || a.getGame().getState().getType() == GameStateType.COUNTDOWN) {
                if (!a.isDisabled()) {
                    availableGames.put(a.getGame(), a.getGame().getPlayers().size());
                }
            }
        }

        if (availableGames.size() == 0) {
            return null;
        }

        if (allGamesEmpty()) {
            Random rand = new Random();
            int random = rand.nextInt(availableGames.keySet().size());
            return (Game) availableGames.keySet().toArray()[random];
        }

        return (Game) sortByValue(availableGames).keySet().toArray()[availableGames.size() - 1];
    }

    private boolean allGamesEmpty() {
        for (SpleefArena a : Spleef.arenas) {
            if (a.getGame().getPlayers().size() > 0) {
                return false;
            }
        }
        return true;
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
