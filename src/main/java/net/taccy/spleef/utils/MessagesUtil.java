package net.taccy.spleef.utils;

import net.taccy.spleef.game.DeathReason;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessagesUtil {

    // player kills
    public ArrayList<String> plm = new ArrayList<>();
    // void without credit
    public ArrayList<String> vom = new ArrayList<>();
    // void kills with credit
    public ArrayList<String> vkm = new ArrayList<>();
    // tnt without credit
    public ArrayList<String> tnt = new ArrayList<>();
    // tnt with credit
    public ArrayList<String> tnp = new ArrayList<>();
    // lightning strike with credit
    public ArrayList<String> lgt = new ArrayList<>();

    public MessagesUtil() {
        // PLAYER MESSAGES
        plm.add(" was killed by ");
        plm.add(" has been killed by ");
        plm.add(" was owned by ");
        plm.add(" met their end to ");
        plm.add(" was doomed by ");
        plm.add(" got trickshotted on by ");
        plm.add(" lost a fight to ");
        plm.add(" was sniped by ");
        plm.add(" was powerless against ");
        plm.add(" took the L to ");
        plm.add(" got nani'd by ");
        plm.add(" got bad'd by ");
        plm.add(" got thunderstruck by ");
        plm.add(" got hit or misssed by ");
        plm.add(" got outbid by ");
        plm.add(" lost a bet to ");
        plm.add(" was sent into the shadow realm by ");
        plm.add(" got mlg 360 triggle inclined jitter bridge no scoped by ");
        plm.add(" got deported by ");
        plm.add(" got dora the explorer'd by ");
        plm.add(" got maccas double whopper premium deluxe edition'd 50% off t's, c's and fees apply made by red rooster sponsored by kfc spoken by Victoria Banks'd by ");

        // VOID MESSAGES
        vom.add(" slipped off the edge");
        vom.add(" fell into the void");
        vom.add(" forgot to press shift");
        vom.add(" under-estimated the launcher");
        vom.add(" was welcomed by the void");
        vom.add(" didn't have their inner health plus today!");
        vom.add(" thought they could fly..");
        vom.add(" missed a parkour jump");
        vom.add(" was sent into the shadow realm");
        vom.add(" got pushed onto the spherical side of the northern pole located at the left side of a pink elephant weighing in at about 20 papers");

        // VOID KNOCK MESSAGES
        vkm.add(" was hit into the void by ");
        vkm.add(" got knocked into the void by ");
        vkm.add(" was pushed a little too far by ");
        vkm.add(" got launched into the void by ");

        // TNT NO CREDIT MESSAGE
        tnt.add(" was rained on from above");
        tnt.add(" didn't escape in time");
        tnt.add(" was exploded by the console");
        tnt.add(" cheaped out on their power supply");
        tnt.add(" was exploded");
        tnt.add(" got blown into pieces");
        tnt.add(" forgot to run...");

        // TNT MESSAGES
        tnp.add(" got exploded by ");
        tnp.add(" was detonated by ");
        tnp.add(" failed to diffuse the bomb planted by ");
        tnp.add(" lost to terrorist ");
        tnp.add(" cut the wrong wire of a bomb made by ");
        tnp.add(" didn't escape in time from ");

        // LIGHTNING STRIKE CREDIT
        lgt.add(" was cooked by ");
        lgt.add(" got struck down by ");
        lgt.add(" was smite-ed by ");
        lgt.add(" was fried by ");
    }

    public String getRandomMessage(DeathReason reason) {
        Integer index;
        Random rand = new Random();
        List<String> messages = null;
        if (reason == DeathReason.PLAYER) {
            messages = plm;
        } else if (reason == DeathReason.NONE) {
            messages = vom;
        } else if (reason == DeathReason.KNOCK_VOID) {
            messages = vkm;
        } else if (reason == DeathReason.EXPLOSION) {
            messages = tnt;
        } else if (reason == DeathReason.EXPLOSION_PLAYER) {
            messages = tnp;
        }

        if (messages == null) {
            return null;
        } else {
            index = rand.nextInt(messages.size());
            return messages.get(index);
        }
    }

}
