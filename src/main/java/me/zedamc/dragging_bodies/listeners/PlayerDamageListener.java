/*
 * This file is part of Dragging Bodies.
 * Copyright (C) 2024 Zeda

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.zedamc.dragging_bodies.listeners;

import me.zedamc.dragging_bodies.main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDamageListener implements Listener {
    private main Main;

    public PlayerDamageListener(main main) {
        Main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType().equals(EntityType.PLAYER) && (e.getDamager().getType().equals(EntityType.PLAYER))) {
            Player player = (Player) e.getEntity();
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if (!(offHand.getType().equals(Material.TOTEM_OF_UNDYING))) {
                if (!(Main.unconscious.contains(player.getName()))) {
                    if (player.getHealth() - e.getFinalDamage() <= 0) {
                        Bukkit.getLogger().info("player " + player.getName() + " is unconscious");
                        Location location = player.getLocation();
                        World world = location.getWorld();
                        e.setCancelled(true);
                        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()/2);
                        Main.unconscious.add(player.getName());
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 6000, 1));
                        Location i;
                        double y = location.getY();
                        for (i = location; (world.getBlockAt(i).equals(BlockType.AIR)); ) {
                            y--;
                            i.setY(y);
                        }

                        Location armorStandLocation = i;
                        i.setY(armorStandLocation.getBlockY() - 0.8);
                        Pig entity = world.spawn(i, Pig.class);
                        Main.EntityUUID.put(entity.getUniqueId(), "dragging");
                        ((CraftEntity) entity).getHandle().noPhysics = true;
                        entity.setAware(false);
                        entity.setInvisible(true);
                        entity.setGravity(false);
                        entity.setInvulnerable(true);
                        entity.setSilent(true);
                        entity.addPassenger(player);
                        entity.setLeashHolder(null);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (entity.isDead() || (!(Main.unconscious.contains(player.getName())))) {
                                    this.cancel();
                                    return;
                                }
                                if (entity.isLeashed()) {
                                    Location pigLocation = entity.getLocation();
                                    Double desiredY = entity.getLeashHolder().getLocation().getY() - 0.8;
                                    Location newLocation = entity.getLeashHolder().getLocation().clone();
                                    newLocation.setY(desiredY);
                                    if (!(pigLocation.getY() == desiredY)) {
                                        Main.EntityUUID.put(entity.getUniqueId(), "dismounting");
                                        entity.removePassenger(player);
                                        entity.teleport(newLocation);
                                        Main.EntityUUID.put(entity.getUniqueId(), "dragging");
                                        entity.addPassenger(player);
                                    }
                                }
                            }
                        }.runTaskTimer(Main, 0, 15);
                        Main.getServer().getScheduler().scheduleSyncDelayedTask(Main, new Runnable() {
                            @Override
                            public void run() {
                                Main.unconscious.remove(player.getName());
                                entity.remove();
                                Bukkit.getLogger().info("player " + player.getName() + " is not unconscious");
                            }
                        }, 6000);

                    }
                } else {
                    if (player.getHealth() - e.getFinalDamage() <= 0) {
                        Pig pig = (Pig) player.getVehicle();
                        if (!(pig.isDead())) {
                            pig.remove();
                        }
                        Bukkit.getLogger().info("player " + player.getName() + " is not unconscious");
                    }
                }
            }
        }
    }

    @EventHandler
    public void preventPig(EntityDamageEvent e) {
        if (Main.EntityUUID.containsKey(e.getEntity().getUniqueId())) {
            if (Main.EntityUUID.get(e.getEntity().getUniqueId()).equals("dragging")) {
                e.setCancelled(true);
            }
        }
    }
}