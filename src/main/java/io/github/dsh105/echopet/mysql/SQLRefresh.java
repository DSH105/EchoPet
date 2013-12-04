package io.github.dsh105.echopet.mysql;

import io.github.dsh105.echopet.EchoPet;
import org.bukkit.scheduler.BukkitRunnable;

public class SQLRefresh extends BukkitRunnable {

    private int timeout;

    public SQLRefresh(int timeout) {
        this.timeout = timeout;
        this.runTaskLater(EchoPet.getInstance(), this.timeout);
    }

    @Override
    public void run() {
        SQLConnection sql = EchoPet.getInstance().sqlCon;
        sql.close();
        sql.connect();
    }
}