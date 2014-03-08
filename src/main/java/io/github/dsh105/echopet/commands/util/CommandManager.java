package io.github.dsh105.echopet.commands.util;

import io.github.dsh105.echopet.reflection.FieldAccessor;
import io.github.dsh105.echopet.reflection.SafeField;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommandManager {

    protected static final FieldAccessor<CommandMap> SERVER_COMMAND_MAP = new SafeField<CommandMap>(SimplePluginManager.class, "commandMap");
    protected static final FieldAccessor<Map<String, Command>> KNOWN_COMMANDS = new SafeField<Map<String, Command>>(SimpleCommandMap.class, "knownCommands");

    private CommandMap fallback;

    private final Plugin plugin;

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void register(DynamicPluginCommand command) {
        getCommandMap().register(this.plugin.getName(), command);
    }

    public boolean unregister() {
        CommandMap commandMap = getCommandMap();
        List<String> toRemove = new ArrayList<String>();
        Map<String, Command> knownCommands = KNOWN_COMMANDS.get(commandMap);
        if (knownCommands == null) {
            return false;
        }
        for (Iterator<Command> i = knownCommands.values().iterator(); i.hasNext();) {
            Command cmd = i.next();
            if (cmd instanceof DynamicPluginCommand) {
                i.remove();
                for (String alias : cmd.getAliases()) {
                    Command aliasCmd = knownCommands.get(alias);
                    if (cmd.equals(aliasCmd)) {
                        toRemove.add(alias);
                    }
                }
            }
        }
        for (String string : toRemove) {
            knownCommands.remove(string);
        }
        return true;
    }

    public CommandMap getCommandMap() {
        CommandMap map = SERVER_COMMAND_MAP.get(Bukkit.getPluginManager());

        if(map == null) {
            if(fallback != null) {
                return fallback;
            } else {
                fallback = map = new SimpleCommandMap(Bukkit.getServer());
                Bukkit.getPluginManager().registerEvents(new FallbackCommandRegistrationListener(fallback), this.plugin);
            }
        }
        return map;
    }
}
