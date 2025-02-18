package com.simondmc.borderhoarder.cmd;

import com.simondmc.borderhoarder.game.GameData;
import com.simondmc.borderhoarder.game.ItemHandler;
import com.simondmc.borderhoarder.game.ScoreboardHandler;
import com.simondmc.borderhoarder.world.BorderWorldCreator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class StartGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("startbordergame")) {

            if (!sender.isOp()) {
                sender.sendMessage("§cYou don't have permission to start a game!");
                return true;
            }

            // create world with seed
            long seed;
            if (args.length >= 1) {
                try {
                    seed = Long.parseLong(args[0]);
                } catch (Exception e) {
                    sender.sendMessage("§cThat's not a valid seed!");
                    return true;
                }
            } else {
                seed = new Random().nextLong();
            }

            // reset data
            ItemHandler.resetCollectedItems();
            GameData.clear();
            // set main scoreboard
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
            // init tab
            ScoreboardHandler.updateScoreboard();

            new BorderWorldCreator(seed);
            return true;
        }
        return false;
    }
}
