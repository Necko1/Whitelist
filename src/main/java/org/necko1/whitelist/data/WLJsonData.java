package org.necko1.whitelist.data;

import org.necko1.whitelist.Utils;
import org.necko1.whitelist.Whitelist;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WLJsonData {

    private Set<WhitelistData> data;
    public File file;

    public void init() {
        this.file = new File(Whitelist.getInstance().getDataFolder(), "whitelist.json");

        try {
            if (!this.file.exists()) {
                Whitelist.getInstance().getDataFolder().mkdirs();
            }
            if (this.file.createNewFile()) this.migrate();
            this.load(false);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    public void migrate() throws ParseException, IOException {
        File migrateFrom = new File(Whitelist.getInstance().getDataFolder().getParentFile().getParentFile(), "whitelist.json");

        JSONArray a = (JSONArray) new JSONParser().parse(new FileReader(migrateFrom));
        JSONArray toWrite = new JSONArray();
        for (Object o : a) {
            JSONObject j = (JSONObject) o;
            JSONObject toAdd = new JSONObject();

            toAdd.put("uuid", Utils.getUUID(j.get("name").toString()).toString());
            toAdd.put("name", j.get("name"));
            toAdd.put("good_by", -1);
            toWrite.add(toAdd);
        }

        try (FileWriter file = new FileWriter(this.file)) {
            file.write(toWrite.toJSONString());
            file.flush();

            Bukkit.getLogger().info(Whitelist.serializer.serialize(Whitelist.wlConfig.getString("migrate-success")));
        } catch (IOException e) {
            e.printStackTrace();
            Whitelist.serializer.serialize(Whitelist.wlConfig.getString("migrate-failed"));
        }
    }

    public void save() {
        JSONArray toWrite = new JSONArray();
        for (WhitelistData d : getData()) {
            JSONObject toAdd = new JSONObject();
            toAdd.put("uuid", d.getUuid().toString());
            toAdd.put("name", d.getName());
            toAdd.put("good_by", d.getGood_by());
            toWrite.add(toAdd);
        }

        try (FileWriter file = new FileWriter(this.file)) {
            file.write(toWrite.toJSONString());
            file.flush();
        } catch (IOException e) {
            this.init();
            this.save();
        }
    }

    public void load(boolean a1) throws ParseException, IOException {
        try {
            if (a1) setData(new HashSet<>());
            JSONArray dataRaw = (JSONArray) new JSONParser().parse(new FileReader(this.file));
            for (Object o : dataRaw) {
                JSONObject j = (JSONObject) o;
                addData((String) j.get("uuid"), (String) j.get("name"), (long) j.get("good_by"));
            }
        } catch (ParseException | IOException e) {
            this.migrate();
            this.load(a1);
        }
    }

    private void ifDataIsNull() {
        if (this.data == null) {
            this.data = new HashSet<>();
        }
    }

    public void setData(Set<WhitelistData> data) {
        this.data = data;
    }

    public void addData(WhitelistData data) {
        ifDataIsNull();
        this.data.add(data);
    }

    public void addData(UUID uuid, String name, long good_by) {
        this.addData(new WhitelistData(uuid, name, good_by));
    }

    public void addData(String uuid, String name, long good_by) {
        this.addData(new WhitelistData(UUID.fromString(uuid), name, good_by));
    }
    public void removeByNickname(String name) {
        this.data.removeIf(d -> d.getName().equals(name));
    }
    public WhitelistData getByUUID(UUID uuid) {
        for (WhitelistData d : getData()) {
            if (d.getUuid().equals(uuid)) {
                return d;
            }
        }
        return null;
    }

    public Set<WhitelistData> getData() {
        ifDataIsNull();
        return new HashSet<>(this.data);
    }

    public Set<String> getNicknames() {
        Set<String> nicknames = new HashSet<>();
        for (WhitelistData d : getData()) {
            nicknames.add(d.getName());
        }
        return nicknames;
    }

    public Set<UUID> getUUIDs() {
        Set<UUID> uuids = new HashSet<>();
        for (WhitelistData d : getData()) {
            uuids.add(d.getUuid());
        }
        return uuids;
    }

}
