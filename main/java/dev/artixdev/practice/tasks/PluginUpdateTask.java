package dev.artixdev.practice.tasks;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.UpdateChecker;

public class PluginUpdateTask implements Runnable {

    public PluginUpdateTask() {
        super();
    }

    @Override
    public void run() {
        Main plugin = Main.getInstance();
        if (plugin != null) {
            new UpdateChecker(plugin).checkForUpdates();
        }
    }
}
