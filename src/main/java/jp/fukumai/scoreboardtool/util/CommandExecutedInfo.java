package jp.fukumai.scoreboardtool.util;

import org.bukkit.command.CommandSender;

public class CommandExecutedInfo {
    public CommandSender sender;
    public String[] args;
    public CommandExecutedInfo(CommandSender sender, String[] args) {
        this.sender = sender; this.args = args;
    }
}
