package jp.fukumai.scoreboardtool;

import jp.fukumai.scoreboardtool.util.CommandManager;
import jp.fukumai.scoreboardtool.util.Config;
import jp.fukumai.scoreboardtool.util.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ScoreboardTool extends JavaPlugin implements Listener {
    public static final String MESSAGE_PREFIX = ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + " ScoreBoardTool " + ChatColor.GOLD + "]" + ChatColor.RESET + " ";
    private ScoreboardSetting setting;
    public Map<String, Integer> items = new HashMap<>();
    public String displayName;
    public long updateInterval;
    public String escapeSequence;

    @Override
    public void onEnable() {
        Config config = new Config(this);
        config.reloadConfig();
        this.updateInterval = config.config.getLong("updateInterval");
        this.escapeSequence = config.config.getString("escapeSequence");

        this.setting = new ScoreboardSetting(this);
        try {
            setting.loadScoreboardSetting();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommandManager cm = new CommandManager(this);
        cm.addSubCommand(new String[]{"create"}, p -> {
            Scoreboard sb = getServer().getScoreboardManager().getNewScoreboard();
            if (sb.getObjective("sbt") != null) return false;
            Objective obj = sb.registerNewObjective("sbt", "", "Scoreboard");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            items.put("Default", 1);
            obj.getScore("Default").setScore(1);
            return true;
        });
        cm.addSubCommand(new String[]{"changeTitle"}, p -> {
            if (p.args[0] == null) return false;
            String title = p.args[0];
            title.replace("/s", " ");

            displayName = title;
            Objective obj = getServer().getScoreboardManager().getNewScoreboard().getObjective("sbt");
            obj.setDisplayName(title);

            p.sender.sendMessage(MESSAGE_PREFIX + "Changed Sidebar Title.");
            return true;
        });
        cm.addSubCommand(new String[]{"append"}, p -> {
            if (p.args[0] == null) return false;
            String entry = p.args[0];
            entry.replace("/s", " ");

            Objective obj = getServer().getScoreboardManager().getNewScoreboard().getObjective("sbt");
            obj.getScore(entry).setScore(getMinScore() - 1);
            items.put(entry, getMinScore() - 1);
            p.sender.sendMessage(MESSAGE_PREFIX + "Appended Score to Sidebar.");
            return true;
        });
        cm.addSubCommand(new String[]{"insert"}, p -> {
            return true;
        });
        cm.addSubCommand(new String[]{"remove"}, p -> {
            return true;
        });
        cm.addSubCommand(new String[]{"pop"}, p -> {
            int willPopColumn = p.args.length == 0 ? getMinScore() : Integer.parseInt(p.args[1]);
            Scoreboard sb = getServer().getScoreboardManager().getNewScoreboard();
            sb.resetScores(getEntryByScore(willPopColumn));

            p.sender.sendMessage(MESSAGE_PREFIX + "Popped selected column.");
            return true;
        });
        cm.addSubCommand(new String[]{"clear"}, p -> {
            List<String> list = items.values().stream()
                    .map(this::getEntryByScore)
                    .collect(Collectors.toList());
            Scoreboard sb = getServer().getScoreboardManager().getNewScoreboard();
            list.forEach(sb::resetScores);

            p.sender.sendMessage(MESSAGE_PREFIX + "Reset Sidebar scores.");
            return true;
        });
        cm.addSubCommand(new String[]{"reload"}, p -> {
            try {
                this.setting.reloadScoreboardSetting();
                Bukkit.getOnlinePlayers().forEach(pl -> items.forEach((k, v) -> registerScore(k, v, pl)));
                p.sender.sendMessage(MESSAGE_PREFIX + "Reloaded Sidebar setting.");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sender.sendMessage(MESSAGE_PREFIX + "Failed to reload Sidebar setting.");
            return false;
        });

        PlayerObserver observer = new PlayerObserver(this);
        observer.runTaskTimer(this, 0L, 1L);
        getServer().getPluginManager().registerEvents(observer, this);
    }

    @Override
    public void onDisable() {
        try {
            this.setting.saveScoreboardSetting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!items.isEmpty())
            items.forEach((k, v) -> registerScore(k, v, e.getPlayer()));
    }

    private int getMinScore() {
        return items.values().stream()
                .sorted(Integer::compareTo)
                .collect(Collectors.toList())
                .get(0);
    }

    private String getEntryByScore(int score) {
        return items.entrySet().stream()
                .filter(v -> v.getValue().equals(score))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private void registerScore(String entry, int score, Player player) {
        entry.replace(this.escapeSequence, " ");
        Objective obj = getServer().getScoreboardManager().getMainScoreboard().getObjective("sbt");
        if (containsVariables(entry))
            replaceEntryAndRegister(entry, score, player, obj);
        else
            obj.getScore(entry).setScore(score);
    }

    private boolean containsVariables(String entry) {
        return Arrays.stream(Variables.values()).anyMatch(v -> entry.contains(v.variable));
    }

    private void replaceEntryAndRegister(String entry, int score, Player player, Objective objective) {

    }
}
