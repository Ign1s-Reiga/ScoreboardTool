package jp.fukumai.scoreboardtool.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerDisplayNameChangeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String oldDisplayName;
    private final String newDisplayName;

    public PlayerDisplayNameChangeEvent(Player who, String oldDisplayName, String newDisplayName) {
        super(who);
        this.oldDisplayName = oldDisplayName;
        this.newDisplayName = newDisplayName;
    }

    public String getOldDisplayName() {
        return oldDisplayName;
    }

    public String getNewDisplayName() {
        return newDisplayName;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
