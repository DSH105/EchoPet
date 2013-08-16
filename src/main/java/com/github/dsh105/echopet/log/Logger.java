package com.github.dsh105.echopet.log;

import com.github.dsh105.echopet.EchoPet;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private static File logFile;
	private static boolean enabled = false;

	public static void initiate() {
		EchoPet ec = EchoPet.getPluginInstance();
		File folder = ec.getDataFolder();
		if (!folder.exists()) {
			folder.mkdir();
		}

		File log = new File(ec.getDataFolder(), "EchoPet.log");
		if (!log.exists()) {
			try {
				log.createNewFile();
				logFile = log;
				enabled = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(LogLevel logLevel, String message) {
		if (enabled) {
			FileWriter fw = null;
			try {
				fw = new FileWriter(logFile, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			PrintWriter pw = new PrintWriter(fw);
			String date = new SimpleDateFormat("[dd/MM/yyyy]---[HH:mm:ss]").format(new Date());
			pw.println(date + logLevel.getPrefix() + " " + message);
			pw.flush();
			pw.close();
		}
	}

	public enum LogLevel {
		NORMAL(ChatColor.GREEN + "[INFO]"),
		SEVERE(ChatColor.RED + "[SEVERE]"),
		WARNING(ChatColor.RED + "[WARNING]");

		private String prefix;
		LogLevel(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return EchoPet.getPluginInstance().prefix + " " + this.prefix;
		}
	}
}