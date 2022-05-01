package jp.fukumai.scoreboardtool.util;

import jp.fukumai.scoreboardtool.ScoreboardTool;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandManager implements CommandExecutor, TabCompleter {
    ScoreboardTool main;
    public CommandManager (ScoreboardTool plugin) {
        main = plugin;
        Objects.requireNonNull(main.getCommand("scoreboardtool")).setExecutor(this);
        Objects.requireNonNull(main.getCommand("userinteract")).setExecutor(this);
    }
    public HashMap<String[], Predicate<CommandExecutedInfo>> Commands = new HashMap<>();
    public HashMap<String[], Predicate<CommandExecutedInfo>> Interacts = new HashMap<>();

    public void addSubCommand(String[] args, Predicate<CommandExecutedInfo> lambda) {
        Commands.put(args, lambda);
    }
    public void addUserInteract(String[] args, Predicate<CommandExecutedInfo> lambda) { Interacts.put(args, lambda); }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("userinteract")) {
            return Interacts.entrySet().stream()
                    .filter(v -> Arrays.equals(args, v.getKey()))
                    .findFirst()
                    .map(v -> v.getValue().test(new CommandExecutedInfo(sender, args)))
                    .orElse(false);
        }
        return Commands.entrySet().stream()
                .filter(v -> {
                    for (int i = 0; i < v.getKey().length; i++) {
                        if ( !v.getKey()[i].equals(args[i]) ) return false;
                    }
                    return true;
                }).findFirst()
                .map(v -> {
                    if (v.getValue() == null) {
                        sender.sendMessage(
                                ScoreboardTool.MESSAGE_PREFIX + ChatColor.RED + "This command not implemented."); return false;
                    } else {
                        return v.getValue().test(new CommandExecutedInfo(sender, args));
                    }
                })
                .orElseGet(() -> {
                    sender.sendMessage(ScoreboardTool.MESSAGE_PREFIX + ChatColor.RED + "Subcommand not found."); return false;
                });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completes = Commands.keySet().stream()
                .filter(v -> v.length >= args.length)
                .filter(v ->
                                Arrays.stream(v).limit(args.length - 1).collect(Collectors.toList()).equals(
                                        Arrays.stream(args).limit(args.length - 1).collect(Collectors.toList())
                                )
                )
                .map(v -> v[args.length - 1])
                .collect(Collectors.toList());
        return completes.isEmpty() ? main.onTabComplete(sender, command, alias, args) : completes;
    }
}
