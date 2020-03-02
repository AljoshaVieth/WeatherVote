package me.F_o_F_1092.WeatherVote;

import me.F_o_F_1092.WeatherVote.PluginManager.ServerLog;
import me.F_o_F_1092.WeatherVote.PluginManager.Spigot.JSONMessageListener;
import me.F_o_F_1092.WeatherVote.VotingGUI.VotingGUIListener;
import me.F_o_F_1092.WeatherVote.VotingPlayers.VotePlayer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WeatherVote {

  String worldName;
  ArrayList<VotePlayer> votePlayers = new ArrayList<>();
  Weather weather;
  TimerType timerType;
  BossBar bossbar;

  Integer taskReminding, taskEnding, taskTimeout;

  Long timeUntillNextVote;

  public enum Weather {
    SUN,
    RAIN
  }

  protected WeatherVote(String worldName, Weather weather, UUID uuid) {
    this.worldName = worldName;
    this.weather = weather;

    VotingGUIListener.closeVotingGUIsAtWorld(this.worldName);

    VotePlayer votePlayer = new VotePlayer(uuid, true);
    votePlayers.add(votePlayer);

    startTimer(TimerType.REMINDING, Options.remindingTime);
    startTimer(TimerType.ENDING, Options.votingTime);
    startTimer(TimerType.TIMEOUT, (Options.votingTime + Options.timeoutPeriod));

    if (checkPrematureEnd()) {
      prematureEnd();
    } else {

      if (Options.rawMessages) {
        String votingMessage = Options.msg.get("rmsg.1");

        if (getWeather() == Weather.SUN) {
          votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.1"));
        } else {
          votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.2"));
        }

        votingMessage =
            votingMessage.replace("[PLAYER]", this.votePlayers.get(0).getPlayer().getName());
        votingMessage =
            votingMessage.replace(
                "[\"\",", "[\"\",{\"text\":\"" + Options.msg.get("[WeatherVote]") + "\"},");

        sendJSONMessage(votingMessage);
      } else {
        String votingMessage = Options.msg.get("msg.3");

        if (getWeather() == Weather.SUN) {
          votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.1"));
        } else {
          votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.2"));
        }

        votingMessage =
            votingMessage.replace("[PLAYER]", this.votePlayers.get(0).getPlayer().getName());

        sendMessage(Options.msg.get("[WeatherVote]") + votingMessage);
      }

      if (Options.useScoreboard) {
        registerScoreboard();
      }

      if (Options.useTitle) {
        String timeString = Options.msg.get("titleMessage.Title.1");
        if (getWeather() == Weather.SUN) {
          timeString = timeString.replace("[WEATHER]", Options.msg.get("text.1"));
        } else {
          timeString = timeString.replace("[WEATHER]", Options.msg.get("text.2"));
        }

        sendTitle(timeString, Options.msg.get("titleMessage.SubTitle"), 10, 60, 10);
      }

      if (Options.useBossBar) {
        setupBossBar();
      }
    }
  }

  public String getWorldName() {
    return this.worldName;
  }

  public Weather getWeather() {
    return this.weather;
  }

  public int getSecondsUntillNextVoting() {
    return (int) ((this.timeUntillNextVote - System.currentTimeMillis()) / 1000);
  }

  /*
   * Valid VotePlayers
   */

  protected List<Player> getAllPlayersAtWorld() {
    List<Player> players = new ArrayList<>();

    for (Player p : Bukkit.getWorld(this.worldName).getPlayers()) {
      if (checkPlayerAtWorldPermission(p)) {
        players.add(p);
      }
    }

    return players;
  }

  boolean checkPlayerAtWorldPermission(Player p) {
    if (Bukkit.getPlayer(p.getName()) != null
        && Bukkit.getPlayer(p.getName()).isOnline()
        && !p.hasMetadata("NPC")) {
      if (!Options.showVoteOnlyToPlayersWithPermission
          || Options.showVoteOnlyToPlayersWithPermission
              && (p.hasPermission("WeatherVote.Vote")
                  || p.hasPermission("WeatherVote.Rain")
                  || p.hasPermission("WeatherVote.Sun"))) {
        return true;
      }
    }

    return false;
  }

  /*
   * Vote System
   */

  public ArrayList<VotePlayer> getVotePlayers() {
    return this.votePlayers;
  }

  void vote(UUID uuid, boolean yesNo) {
    VotePlayer votePlayer = new VotePlayer(uuid, yesNo);

    votePlayers.add(votePlayer);

    if (Options.useScoreboard) {
      updateScore();
    }

    if (Options.prematureEnd) {
      if (checkPrematureEnd()) {
        prematureEnd();
      }
    }
  }

  VotePlayer getVoted(UUID uuid) {
    for (VotePlayer votePlayer : this.votePlayers) {
      if (votePlayer.getPlayerId().equals(uuid)) {
        return votePlayer;
      }
    }

    return null;
  }

  boolean hasVoted(UUID uuid) {
    return getVoted(uuid) != null;
  }

  int getYesVotes() {
    int yes = 0;

    for (VotePlayer votePlayer : this.votePlayers) {
      if (votePlayer.getYesNo()) {
        yes++;
      }
    }

    return yes;
  }

  int getNoVotes() {
    int no = 0;

    for (VotePlayer votePlayer : this.votePlayers) {
      if (!votePlayer.getYesNo()) {
        no++;
      }
    }

    return no;
  }

  /*
   * Player world switching
   */

  void switchWorld(Player p, boolean votingWorld) {
    if (checkPlayerAtWorldPermission(p)) {
      if (getTimerType() != TimerType.TIMEOUT) {
        if (votingWorld) {

          if (Options.rawMessages) {
            String votingMessage = Options.msg.get("rmsg.1");

            if (getWeather() == Weather.SUN) {
              votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.1"));
            } else {
              votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.2"));
            }

            votingMessage =
                votingMessage.replace("[PLAYER]", this.votePlayers.get(0).getPlayer().getName());
            votingMessage =
                votingMessage.replace(
                    "[\"\",", "[\"\",{\"text\":\"" + Options.msg.get("[WeatherVote]") + "\"},");

            sendJSONMessage(votingMessage);
          } else {
            String votingMessage = Options.msg.get("msg.3");

            if (getWeather() == Weather.SUN) {
              votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.1"));
            } else {
              votingMessage = votingMessage.replace("[WEATHER]", Options.msg.get("text.2"));
            }

            votingMessage =
                votingMessage.replace("[PLAYER]", this.votePlayers.get(0).getPlayer().getName());

            sendMessage(Options.msg.get("[WeatherVote]") + votingMessage);
          }

          if (Options.useScoreboard) {
            registerScoreboard(p);
          }

          if (Options.useBossBar) {
            setupBossBar(p);
          }
        } else {
          if (Options.useScoreboard) {
            removeScoreboard(p);
          }

          if (Options.useBossBar) {
            removeBossBar(p);
          }

          if (checkPrematureEndAndIrnore(p.getUniqueId())) {
            prematureEnd();
          }
        }
      }
    }
  }

  /*
   * Message sending
   */

  void sendMessage(String text) {
    for (Player p : getAllPlayersAtWorld()) {
      p.sendMessage(text);
    }
  }

  void sendJSONMessage(String text) {
    for (Player p : getAllPlayersAtWorld()) {
      JSONMessageListener.send(p, text);
    }
  }

  /*
   * Hidden player check
   */

  boolean isHidden(Player p) {
    int hiddenPlayers = 0;

    for (Player p2 : getAllPlayersAtWorld()) {
      if (!p2.canSee(p)) {
        hiddenPlayers++;
      }
    }

    if (hiddenPlayers >= getAllPlayersAtWorld().size() / 2) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * Start/Stop Timer
   */

  public TimerType getTimerType() {
    return this.timerType;
  }

  void setTimertType(TimerType timerType) {
    this.timerType = timerType;
  }

  void startTimer(TimerType timerType, Long time) {
    if (timerType == TimerType.REMINDING) {
      setTimertType(TimerType.REMINDING);

      taskReminding =
          Bukkit.getScheduler()
              .scheduleSyncDelayedTask(
                  Main.getPlugin(),
                  new Runnable() {
                    @Override
                    public void run() {

                      String text = Options.msg.get("msg.14");

                      if (getWeather() == Weather.SUN) {
                        text = text.replace("[WEATHER]", Options.msg.get("text.1"));
                      } else {
                        text = text.replace("[WEATHER]", Options.msg.get("text.2"));
                      }

                      text =
                          text.replace(
                              "[SECONDS]", (Options.votingTime - Options.remindingTime) + "");
                      sendMessage(Options.msg.get("[WeatherVote]") + text);

                      if (Options.useTitle) {
                        String secondsLeftString = Options.msg.get("titleMessage.Title.2");
                        secondsLeftString =
                            secondsLeftString.replace(
                                "[SECONDS]", (Options.votingTime - Options.remindingTime) + "");

                        sendTitle(
                            secondsLeftString,
                            Options.msg.get("titleMessage.SubTitle"),
                            10,
                            60,
                            10);
                      }

                      setTimertType(TimerType.ENDING);

                      taskReminding = null;
                    }
                  },
                  Options.remindingTime * 20L);

    } else if (timerType == TimerType.ENDING) {
      taskEnding =
          Bukkit.getScheduler()
              .scheduleSyncDelayedTask(
                  Main.getPlugin(),
                  new Runnable() {
                    @Override
                    public void run() {
                      VotingGUIListener.closeVotingGUIsAtWorld(getWorldName());

                      WeatherVoteStats wvs = new WeatherVoteStats();

                      if (getYesVotes() > getNoVotes()) {
                        sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.12"));

                        if (Options.useTitle) {
                          String secondsLeftString = Options.msg.get("titleMessage.Title.3");
                          secondsLeftString =
                              secondsLeftString.replace(
                                  "[SECONDS]", (Options.votingTime - Options.remindingTime) + "");

                          sendTitle(secondsLeftString, null, 10, 60, 10);
                        }

                        if (getWeather() == Weather.SUN) {
                          Bukkit.getWorld(worldName).setStorm(false);
                          wvs.setSunnyStats(getYesVotes(), getNoVotes(), true, Options.price);
                        } else {
                          Bukkit.getWorld(worldName).setStorm(true);
                          wvs.setRainyStats(getYesVotes(), getNoVotes(), true, Options.price);
                        }
                      } else {
                        sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.13"));

                        if (Options.useTitle) {
                          String secondsLeftString = Options.msg.get("titleMessage.Title.4");
                          secondsLeftString =
                              secondsLeftString.replace(
                                  "[SECONDS]", (Options.votingTime - Options.remindingTime) + "");

                          sendTitle(secondsLeftString, null, 10, 60, 10);
                        }

                        if (Options.refundVotingPriceIfVotingFails) {
                          if (WeatherVoteListener.isVaultInUse()) {
                            WeatherVoteListener.getVault()
                                .depositPlayer(votePlayers.get(0).getPlayer(), Options.price);
                          }

                          if (getWeather() == Weather.SUN) {
                            wvs.setSunnyStats(getYesVotes(), getNoVotes(), false, Options.price);
                          } else {
                            wvs.setRainyStats(getYesVotes(), getNoVotes(), false, Options.price);
                          }
                        } else {
                          if (getWeather() == Weather.SUN) {
                            wvs.setSunnyStats(getYesVotes(), getNoVotes(), false, 0.0);
                          } else {
                            wvs.setRainyStats(getYesVotes(), getNoVotes(), false, 0.0);
                          }
                        }
                      }

                      if (Options.refundVotingPriceIfVotingFails) {
                        if (WeatherVoteListener.isVaultInUse()) {
                          WeatherVoteListener.getVault()
                              .depositPlayer(votePlayers.get(0).getPlayer(), Options.price);
                        }
                      }

                      if (Options.useScoreboard) {
                        removeScoreboard();
                      }

                      if (Options.useBossBar) {
                        removeBossBar();
                      }

                      if (Options.timeoutPeriod > 0) {
                        setTimertType(TimerType.TIMEOUT);
                      } else {
                        setTimertType(null);
                      }

                      taskEnding = null;
                    }
                  },
                  time * 20L);

    } else if (timerType == TimerType.TIMEOUT) {
      this.timeUntillNextVote = System.currentTimeMillis() + (time * 1000);

      taskTimeout =
          Bukkit.getScheduler()
              .scheduleSyncDelayedTask(
                  Main.getPlugin(),
                  new Runnable() {
                    @Override
                    public void run() {

                      WeatherVoteListener.removeWeatherVote(getWorldName());

                      setTimertType(null);

                      taskTimeout = null;
                    }
                  },
                  time * 20L);
    }
  }

  void stopTimer(TimerType timerType) {
    if (timerType == TimerType.REMINDING) {

      if (taskReminding != null) {
        Bukkit.getServer().getScheduler().cancelTask(taskReminding);
        taskReminding = null;
      }
    } else if (timerType == TimerType.ENDING) {

      if (taskEnding != null) {
        Bukkit.getServer().getScheduler().cancelTask(taskEnding);
        taskEnding = null;
      }
    } else if (timerType == TimerType.TIMEOUT) {

      if (taskTimeout != null) {
        Bukkit.getServer().getScheduler().cancelTask(taskTimeout);
        taskTimeout = null;
      }
    }
  }

  /*
   * Voting PreEnding
   */

  boolean checkPrematureEnd() {
    for (Player p : getAllPlayersAtWorld()) {
      if (!hasVoted(p.getUniqueId()) && !Options.checkForHiddenPlayers
          || !hasVoted(p.getUniqueId()) && Options.checkForHiddenPlayers && !isHidden(p)) {
        return false;
      }
    }

    return true;
  }

  boolean checkPrematureEndAndIrnore(UUID uuid) {
    for (Player p : getAllPlayersAtWorld()) {
      if (!p.getUniqueId().equals(uuid)) {
        if (!hasVoted(p.getUniqueId()) && !Options.checkForHiddenPlayers
            || !hasVoted(p.getUniqueId()) && Options.checkForHiddenPlayers && !isHidden(p)) {
          return false;
        }
      }
    }

    return true;
  }

  void prematureEnd() {
    if (this.votePlayers.size() == 1) {
      String text = Options.msg.get("msg.23");

      if (getWeather() == Weather.SUN) {
        text = text.replace("[WEATHER]", Options.msg.get("text.1"));
      } else {
        text = text.replace("[WEATHER]", Options.msg.get("text.2"));
      }

      sendMessage(Options.msg.get("[WeatherVote]") + text);
    } else {
      sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.17"));
    }

    stopTimer(TimerType.REMINDING);
    stopTimer(TimerType.ENDING);
    stopTimer(TimerType.TIMEOUT);

    startTimer(TimerType.ENDING, 0L);
    startTimer(TimerType.TIMEOUT, Options.timeoutPeriod);
  }

  void stopVoting(boolean useTimer) {
    sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.24"));

    stopTimer(TimerType.REMINDING);
    stopTimer(TimerType.ENDING);
    stopTimer(TimerType.TIMEOUT);

    if (!useTimer) {
      if (Options.refundVotingPriceIfVotingFails) {
        if (WeatherVoteListener.isVaultInUse()) {
          WeatherVoteListener.getVault()
              .depositPlayer(votePlayers.get(0).getPlayer(), Options.price);
        }
      }

      if (Options.useScoreboard) {
        removeScoreboard();
      }

      if (Options.useBossBar) {
        removeBossBar();
      }
    } else {
      startTimer(TimerType.ENDING, 0L);
      startTimer(TimerType.TIMEOUT, 0L);
    }
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
        objective.setDisplayName("˜f[˜9Weather˜bVote˜f] ˜6Sunny");

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
        objective.setDisplayName("˜f[˜9Weather˜bVote˜f] ˜6Rainy");

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

  void registerScoreboard() {
    for (Player p : getAllPlayersAtWorld()) {
      registerScoreboard(p);
    }
  }

  void removeScoreboard(Player p) {
    if (p.getScoreboard().getObjective("WeatherVote") != null) {
      p.getScoreboard().getObjective("WeatherVote").unregister();
    }
  }

  void removeScoreboard() {
    for (Player p : getAllPlayersAtWorld()) {
      removeScoreboard(p);
    }
  }

  protected void updateScore(Player p) {
    if (p.getScoreboard().getObjective("WeatherVote") != null) {
      Objective objective = p.getScoreboard().getObjective("WeatherVote");
      Score scoreYes;

      try {
        scoreYes = objective.getScore(Options.msg.get("color.1") + Options.msg.get("text.3"));
      } catch (Exception e) {
        scoreYes = objective.getScore(Options.msg.get("color.1") + "Yes");

        ServerLog.err(
            "The scoreboard text for YES caused a problem. (Message: text.3) ["
                + e.getMessage()
                + "]");
      }

      scoreYes.setScore(getYesVotes());
      Score scoreNo;

      try {
        scoreNo = objective.getScore(Options.msg.get("color.1") + Options.msg.get("text.4"));
      } catch (Exception e) {
        scoreNo = objective.getScore(Options.msg.get("color.1") + "No");

        ServerLog.err(
            "The scoreboard text for NO caused a problem. (Message: text.4) ["
                + e.getMessage()
                + "]");
      }

      scoreNo.setScore(getNoVotes());
    }
  }

  void updateScore() {
    for (Player p : getAllPlayersAtWorld()) {
      updateScore(p);
    }
  }

  /*
   * Bossbar sending
   */

  protected void setupBossBar(Player p) {
    this.bossbar.addPlayer(p);
  }

  protected void setupBossBar() {
    String timeString = Options.msg.get("bossBarMessage");

    if (getWeather() == Weather.SUN) {
      timeString = timeString.replace("[WEATHER]", Options.msg.get("text.1"));
    } else {
      timeString = timeString.replace("[WEATHER]", Options.msg.get("text.2"));
    }

    this.bossbar = Bukkit.createBossBar(timeString, BarColor.BLUE, BarStyle.SEGMENTED_20);

    for (Player p : getAllPlayersAtWorld()) {
      setupBossBar(p);
    }

    bossBarTimer();
  }

  protected void removeBossBar(Player p) {
    if (bossbar != null && bossbar.getPlayers().contains(p)) {
      this.bossbar.removePlayer(p);

      if (this.bossbar.getPlayers().isEmpty()) {
        this.bossbar = null;
      }
    }
  }

  protected void removeBossBar() {
    for (Player p : getAllPlayersAtWorld()) {
      removeBossBar(p);
    }
  }

  void bossBarTimer() {
    Bukkit.getScheduler()
        .scheduleSyncDelayedTask(
            Main.getPlugin(),
            new Runnable() {
              @Override
              public void run() {
                if (bossbar != null
                    && bossbar.getProgress() - (double) (1.0 / Options.votingTime) > 0) {

                  bossbar.setProgress(bossbar.getProgress() - (double) (1.0 / Options.votingTime));

                  bossBarTimer();
                }
              }
            },
            20L);
  }

  /*
   * Title sending
   */

  protected void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    for (Player p : getAllPlayersAtWorld()) {
      p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
  }
}
