package com.simondmc.borderhoarder.game;

import com.simondmc.borderhoarder.BorderHoarder;
import com.simondmc.borderhoarder.inventory.InventoryBuilder;
import com.simondmc.borderhoarder.world.BorderWorldCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    @EventHandler
    public void cancelViewClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Collected Items§a")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;
            // current page
            int page = Integer.parseInt(e.getView().getTitle().split("/")[0].split("-")[1].substring(6));
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aNext Page")) {
                e.getWhoClicked().openInventory(InventoryBuilder.buildCompletedInventory(page + 1));
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aPrevious Page")) {
                e.getWhoClicked().openInventory(InventoryBuilder.buildCompletedInventory(page - 1));
            }
        }
        if (e.getView().getTitle().contains("Missing Items§a")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;
            // current page
            int page = Integer.parseInt(e.getView().getTitle().split("/")[0].split("-")[1].substring(6));
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aNext Page")) {
                e.getWhoClicked().openInventory(InventoryBuilder.buildMissingInventory(page + 1));
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aPrevious Page")) {
                e.getWhoClicked().openInventory(InventoryBuilder.buildMissingInventory(page - 1));
            }
        }
    }

    @EventHandler
    public void pickupItem(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getEntity().getLocation().getWorld().getName().equals(BorderWorldCreator.worldName) ||
                e.getEntity().getLocation().getWorld().getName().equals("world_the_nether")
                || e.getEntity().getLocation().getWorld().getName().equals("world_the_end"))) return;
        ItemHandler.gainItem(e.getItem().getItemStack().getType(), (Player) e.getEntity());
    }

    @EventHandler
    public void craft(CraftItemEvent e) {
        if (!(e.getWhoClicked().getLocation().getWorld().getName().equals(BorderWorldCreator.worldName) ||
                e.getWhoClicked().getLocation().getWorld().getName().equals("world_the_nether")
                || e.getWhoClicked().getLocation().getWorld().getName().equals("world_the_end"))) return;
        ItemHandler.gainItem(e.getCurrentItem().getType(), (Player) e.getWhoClicked());
    }

    @EventHandler
    public void inventoryGet(InventoryClickEvent e) {
        if (!(e.getWhoClicked().getLocation().getWorld().getName().equals(BorderWorldCreator.worldName) ||
                e.getWhoClicked().getLocation().getWorld().getName().equals("world_nether")
                || e.getWhoClicked().getLocation().getWorld().getName().equals("world_the_end"))) return;
        if (e.getClickedInventory() == null) return;

        // shift click item from somewhere else
        if (e.getClick().equals(ClickType.SHIFT_LEFT) ||
                e.getClick().equals(ClickType.SHIFT_RIGHT)) {
            if (e.getCurrentItem() == null) return;
            Material clickedItem = e.getCurrentItem().getType();
            new BukkitRunnable() {

                @Override
                public void run() {
                    // wait a tick to see if the player still has the item
                    for (ItemStack item : e.getWhoClicked().getInventory().getContents()) {
                        if (item == null) continue;
                        if (item.getType() == clickedItem) {
                            // player still has the item
                            ItemHandler.gainItem(item.getType(), (Player) e.getWhoClicked());
                            return;
                        }
                    }
                }
            }.runTaskLater(BorderHoarder.plugin, 1);
            return;
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                // drag over to inventory
                if (e.getClickedInventory().getType().equals(InventoryType.PLAYER) && e.getCurrentItem() != null) {
                    ItemHandler.gainItem(e.getCurrentItem().getType(), (Player) e.getWhoClicked());
                }
            }
        }.runTaskLater(BorderHoarder.plugin, 1);
    }

    // clear title if kicked during generation
    @EventHandler
    public void joinBorderWorld(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals(BorderWorldCreator.worldName)) {
            e.getPlayer().resetTitle();
        }
    }
}
