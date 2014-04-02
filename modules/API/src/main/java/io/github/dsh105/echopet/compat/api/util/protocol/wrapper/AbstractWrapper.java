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

package io.github.dsh105.echopet.compat.api.util.protocol.wrapper;

public class AbstractWrapper {

    private Object handle;

    public AbstractWrapper() {
    }

    protected void setHandle(Object handle) {
        if (this.handle == null) {
            this.handle = handle;
            return;
        }
        throw new RuntimeException("Handle already set!");
    }

    public Object getHandle() {
        return this.handle;
    }
}
