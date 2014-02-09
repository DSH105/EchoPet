package io.github.dsh105.echopet.menu.selector;

import com.dsh105.dshutils.inventory.InventoryMenu;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetMenuOpenEvent;
import io.github.dsh105.echopet.config.ConfigOptions;
import org.bukkit.entity.Player;

import java.util.Map;

public class SelectorMenu extends InventoryMenu {

    public SelectorMenu() {
        super(EchoPetPlugin.getInstance(), ConfigOptions.instance.getConfig().getString("petSelector.menu.title", "Pets"), ConfigOptions.instance.getConfig().getInt("petSelector.menu.slots", 45));
        for (Map.Entry<Integer, SelectorIcon> entry : SelectorLayout.getLoadedLayout().entrySet()) {
            //ConsoleLogger.log(entry.getKey() + "," + entry.getValue().getPetType().toString());
            this.setSlot(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public InventoryMenu showTo(Player viewer) {
        PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(viewer, PetMenuOpenEvent.MenuType.SELECTOR);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(menuEvent);
        if (menuEvent.isCancelled()) {
            return this;
        }
        return super.showTo(viewer);
    }
}