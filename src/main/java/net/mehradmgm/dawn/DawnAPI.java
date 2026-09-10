package net.mehradmgm.dawn;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.mehradmgm.dawn.api.DawnManager;
import net.mehradmgm.dawn.api.DawnPlayer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

public final class DawnAPI {

    private static DawnManager manager;

    private DawnAPI() {
    }

    /*
     * =========================================================
     * INITIALIZATION
     * =========================================================
     */

    public static void initialize(
            Dawn plugin,
            DawnManager dawnManager
    ) {
        manager = dawnManager;
    }

    public static void shutdown() {
        manager = null;
    }

    public static boolean isInitialized() {
        return manager != null;
    }

    /*
     * =========================================================
     * DAWN DETECTION
     * =========================================================
     */

    public static boolean isUsingDawn(Player player) {

        if (player == null || manager == null) {
            return false;
        }

        return manager.isUsingDawn(player);
    }

    public static DawnPlayer getPlayer(Player player) {

        if (player == null || manager == null) {
            return null;
        }

        return manager.getDawnPlayer(player);
    }

    public static int getDawnUsersCount() {

        if (manager == null) {
            return 0;
        }

        return manager.getDawnUsersCount();
    }

    public static Collection<DawnPlayer> getDawnPlayers() {

        if (manager == null) {
            return Collections.emptyList();
        }

        return manager.getDawnPlayers();
    }

    public static DawnManager getManager() {
        return manager;
    }

    /*
     * =========================================================
     * CONSOLE COMMAND API
     * =========================================================
     *
     * Execute any server command as Console.
     *
     * Examples:
     *
     * DawnAPI.console("say Hello");
     * DawnAPI.console("noob Player");
     * DawnAPI.console("ban Player reason");
     * DawnAPI.console("lp user Player parent add vip");
     *
     * "/" is optional.
     */

