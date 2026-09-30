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

    private static void initObjective(String objectiveName, String displayName) {
        if (BOARD.getObjective(objectiveName) == null) {
            BOARD.registerNewObjective(
                    objectiveName,
                    Criteria.DUMMY,
                    Component.text(displayName)
            );
        }
    }

    public static void loadSetting() {
        CustomConfiguration config = CustomStatus.getCustomConfig();
        if (config.getConfig().getBoolean("custom-status.hp.enable", false)) {
            maxHP = config.getConfig().getInt("custom-status.hp.max", 100);
            minHP = config.getConfig().getInt("custom-status.hp.min", 0);
            defaultHP = config.getConfig().getInt("custom-status.hp.default", maxHP);

            initObjective("custom_hp", "Custom HP");
        } else {
            enableHP = false;
        }
        if (config.getConfig().getBoolean("custom-status.armor.enable", false)) {
            maxArmor = config.getConfig().getInt("custom-status.armor.max", 100);
            minArmor = config.getConfig().getInt("custom-status.armor.min", 0);
            defaultArmor = config.getConfig().getInt("custom-status.armor.default", maxArmor);

            initObjective("custom_armor", "Custom Armor");
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

            initObjective("custom_food", "Custom Food");
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

            initObjective("custom_wetness", "Custom Wetness");
        } else {
            enableWetness = false;
        }

        if (enableHP || enableArmor || enableFood || enableWetness) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                initPlayerStatus(all.getName());
            }
        }
    }

    public static void initPlayerStatus(String playerName) {
        if (!enableHP && !enableArmor && !enableFood && !enableWetness) return;
        if (enableHP) {
            if (getHP(playerName) == -1) setHP(playerName, defaultHP);
        }
        if (enableArmor) {
            if (getArmor(playerName) == -1) setArmor(playerName, defaultArmor);
        }
        if (enableFood) {
            if (getHunger(playerName) == -1) setHunger(playerName, defaultFood);
        }
        if (enableWetness) {
            if (getWetness(playerName) == -1) setWetness(playerName, defaultWetness);
        }
    }

    public static int getHP(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_hp");
        if (ps_obj == null) initObjective("custom_hp", "Custom HP");
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static int setHP(String playerName, int hp) {
        Objective ps_obj = BOARD.getObjective("custom_hp");
        if (ps_obj == null) initObjective("custom_hp", "Custom HP");
        int newHP = hp;
        if (newHP > maxHP) newHP = maxHP;
        if (newHP < minHP) newHP = minHP;
        Score score = ps_obj.getScore(playerName);
        score.setScore(newHP);
        return newHP;
    }

    public static int addHP(String playerName, int hp) {
        return setHP(playerName, getHP(playerName) + hp);
    }

    public static int removeHP(String playerName, int hp) {
        return setHP(playerName, getHP(playerName) - hp);
    }

    public static int getHunger(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_food");
        if (ps_obj == null) initObjective("custom_food", "Custom Food");
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static int setHunger(String playerName, int food) {
        Objective ps_obj = BOARD.getObjective("custom_food");
        if (ps_obj == null) initObjective("custom_food", "Custom Food");
        int newFood = food;
        if (newFood > maxFood) newFood = maxFood;
        if (newFood < minFood) newFood = minFood;
        Score score = ps_obj.getScore(playerName);
        score.setScore(newFood);
        return newFood;
    }

    public  static int addHunger(String playerName, int food) {
        return setHunger(playerName, getHunger(playerName) + food);
    }

    public static int removeHunger(String playerName, int food) {
        return setHunger(playerName, getHunger(playerName) - food);
    }

    public static int getWetness(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_wetness");
        if (ps_obj == null) initObjective("custom_wetness", "Custom Wetness");
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static int setWetness(String playerName, int wetness) {
        Objective ps_obj = BOARD.getObjective("custom_wetness");
        if (ps_obj == null) initObjective("custom_wetness", "Custom Wetness");
        int newWetness = wetness;
        if (newWetness > maxWetness) newWetness = maxWetness;
        if (newWetness < minWetness) newWetness = minWetness;
        Score score = ps_obj.getScore(playerName);
        score.setScore(newWetness);
        return newWetness;
    }

    public static int addWetness(String playerName, int wetness) {
        return setWetness(playerName, getWetness(playerName) + wetness);
    }

    public static int removeWetness(String playerName, int wetness) {
        return setWetness(playerName, getWetness(playerName) - wetness);
    }

    public static int getArmor(String playerName) {
        Objective ps_obj = BOARD.getObjective("custom_armor");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static int setArmor(String playerName, int armor) {
        Objective ps_obj = BOARD.getObjective("custom_armor");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        score.setScore(armor);
        return armor;
    }

    public static int addArmor(String playerName, int armor) {
        return setArmor(playerName, getArmor(playerName) + armor);
    }

    public static int removeArmor(String playerName, int armor) {
        return setArmor(playerName, getArmor(playerName) - armor);
    }

}
