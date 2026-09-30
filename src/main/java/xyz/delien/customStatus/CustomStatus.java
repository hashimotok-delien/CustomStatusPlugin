package xyz.delien.customStatus;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.delien.customStatus.listeners.PlayerJoinListener;
import xyz.delien.customStatus.utils.status.CStatus;
import xyz.mlserver.mc.util.CustomConfiguration;

public final class CustomStatus extends JavaPlugin {

    private static JavaPlugin plugin;

    private static CustomConfiguration config;

    public static CustomConfiguration getCustomConfig() {
        return config;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        config = new CustomConfiguration(this);
        config.saveDefaultConfig();

        CStatus.loadSetting();
        // Plugin startup logic
//        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
//            event.registrar().register(
//                    "menu",
//                    new MenuCommand()
//            );
//        });
//        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
