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

package com.dsh105.echopet.compat.api.reflection;

import com.dsh105.echopet.compat.api.util.ReflectionUtil;

public enum ReflectionConstants {

    PACKET_PARTICLES_FIELD_NAME(new String[] {"a"}, "field_98209_a", "field_149236_a"),
    PACKET_PARTICLES_FIELD_X(new String[] {"b"}, "field_98207_b", "field_149234_b"),
    PACKET_PARTICLES_FIELD_Y(new String[] {"c"}, "field_98208_c", "field_149235_c"),
    PACKET_PARTICLES_FIELD_Z(new String[] {"d"}, "field_98205_d", "field_149232_d"),
    PACKET_PARTICLES_FIELD_OFFSETX(new String[] {"e"}, "field_98206_e", "field_149233_e"),
    PACKET_PARTICLES_FIELD_OFFSETY(new String[] {"f"}, "field_98203_f", "field_149230_f"),
    PACKET_PARTICLES_FIELD_OFFSETZ(new String[] {"g"}, "field_98204_g", "field_149231_g"),
    PACKET_PARTICLES_FIELD_SPEED(new String[] {"h"}, "field_98210_h", "field_149237_h"),
    PACKET_PARTICLES_FIELD_QUANTITY(new String[] {"i"}, "field_98211_i", "field_149238_i"),

    PACKET_NAMEDSPAWN_FIELD_ID(new String[] {"a"}, "field_73516_a", "field_148957_a"),
    PACKET_NAMEDSPAWN_FIELD_PROFILE(new String[] {"b"}, "field_73514_b", "field_148955_b"),
    PACKET_NAMEDSPAWN_FIELD_X(new String[] {"c"}, "field_73515_c", "field_148956_c"),
    PACKET_NAMEDSPAWN_FIELD_Y(new String[] {"d"}, "field_73512_d", "field_148953_d"),
    PACKET_NAMEDSPAWN_FIELD_Z(new String[] {"e"}, "field_73513_e", "field_148954_e"),
    PACKET_NAMEDSPAWN_FIELD_YAW(new String[] {"f"}, "field_73510_f", "field_148951_f"),
    PACKET_NAMEDSPAWN_FIELD_PITCH(new String[] {"g"}, "field_73511_g", "field_148952_g"),
    PACKET_NAMEDSPAWN_FIELD_EQUIPMENT(new String[] {"h"}, "field_73518_h", "field_148959_h"),
    PACKET_NAMEDSPAWN_FIELD_META(new String[] {"i"}, "field_73519_i", "field_148960_i"),

    PACKET_CHAT_FIELD_MESSAGE(new String[] {"message", "a"}, "field_73476_b", "field_148919_a"),
    PACKET_CHAT_FUNC_SETCOMPONENT(new String[] {"a"}, "func_150699_a"),
    PACKET_CHAT_FUNC_GETMESSAGE(new String[] {"a"}, "func_150696_a"),

    PACKET_ENTITYMETADATA_FIELD_ID(new String[] {"a"}, "field_73393_a", "field_149379_a"),
    PACKET_ENTITYMETADATA_FIELD_META(new String[] {"b"}, "field_73392_b", "field_149378_b"),
    PACKET_ENTITYMETADATA_FUNC_PREPARE(new String[] {"c"}, "func_75685_c", "func_75685_c"),

    DATAWATCHER_FUNC_INITIATE(new String[] {"a"}, "func_75682_a", "func_75682_a"),
    DATAWATCHER_FUNC_WATCH(new String[] {"a"}, "func_75692_b", "func_75692_b"),

    PLAYER_FIELD_CONNECTION(new String[] {"playerConnection"}, "field_71135_a", "field_71135_a"),
    PLAYER_FUNC_SENDPACKET(new String[] {"sendPacket"}, "func_72567_b", "func_147359_a"),

    ENTITYTYPES_FIELD_NAMETOCLASSMAP(new String[] {"b", "c"}, "field_75625_b"),
    ENTITYTYPES_FIELD_CLASSTONAMEMAP(new String[] {"c", "d"}, "field_75626_c"),
    ENTITYTYPES_FIELD_CLASSTOIDMAP(new String[] {"e", "f"}, "field_75624_e"),
    ENTITYTYPES_FIELD_NAMETOIDMAP(new String[] {"f", "g"}, "field_75622_f"),

    ACHIEVEMENT_FIELD_NAME(new String[] {"name"}, "field_75996_k"),

    ITEMSTACK_FUNC_SAVE(new String[] {"save"}, "func_77955_b"),

    PROTOCOL_FIELD_CLIENTPACKETMAP(new String[] {"h"}, "field_150769_h"),
    PROTOCOL_FIELD_SERVERPACKETMAP(new String[] {"i"}, "field_150770_i"),
    PROTOCOL_FIELD_PACKETMAP(new String[] {"a"}, "field_73291_a"),

    ENTITY_FUNC_MOUNT(new String[] {"mount"}, "func_70078_a"),

    GAMEPROFILE_FUNC_ID(new String[] {"getId"})
    ;

    /*
     * Prepare for some magic here >:D
     *
     * Supports all reflection fields for all versions this plugin is known to support
     * Even MCPC+ is included! :D
     * All that's needed for each update is to add a new case for each enum thingy here
     * If it's left, the name used for the last known supported version is used instead
     */

    private String[] vanilla;
    private String[] mcpc;
    private int[] knownVersions = new int[] {163, 171, 172, 173, 174};
    private int[] knownMCPCVersions = new int[] {16, 17};
    private int versionIndex = -1;
    private int mcpcVersionIndex = -1;

    ReflectionConstants(String[] vanilla, String... mcpc) {
        this.vanilla = vanilla;
        this.mcpc = mcpc;

        for (int i = 0; i < knownVersions.length; i++) {
            if (ReflectionUtil.MC_VERSION_NUMERIC == knownVersions[i]) {
                versionIndex = i;
            }
        }

        for (int i = 0; i < knownMCPCVersions.length; i++) {
            if (String.valueOf(ReflectionUtil.MC_VERSION_NUMERIC).startsWith(String.valueOf(knownMCPCVersions[i]))) {
                mcpcVersionIndex = i;
            }
        }
    }

    public String getName() {
        // If the mapped versions only contains one, we can use that
        // If there's more, but the mapped version isn't specifically supported, use the mappings of the latest supported version
        String[] valueArray = ReflectionUtil.isServerMCPC() ? this.mcpc : this.vanilla;
        int vIndex = ReflectionUtil.isServerMCPC() ? this.mcpcVersionIndex : this.versionIndex;
        return (vIndex < 0 || valueArray.length == 1) ? valueArray[0] : (vIndex >= valueArray.length) ? valueArray[valueArray.length - 1] : valueArray[vIndex];
    }
}