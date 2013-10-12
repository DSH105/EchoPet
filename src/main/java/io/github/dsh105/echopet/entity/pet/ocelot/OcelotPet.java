package io.github.dsh105.echopet.entity.pet.ocelot;


import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.IAgeablePet;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;

public class OcelotPet extends Pet implements IAgeablePet {

    boolean baby;
    Type type = Type.WILD_OCELOT;

    public OcelotPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setBaby(boolean flag) {
        ((EntityOcelotPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public Type getCatType() {
        return type;
    }

    public void setCatType(Type t) {
        setCatType(t.getId());
    }

    public void setCatType(int i) {
        ((EntityOcelotPet) getEntityPet()).setCatType(i);
    }
}