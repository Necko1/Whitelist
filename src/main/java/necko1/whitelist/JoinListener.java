package necko1.whitelist;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import necko1.whitelist.data.WhitelistData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        if (!Whitelist.isTurned()) return;

        UUID uuid = Utils.getUUID(event.getName());
        WhitelistData playerData = Whitelist.json.getByUUID(uuid);
        if (playerData != null && playerData.getGood_by() < 0) return;
        if (playerData != null && Whitelist.json.getUUIDs().contains(uuid)) {
            long time_left = playerData.getGood_by();
            long current_time = System.currentTimeMillis();

            if (time_left < current_time) {
                Whitelist.json.removeByNickname(event.getName());
                Whitelist.json.save();
            } else return;
        }
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Whitelist.serializer.serialize(Whitelist.wlConfig.getString("kick-message")));
//        this.kickPlayer(player, );
    }

    public void kickPlayer(Player player, String reason) {
        player.kickPlayer(reason);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("CustomMessage");
        out.writeUTF(player.getName());
        out.writeUTF(reason);

        player.sendPluginMessage(Whitelist.getInstance(), "bungeecord:channel", out.toByteArray());
    }

}
