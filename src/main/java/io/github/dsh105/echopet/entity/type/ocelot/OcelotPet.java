package io.github.dsh105.echopet.entity.type.ocelot;


import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.IAgeablePet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.OCELOT)
public class OcelotPet extends Pet implements IAgeablePet {

    boolean baby;
    Type type = Type.WILD_OCELOT;

    public OcelotPet(Player owner) {
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