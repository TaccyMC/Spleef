package net.taccy.spleef.managers;

import net.taccy.spleef.event.Event;
import net.taccy.spleef.event.events.*;
import net.taccy.spleef.game.Game;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventManager {

    public List<Event> events = new ArrayList<>();
    public Random rand = new Random();

    public EventManager() {
        // register events in list
        events.add(new GlowingEvent(null));
        events.add(new PowerupFrenzy(null));
    }

    public List<Event> getRandomEventSequence(Game game, Integer size) {
        List<Event> sequence = new ArrayList<>();

        // copy events registered
        List<Event> available = new ArrayList<>(events);

        if (size > available.size()) {
            return null;
        }

        for (int i = 0; i < size; i++) {
            Event chosen = available.get(rand.nextInt(available.size()));
            Event chosenNewInstance = getNewEventInstance(game, chosen);

            available.remove(chosen);
            sequence.add(chosenNewInstance);
        }

        return sequence;

    }

    public Event getNewEventInstance(Game game, Event event) {
        try {
            return event.getClass().getDeclaredConstructor(Game.class).newInstance(game);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
