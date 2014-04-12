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

package com.dsh105.echopet.compat.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface IPet {

    public IEntityPet getEntityPet();

    public ICraftPet getCraftPet();

    public Location getLocation();

    public Player getOwner();

    public String getNameOfOwner();

    public PetType getPetType();

    public boolean isRider();

    public IPet getRider();

    public boolean isOwnerInMountingProcess();

    public String getPetName();

    public String getPetNameWithoutColours();

    public boolean setPetName(String name);

    public boolean setPetName(String name, boolean sendFailMessage);

    public ArrayList<PetData> getPetData();

    public void removeRider();

    public void removePet(boolean makeSound);

    public void teleportToOwner();

    public void teleport(Location to);

    public boolean isOwnerRiding();

    public boolean isHat();

    public void ownerRidePet(boolean flag);

    public void setAsHat(boolean flag);

    public IPet createRider(final PetType pt, boolean sendFailMessage);
}