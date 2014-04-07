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

package com.dsh105.echopet.compat.api.util.protocol.wrapper;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;

public class WrappedDataWatcher extends AbstractWrapper {

    public WrappedDataWatcher() {
        try {
            if (EchoPet.isUsingNetty()) {
                super.setHandle(ReflectionUtil.getNMSClass("DataWatcher").getConstructor(ReflectionUtil.getNMSClass("Entity")).newInstance(new Object[]{null}));
            } else {
                super.setHandle(ReflectionUtil.getNMSClass("DataWatcher").newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public WrappedDataWatcher(Object entity) {
        try {
            if (EchoPet.isUsingNetty()) {
                super.setHandle(ReflectionUtil.getNMSClass("DataWatcher").getConstructor(ReflectionUtil.getNMSClass("Entity")).newInstance(entity));
            } else {
                super.setHandle(ReflectionUtil.getNMSClass("DataWatcher").newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void watch(int index, Object value) {
        ReflectionUtil.invokeMethod(ReflectionUtil.getMethod(getHandle().getClass(), "a", int.class, Object.class), getHandle(), index, value);
    }
}
