package com.simondmc.borderhoarder.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;

import java.util.Collections;

public class ScoreboardHandler {
    public static void updateScoreboard() {
        Objective o = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("itemscollected") == null ?
                Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("itemscollected", Criteria.DUMMY, "ItemsCollected") :
                Bukkit.getScoreboardManager().getMainScoreboard().getObjective("itemscollected");
        // set player scores
        for (Player p : Bukkit.getOnlinePlayers()) {
            // get amount of items collected by UUID
            o.getScore(p).setScore(Collections.frequency(ItemHandler.getCollectedItems().values(), p.getUniqueId()));
        }
    }
}
