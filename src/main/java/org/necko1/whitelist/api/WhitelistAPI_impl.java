package org.necko1.whitelist.api;

import org.necko1.whitelist.Utils;
import org.necko1.whitelist.Whitelist;
import org.necko1.whitelist.data.WLJsonData;
import org.necko1.whitelist.data.WhitelistData;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistAPI_impl implements WhitelistAPI {
    private final WLJsonData local_json;
    public WhitelistAPI_impl(WLJsonData json) {
        local_json = json;
    }

    @Override
    public boolean migrate(boolean quiet) {
        try {
            local_json.migrate();
            return true;
        } catch (ParseException | IOException e) {
            if (!quiet) e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean save(boolean quiet) {
        try {
            local_json.save();
        } catch (RuntimeException e) {
            if (!quiet) e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<WhitelistData> getList() {
        return new ArrayList<>(local_json.getData());
    }

    @Override
    public boolean load(boolean quiet) {
        try {
            local_json.load(true);
            Whitelist.wlConfig.load();
            Whitelist.setTurned(Whitelist.wlConfig.config.getBoolean("turned", true));
            return true;
        } catch (ParseException | IOException e) {
            if (!quiet) e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(UUID uuid, String nickname, long expire_after) {
        if (local_json.getUUIDs().contains(uuid)) return false;

        try {
            local_json.addData(uuid, nickname, expire_after);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean add(String nickname, long expire_after) {
        UUID uuid = Utils.getUUID(nickname);

        return add(uuid, nickname, expire_after);
    }

    @Override
    public boolean add(UUID uuid, String nickname) {
        return add(uuid, nickname, -1);
    }

    @Override
    public boolean add(String nickname) {
        return add(nickname, -1);
    }

    @Override
    public boolean remove(String nickname) {
        UUID uuid = Utils.getUUID(nickname);

        return remove(uuid, nickname);
    }

    @Override
    public boolean remove(UUID uuid, String nickname) {
        if (!local_json.getUUIDs().contains(uuid)) return false;

        try {
            local_json.removeByNickname(nickname);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean on() {
        if (Whitelist.isTurned()) return false;
        Whitelist.setTurned(true);
        return true;
    }

    @Override
    public boolean off() {
        if (!Whitelist.isTurned()) return false;
        Whitelist.setTurned(false);
        return true;
    }

    @Override
    public UUID getUUID(Player player) {
        return Utils.getUUID(player.getName());
    }

    @Override
    public UUID getUUID(String nickname) {
        return Utils.getUUID(nickname);
    }

    @Override
    public List<String> getNicknames() {
        return new ArrayList<>(local_json.getNicknames());
    }

    @Override
    public List<UUID> getUUIDs() {
        return new ArrayList<>(local_json.getUUIDs());
    }

    @Override
    public WhitelistData getData(UUID uuid) {
        return local_json.getByUUID(uuid);
    }

    @Override
    public long parseTime(String time, boolean add_current) {
        return Utils.parseTime(time, add_current);
    }
}
