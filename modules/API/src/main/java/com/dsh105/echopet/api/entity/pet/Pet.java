/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.api.entity.pet;

import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.nms.EntityPet;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface Pet {

    float width();

    float height();

    public void setDataValue(PetData petData);

    void setDataValue(PetData petData, Object value);

    public ArrayList<PetData> getRegisteredData();

    public ArrayList<PetData> getActiveData();

    void setDataValue(PetData... dataArray);

    void setDataValue(boolean on, PetData... dataArray);

    void invertDataValue(PetData petData);

    public EntityPet getEntityPet();

    public Creature getCraftPet();

    public Location getLocation();

    public Player getOwner();

    public String getNameOfOwner();

    public UUID getOwnerUUID();

    public Object getOwnerIdentification();

    public PetType getPetType();

    public boolean isRider();

    public Pet getRider();

    public boolean isOwnerInMountingProcess();

    public String getPetName();

    public String getPetNameWithoutColours();

    public boolean setPetName(String name);

    public boolean setPetName(String name, boolean sendFailMessage);

    public void removeRider();

    public void removePet(boolean makeSound);

    public boolean teleportToOwner();

    public boolean teleport(Location to);

    public boolean isOwnerRiding();

    public boolean isHat();

    public void ownerRidePet(boolean flag);

    public void setAsHat(boolean flag);

    public Pet createRider(final PetType pt, boolean sendFailMessage);
}