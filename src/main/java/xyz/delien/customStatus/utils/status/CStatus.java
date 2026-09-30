package xyz.delien.customStatus.utils.status;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import xyz.delien.customStatus.CustomStatus;
import xyz.mlserver.mc.util.CustomConfiguration;

public class CStatus {

    private static ScoreboardManager MANAGER = Bukkit.getScoreboardManager();
    private static Scoreboard BOARD = MANAGER.getMainScoreboard();

    private static boolean enableHP = true;
    private static int maxHP = 100;
    private static int minHP = 0;
    private static int defaultHP = maxHP;

    private static boolean enableArmor = true;
    private static int maxArmor = 100;
    private static int minArmor = 0;
    private static int defaultArmor = maxArmor;

    private static boolean enableFood = true;
    private static int maxFood = 100;
    private static int minFood = 0;
    private static int countdownPerMinuteFood = 1;
    private static int zeroDamageAmount = 1;
    private static int zeroDamageInterval = 20;
    private static int defaultFood = maxFood;

    private static boolean enableWetness = true;
    private static int maxWetness = 100;
    private static int minWetness = 0;
    private static int countdownPerMinuteWetness = 1;
    private static int zeroDamageAmountWetness = 1;
    private static int zeroDamageIntervalWetness = 20;
    private static int defaultWetness = maxWetness;

    public static void loadSetting() {
        CustomConfiguration config = CustomStatus.getCustomConfig();
        if (config.getConfig().getBoolean("custom-status.hp.enable", false)) {
            maxHP = config.getConfig().getInt("custom-status.hp.max", 100);
            minHP = config.getConfig().getInt("custom-status.hp.min", 0);
            defaultHP = config.getConfig().getInt("custom-status.hp.default", maxHP);
            Objective hp_obj = BOARD.getObjective("custom_hp");
            if (hp_obj == null) {
                hp_obj = BOARD.registerNewObjective(
                        "custom_hp",
                        Criteria.DUMMY,
                        Component.text("Custom HP")
                );
            }
        } else {
            enableHP = false;
        }
        if (config.getConfig().getBoolean("custom-status.armor.enable", false)) {
            maxArmor = config.getConfig().getInt("custom-status.armor.max", 100);
            minArmor = config.getConfig().getInt("custom-status.armor.min", 0);
            defaultArmor = config.getConfig().getInt("custom-status.armor.default", maxArmor);
            Objective armor_obj = BOARD.getObjective("custom_armor");
            if (armor_obj == null) {
                armor_obj = BOARD.registerNewObjective(
                        "custom_armor",
                        Criteria.DUMMY,
                        Component.text("Custom Armor")
                );
            }
        } else {
            enableArmor = false;
        }
        if (config.getConfig().getBoolean("custom-status.food.enable", false)) {
            maxFood = config.getConfig().getInt("custom-status.food.max", 100);
            minFood = config.getConfig().getInt("custom-status.food.min", 0);
            defaultFood = config.getConfig().getInt("custom-status.food.default", maxFood);
            countdownPerMinuteFood = config.getConfig().getInt("custom-status.food.countdown-per-minute", 1);
            zeroDamageAmount = config.getConfig().getInt("custom-status.food.zero-damage-amount", 1);
            zeroDamageInterval = config.getConfig().getInt("custom-status.food.zero-damage-interval", 20);

            Objective food_obj = BOARD.getObjective("custom_food");
            if (food_obj == null) {
                food_obj = BOARD.registerNewObjective(
                        "custom_food",
                        Criteria.DUMMY,
                        Component.text("Custom Food")
                );
            }
        } else {
            enableFood = false;
        }
        if (config.getConfig().getBoolean("custom-status.wetness.enable", false)) {
            maxWetness = config.getConfig().getInt("custom-status.wetness.max", 100);
            minWetness = config.getConfig().getInt("custom-status.wetness.min", 0);
            defaultWetness = config.getConfig().getInt("custom-status.wetness.default", maxWetness);
            countdownPerMinuteWetness = config.getConfig().getInt("custom-status.wetness.countdown-per-minute", 1);
            zeroDamageAmountWetness = config.getConfig().getInt("custom-status.wetness.zero-damage-amount", 1);
            zeroDamageIntervalWetness = config.getConfig().getInt("custom-status.wetness.zero-damage-interval", 20);

            Objective wetness_obj = BOARD.getObjective("custom_wetness");
            if (wetness_obj == null) {
                wetness_obj = BOARD.registerNewObjective(
                        "custom_wetness",
                        Criteria.DUMMY,
                        Component.text("Custom Wetness")
                );
            }
        } else {
            enableWetness = false;
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (enableHP) {
                setHP(all.getName(), defaultHP);
            }
            if (enableArmor) {
                setArmor(all.getName(), defaultArmor);
            }
            if (enableFood) {
                setHunger(all.getName(), defaultFood);
            }
            if (enableWetness) {
                setWetness(all.getName(), defaultWetness);
            }
        }
    }

    public static int getHP(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_hp");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static void setHP(String playerName, int hp) {
        Objective ps_obj = BOARD.getObjective("custom_hp");
        if (ps_obj == null) return;
        Score score = ps_obj.getScore(playerName);
        score.setScore(hp);
    }

    public static int getHunger(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_food");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static void setHunger(String playerName, int food) {
        Objective ps_obj = BOARD.getObjective("custom_food");
        if (ps_obj == null) return;
        Score score = ps_obj.getScore(playerName);
        score.setScore(food);
    }

    public static int getWetness(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_wetness");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static void setWetness(String playerName, int wetness) {
        Objective ps_obj = BOARD.getObjective("custom_wetness");
        if (ps_obj == null) return;
        Score score = ps_obj.getScore(playerName);
        score.setScore(wetness);
    }

    public static int getArmor(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_armor");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static void setArmor(String playerName, int armor) {
        Objective ps_obj = BOARD.getObjective("custom_armor");
        if (ps_obj == null) return;
        Score score = ps_obj.getScore(playerName);
        score.setScore(armor);
    }

}
