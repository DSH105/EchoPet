package io.github.dsh105.echopet.util;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.menu.main.MenuItem;
import io.github.dsh105.echopet.menu.main.MenuOption;

import java.util.ArrayList;


public class MenuUtil {

    public static ArrayList<MenuOption> createOptionList(PetType pt) {
        ArrayList<MenuOption> options = new ArrayList<MenuOption>();
        int i = 0;
        options.add(new MenuOption(i++, MenuItem.HAT));
        options.add(new MenuOption(i++, MenuItem.RIDE));
        if (pt == PetType.BLAZE) {
            options.add(new MenuOption(i++, MenuItem.FIRE));
        }
        if (pt == PetType.CREEPER) {
            options.add(new MenuOption(i++, MenuItem.POWER));
        }
        if (pt == PetType.ENDERMAN) {
            options.add(new MenuOption(i++, MenuItem.SCREAMING));
        }
        if (pt == PetType.MAGMACUBE) {
            options.add(new MenuOption(i++, MenuItem.SIZE));
        }
        if (pt == PetType.PIGZOMBIE) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.VILLAGER));
        }
        if (pt == PetType.SKELETON) {
            options.add(new MenuOption(i++, MenuItem.WITHER));
        }
        if (pt == PetType.SLIME) {
            options.add(new MenuOption(i++, MenuItem.SIZE));
        }
        if (pt == PetType.WITCH) {
            options.add(new MenuOption(i++, MenuItem.POTION));
        }
        if (pt == PetType.WITHER) {
            options.add(new MenuOption(i++, MenuItem.SHIELD));
        }
        if (pt == PetType.ZOMBIE) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.VILLAGER));
        }
        if (pt == PetType.CHICKEN) {
            options.add(new MenuOption(i++, MenuItem.BABY));
        }
        if (pt == PetType.COW) {
            options.add(new MenuOption(i++, MenuItem.BABY));
        }
        if (pt == PetType.MUSHROOMCOW) {
            options.add(new MenuOption(i++, MenuItem.BABY));
        }
        if (pt == PetType.OCELOT) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.CAT_TYPE));
        }
        if (pt == PetType.PIG) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.SADDLE));
        }
        if (pt == PetType.SHEEP) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.COLOR));
            options.add(new MenuOption(i++, MenuItem.SHEARED));
        }
        if (pt == PetType.WOLF) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.TAMED));
            options.add(new MenuOption(i++, MenuItem.ANGRY));
            options.add(new MenuOption(i++, MenuItem.COLOR));
        }
        if (pt == PetType.VILLAGER) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.PROFESSION));
        }
        if (pt == PetType.HORSE) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.CHESTED));
            options.add(new MenuOption(i++, MenuItem.SADDLE));
            options.add(new MenuOption(i++, MenuItem.HORSE_TYPE));
            options.add(new MenuOption(i++, MenuItem.HORSE_ARMOUR));
            options.add(new MenuOption(i++, MenuItem.HORSE_VARIANT));
            options.add(new MenuOption(i++, MenuItem.HORSE_MARKING));
        }
        return options;
    }
}
