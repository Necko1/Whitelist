package org.necko1.whitelist.cmd;

import org.necko1.whitelist.Utils;
import org.necko1.whitelist.Whitelist;
import org.necko1.whitelist.data.WhitelistData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class WhitelistCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 0) return help(commandSender);

        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("reload")) {
            return reload(commandSender);
        }else if (subcommand.equalsIgnoreCase("list")) {
            return list(commandSender);
        } else if (subcommand.equalsIgnoreCase("add")) {
            return add(commandSender, args);
        } else if (subcommand.equalsIgnoreCase("remove")) {
            return remove(commandSender, args);
        } else if (subcommand.equalsIgnoreCase("on")) {
            return on(commandSender);
        } else if (subcommand.equalsIgnoreCase("off")) {
            return off(commandSender);
        } else if (subcommand.equalsIgnoreCase("help")) {
            return help(commandSender);
        } else if (subcommand.equalsIgnoreCase("migrate")) {
            return migrate(commandSender);
        }
        return false;
    }

    private boolean migrate(CommandSender commandSender) {
        try {
            Whitelist.json.migrate();
            Whitelist.json.load(true);
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("migrate-success")));
            return true;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("migrate-failed")));
            return false;
        }
    }

    private boolean test(CommandSender commandSender, String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s).append(" ");
        }
        commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.miniMessage.deserialize(sb.toString())));
        return true;
    }

    private boolean help(CommandSender commandSender) {
        for (String s : Whitelist.wlConfig.getStringList("help")) {
            commandSender.sendMessage(s);
        }
        return true;
    }

    private boolean reload(CommandSender commandSender) {
        try {
            Whitelist.json.load(true);
            Whitelist.wlConfig.load();
            Whitelist.setTurned(Whitelist.wlConfig.config.getBoolean("turned", true));
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("reload-success")));
            return true;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("reload-failed")));
            return false;
        }
    }

    private boolean list(CommandSender commandSender) {
        try {
            List<WhitelistData> list = new ArrayList<>(Whitelist.json.getData());
            if (list.isEmpty()) {
                commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("list-empty")));
                return true;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                String name = list.get(i).getName();
                sb.append(name);
                if (i < list.size() - 1) {
                    sb.append(", ");
                }
            }

            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("list-message")).replace("<size>", String.valueOf(list.size())).replace("<list>", sb.toString()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("list-failed")));
            return false;
        }
    }

    private boolean add(CommandSender commandSender, String[] args) {
        if (args.length <= 1 || args.length > 3) {
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("add-usage")));
            return false;
        }

        try {
            String nickname = args[1];
            if (Whitelist.json.getNicknames().contains(nickname)) {
                commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("player-already-whitelisted")));
                return false;
            }

            UUID uuid = Utils.getUUID(nickname);

            long time = -1;
            String arg2 = "forever";
            if (args.length == 3) {
                time = Utils.parseTime(args[2], true);
                arg2 = args[2];
            }
            Whitelist.json.addData(uuid, nickname, time);
            String time_string = time == -1 ? "forever" : arg2;
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("add-success")).replace("<name>", nickname).replace("<time>", time_string));
            Whitelist.json.save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("add-failed")));
            return false;
        }
    }

    private boolean remove(CommandSender commandSender, String[] args) {
        if (args.length != 2) {
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("remove-usage")));
            return false;
        }

        try {
            String nickname = args[1];
            if (!Whitelist.json.getNicknames().contains(nickname)) {
                commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("player-not-whitelisted")));
                return false;
            }

            Whitelist.json.removeByNickname(nickname);
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("remove-success")).replace("<name>", nickname));
            Whitelist.json.save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("remove-failed")));
            return false;
        }
    }

    private boolean on(CommandSender commandSender) {
        if (Whitelist.isTurned()) {
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("wl_on_failed")));
            return false;
        }
        Whitelist.setTurned(true);
        commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("wl_on_success")));
        return true;
    }

    private boolean off(CommandSender commandSender) {
        if (!Whitelist.isTurned()) {
            commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("wl_off_failed")));
            return false;
        }
        Whitelist.setTurned(false);
        commandSender.sendMessage(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("wl_off_success")));
        return true;
    }

}
