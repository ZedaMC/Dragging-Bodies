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
import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    private main Main;

    public PlayerInteractListener(main main) {
        Main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            Player player = (Player) e.getRightClicked();
            if (Main.unconscious.contains(player.getName())) {
                Pig vehicle = (Pig) player.getVehicle();
                if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.LEAD) && (!(vehicle.isLeashed()))) {
                    vehicle.setLeashHolder(e.getPlayer());
                    ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
                    if (hand.getAmount() > 1) {
                        hand.setAmount(hand.getAmount()-1);
                    } else {
                        hand.setAmount(0);
                    }
                } else if (vehicle.isLeashed()) {
                    vehicle.setLeashHolder(null);
                    ItemStack lead = new ItemStack(Material.LEAD);
                    lead.setAmount(1);
                    e.getPlayer().getInventory().addItem(lead);
                }
            }
        }
    }
}
