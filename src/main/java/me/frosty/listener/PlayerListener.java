package me.frosty.listener;

import me.frosty.BowsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class PlayerListener implements Listener {

    //Instance of the bows plugin.
    private final BowsPlugin plugin;

    public PlayerListener(BowsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBow(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();    // Sets the player as the one that fired the event.
        final UUID uuid = player.getUniqueId();     // This just grabs the uuid from the player.
        final Action action = event.getAction();    // Gets the action from the event.

        // Checks if the player is event holding a crosshair first.
        if (player.getInventory().getItemInMainHand().getType().equals(Material.CROSSBOW)) {        //This code is actual dogshit leave me alone its 3 am.
            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) && this.plugin.getEnabled().get()) {   // Checks if the crossbow should actually interact & start the client.
                if (!this.plugin.getCooldown().containsKey(uuid)) { // Checks if the cooldown map doesnt contain the uuid before acutally running the bow code.
                    this.plugin.getCooldown().put(uuid, this.plugin.getConfig().getInt("shot-cooldown")); // This is ticks!
                    this.runMetaUpdate(player); // Fires the metadata method with the player.
                    player.sendMessage(ChatColor.GREEN + "You shot the crossbow you now have jumpboost for one second!");
                } else {
                    event.setCancelled(true);   // Cancel the event if they are on timer.                                     // Divide the ticks by 20 to get the actual count in seconds.
                    player.sendMessage(ChatColor.RED + "You have " + ChatColor.AQUA + plugin.getCooldown().get(uuid) / 20 + " seconds " + ChatColor.RED + "until you can use this again!");
                }
            }
        }
    }

    /**
     * Runs the meta change and effects on the bow and player.
     *
     * @param player Takes in a Bukkit player object to run the changes too.
     */
    public void runMetaUpdate(Player player) {
        // Sets the item to the players held item.
        final ItemStack item = player.getInventory().getItemInMainHand();
        // Gets the crossbows meta from the itemstack.
        final CrossbowMeta meta = (CrossbowMeta) item.getItemMeta();

        if (meta != null) {
            // Add the projectile to the bow
            meta.addChargedProjectile(new ItemStack(Material.TIPPED_ARROW, 1));
            item.setItemMeta(meta);
        }
        // Add the potion effect.
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, this.plugin.getConfig().getInt("jumpboost-duration"), 3, true));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        // So london is reminded I am his slave every time he logs into his server.
        player.sendMessage(ChatColor.LIGHT_PURPLE + "This server is running Bows Plugin by frosty#9999.");
    }

    @EventHandler
    public void changeArrowDamage(EntityDamageByEntityEvent event) {
        // Check if the damage cause is a PROJECTILE and the modify damage is on
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && this.plugin.getConfig().getBoolean("modify-damage")) {
            // Checks if the damager is an arrow.
            if (event.getDamager() instanceof Arrow) {
                // Set the damage to the damage integer.
                event.setDamage(this.plugin.getConfig().getInt("arrow-damage"));
            }
        }
    }
}
