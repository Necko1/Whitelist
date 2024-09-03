package necko1.whitelist.api;

import necko1.whitelist.data.WhitelistData;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface WhitelistAPI {

    /**
     * Migrates old whitelist file to new one.
     * @param quiet If true, don't print anything to console.
     * @return true if migration was successful, false otherwise.
     */
    boolean migrate(boolean quiet);

    /**
     * Saves whitelist data to file.
     * @param quiet If true, don't print anything to console.
     * @return true if saving was successful, false otherwise.
     */
    boolean save(boolean quiet);

    /**
     * Loads whitelist data from file.
     * @param quiet If true, don't print anything to console.
     * @return true if loading was successful, false otherwise.
     */
    boolean load(boolean quiet);

    /**
     * Returns all whitelist data.
     * @return list of all whitelist data.
     */
    List<WhitelistData> getList();

    /**
     * Adds player to whitelist.
     * @param uuid Player's UUID. (Must be gotten by {@link necko1.whitelist.api.WhitelistAPI#getUUID(String)})
     * @param expire_after How long player will be whitelisted in seconds.
     *                     If -1, player will be whitelisted forever.
     * @return true if adding was successful, false otherwise.
     */
    boolean add(UUID uuid, long expire_after);

    /**
     * Adds player to whitelist.
     * @param nickname Player's nickname.
     * @param expire_after How long player will be whitelisted in seconds.
     *                     If -1, player will be whitelisted forever.
     * @return true if adding was successful, false otherwise.
     */
    boolean add(String nickname, long expire_after);

    /**
     * Adds player to whitelist.
     * @param uuid Player's UUID. (Must be gotten by {@link necko1.whitelist.api.WhitelistAPI#getUUID(String)})
     * @return true if adding was successful, false otherwise.
     */
    boolean add(UUID uuid);

    /**
     * Adds player to whitelist.
     * @param nickname Player's nickname.
     * @return true if adding was successful, false otherwise.
     */
    boolean add(String nickname);

    /**
     * Removes player from whitelist.
     * @param nickname Player's nickname.
     * @return true if removing was successful, false otherwise.
     */
    boolean remove(String nickname);

    /**
     * Removes player from whitelist.
     * @param uuid Player's UUID. (Must be gotten by {@link necko1.whitelist.api.WhitelistAPI#getUUID(String)})
     * @return true if removing was successful, false otherwise.
     */
    boolean remove(UUID uuid);
    /**
     * Enables whitelist.
     * @return true if enabling was successful, false otherwise.
     */
    boolean on();

    /**
     * Disables whitelist.
     * @return true if disabling was successful, false otherwise.
     */
    boolean off();

    /**
     * @return player's UUID
     */
    UUID getUUID(Player player);

    /**
     * Gets player's UUID by their nickname.
     * @param nickname player's nickname
     * @return player's UUID
     */
    UUID getUUID(String nickname);
    /**
     * Returns all whitelisted nicknames.
     * @return list of all whitelisted nicknames.
     */
    List<String> getNicknames();

    /**
     * Returns all whitelisted UUIDs.
     * @return list of all whitelisted UUIDs.
     */
    List<UUID> getUUIDs();

    /**
     * Gets data of a player from whitelist.
     * @param uuid Player's UUID. (Must be gotten by {@link #getUUID(String)})
     * @return data of player, or null if no such player exists in whitelist.
     */
    WhitelistData getData(UUID uuid);

}
