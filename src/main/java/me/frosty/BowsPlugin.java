package me.frosty;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import me.frosty.command.BowCommand;
import me.frosty.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

// Plugin made for 1.17 (untested).
public final class BowsPlugin extends JavaPlugin {

    // Instance of the plugin.
    @Getter private static BowsPlugin instance;

    // The cooldown hashmap holds the players uuid and the cooldown as an intetger in ticks.
    @Getter private final HashMap<UUID, Integer> cooldown = new HashMap<>();

    // An atomic boolean for enable because why not.
    @Getter private final AtomicBoolean enabled = new AtomicBoolean(true);

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        // Register the listener becuz yes.
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        // Register the bows command or some shit.
        this.getCommand("bows").setExecutor(new BowCommand(this));
        // Just fire the runnable method.
        this.createRunnable();
    }

    public void createRunnable() {  //If this lags the server, L.
        new BukkitRunnable() {  // lOl

            @Override
            public void run() {
                // Iterates through the uuids in the hashmap.
                for (UUID uuid : cooldown.keySet()) {
                    // Gets the time remaining from the uuid as a key/
                    final int remaining = cooldown.get(uuid);
                    if (remaining <= 0) {
                        // Removes if completed.
                        cooldown.remove(uuid);
                    } else {
                        // if it isnt removed then just like add it and remove/
                        cooldown.put(uuid, remaining - 1);
                    }
                }
            }
        }.runTaskTimer(this, 5, 0); // Run the task and stuff... Just making the delay 5 because I cant be fucked to actually calculate it.
    }
}
