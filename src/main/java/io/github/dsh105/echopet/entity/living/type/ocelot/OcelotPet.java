package io.github.dsh105.echopet.entity.living.type.ocelot;


import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;

public class OcelotPet extends LivingPet implements IAgeablePet {

    boolean baby;
    Type type = Type.WILD_OCELOT;

    public OcelotPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityOcelotPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public Type getCatType() {
        return type;
    }

    public void setCatType(Type t) {
        setCatType(t.getId());
        this.type = t;
    }

    public void setCatType(int i) {
        ((EntityOcelotPet) getEntityPet()).setCatType(i);
        this.type = Type.getType(i);
    }
}