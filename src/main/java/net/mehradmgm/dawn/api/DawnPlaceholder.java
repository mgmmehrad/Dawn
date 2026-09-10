package net.mehradmgm.dawn.api;

import net.mehradmgm.dawn.Dawn;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DawnPlaceholder
        extends PlaceholderExpansion {

    private final Dawn plugin;

    public DawnPlaceholder(Dawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "dawn";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MehradMGM";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription()
                .getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(
            Player player,
            @NotNull String params
    ) {

        DawnManager manager =
                plugin.getDawnManager();

        if (params.equalsIgnoreCase(
                "users_count"
        )) {

            return String.valueOf(
                    manager.getDawnUsersCount()
            );
        }

        if (params.equalsIgnoreCase(
                "using_dawn"
        )) {

            if (player == null) {
                return "false";
            }

            boolean using =
                    manager.isUsingDawn(player);

            String path = using
                    ? "Dawn-using-message"
                    : "Dawn-not-using-message";

            return plugin.getConfig()
                    .getString(path,
                            using
                                    ? "true"
                                    : "false");
        }

        return null;
    }
}