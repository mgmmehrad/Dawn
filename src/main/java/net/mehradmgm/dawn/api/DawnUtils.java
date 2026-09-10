package net.mehradmgm.dawn.api;

import org.bukkit.entity.Player;

public final class DawnUtils {

    private DawnUtils() {
    }

    public static String getClientBrand(Player player) {

        if (player == null) {
            return "unknown";
        }

        String brand =
                player.getClientBrandName();

        if (brand == null ||
                brand.isBlank()) {

            return "unknown";
        }

        return brand;
    }

    public static DawnClient detectClient(
            String brand
    ) {

        if (brand == null ||
                brand.isBlank()) {

            return DawnClient.UNKNOWN;
        }

        String normalized =
                brand.toLowerCase();

        if (normalized.contains("dawn")) {
            return DawnClient.DAWN;
        }

        /*
         * Dawn is the successor to Feather Client.
         * Older Dawn/transition versions may still
         * report Feather-related branding.
         */
        if (normalized.contains("feather")) {
            return DawnClient.FEATHER;
        }

        return DawnClient.OTHER;
    }

    public static boolean isDawnBrand(
            String brand
    ) {

        DawnClient client =
                detectClient(brand);

        return client == DawnClient.DAWN ||
                client == DawnClient.FEATHER;
    }
}