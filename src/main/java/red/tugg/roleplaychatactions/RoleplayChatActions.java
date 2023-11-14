package red.tugg.roleplaychatactions;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RoleplayChatActions extends JavaPlugin {

    public RoleplayChatActionsCommand roleplayChatActionsCommand;

    /*
    *
    * CMI & Vanish support
    *
    */


    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        this.roleplayChatActionsCommand = new RoleplayChatActionsCommand(this);

        getCommand("rpca").setExecutor(this.roleplayChatActionsCommand);
        getCommand("roleplaychatactions").setExecutor(this.roleplayChatActionsCommand);

        getCommand("rpca").setTabCompleter(this.roleplayChatActionsCommand);
        getCommand("roleplaychatactions").setTabCompleter(this.roleplayChatActionsCommand);

        Bukkit.getServer().getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>Plugin loaded :>"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
