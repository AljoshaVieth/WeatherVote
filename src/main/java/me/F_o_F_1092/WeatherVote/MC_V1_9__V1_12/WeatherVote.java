package me.F_o_F_1092.WeatherVote.MC_V1_9__V1_12;

import me.F_o_F_1092.WeatherVote.Options;
import me.F_o_F_1092.WeatherVote.PluginManager.ServerLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class WeatherVote extends me.F_o_F_1092.WeatherVote.WeatherVote {

  public WeatherVote(String worldName, Weather weather, UUID uuid) {
    super(worldName, weather, uuid);
  }

  /*
   * Scoreboard Managing
   */

  protected void registerScoreboard(Player p) {
    Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
    Objective objective = sb.registerNewObjective("WeatherVote", "dummy");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    if (getWeather() == Weather.SUN) {
      try {
        objective.setDisplayName(
            Options.msg.get("[WeatherVote]")
                + Options.msg.get("color.1")
                + Options.msg.get("text.1"));
      } catch (Exception e) {
        objective.setDisplayName("Åòf[Åò9WeatherÅòbVoteÅòf] Åò6Sunny");

        ServerLog.err(
            "The scoreboard name caused a problem. (Message: text.1) [" + e.getMessage() + "]");
      }
    } else {
      try {
        objective.setDisplayName(
            Options.msg.get("[WeatherVote]")
                + Options.msg.get("color.1")
                + Options.msg.get("text.2"));
      } catch (Exception e) {
        objective.setDisplayName("Åòf[Åò9WeatherÅòbVoteÅòf] Åò6Rainy");

        ServerLog.err(
            "The scoreboard name caused a problem. (Message: text.2) [" + e.getMessage() + "]");
      }
    }

    try {
      p.setScoreboard(sb);
    } catch (Exception e) {
      ServerLog.err(
          "Faild to remove the Scoreboard from " + p.getName() + " [" + e.getMessage() + "]");
    }

    updateScore(p);
  }
}
