package net.mehradmgm.dawn.api;

import org.bukkit.entity.Player;

public final class DawnPlayer {

    private final Player player;
    private final String clientBrand;
    private final DawnClient client;

    public DawnPlayer(
            Player player,
            String clientBrand,
            DawnClient client
    ) {
        this.player = player;
        this.clientBrand = clientBrand;
        this.client = client;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return player.getName();
    }

    public String getClientBrand() {
        return clientBrand;
    }

    public DawnClient getClient() {
        return client;
    }

    public boolean isUsingDawn() {
        return client == DawnClient.DAWN;
    }

    public boolean isUsingFeather() {
        return client == DawnClient.FEATHER;
    }
}