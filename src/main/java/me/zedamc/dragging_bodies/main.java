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

package me.zedamc.dragging_bodies;

import me.zedamc.dragging_bodies.listeners.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class main extends JavaPlugin implements Listener {
    public Set<String> unconscious = new HashSet<>();
    public HashMap<UUID, String> EntityUUID = new HashMap<>();

    @Override
    public void onEnable() {
        int pluginId = 23326;

        Bukkit.getLogger().info("[DraggingBodies] Enabled");
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDismountListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityMountListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerBlockDestroyListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityUnleashListener(this), this);

        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (unconscious.contains(player.getName())) {
                Pig pig = (Pig) player.getVehicle();
                UUID uuid = pig.getUniqueId();
                EntityUUID.remove(uuid);
                pig.remove();
                player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
        }
        if (!(EntityUUID.isEmpty())) {
            for (UUID uuid : EntityUUID.keySet()) {
                Pig pig = (Pig) Bukkit.getEntity(uuid);
                pig.remove();
            }
        }
        Bukkit.getLogger().info("[DraggingBodies] Disabled");
    }
}