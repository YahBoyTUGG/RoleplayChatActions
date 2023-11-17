package red.tugg.roleplaychatactions;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RoleplayChatActions extends JavaPlugin {

    public RoleplayChatActionsCommand roleplayChatActionsCommand;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        this.roleplayChatActionsCommand = new RoleplayChatActionsCommand(this);

        getCommand("roleplaychatactions").setExecutor(this.roleplayChatActionsCommand);
        getCommand("rpca").setExecutor(this.roleplayChatActionsCommand);
        getCommand("ca").setExecutor(this.roleplayChatActionsCommand);

        getCommand("roleplaychatactions").setTabCompleter(this.roleplayChatActionsCommand);
        getCommand("rpca").setTabCompleter(this.roleplayChatActionsCommand);
        getCommand("ca").setTabCompleter(this.roleplayChatActionsCommand);

        Bukkit.getServer().getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<bold><dark_gray>[<blue>RPCA<dark_gray>] <reset><white> The plugin has been enabled!"));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
