package necko1.whitelist.data;

import java.util.UUID;

public class WhitelistData {
    private UUID uuid;
    private String name;
    private long good_by;

    public WhitelistData() {}
    public WhitelistData(UUID uuid, String name, long good_by) {
        this.uuid = uuid;
        this.name = name;
        this.good_by = good_by;
    }

    /**
     * Retrieves the unique identifier associated with this WhitelistData instance.
     *
     * @return  the unique identifier (UUID) of this WhitelistData instance
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Retrieves the name associated with this WhitelistData instance.
     *
     * @return  the name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the time in milliseconds when this WhitelistData instance will become invalid.
     *
     * @return  the time in milliseconds when this WhitelistData instance will become invalid (good_by = expires_after)
     */
    public long getGood_by() {
        return good_by;
    }

    public void setGood_by(long good_by) {
        this.good_by = good_by;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

}
