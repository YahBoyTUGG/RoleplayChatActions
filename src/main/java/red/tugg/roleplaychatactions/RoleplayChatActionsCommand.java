package red.tugg.roleplaychatactions;

import com.Zrips.CMI.CMI;
import com.google.common.base.Joiner;
import de.myzelyam.api.vanish.VanishAPI;
import de.myzelyam.supervanish.SuperVanish;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import red.tugg.roleplaychatactions.RoleplayChatActions;

import java.util.*;

public class RoleplayChatActionsCommand implements CommandExecutor, TabCompleter {

    public RoleplayChatActions roleplayChatActions;

    public RoleplayChatActionsCommand(RoleplayChatActions roleplayChatActions) {
        this.roleplayChatActions = roleplayChatActions;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(command.getName().equalsIgnoreCase("rpca") || command.getName().equalsIgnoreCase("roleplaychatactions")) {
            FileConfiguration config = roleplayChatActions.getConfig();

            if(strings.length == 1 && strings[0].equalsIgnoreCase("reload") ||
                    strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
                if(!commandSender.hasPermission("rpca.admin")) {
                    commandSender.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("permission")));
                    return false;
                }

                if(strings[0].equalsIgnoreCase("reload")) {
                    roleplayChatActions.reloadConfig();
                    commandSender.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("reload")));
                } else if(strings[0].equalsIgnoreCase("list")) {
                    ConfigurationSection list = config.getConfigurationSection("actions");
                    Set<String> keys = list.getKeys(false);
                    String joinedKeys = String.join(", ", keys);
                    commandSender.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("list"),
                            Placeholder.parsed("actions", joinedKeys)));
                }
                return true;
            }

            if(!(commandSender instanceof Player)) {
                commandSender.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("console")));
                return false;
            }

            Player targetPlayer = null;
            String action = strings[0];

            if(!config.contains("actions." + action)) {
                commandSender.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("action")));
                return false;
            }

            if(!commandSender.hasPermission("actions." + action + ".permission")){
                commandSender.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("permission")));
                return false;
            }

            if(strings.length == 2) {
                targetPlayer = Bukkit.getPlayerExact(strings[1]);
                if (targetPlayer == null) {
                    commandSender.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("player")));
                    return false;
                }
            }

            if(config.getBoolean("actions." + action + ".broadcast.enabled")) {
                if (targetPlayer != null) {
                    Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize(randomString(action, "broadcast.public"),
                            Placeholder.parsed("player1", commandSender.getName()),
                            Placeholder.parsed("player2", targetPlayer.getName())));

                    targetPlayer.sendMessage(MiniMessage.miniMessage().deserialize(randomString(action, "message.target"),
                            Placeholder.parsed("player", commandSender.getName())));

                    commandSender.sendMessage(MiniMessage.miniMessage().deserialize(randomString(action, "message.self"),
                            Placeholder.parsed("player", targetPlayer.getName())));
                } else {
                    Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize(randomString(action, "broadcast.self"),
                            Placeholder.parsed("player", commandSender.getName())));
                }
            } else {
                if (targetPlayer != null) {
                    targetPlayer.sendMessage(MiniMessage.miniMessage().deserialize(randomString(action, "message.target"),
                            Placeholder.parsed("player", commandSender.getName())));

                    commandSender.sendMessage(MiniMessage.miniMessage().deserialize(randomString(action, "message.self"),
                            Placeholder.parsed("player", targetPlayer.getName())));
                } else {
                    commandSender.sendMessage(MiniMessage.miniMessage().deserialize(randomString(action, "self")));
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        Player player = (Player) commandSender;

        if (!player.hasPermission("rpca.admin")) {
            if (strings.length == 2 && !strings[0].equalsIgnoreCase("reload")
                    && !strings[0].equalsIgnoreCase("list")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    completions.add(onlinePlayer.getName());
                }
            }
        }

        completions.add("reload");
        completions.add("list");
        return completions;
    }

    public String randomString(String action, String path) {
        FileConfiguration config = roleplayChatActions.getConfig();

        List<String> messageList = config.getStringList("actions." + action + "." + path);

        Random random = new Random();
        int randomIndex = random.nextInt(messageList.size());

        return messageList.get(randomIndex);
    }
}
