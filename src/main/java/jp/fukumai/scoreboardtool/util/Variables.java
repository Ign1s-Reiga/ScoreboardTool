package jp.fukumai.scoreboardtool.util;

public enum Variables {
    PLAYER_NAME("%player_name%"),
    PLAYER_DISPLAY_NAME("%player_display_name%");

    public final String variable;

    Variables(String variable) {
        this.variable = variable;
    }
}
