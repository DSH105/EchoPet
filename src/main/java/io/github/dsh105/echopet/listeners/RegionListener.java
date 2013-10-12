package io.github.dsh105.echopet.listeners;

import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.pet.Pet;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.WorldUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Project by DSH105
 */

public class RegionListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Pet pet = PetHandler.getInstance().getPet(p);
        if (pet != null) {
            if (!WorldUtil.allowRegion(event.getTo())) {
                PetHandler.getInstance().removePet(pet);
                Lang.sendTo(p, Lang.ENTER_PET_DISABLED_REGION.toString());
            }
        }
    }
}