package jp.fukumai.scoreboardtool;

import jp.fukumai.scoreboardtool.event.PlayerDisplayNameChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PlayerObserver extends BukkitRunnable implements Listener {
    private final ScoreboardTool plugin;

    private final List<Player> oldPlayerDatas = new ArrayList<>();

    public PlayerObserver(ScoreboardTool plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!oldPlayerDatas.contains(p)) {
                oldPlayerDatas.add(p);
                return;
            }
            Player oldPlayer = getListItemByObject(oldPlayerDatas, p);
            if (oldPlayer != null) {
                if (!p.getDisplayName().equals(oldPlayer.getDisplayName())) {
                    PlayerDisplayNameChangeEvent event = new PlayerDisplayNameChangeEvent(p, oldPlayer.getDisplayName(), p.getDisplayName());
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }
        });
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Player oldPlayer = getListItemByObject(oldPlayerDatas, p);
        if (oldPlayerDatas.contains(p) && oldPlayer != null) {
            if (!oldPlayer.getLocation().equals(p.getLocation())) {
                
            }
        }
    }

    private Player getListItemByObject(List<Player> list, @Nonnull Player p) {
        return list.contains(p) ? list.get(list.indexOf(p)) : null;
    }
}
