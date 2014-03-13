package io.github.dsh105.echopet.util.pagination;

import mkremins.fanciful.FancyMessage;

import java.util.ArrayList;


public class FancyPaginator {

    private ArrayList<FancyMessage> raw = new ArrayList<FancyMessage>();
    private int perPage;

    public FancyPaginator(FancyMessage[] raw, int perPage) {
        this.perPage = perPage;
        this.setRaw(raw);
    }

    public FancyPaginator(ArrayList<FancyMessage> raw, int perPage) {
        this.perPage = perPage;
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