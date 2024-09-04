package org.necko1.whitelist.cmd;

import org.necko1.whitelist.Whitelist;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhiteListTabCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Set<String> suggestions = new HashSet<>();

        if (args.length == 1) {
            suggestions.add("reload");
            suggestions.add("list");
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("on");
            suggestions.add("off");
            suggestions.add("migrate");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            Bukkit.getOnlinePlayers().forEach(p -> suggestions.add(p.getName()));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            suggestions.addAll(Whitelist.json.getNicknames());
        } else if (args.length == 3
                && (args[0].equalsIgnoreCase("add")
                || args[0].equalsIgnoreCase("remove"))) {
            suggestions.add("1s");
            suggestions.add("1m");
            suggestions.add("1h");
            suggestions.add("1d");
        }

        return new ArrayList<>(suggestions);
    }

}
