package net.mehradmgm.dawn.api;

import net.mehradmgm.dawn.Dawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class DawnManager
        implements CommandExecutor, TabCompleter {

    private final Dawn plugin;

    private final Map<UUID, DawnPlayer> dawnPlayers =
            new ConcurrentHashMap<>();

    private final List<DawnEventListener> eventListeners =
            new ArrayList<>();

    public DawnManager(Dawn plugin) {
        this.plugin = plugin;
    }

    public DawnDetectionResult detect(Player player) {

        String brand = DawnUtils.getClientBrand(player);

        DawnClient client =
                DawnUtils.detectClient(brand);

        boolean usingDawn =
                client == DawnClient.DAWN ||
                        client == DawnClient.FEATHER;

        return new DawnDetectionResult(
                player,
                brand,
                client,
                usingDawn
        );
    }

    public void addPlayer(Player player) {

        DawnDetectionResult result =
                detect(player);

        DawnPlayer dawnPlayer =
                new DawnPlayer(
                        player,
                        result.getClientBrand(),
                        result.getClient()
                );

        dawnPlayers.put(
                player.getUniqueId(),
                dawnPlayer
        );
    }

    public void removePlayer(Player player) {

        dawnPlayers.remove(
                player.getUniqueId()
        );
    }

    public boolean isUsingDawn(Player player) {

        return dawnPlayers.containsKey(
                player.getUniqueId()
        );
    }

    public DawnPlayer getDawnPlayer(Player player) {

        return dawnPlayers.get(
                player.getUniqueId()
        );
    }

    public int getDawnUsersCount() {

        return dawnPlayers.size();
    }

    public Collection<DawnPlayer> getDawnPlayers() {

        return Collections.unmodifiableCollection(
                dawnPlayers.values()
        );
    }

    public List<DawnEventListener> getEventListeners() {

        return Collections.unmodifiableList(
                eventListeners
        );
    }

    public void addEventListener(
            DawnEventListener listener
    ) {

        if (listener != null) {
            eventListeners.add(listener);
        }
    }

    public void removeEventListener(
            DawnEventListener listener
    ) {

        eventListeners.remove(listener);
    }

    public void shutdown() {

        dawnPlayers.clear();
        eventListeners.clear();
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length == 0) {

            sender.sendMessage(
                    ChatColor.GOLD +
                            "Dawn Detector"
            );

            sender.sendMessage(
                    ChatColor.GRAY +
                            "Dawn users online: " +
                            ChatColor.WHITE +
                            getDawnUsersCount()
            );

            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            plugin.reloadDawnConfig();

            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            plugin.getConfig().getString(
                                    "Dawn-reloaded-message",
                                    "&aDawn has been reloaded"
                            )
                    )
            );

            return true;
        }

        if (args[0].equalsIgnoreCase("lists")) {

            if (dawnPlayers.isEmpty()) {

                sender.sendMessage(
                        ChatColor.YELLOW +
                                "There are no Dawn players online."
                );

                return true;
            }

            sender.sendMessage(
                    ChatColor.GOLD +
                            "Dawn Players:"
            );

            for (DawnPlayer player :
                    dawnPlayers.values()) {

                sender.sendMessage(
                        ChatColor.GRAY +
                                "- " +
                                ChatColor.WHITE +
                                player.getName()
                );
            }

            return true;
        }

        sender.sendMessage(
                ChatColor.RED +
                        "Usage: /dawn <lists|reload>"
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (args.length == 1) {

            List<String> result =
                    new ArrayList<>();

            result.add("lists");
            result.add("reload");

            String input =
                    args[0].toLowerCase();

            result.removeIf(
                    value -> !value
                            .startsWith(input)
            );

            return result;
        }

        return Collections.emptyList();
    }
}