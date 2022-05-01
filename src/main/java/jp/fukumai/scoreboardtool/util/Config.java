package jp.fukumai.scoreboardtool.util;

import jp.fukumai.scoreboardtool.ScoreboardTool;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private final ScoreboardTool plugin;
    public FileConfiguration config;

    public Config(ScoreboardTool plugin) {
        this.plugin = plugin;
    }

    public void reloadConfig() {
        this.plugin.saveDefaultConfig();
        if (config != null)
            this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
    }
}
