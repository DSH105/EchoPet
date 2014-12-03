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

package com.dsh105.echopet.compat.api.util;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static boolean enabled = false;
    private static String logPrefix;

    public static void initiate(JavaPlugin plugin, String name, String prefix) {
        logPrefix = prefix;
        enabled = true;
    }

    public static void log(LogLevel logLevel, String message, boolean logToConsole) {
        if (enabled) {
            if (logToConsole) {
                EchoPet.LOG.log(message);
            }
        }
    }

    public static void log(LogLevel logLevel, String message, Exception e, boolean logToConsole) {
        if (enabled) {
            if (logToConsole) {
                EchoPet.LOG.severe(message);
            }
            e.printStackTrace();
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
            return Logger.logPrefix + " " + this.prefix;
        }
    }
}