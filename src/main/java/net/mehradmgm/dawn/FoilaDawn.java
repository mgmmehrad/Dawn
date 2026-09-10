package net.mehradmgm.dawn;

import net.mehradmgm.dawn.api.DawnDetectionResult;
import net.mehradmgm.dawn.api.DawnEvent;
import net.mehradmgm.dawn.api.DawnEventListener;
import net.mehradmgm.dawn.api.DawnManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class FoilaDawn implements Listener {

    private final Dawn plugin;

    public FoilaDawn(Dawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        DawnManager manager = plugin.getDawnManager();

        DawnDetectionResult result =
                manager.detect(event.getPlayer());

        if (result.isUsingDawn()) {
            manager.addPlayer(event.getPlayer());

            DawnEvent dawnEvent =
                    new DawnEvent(
                            event.getPlayer(),
                            result
                    );

            for (DawnEventListener listener :
                    manager.getEventListeners()) {

                listener.onDawnDetected(dawnEvent);
            }

        } else {
            manager.removePlayer(event.getPlayer());
        }
    }
}