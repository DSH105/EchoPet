package com.github.dsh105.echopet.mysql;

import com.github.dsh105.echopet.EchoPet;
import org.bukkit.scheduler.BukkitRunnable;

public class SQLRefresh extends BukkitRunnable {

	private int timeout;

	public SQLRefresh(int timeout) {
		this.timeout = timeout;
		this.runTaskLater(EchoPet.getPluginInstance(), this.timeout);
	}

	@Override
	public void run() {
		SQLConnection sql = EchoPet.getPluginInstance().sqlCon;
		sql.close();
		sql.connect();
	}
}