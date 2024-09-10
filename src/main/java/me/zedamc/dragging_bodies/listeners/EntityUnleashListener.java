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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;

public class EntityUnleashListener implements Listener {
    private main Main;

    public EntityUnleashListener(main main) {
        Main = main;
    }


    @EventHandler
    public void onUnleash(EntityUnleashEvent e) {
        if (Main.EntityUUID.containsKey(e.getEntity().getUniqueId())) {
            Location location = e.getEntity().getLocation();
            Location i;
            World world = location.getWorld();
            double y = location.getY();
            Pig entity = (Pig) e.getEntity();
            Player player = (Player) e.getEntity().getPassengers().getFirst();
            if (world.getBlockAt(location).getType().equals(Material.AIR)) {
                for (i = location; (world.getBlockAt(i).getType().equals(Material.AIR)); ) {
                    y = y - 0.2;
                    i.setY(y);
                }
                i.setY(y - 0.6);
                if (!(y - 0.6 == entity.getLocation().getY())) {
                    Main.EntityUUID.put(entity.getUniqueId(), "dismounting");
                    entity.removePassenger(player);
                    entity.teleport(i);
                    Main.EntityUUID.put(entity.getUniqueId(), "dragging");
                    entity.addPassenger(player);

                }
            }
        }
    }
}
