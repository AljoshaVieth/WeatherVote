package me.F_o_F_1092.WeatherVote.MC_V1_7__V1_8;

import com.connorlinfoot.titleapi.TitleAPI;
import me.F_o_F_1092.WeatherVote.Options;
import me.F_o_F_1092.WeatherVote.PluginManager.ServerLog;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.inventivetalent.bossbar.BossBarAPI;

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

  /*
   * Bossbar sending
   */

  protected void setupBossBar(Player p) {
    String timeString = Options.msg.get("bossBarMessage");

    if (getWeather() == Weather.SUN) {
      timeString = timeString.replace("[WEATHER]", Options.msg.get("text.1"));
    } else {
      timeString = timeString.replace("[WEATHER]", Options.msg.get("text.2"));
    }

    try {
      BossBarAPI.addBar(
          p,
          new TextComponent(timeString),
          BossBarAPI.Color.BLUE,
          BossBarAPI.Style.PROGRESS,
          1.0f,
          0,
          1);
    } catch (Exception e) {
      ServerLog.err("Faild to send the BossBar Message with BossBarAPI [" + e.getMessage() + "]");
    }
  }

  protected void setupBossBar() {
    for (Player p : getAllPlayersAtWorld()) {
      setupBossBar(p);
    }
  }

  @SuppressWarnings("deprecation")
  protected void removeBossBar(Player p) {
    try {
      if (BossBarAPI.getBossBar(p) != null) {
        BossBarAPI.getBossBar(p).removePlayer(p);
      }
    } catch (Exception e) {
      ServerLog.err("Faild to remove the BossBar Message with BossBarAPI [" + e.getMessage() + "]");
    }
  }

  /*
   * Title sending
   */

  protected void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    for (Player p : getAllPlayersAtWorld()) {
      try {
        TitleAPI.sendTitle(p, fadeIn, stay, fadeOut, title, subtitle);
      } catch (Exception e) {
        ServerLog.err("Faild to send the Title Message with TitleAPI [" + e.getMessage() + "]");
      }
    }
  }
}
