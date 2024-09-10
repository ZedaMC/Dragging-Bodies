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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;

public class EntityDismountListener implements Listener {
    private main Main;

    public EntityDismountListener(main main) {
        Main = main;
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) e.getEntity();
            if (Main.unconscious.contains(player.getName())) {
                if (Main.EntityUUID.containsKey(e.getDismounted().getUniqueId())) {
                    if (Main.EntityUUID.get(e.getDismounted().getUniqueId()).equals("dragging")) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}