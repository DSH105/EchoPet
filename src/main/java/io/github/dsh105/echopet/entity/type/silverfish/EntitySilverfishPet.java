package io.github.dsh105.echopet.entity.type.silverfish;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.EntitySize;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.SizeCategory;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.3F, height = 0.7F)
@EntityPetType(petType = PetType.SILVERFISH)
public class EntitySilverfishPet extends EntityPet {

    public EntitySilverfishPet(World world) {
        super(world);
    }

    public EntitySilverfishPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.silverfish.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.silverfish.kill";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }
}