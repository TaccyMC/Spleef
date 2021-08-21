package net.taccy.spleef.utils;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CustomRunnable {

    private BukkitRunnable bRunnable;

    /**
     * This is your equivalent of {@code Runnable.run()}. We name it differently to prevent confusion!
     */
    public abstract void handle();

    public CustomRunnable schedule(JavaPlugin plugin, long delay, long interval) {
        if (isRunning()) {
            // the runnable was still running, you can either auto-cancel it or throw an exception
            cancel();
        }
        bRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                handle();
            }
        };
        bRunnable.runTaskTimer(plugin, delay, interval);
        return this;
    }

    public void cancel() {
        if (!isRunning()) {
            // the runnable wasn't running, either ignore or throw an exception
            return;
        }
        bRunnable.cancel();
    }

    public boolean isRunning() {
        return bRunnable != null && !bRunnable.isCancelled();
    }

}