    public static boolean console(String command) {

        if (command == null || command.isBlank()) {
            return false;
        }

        command = removeSlash(command);

        return Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                command
        );
    }

    public static boolean console(
            String command,
            Object... arguments
    ) {

        if (command == null || command.isBlank()) {
            return false;
        }

        command = removeSlash(command);

        if (arguments != null) {
            command = String.format(command, arguments);
        }

        return Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                command
        );
    }

    /*
     * =========================================================
     * PLAYER COMMAND API
     * =========================================================
     */

    public static boolean command(
            Player player,
            String command
    ) {

        if (player == null ||
                command == null ||
                command.isBlank()) {

            return false;
        }

        command = removeSlash(command);

        return player.performCommand(command);
    }

    public static boolean command(
            Player player,
            String command,
            Object... arguments
    ) {

        if (player == null ||
                command == null ||
                command.isBlank()) {

            return false;
        }

        command = removeSlash(command);

        if (arguments != null) {
            command = String.format(command, arguments);
        }

        return player.performCommand(command);
    }

    /*
     * =========================================================
     * UNIVERSAL EXECUTION
     * =========================================================
     */

    public static boolean execute(String command) {
        return console(command);
    }

    public static boolean execute(
            Player player,
            String command
    ) {
        return command(player, command);
    }

    /*
     * =========================================================
     * GAMEMODE
     * =========================================================
     */

    public static boolean gamemode(
            Player player,
            GameMode gameMode
    ) {

        if (player == null || gameMode == null) {
            return false;
        }

        player.setGameMode(gameMode);

        return true;
    }

    public static boolean gamemode(
            Player player,
            String gameMode
    ) {

        if (player == null || gameMode == null) {
            return false;
        }

        try {

            GameMode mode =
                    GameMode.valueOf(
                            gameMode.toUpperCase()
                    );

            return gamemode(player, mode);

        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public static boolean survival(Player player) {
        return gamemode(
                player,
                GameMode.SURVIVAL
        );
    }

    public static boolean creative(Player player) {
        return gamemode(
                player,
                GameMode.CREATIVE
        );
    }

    public static boolean adventure(Player player) {
        return gamemode(
                player,
                GameMode.ADVENTURE
        );
    }

    public static boolean spectator(Player player) {
        return gamemode(
                player,
                GameMode.SPECTATOR
        );
    }

    /*
     * =========================================================
     * KICK
     * =========================================================
     */

    public static boolean kick(
            Player player,
            String reason
    ) {

        if (player == null) {
            return false;
        }

        if (reason == null) {
            reason = "Kicked by Dawn";
        }

        player.kickPlayer(reason);

        return true;
    }

    public static boolean kick(Player player) {
        return kick(
                player,
                "Kicked by Dawn"
        );
    }

    /*
     * =========================================================
     * BAN
     * =========================================================
     */

    public static boolean ban(
            Player player,
            String reason
    ) {

        if (player == null) {
            return false;
        }

        return ban(
                player.getName(),
                reason
        );
    }

    public static boolean ban(
            String playerName,
            String reason
    ) {

        if (playerName == null ||
                playerName.isBlank()) {

            return false;
        }

        if (reason == null) {
            reason = "Banned by Dawn";
        }

        Bukkit.getBanList(
                BanList.Type.NAME
        ).addBan(
                playerName,
                reason,
                null,
                "Dawn"
        );

        Player player =
                Bukkit.getPlayerExact(playerName);

        if (player != null) {
            player.kickPlayer(reason);
        }

        return true;
    }

    public static boolean unban(
            String playerName
    ) {

        if (playerName == null ||
                playerName.isBlank()) {

            return false;
        }

        Bukkit.getBanList(
                BanList.Type.NAME
        ).pardon(playerName);

        return true;
    }

    /*
     * =========================================================
     * EFFECTS
     * =========================================================
     */

    public static boolean effect(
            Player player,
            PotionEffectType type,
            int duration,
            int amplifier
    ) {

        if (player == null ||
                type == null ||
                duration <= 0 ||
                amplifier < 0) {

            return false;
        }

        player.addPotionEffect(
                new PotionEffect(
                        type,
                        duration,
                        amplifier
                )
        );

        return true;
    }

    public static boolean effect(
            Player player,
            String effect,
            int duration,
            int amplifier
    ) {

        if (player == null ||
                effect == null) {

            return false;
        }

        PotionEffectType type =
                PotionEffectType.getByName(
                        effect.toUpperCase()
                );

        if (type == null) {
            return false;
        }

        return effect(
                player,
                type,
                duration,
                amplifier
        );
    }

    public static boolean removeEffect(
            Player player,
            PotionEffectType type
    ) {

        if (player == null || type == null) {
            return false;
        }

        player.removePotionEffect(type);

        return true;
    }

    public static boolean clearEffects(
            Player player
    ) {

        if (player == null) {
            return false;
        }

        for (PotionEffect effect :
                player.getActivePotionEffects()) {

            player.removePotionEffect(
                    effect.getType()
            );
        }

        return true;
    }

    /*
     * =========================================================
     * INVENTORY
     * =========================================================
     */

    public static boolean give(
            Player player,
            ItemStack item
    ) {

        if (player == null || item == null) {
            return false;
        }

        player.getInventory().addItem(item);

        return true;
    }

    public static boolean give(
            Player player,
            String material,
            int amount
    ) {

        if (player == null ||
                material == null ||
                material.isBlank() ||
                amount <= 0) {

            return false;
        }

        return console(
                "give %s %s %d",
                player.getName(),
                material,
                amount
        );
    }

    public static void clearInventory(
            Player player
    ) {

        if (player == null) {
            return;
        }

        player.getInventory().clear();
    }

    /*
     * =========================================================
     * TELEPORT
     * =========================================================
     */

    public static boolean teleport(
            Player player,
            Location location
    ) {

        if (player == null ||
                location == null) {

            return false;
        }

        return player.teleport(location);
    }

    public static boolean teleport(
            Player player,
            Player target
    ) {

        if (player == null ||
                target == null) {

            return false;
        }

        return player.teleport(
                target.getLocation()
        );
    }

    /*
     * =========================================================
     * HEALTH
     * =========================================================
     */

    public static boolean health(
            Player player,
            double amount
    ) {

        if (player == null) {
            return false;
        }

        double max =
                player.getMaxHealth();

        amount = Math.max(
                0,
                Math.min(amount, max)
        );

        player.setHealth(amount);

        return true;
    }

    public static boolean heal(
            Player player
    ) {

        if (player == null) {
            return false;
        }

        player.setHealth(
                player.getMaxHealth()
        );

        return true;
    }

    public static boolean kill(
            Player player
    ) {

        if (player == null) {
            return false;
        }

        player.setHealth(0);

        return true;
    }

    /*
     * =========================================================
     * FOOD
     * =========================================================
     */

    public static boolean food(
            Player player,
            int amount
    ) {

        if (player == null) {
            return false;
        }

        amount = Math.max(
                0,
                Math.min(amount, 20)
        );

        player.setFoodLevel(amount);

        return true;
    }

    public static boolean feed(
            Player player
    ) {
        return food(player, 20);
    }

    /*
     * =========================================================
     * EXPERIENCE
     * =========================================================
     */

    public static boolean experience(
            Player player,
            int amount
    ) {

        if (player == null) {
            return false;
        }

        player.giveExp(amount);

        return true;
    }

    public static boolean levels(
            Player player,
            int amount
    ) {

        if (player == null) {
            return false;
        }

        player.giveExpLevels(amount);

        return true;
    }

    /*
     * =========================================================
     * MESSAGES
     * =========================================================
     */

    public static boolean message(
            Player player,
            String message
    ) {

        if (player == null ||
                message == null) {

            return false;
        }

        player.sendMessage(message);

        return true;
    }

    public static void broadcast(
            String message
    ) {

        if (message == null) {
            return;
        }

        Bukkit.broadcastMessage(message);
    }

    /*
     * =========================================================
     * PLAYER STATE
     * =========================================================
     */

    public static boolean allowFlight(
            Player player,
            boolean value
    ) {

        if (player == null) {
            return false;
        }

        player.setAllowFlight(value);

        return true;
    }

    public static boolean flying(
            Player player,
            boolean value
    ) {

        if (player == null) {
            return false;
        }

        player.setFlying(value);

        return true;
    }

    public static boolean invulnerable(
            Player player,
            boolean value
    ) {

        if (player == null) {
            return false;
        }

        player.setInvulnerable(value);

        return true;
    }

    public static boolean invisible(
            Player player,
            boolean value
    ) {

        if (player == null) {
            return false;
        }

        player.setInvisible(value);

        return true;
    }

    /*
     * =========================================================
     * FIRE / FREEZE / AIR
     * =========================================================
     */

    public static boolean fire(
            Player player,
            int ticks
    ) {

        if (player == null) {
            return false;
        }

        player.setFireTicks(
                Math.max(0, ticks)
        );

        return true;
    }

    public static boolean extinguish(
            Player player
    ) {
        return fire(player, 0);
    }

    public static boolean freeze(
            Player player,
            int ticks
    ) {

        if (player == null) {
            return false;
        }

        player.setFreezeTicks(
                Math.max(0, ticks)
        );

        return true;
    }

    public static boolean air(
            Player player,
            int amount
    ) {

        if (player == null) {
            return false;
        }

        player.setRemainingAir(amount);

        return true;
    }

    /*
     * =========================================================
     * OP
     * =========================================================
     */

    public static boolean op(
            Player player
    ) {

        if (player == null) {
            return false;
        }

        player.setOp(true);

        return true;
    }

    public static boolean deop(
            Player player
    ) {

        if (player == null) {
            return false;
        }

        player.setOp(false);

        return true;
    }

    /*
     * =========================================================
     * ENTITY
     * =========================================================
     */

    public static boolean removeEntity(
            Entity entity
    ) {

        if (entity == null) {
            return false;
        }

        entity.remove();

        return true;
    }

    /*
     * =========================================================
     * PLAYER LOOKUP
     * =========================================================
     */

    public static Player getPlayer(
            String name
    ) {

        if (name == null ||
                name.isBlank()) {

            return null;
        }

        return Bukkit.getPlayerExact(name);
    }

    public static Player getPlayer(
            UUID uuid
    ) {

        if (uuid == null) {
            return null;
        }

        return Bukkit.getPlayer(uuid);
    }

    /*
     * =========================================================
     * ONLINE PLAYERS
     * =========================================================
     */

    public static Collection<? extends Player>
    getOnlinePlayers() {

        return Bukkit.getOnlinePlayers();
    }

    public static int getOnlinePlayersCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    /*
     * =========================================================
     * FOLIA / PAPER GLOBAL SCHEDULER
     * =========================================================
     *
     * 20 ticks = 1 second
     *
     * These methods use Paper's/Folia's
     * GlobalRegionScheduler instead of BukkitScheduler.
     */

    public static ScheduledTask runTask(
            Runnable task
    ) {

        if (task == null ||
                Dawn.getInstance() == null) {

            return null;
        }

        return Bukkit.getGlobalRegionScheduler().run(
                Dawn.getInstance(),
                scheduledTask -> task.run()
        );
    }

    public static ScheduledTask runTaskLater(
            Runnable task,
            long delayTicks
    ) {

        if (task == null ||
                Dawn.getInstance() == null) {

            return null;
        }

        return Bukkit.getGlobalRegionScheduler().runDelayed(
                Dawn.getInstance(),
                scheduledTask -> task.run(),
                Math.max(1L, delayTicks)
        );
    }

    public static ScheduledTask runTaskTimer(
            Runnable task,
            long initialDelayTicks,
            long periodTicks
    ) {

        if (task == null ||
                Dawn.getInstance() == null) {

            return null;
        }

        if (periodTicks <= 0) {
            throw new IllegalArgumentException(
                    "periodTicks must be greater than 0"
            );
        }

        return Bukkit.getGlobalRegionScheduler()
                .runAtFixedRate(
                        Dawn.getInstance(),
                        scheduledTask -> task.run(),
                        Math.max(1L, initialDelayTicks),
                        periodTicks
                );
    }

    /*
     * =========================================================
     * FOLIA / PAPER GLOBAL SCHEDULER WITH TASK
     * =========================================================
     */

    public static ScheduledTask runTask(
            Consumer<ScheduledTask> task
    ) {

        if (task == null ||
                Dawn.getInstance() == null) {

            return null;
        }

        return Bukkit.getGlobalRegionScheduler().run(
                Dawn.getInstance(),
                task
        );
    }

    public static ScheduledTask runTaskLater(
            Consumer<ScheduledTask> task,
            long delayTicks
    ) {

        if (task == null ||
                Dawn.getInstance() == null) {

            return null;
        }

        return Bukkit.getGlobalRegionScheduler().runDelayed(
                Dawn.getInstance(),
                task,
                Math.max(1L, delayTicks)
        );
    }

    public static ScheduledTask runTaskTimer(
            Consumer<ScheduledTask> task,
            long initialDelayTicks,
            long periodTicks
    ) {

        if (task == null ||
                Dawn.getInstance() == null) {

            return null;
        }

        if (periodTicks <= 0) {
            throw new IllegalArgumentException(
                    "periodTicks must be greater than 0"
            );
        }

        return Bukkit.getGlobalRegionScheduler()
                .runAtFixedRate(
                        Dawn.getInstance(),
                        task,
                        Math.max(1L, initialDelayTicks),
                        periodTicks
                );
    }

    /*
     * =========================================================
     * FOLIA ENTITY / PLAYER SCHEDULER
     * =========================================================
     *
     * These are especially useful when the operation
     * modifies a Player.
     *
     * Example:
     *
     * DawnAPI.runPlayerTask(player, () -> {
     *     player.sendMessage("Hello!");
     * });
     */

    public static ScheduledTask runPlayerTask(
            Player player,
            Runnable task
    ) {

        if (player == null || task == null) {
            return null;
        }

        return player.getScheduler().run(
                Dawn.getInstance(),
                scheduledTask -> task.run(),
                null
        );
    }

    public static ScheduledTask runPlayerTaskLater(
            Player player,
            Runnable task,
            long delayTicks
    ) {

        if (player == null || task == null) {
            return null;
        }

        return player.getScheduler().runDelayed(
                Dawn.getInstance(),
                scheduledTask -> task.run(),
                null,
                Math.max(1L, delayTicks)
        );
    }

    public static ScheduledTask runPlayerTaskTimer(
            Player player,
            Runnable task,
            long initialDelayTicks,
            long periodTicks
    ) {

        if (player == null || task == null) {
            return null;
        }

        if (periodTicks <= 0) {
            throw new IllegalArgumentException(
                    "periodTicks must be greater than 0"
            );
        }

        return player.getScheduler().runAtFixedRate(
                Dawn.getInstance(),
                scheduledTask -> task.run(),
                null,
                Math.max(1L, initialDelayTicks),
                periodTicks
        );
    }

    /*
     * =========================================================
     * PLAYER SCHEDULER WITH SCHEDULED TASK
     * =========================================================
     */

    public static ScheduledTask runPlayerTask(
            Player player,
            Consumer<ScheduledTask> task
    ) {

        if (player == null || task == null) {
            return null;
        }

        return player.getScheduler().run(
                Dawn.getInstance(),
                task,
                null
        );
    }

    public static ScheduledTask runPlayerTaskLater(
            Player player,
            Consumer<ScheduledTask> task,
            long delayTicks
    ) {

        if (player == null || task == null) {
            return null;
        }

        return player.getScheduler().runDelayed(
                Dawn.getInstance(),
                task,
                null,
                Math.max(1L, delayTicks)
        );
    }

    public static ScheduledTask runPlayerTaskTimer(
            Player player,
            Consumer<ScheduledTask> task,
            long initialDelayTicks,
            long periodTicks
    ) {

        if (player == null || task == null) {
            return null;
        }

        if (periodTicks <= 0) {
            throw new IllegalArgumentException(
                    "periodTicks must be greater than 0"
            );
        }

        return player.getScheduler().runAtFixedRate(
                Dawn.getInstance(),
                task,
                null,
                Math.max(1L, initialDelayTicks),
                periodTicks
        );
    }

    /*
     * =========================================================
     * INTERNAL
     * =========================================================
     */

    private static String removeSlash(
            String command
    ) {

        command = command.trim();

        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        return command;
    }
}