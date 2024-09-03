package necko1.whitelist.api;

import necko1.whitelist.Utils;
import necko1.whitelist.Whitelist;
import necko1.whitelist.data.WhitelistData;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistAPI_impl implements WhitelistAPI {
    /**
     * Migrates old data to new format.
     *
     * @param quiet if {@code true}, no errors will be printed to console.
     * @return {@code true} if migration was successful, {@code false} otherwise.
     */
    @Override
    public boolean migrate(boolean quiet) {
        try {
            Whitelist.json.migrate();
            return true;
        } catch (ParseException | IOException e) {
            if (!quiet) e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean save(boolean quiet) {
        try {
            Whitelist.json.save();
        } catch (RuntimeException e) {
            if (!quiet) e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<WhitelistData> getList() {
        return new ArrayList<>(Whitelist.json.getData());
    }

    @Override
    public boolean load(boolean quiet) {
        try {
            Whitelist.json.load(true);
            Whitelist.wlConfig.load();
            Whitelist.setTurned(Whitelist.wlConfig.config.getBoolean("turned", true));
            return true;
        } catch (ParseException | IOException e) {
            if (!quiet) e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(UUID uuid, long expire_after) {
        if (Whitelist.json.getUUIDs().contains(uuid)) return false;

        try {
            Whitelist.json.addData(uuid, Whitelist.json.getByUUID(uuid).getName(), expire_after);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean add(String nickname, long expire_after) {
        UUID uuid = Utils.getUUID(nickname);

        return this.add(uuid, expire_after);
    }

    @Override
    public boolean add(UUID uuid) {
        return this.add(uuid, -1);
    }

    @Override
    public boolean add(String nickname) {
        return this.add(nickname, -1);
    }

    @Override
    public boolean remove(String nickname) {
        UUID uuid = Utils.getUUID(nickname);

        return this.remove(uuid);
    }

    @Override
    public boolean remove(UUID uuid) {
        if (!Whitelist.json.getUUIDs().contains(uuid)) return false;

        try {
            Whitelist.json.removeByNickname(Whitelist.json.getByUUID(uuid).getName());
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
        return new ArrayList<>(Whitelist.json.getNicknames());
    }

    @Override
    public List<UUID> getUUIDs() {
        return new ArrayList<>(Whitelist.json.getUUIDs());
    }

    @Override
    public WhitelistData getData(UUID uuid) {
        return Whitelist.json.getByUUID(uuid);
    }
}
