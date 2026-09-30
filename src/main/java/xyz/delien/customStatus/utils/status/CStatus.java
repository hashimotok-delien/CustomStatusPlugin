package xyz.delien.customStatus.utils.status;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import xyz.delien.customStatus.CustomStatus;
import xyz.mlserver.mc.util.CustomConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private static boolean enableStress = true;
    private static int maxStress = 100;
    private static int minStress = 0;
    private static int defaultStress = minStress;
    private static Table <String, Integer, List<PotionEffectType>> stressDebuffEffectTypes = HashBasedTable.create();
    private static Table <String, Integer, Integer> stressDebuffEffectAmplifier = HashBasedTable.create();

    private static boolean enableStamina = true;
    private static int maxStamina = 100;
    private static int minStamina = 0;
    private static int defaultStamina = maxStamina;
    private static int countdownPerSecStamina = 1;
    private static int countupPerSecStamina = 1;
    private static List<PotionEffectType> staminaZeroDebuffEffectTypes = new ArrayList<>();
    private static int staminaZeroDebuffEffectAmplifier = 1;

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

        if (config.getConfig().getBoolean("custom-status.stress.enable", false)) {
            maxStress = config.getConfig().getInt("custom-status.stress.max", 100);
            minStress = config.getConfig().getInt("custom-status.stress.min", 0);
            defaultStress = config.getConfig().getInt("custom-status.stress.default", minStress);
            stressDebuffEffectTypes = HashBasedTable.create();
            stressDebuffEffectAmplifier = HashBasedTable.create();
            for (String keyPer : config.getConfig().getConfigurationSection("custom-status.stress.debuff").getKeys(false)) {
                try {
                    int per = Integer.parseInt(keyPer);

                    List<PotionEffectType> effects = config.getConfig().getStringList("custom-status.stress.debuff." + keyPer).stream()
                            .map(PotionEffectType::getByName)
                            .toList();
                    stressDebuffEffectTypes.put(keyPer, per, effects);
                    stressDebuffEffectAmplifier.put(keyPer, per, config.getConfig().getInt("custom-status.stress.debuff." + keyPer + ".amplifier", 1));
                } catch (NumberFormatException e) {
                    // Handle the case where the key is not a valid integer
                }
            }

            initObjective("custom_stress", "Custom Stress");
        } else {
            enableStress = false;
        }

        if (config.getConfig().getBoolean("custom-status.stamina.enable", false)) {
            maxStamina = config.getConfig().getInt("custom-status.stamina.max", 100);
            minStamina = config.getConfig().getInt("custom-status.stamina.min", 0);
            defaultStamina = config.getConfig().getInt("custom-status.stamina.default", maxStamina);
            countdownPerSecStamina = config.getConfig().getInt("custom-status.stamina.countdown-per-sec", 1);
            countupPerSecStamina = config.getConfig().getInt("custom-status.stamina.countup-per-sec", 1);
            staminaZeroDebuffEffectTypes = config.getConfig().getStringList("custom-status.stamina.zero-debuff").stream()
                    .map(PotionEffectType::getByName)
                    .toList();
            staminaZeroDebuffEffectAmplifier = config.getConfig().getInt("custom-status.stamina.zero-debuff-amplifier", 1);

            initObjective("custom_stamina", "Custom Stamina");
        } else {
            enableStamina = false;
        }

        if (enableHP || enableArmor || enableFood || enableWetness || enableStress || enableStamina) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                initPlayerStatus(all.getName());
            }
        }
    }

    public static void initPlayerStatus(String playerName) {
        if (!enableHP && !enableArmor && !enableFood && !enableWetness && !enableStress && !enableStamina) return;
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
        if (enableStress) {
            if (getStress(playerName) == -1) setStress(playerName, defaultStress);
        }
        if (enableStamina) {
            if (getStamina(playerName) == -1) setStamina(playerName, defaultStamina);
        }
    }

    public static void timerStart() {
        if (!enableHP && !enableArmor && !enableFood && !enableWetness && !enableStress && !enableStamina) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (enableFood) {
                        removeHunger(all.getName(), countdownPerMinuteFood);
                        if (getHunger(all.getName()) <= 0) {
                            all.damage(zeroDamageAmount);
                        }
                    }
                    if (enableWetness) {
                        removeWetness(all.getName(), countdownPerMinuteWetness);
                        if (getWetness(all.getName()) <= 0) {
                            all.damage(zeroDamageAmountWetness);
                        }
                    }
                }
            }
        }.runTaskTimer(CustomStatus.getPlugin(), 0, 20 * 60);
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

    public static int getStress(String playerName) {
        if (!enableStress) return -1;
        Objective ps_obj = BOARD.getObjective("custom_stress");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static int setStress(String playerName, int stress) {
        if (!enableStress) return -1;
        Objective ps_obj = BOARD.getObjective("custom_stress");
        if (ps_obj == null) return -1;
        int newStress = stress;
        if (newStress > maxStress) newStress = maxStress;
        if (newStress < minStress) newStress = minStress;
        Score score = ps_obj.getScore(playerName);
        score.setScore(newStress);
        return newStress;
    }

    public static int addStress(String playerName, int stress) {
        return setStress(playerName, getStress(playerName) + stress);
    }

    public static int removeStress(String playerName, int stress) {
        return setStress(playerName, getStress(playerName) - stress);
    }

    public static int getStamina(String playerName) {
        if (!enableStamina) return -1;
        Objective ps_obj = BOARD.getObjective("custom_stamina");
        if (ps_obj == null) return -1;
        Score score = ps_obj.getScore(playerName);
        return score.getScore();
    }

    public static int setStamina(String playerName, int stamina) {
        if (!enableStamina) return -1;
        Objective ps_obj = BOARD.getObjective("custom_stamina");
        if (ps_obj == null) return -1;
        int newStamina = stamina;
        if (newStamina > maxStamina) newStamina = maxStamina;
        if (newStamina < minStamina) newStamina = minStamina;
        Score score = ps_obj.getScore(playerName);
        score.setScore(newStamina);
        return newStamina;
    }

    public static int addStamina(String playerName, int stamina) {
        return setStamina(playerName, getStamina(playerName) + stamina);
    }

    public static int removeStamina(String playerName, int stamina) {
        return setStamina(playerName, getStamina(playerName) - stamina);
    }

}
