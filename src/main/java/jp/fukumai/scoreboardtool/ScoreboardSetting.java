package jp.fukumai.scoreboardtool;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ScoreboardSetting {
    private final ScoreboardTool plugin;
    private final File dataFolder;
    private final File sbtSettingFile;

    public ScoreboardSetting(ScoreboardTool plugin) {
        this.plugin = plugin;
        this.dataFolder = this.plugin.getDataFolder();
        this.sbtSettingFile = new File(dataFolder + File.separator + "setting.json");
    }

    public void loadScoreboardSetting() throws IOException {
        if (!dataFolder.exists())
            dataFolder.mkdir();
        if (!sbtSettingFile.exists()) return;
        ScoreboardItems obj = new Gson().fromJson(new JsonReader(new FileReader(sbtSettingFile)), ScoreboardItems.class);
        this.plugin.displayName = obj.displayName;
        this.plugin.items.putAll(obj.items);

        this.plugin.getServer().getScoreboardManager().getNewScoreboard().registerNewObjective("sbt", "", obj.displayName);
    }

    public void saveScoreboardSetting() throws IOException {
        if (!dataFolder.exists())
            dataFolder.mkdir();
        if (!sbtSettingFile.exists())
            sbtSettingFile.createNewFile();
        String str = new Gson().toJson(this.plugin.items);
        BufferedWriter writer = new BufferedWriter(new FileWriter(sbtSettingFile));
        writer.write(str);
        writer.close();
    }

    public void reloadScoreboardSetting() throws IOException {
        this.plugin.items.clear();
        loadScoreboardSetting();
    }

    class ScoreboardItems {
        private final String displayName;
        private final Map<String, Integer> items;

        public ScoreboardItems(String displayName, Map<String, Integer> items) {
            this.displayName = displayName;
            this.items = items;
        }
    }
}
