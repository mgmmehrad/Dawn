package net.mehradmgm.dawn.api;

@FunctionalInterface
public interface DawnEventListener {

    void onDawnDetected(DawnEvent event);
}