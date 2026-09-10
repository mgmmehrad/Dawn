package net.mehradmgm.dawn.api;

import org.bukkit.entity.Player;

public final class DawnDetectionResult {

    private final Player player;
    private final String clientBrand;
    private final DawnClient client;
    private final boolean usingDawn;

    public DawnDetectionResult(
            Player player,
            String clientBrand,
            DawnClient client,
            boolean usingDawn
    ) {
        this.player = player;
        this.clientBrand = clientBrand;
        this.client = client;
        this.usingDawn = usingDawn;
    }

    public Player getPlayer() {
        return player;
    }

    public String getClientBrand() {
        return clientBrand;
    }

    public DawnClient getClient() {
        return client;
    }

    public boolean isUsingDawn() {
        return usingDawn;
    }
}