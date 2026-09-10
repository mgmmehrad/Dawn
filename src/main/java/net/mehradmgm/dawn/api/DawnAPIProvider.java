package net.mehradmgm.dawn.api;

import org.bukkit.entity.Player;

public final class DawnAPIProvider
        implements DawnProvider {

    private final DawnManager manager;

    public DawnAPIProvider(
            DawnManager manager
    ) {
        this.manager = manager;
    }

    @Override
    public boolean isUsingDawn(Player player) {
        return manager.isUsingDawn(player);
    }

    @Override
    public DawnDetectionResult detect(Player player) {
        return manager.detect(player);
    }

    @Override
    public int getDawnUsersCount() {
        return manager.getDawnUsersCount();
    }

    @Override
    public DawnPlayer getDawnPlayer(Player player) {
        return manager.getDawnPlayer(player);
    }
}