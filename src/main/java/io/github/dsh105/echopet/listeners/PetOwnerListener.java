package io.github.dsh105.echopet.listeners;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.api.event.PetInteractEvent;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.type.human.EntityHumanPet;
import io.github.dsh105.echopet.entity.living.type.human.HumanPet;
import io.github.dsh105.echopet.menu.selector.PetSelector;
import io.github.dsh105.echopet.menu.selector.SelectorItem;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.StringUtil;
import io.github.dsh105.echopet.util.WorldUtil;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class PetOwnerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.isSimilar(SelectorItem.SELECTOR.getItem())) {
            PetSelector petSelector = new PetSelector(45, p);
            petSelector.open(false);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity e = event.getRightClicked();
        if (e instanceof CraftLivingPet) {
            LivingPet pet = ((CraftLivingPet) e).getPet();
            event.setCancelled(true);
            PetInteractEvent iEvent = new PetInteractEvent(pet, p, PetInteractEvent.Action.RIGHT_CLICK, false);
            EchoPet.getInstance().getServer().getPluginManager().callEvent(iEvent);
            if (!iEvent.isCancelled()) {
                pet.getEntityPet().a(((CraftPlayer) p).getHandle());
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                LivingPet pet = PetHandler.getInstance().getPet(p);
                if (pet != null && pet.isOwnerRiding()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final Player p = event.getPlayer();
        final LivingPet pi = PetHandler.getInstance().getPet(p);
        if (pi != null) {
            if (pi.ownerIsMounting) return;
            if (event.getFrom().getWorld() == event.getTo().getWorld()) {
                PetHandler.getInstance().saveFileData("autosave", pi);
                SQLPetHandler.getInstance().saveToDatabase(pi, false);
                PetHandler.getInstance().removePet(pi, false);
                if (!WorldUtil.allowPets(event.getTo())) {
                    Lang.sendTo(p, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
                    return;
                }
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        PetHandler.getInstance().loadPets(p, false, false, false);
                    }

                }.runTaskLater(EchoPet.getInstance(), 20L);
            } else {
                PetHandler.getInstance().saveFileData("autosave", pi);
                SQLPetHandler.getInstance().saveToDatabase(pi, false);
                PetHandler.getInstance().removePet(pi, false);
                if (!WorldUtil.allowPets(event.getTo())) {
                    Lang.sendTo(p, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
                    return;
                }
                Lang.sendTo(p, Lang.DIMENSION_CHANGE.toString());
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        PetHandler.getInstance().loadPets(p, false, false, false);
                    }

                }.runTaskLater(EchoPet.getInstance(), 20L);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        LivingPet pi = PetHandler.getInstance().getPet(p);
        if (pi != null) {
            //ec.PH.saveFileData("autosave", pi);
            PetHandler.getInstance().saveFileData("autosave", pi);
            SQLPetHandler.getInstance().saveToDatabase(pi, false);
            PetHandler.getInstance().removePet(pi, true);
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        EchoPet ec = EchoPet.getInstance();
        final Player p = event.getPlayer();
        Inventory inv = p.getInventory();
        if (ec.update && p.hasPermission("echopet.update")) {
            p.sendMessage(ec.prefix + ChatColor.GOLD + "An update is available: " + ec.name + " (" + ec.size + " bytes).");
            p.sendMessage(ec.prefix + ChatColor.GOLD + "Type /ecupdate to update.");
        }

        for (int j = 0; j <= 35; j++) {
            ItemStack item = inv.getItem(j);
            if (item != null && item.isSimilar(SelectorItem.SELECTOR.getItem())) {
                inv.clear(j);
            }
        }

        if ((Boolean) ec.options.getConfigOption("petSelector.clearInvOnJoin", false)) {
            inv.clear();
        }
        if ((Boolean) ec.options.getConfigOption("petSelector.giveOnJoin.enable", true)
                && (((Boolean) ec.options.getConfigOption("petSelector.giveOnJoin.usePerm", false) && p.hasPermission((String) ec.options.getConfigOption("petSelector.giveOnJoin.perm", "echopet.selector.join")))
                || !((Boolean) ec.options.getConfigOption("petSelector.giveOnJoin.usePerm", false)))) {
            int slot = ((Integer) ec.options.getConfigOption("petSelector.giveOnJoin.slot", 9)) - 1;
            ItemStack i = inv.getItem(slot);
            ItemStack selector = SelectorItem.SELECTOR.getItem();
            if (i != null) {
                inv.clear(slot);
                inv.setItem(slot, selector);
                inv.addItem(i);
            } else {
                inv.setItem(slot, selector);
            }
        }

        Iterator<LivingPet> i = PetHandler.getInstance().getPets().iterator();
        while (i.hasNext()) {
            LivingPet pet = i.next();
            if (pet instanceof HumanPet) {
                ((EntityHumanPet) pet.getEntityPet()).updatePacket();
            }
        }

        final boolean sendMessage = ((Boolean) ec.options.getConfigOption("sendLoadMessage", true));

        new BukkitRunnable() {

            public void run() {
                PetHandler.getInstance().loadPets(p, true, sendMessage, false);
            }

        }.runTaskLater(ec, 20);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        LivingPet pet = PetHandler.getInstance().getPet(p);
        if (pet != null) {
            PetHandler.getInstance().saveFileData("autosave", pet);
            SQLPetHandler.getInstance().saveToDatabase(pet, false);
            PetHandler.getInstance().removePet(pet, true);
            //p.sendMessage(Lang.REMOVE_PET_DEATH.toString());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        PetHandler.getInstance().loadPets(p, true, false, true);
    }
}
