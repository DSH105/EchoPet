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

package com.dsh105.echopet.compat.api.util.pagination;

import com.dsh105.echopet.compat.api.util.fanciful.FancyMessage;

import java.util.ArrayList;


public class FancyPaginator {

    private ArrayList<FancyMessage> raw = new ArrayList<FancyMessage>();
    private int perPage;

    public FancyPaginator(FancyMessage[] raw, int perPage) {
        this.perPage = perPage;
        this.setRaw(raw);
    }

    public FancyPaginator(ArrayList<FancyMessage> raw, int perPage) {
        this.perPage = 5;
        this.setRaw(raw);
    }

    public void setRaw(FancyMessage[] newRaw) {
        for (FancyMessage s : newRaw) {
            this.raw.add(s);
        }
    }

    public void setRaw(ArrayList<FancyMessage> newRaw) {
        this.raw = newRaw;
    }

    public ArrayList<FancyMessage> getRaw() {
        return this.raw;
    }

    public int getIndex() {
        return (int) (Math.ceil(this.raw.size() / ((double) this.perPage)));
    }

    public double getDoubleIndex() {
        return (Math.ceil(this.raw.size() / ((double) this.perPage)));
    }

    public FancyMessage[] getPage(int pageNumber) {
        int index = this.perPage * (Math.abs(pageNumber) - 1);
        ArrayList<FancyMessage> list = new ArrayList<FancyMessage>();
        if (pageNumber <= getDoubleIndex()) {
            for (int i = index; i < (index + this.perPage); i++) {
                if (raw.size() > i) {
                    list.add(raw.get(i));
                }
            }
        } else {
            return null;
        }
        return list.toArray(new FancyMessage[list.size()]);
    }
}