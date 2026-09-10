package net.mehradmgm.dawn.api;

import org.bukkit.entity.Player;

public final class DawnEvent {

    private final Player player;
    private final DawnDetectionResult result;

    public DawnEvent(
            Player player,
            DawnDetectionResult result
    ) {
        this.player = player;
        this.result = result;
    }

    public Player getPlayer() {
        return player;
    }

    public DawnDetectionResult getResult() {
        return result;
    }

    public String getClientBrand() {
        return result.getClientBrand();
    }
}