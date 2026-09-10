package net.mehradmgm.dawn.api;

import org.bukkit.entity.Player;

public interface DawnProvider {

    boolean isUsingDawn(Player player);

    DawnDetectionResult detect(Player player);

    int getDawnUsersCount();

    DawnPlayer getDawnPlayer(Player player);
}