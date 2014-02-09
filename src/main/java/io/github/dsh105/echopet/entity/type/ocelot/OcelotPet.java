package io.github.dsh105.echopet.entity.type.ocelot;


import io.github.dsh105.echopet.entity.*;
import org.bukkit.entity.Ocelot.Type;

@EntityPetType(petType = PetType.OCELOT)
public class OcelotPet extends Pet implements IAgeablePet {

    boolean baby;
    Type type = Type.WILD_OCELOT;

    public OcelotPet(String owner) {
        super(owner);
    }

    public OcelotPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
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