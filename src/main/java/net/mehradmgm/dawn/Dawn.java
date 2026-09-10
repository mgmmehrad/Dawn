package net.mehradmgm.dawn;

import net.mehradmgm.dawn.api.DawnManager;
import net.mehradmgm.dawn.api.DawnPlaceholder;
import net.mehradmgm.dawn.libs.bstats.Metrics.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class Dawn extends JavaPlugin {

    private static Dawn instance;
    private DawnManager dawnManager;

    private Metrics metrics;

    @Override
    public void onLoad() {

        /*
         * Dawn does not support Spigot/CraftBukkit.
         *
         * Paper/Folia detection:
         * - Folia has RegionizedServer
         * - Paper has Paper-specific classes
         *
         * If neither is found, the plugin will intentionally
         * throw an exception and refuse to load.
         */

        if (!isPaperOrFolia()) {
            throw new UnsupportedOperationException(
                    "Dawn does not support Spigot/CraftBukkit. " +
                            "Please use Paper or Folia."
            );
        }

        getLogger().info(
                "Detected supported server platform: Paper/Folia"
        );
    }

    @Override
    public void onEnable() {

        instance = this;

        /*
         * Create config.yml if it does not exist.
         */
        saveDefaultConfig();

        /*
         * Create the main Dawn manager.
         */
        dawnManager = new DawnManager(this);

        /*
         * Register player detection listener.
         */
        getServer().getPluginManager().registerEvents(
                new FoilaDawn(this),
                this
        );

        /*
         * Register /dawn command.
         */
        if (getCommand("dawn") != null) {

            getCommand("dawn").setExecutor(dawnManager);
            getCommand("dawn").setTabCompleter(dawnManager);

        } else {

            getLogger().severe(
                    "Command 'dawn' was not found in plugin.yml!"
            );
        }

        /*
         * Initialize public API.
         */
        DawnAPI.initialize(
                this,
                dawnManager
        );

        /*
         * Hook into PlaceholderAPI if installed.
         */
        if (getServer().getPluginManager()
                .getPlugin("PlaceholderAPI") != null) {

            new DawnPlaceholder(this).register();

            getLogger().info(
                    "PlaceholderAPI detected. Placeholders enabled."
            );
        }

        /*
         * Initialize bStats.
         *
         * Plugin ID: 33958
         */
        int pluginId = 33958;

        metrics = new Metrics(
                this,
                pluginId
        );

        /*
         * bStats custom chart:
         * Shows the current number of players
         * detected as Dawn users.
         */
        metrics.addCustomChart(
                new Metrics.SingleLineChart(
                        "dawn_users",
                        () -> DawnAPI.getDawnUsersCount()
                )
        );

        getLogger().info(
                "bStats metrics enabled."
        );

        /*
         * Plugin startup messages.
         */
        getLogger().info("--------------------------------");
        getLogger().info("Dawn Detector");
        getLogger().info("Version: " +
                getDescription().getVersion());
        getLogger().info("Dawn Client detection enabled.");
        getLogger().info("API enabled.");
        getLogger().info("bStats enabled.");
        getLogger().info("--------------------------------");
    }

    @Override
    public void onDisable() {

        /*
         * Shutdown bStats.
         */
        if (metrics != null) {
            metrics.shutdown();
            metrics = null;
        }

        /*
         * Clear manager data.
         */
        if (dawnManager != null) {
            dawnManager.shutdown();
            dawnManager = null;
        }

        /*
         * Shutdown public API.
         *
         * Do NOT call initialize() here.
         */
        DawnAPI.shutdown();

        instance = null;

        getLogger().info(
                "Dawn Detector has been disabled."
        );
    }

    /**
     * Detects whether the server is running
     * Paper or Folia.
     *
     * @return true if Paper/Folia is detected
     */
    private boolean isPaperOrFolia() {

        /*
         * Folia-specific class.
         */
        try {

            Class.forName(
                    "io.papermc.paper.threadedregions.RegionizedServer"
            );

            return true;

        } catch (ClassNotFoundException ignored) {
            // Not Folia.
        }

        /*
         * Paper-specific class.
         */
        try {

            Class.forName(
                    "io.papermc.paper.configuration.Configuration"
            );

            return true;

        } catch (ClassNotFoundException ignored) {
            // Not Paper.
        }

        /*
         * Additional Paper API check.
         */
        try {

            Class.forName(
                    "io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager"
            );

            return true;

        } catch (ClassNotFoundException ignored) {
            // Not Paper.
        }

        return false;
    }

    /**
     * Returns the singleton Dawn instance.
     *
     * @return Dawn plugin instance
     */
    public static Dawn getInstance() {
        return instance;
    }

    /**
     * Returns the Dawn manager.
     *
     * @return DawnManager
     */
    public DawnManager getDawnManager() {
        return dawnManager;
    }

    /**
     * Returns the bStats Metrics instance.
     *
     * @return Metrics
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Reloads Dawn configuration.
     */
    public void reloadDawnConfig() {
        reloadConfig();
    }
}