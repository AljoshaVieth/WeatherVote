package me.F_o_F_1092.WeatherVote;

import me.F_o_F_1092.WeatherVote.PluginManager.Math;
import me.F_o_F_1092.WeatherVote.PluginManager.*;
import me.F_o_F_1092.WeatherVote.PluginManager.Spigot.HelpPageListener;
import me.F_o_F_1092.WeatherVote.PluginManager.Spigot.JSONMessageListener;
import me.F_o_F_1092.WeatherVote.PluginManager.Spigot.UpdateListener;
import me.F_o_F_1092.WeatherVote.PluginManager.VersionManager.BukkitVersion;
import me.F_o_F_1092.WeatherVote.VotingGUI.VotingGUIListener;
import me.F_o_F_1092.WeatherVote.WeatherVote.Weather;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandWeatherVote implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
    if (args.length == 0) {
      if (!(cs instanceof Player) || !Options.useVoteGUI) {
        String replaceCommand = Options.msg.get("msg.22");
        replaceCommand =
            replaceCommand.replace(
                "[COMMAND]", CommandListener.getCommand("/wv help (Page)").getColoredCommand());
        cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
      } else {
        Player player = (Player) cs;
        if (Options.disabledWorlds.contains(player.getWorld().getName())) {
          cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.4"));
        } else {
          if (WeatherVoteListener.isVoting(player.getWorld().getName())) {
            WeatherVote weatherVote = WeatherVoteListener.getVoteing(player.getWorld().getName());

            if (weatherVote.getTimerType() == TimerType.TIMEOUT) {
              String text = Options.msg.get("msg.15");
              text = text.replace("[SECONDS]", weatherVote.getSecondsUntillNextVoting() + "");
              player.sendMessage(Options.msg.get("[WeatherVote]") + text);
            } else {
              if (Options.votingInventoryMessages) {
                cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.20"));
              }

              VotingGUIListener.addVotingGUIPlayer(
                  player.getUniqueId(), player.getWorld().getName());
            }
          } else {
            if (Options.votingInventoryMessages) {
              cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.20"));
            }
            VotingGUIListener.addVotingGUIPlayer(player.getUniqueId(), player.getWorld().getName());
          }
        }
      }
    } else {
      if (args[0].equalsIgnoreCase("help")) {
        if (!(args.length >= 1 && args.length <= 2)) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv help (Page)").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          if (!(cs instanceof Player)) {
            if (args.length != 1) {
              String replaceCommand = Options.msg.get("msg.22");
              replaceCommand =
                  replaceCommand.replace(
                      "[COMMAND]",
                      CommandListener.getCommand("/wv help (Page)").getColoredCommand());
              cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
            } else {
              HelpPageListener.sendNormalMessage(cs);
            }
          } else {
            Player p = (Player) cs;
            if (args.length == 1) {
              HelpPageListener.sendMessage(p, 0);
            } else {
              if (!Math.isInt(args[1])) {
                String replaceCommand = Options.msg.get("msg.22");
                replaceCommand =
                    replaceCommand.replace(
                        "[COMMAND]",
                        CommandListener.getCommand("/wv help (Page)").getColoredCommand());
                cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
              } else {
                if (Integer.parseInt(args[1]) <= 0
                    || Integer.parseInt(args[1]) > HelpPageListener.getMaxPlayerPages(p)) {
                  String replaceCommand = Options.msg.get("msg.22");
                  replaceCommand =
                      replaceCommand.replace(
                          "[COMMAND]",
                          CommandListener.getCommand("/wv help (Page)").getColoredCommand());
                  cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
                } else {
                  HelpPageListener.sendMessage(p, Integer.parseInt(args[1]) - 1);
                }
              }
            }
          }
        }
      } else if (args[0].equalsIgnoreCase("info")) {
        if (args.length != 1) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv info").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          cs.sendMessage("Åò9Åòm-----------Åòf [Åò9ÅòlWeatherÅòbÅòlVoteÅòf] Åò9Åòm-----------");
          cs.sendMessage("");

          if (cs instanceof Player) {
            Player p = (Player) cs;

            List<JSONMessage> jsonFoFMessages = new ArrayList<JSONMessage>();

            JSONMessage FoFText = new JSONMessage("Åò9By: ");
            JSONMessage FoFLink = new JSONMessage("ÅòfF_o_F_1092");
            FoFLink.setHoverText("Åò9[ÅòfOpen my WebsiteÅò9]");
            FoFLink.setOpenURL("https://fof1092.de");

            jsonFoFMessages.add(FoFText);
            jsonFoFMessages.add(FoFLink);

            JSONMessageListener.send(
                p, JSONMessageListener.putJSONMessagesTogether(jsonFoFMessages));

            cs.sendMessage("");

            List<JSONMessage> jsonTwitterMessages = new ArrayList<>();

            JSONMessage twitterText = new JSONMessage("Åò9Twitter: ");
            JSONMessage twitterLink = new JSONMessage("Åòf@F_o_F_1092");
            twitterLink.setHoverText("Åò9[ÅòfOpen TwitterÅò9]");
            twitterLink.setOpenURL("https://twitter.com/F_o_F_1092");

            jsonTwitterMessages.add(twitterText);
            jsonTwitterMessages.add(twitterLink);

            JSONMessageListener.send(
                p, JSONMessageListener.putJSONMessagesTogether(jsonTwitterMessages));

            cs.sendMessage("");
            cs.sendMessage("Åò9Version: Åòf" + UpdateListener.getUpdateStringVersion());

            List<JSONMessage> jsonPluginPageMessages = new ArrayList<JSONMessage>();

            JSONMessage pluginWebsiteText = new JSONMessage("Åò9WeatherVote: ");
            JSONMessage pluginWebsiteLink = new JSONMessage("Åòfhttps://fof1092.de/Plugins/WV");
            pluginWebsiteLink.setHoverText("Åò9[ÅòfOpen the Plugin PageÅò9]");
            pluginWebsiteLink.setOpenURL("https://fof1092.de/Plugins/WV");

            jsonPluginPageMessages.add(pluginWebsiteText);
            jsonPluginPageMessages.add(pluginWebsiteLink);

            JSONMessageListener.send(
                p, JSONMessageListener.putJSONMessagesTogether(jsonPluginPageMessages));

          } else {
            cs.sendMessage("Åò9By: ÅòfF_o_F_1092");
            cs.sendMessage("");
            cs.sendMessage("Åò9Twitter: Åòf@F_o_F_1092");
            cs.sendMessage("");
            cs.sendMessage("Åò9Version: Åòf" + UpdateListener.getUpdateStringVersion());
            cs.sendMessage("Åò9WeatherVote: Åòfhttps://fof1092.de/Plugins/WV");
          }

          cs.sendMessage("");
          cs.sendMessage("Åò9Åòm-----------Åòf [Åò9ÅòlWeatherÅòbÅòlVoteÅòf] Åò9Åòm-----------");
        }
      } else if (args[0].equalsIgnoreCase("stats")) {
        if (args.length != 1) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv stats").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          WeatherVoteStats wvs = new WeatherVoteStats();

          cs.sendMessage(
              Options.msg.get("color.1")
                  + "-----"
                  + Options.msg.get("[WeatherVote]")
                  + Options.msg.get("color.1")
                  + "-----");
          cs.sendMessage(
              Options.msg.get("statsText.1") + Options.msg.get("color.2") + wvs.getDate());
          cs.sendMessage(
              Options.msg.get("statsText.2")
                  + Options.msg.get("color.2")
                  + wvs.getMoneySpent()
                  + "$");
          cs.sendMessage(
              Options.msg.get("statsText.3") + Options.msg.get("color.2") + wvs.getSunnyVotes());
          cs.sendMessage(
              Options.msg.get("statsText.4") + Options.msg.get("color.2") + wvs.getSunnyYes());
          cs.sendMessage(
              Options.msg.get("statsText.5") + Options.msg.get("color.2") + wvs.getSunnyNo());
          cs.sendMessage(
              Options.msg.get("statsText.6") + Options.msg.get("color.2") + wvs.getSunnyWon());
          cs.sendMessage(
              Options.msg.get("statsText.7") + Options.msg.get("color.2") + wvs.getSunnyLost());
          cs.sendMessage(
              Options.msg.get("statsText.8") + Options.msg.get("color.2") + wvs.getRainyVotes());
          cs.sendMessage(
              Options.msg.get("statsText.4") + Options.msg.get("color.2") + wvs.getRainyYes());
          cs.sendMessage(
              Options.msg.get("statsText.5") + Options.msg.get("color.2") + wvs.getRainyNo());
          cs.sendMessage(
              Options.msg.get("statsText.6") + Options.msg.get("color.2") + wvs.getRainyWon());
          cs.sendMessage(
              Options.msg.get("statsText.7") + Options.msg.get("color.2") + wvs.getRainyLost());
        }
      } else if (args[0].equalsIgnoreCase("sun")) {
        if (args.length != 1) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv sun").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          if (!(cs instanceof Player)) {
            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.1"));
          } else {
            Player p = (Player) cs;
            if (!p.hasPermission("WeatherVote.Sun")) {
              cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.2"));
            } else {
              if (Options.disabledWorlds.contains(p.getWorld().getName())) {
                cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.4"));
              } else {
                if (WeatherVoteListener.isVoting(p.getWorld().getName())) {
                  WeatherVote weatherVote = WeatherVoteListener.getVoteing(p.getWorld().getName());

                  if (weatherVote.getTimerType() == TimerType.TIMEOUT) {
                    String text = Options.msg.get("msg.15");
                    text = text.replace("[SECONDS]", weatherVote.getSecondsUntillNextVoting() + "");
                    p.sendMessage(Options.msg.get("[WeatherVote]") + text);
                  } else {
                    p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.5"));
                  }
                } else {
                  WeatherVote weatherVote = null;

                  if (WeatherVoteListener.isVaultInUse()) {
                    if (!WeatherVoteListener.getVault().has(p, Options.price)) {
                      String text = Options.msg.get("msg.18");
                      text =
                          text.replace(
                              "[MONEY]",
                              ((Options.price * 100)
                                          - (WeatherVoteListener.getVault().getBalance(p) * 100))
                                      / 100
                                  + "");
                      p.sendMessage(Options.msg.get("[WeatherVote]") + text);
                    } else {
                      String text1 = Options.msg.get("msg.19");
                      text1 = text1.replace("[MONEY]", Options.price + "");
                      p.sendMessage(Options.msg.get("[WeatherVote]") + text1);

                      WeatherVoteListener.getVault().withdrawPlayer(p, Options.price);

                      if (VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R2
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R4
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R2
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R3) {

                        weatherVote =
                            new me.F_o_F_1092.WeatherVote.MC_V1_7__V1_8.WeatherVote(
                                p.getWorld().getName(), Weather.SUN, p.getUniqueId());
                      } else if (VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R2
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_10_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_11_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_12_R1) {

                        weatherVote =
                            new me.F_o_F_1092.WeatherVote.MC_V1_9__V1_12.WeatherVote(
                                p.getWorld().getName(), Weather.SUN, p.getUniqueId());
                      } else {
                        weatherVote =
                            new me.F_o_F_1092.WeatherVote.WeatherVote(
                                p.getWorld().getName(), Weather.SUN, p.getUniqueId());
                      }
                    }
                  } else {
                    if (Options.price > 0.0) {
                      ServerLog.log(
                          "The plugin Vault was not found, but a Voting-Price was set in the Config.yml file.");
                    }

                    if (VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R2
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R4
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R2
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R3) {

                      weatherVote =
                          new me.F_o_F_1092.WeatherVote.MC_V1_7__V1_8.WeatherVote(
                              p.getWorld().getName(), Weather.SUN, p.getUniqueId());
                    } else if (VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R2
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_10_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_11_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_12_R1) {

                      weatherVote =
                          new me.F_o_F_1092.WeatherVote.MC_V1_9__V1_12.WeatherVote(
                              p.getWorld().getName(), Weather.SUN, p.getUniqueId());
                    } else {
                      weatherVote =
                          new me.F_o_F_1092.WeatherVote.WeatherVote(
                              p.getWorld().getName(), Weather.SUN, p.getUniqueId());
                    }
                  }

                  if (weatherVote != null) {
                    WeatherVoteListener.addWeatherVote(weatherVote);
                  }
                }
              }
            }
          }
        }
      } else if (args[0].equalsIgnoreCase("rain")) {
        if (args.length != 1) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv rain").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          if (!(cs instanceof Player)) {
            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.1"));
          } else {
            Player p = (Player) cs;
            if (!p.hasPermission("WeatherVote.Rain")) {
              cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.2"));
            } else {
              if (Options.disabledWorlds.contains(p.getWorld().getName())) {
                cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.4"));
              } else {
                if (WeatherVoteListener.isVoting(p.getWorld().getName())) {
                  WeatherVote weatherVote = WeatherVoteListener.getVoteing(p.getWorld().getName());

                  if (weatherVote.getTimerType() == TimerType.TIMEOUT) {
                    String text = Options.msg.get("msg.15");
                    text = text.replace("[SECONDS]", weatherVote.getSecondsUntillNextVoting() + "");
                    p.sendMessage(Options.msg.get("[WeatherVote]") + text);
                  } else {
                    p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.5"));
                  }
                } else {
                  WeatherVote weatherVote = null;

                  if (WeatherVoteListener.isVaultInUse()) {
                    if (!WeatherVoteListener.getVault().has(p, Options.price)) {
                      String text = Options.msg.get("msg.18");
                      text =
                          text.replace(
                              "[MONEY]",
                              ((Options.price * 100)
                                          - (WeatherVoteListener.getVault().getBalance(p) * 100))
                                      / 100
                                  + "");
                      p.sendMessage(Options.msg.get("[WeatherVote]") + text);
                    } else {
                      String text1 = Options.msg.get("msg.19");
                      text1 = text1.replace("[MONEY]", Options.price + "");
                      p.sendMessage(Options.msg.get("[WeatherVote]") + text1);

                      WeatherVoteListener.getVault().withdrawPlayer(p, Options.price);

                      if (VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R2
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R4
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R2
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R3) {

                        weatherVote =
                            new me.F_o_F_1092.WeatherVote.MC_V1_7__V1_8.WeatherVote(
                                p.getWorld().getName(), Weather.RAIN, p.getUniqueId());
                      } else if (VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R2
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_10_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_11_R1
                          || VersionManager.getBukkitVersion() == BukkitVersion.v1_12_R1) {

                        weatherVote =
                            new me.F_o_F_1092.WeatherVote.MC_V1_9__V1_12.WeatherVote(
                                p.getWorld().getName(), Weather.RAIN, p.getUniqueId());
                      } else {
                        weatherVote =
                            new me.F_o_F_1092.WeatherVote.WeatherVote(
                                p.getWorld().getName(), Weather.RAIN, p.getUniqueId());
                      }
                    }
                  } else {
                    if (Options.price > 0.0) {
                      ServerLog.log(
                          "The plugin Vault was not found, but a Voting-Price was set in the Config.yml file.");
                    }

                    if (VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R2
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R4
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R2
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R3) {

                      weatherVote =
                          new me.F_o_F_1092.WeatherVote.MC_V1_7__V1_8.WeatherVote(
                              p.getWorld().getName(), Weather.RAIN, p.getUniqueId());
                    } else if (VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_9_R2
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_10_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_11_R1
                        || VersionManager.getBukkitVersion() == BukkitVersion.v1_12_R1) {

                      weatherVote =
                          new me.F_o_F_1092.WeatherVote.MC_V1_9__V1_12.WeatherVote(
                              p.getWorld().getName(), Weather.RAIN, p.getUniqueId());
                    } else {
                      weatherVote =
                          new me.F_o_F_1092.WeatherVote.WeatherVote(
                              p.getWorld().getName(), Weather.RAIN, p.getUniqueId());
                    }
                  }

                  if (weatherVote != null) {
                    WeatherVoteListener.addWeatherVote(weatherVote);
                  }
                }
              }
            }
          }
        }
      } else if (args[0].equalsIgnoreCase("yes")) {
        if (args.length != 1) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv yes").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          if (!(cs instanceof Player)) {
            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.1"));
          } else {
            Player p = (Player) cs;
            if (!p.hasPermission("WeatherVote.Vote")) {
              p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.2"));
            } else {
              if (!WeatherVoteListener.isVoting(p.getWorld().getName())
                  || (WeatherVoteListener.isVoting(p.getWorld().getName())
                      && WeatherVoteListener.getVoteing(p.getWorld().getName()).getTimerType()
                          == TimerType.TIMEOUT)) {
                p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.6"));
              } else {
                WeatherVote weatherVote = WeatherVoteListener.getVoteing(p.getWorld().getName());

                if (weatherVote.hasVoted(p.getUniqueId())) {
                  p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.7"));
                } else {
                  p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.8"));
                  weatherVote.vote(p.getUniqueId(), true);
                }
              }
            }
          }
        }
      } else if (args[0].equalsIgnoreCase("no")) {
        if (args.length != 1) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv no").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          if (!(cs instanceof Player)) {
            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.1"));
          } else {
            Player p = (Player) cs;
            if (!p.hasPermission("WeatherVote.Vote")) {
              p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.2"));
            } else {
              if (!WeatherVoteListener.isVoting(p.getWorld().getName())
                  || (WeatherVoteListener.isVoting(p.getWorld().getName())
                      && WeatherVoteListener.getVoteing(p.getWorld().getName()).getTimerType()
                          == TimerType.TIMEOUT)) {
                p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.6"));
              } else {
                WeatherVote weatherVote = WeatherVoteListener.getVoteing(p.getWorld().getName());

                if (weatherVote.hasVoted(p.getUniqueId())) {
                  p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.7"));
                } else {
                  p.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.9"));
                  weatherVote.vote(p.getUniqueId(), false);
                }
              }
            }
          }
        }
      } else if (args[0].equalsIgnoreCase("stopVoting")) {
        if (args.length != 2) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]",
                  CommandListener.getCommand("/wv stopVoting [World]").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          if (!cs.hasPermission("WeatherVote.StopVoting")) {
            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.2"));
          } else {
            if (!WeatherVoteListener.isVoting(args[1])
                || (WeatherVoteListener.isVoting(args[1])
                    && WeatherVoteListener.getVoteing(args[1]).getTimerType()
                        == TimerType.TIMEOUT)) {
              cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.6"));
            } else {
              WeatherVoteListener.getVoteing(args[1]).stopVoting(true);

              cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.25"));
            }
          }
        }
      } else if (args[0].equalsIgnoreCase("reload")) {
        if (args.length != 1) {
          String replaceCommand = Options.msg.get("msg.22");
          replaceCommand =
              replaceCommand.replace(
                  "[COMMAND]", CommandListener.getCommand("/wv reload").getColoredCommand());
          cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
        } else {
          if (!cs.hasPermission("WeatherVote.Reload")) {
            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.2"));
          } else {
            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.10"));

            Main.disable();
            Main.setup();

            cs.sendMessage(Options.msg.get("[WeatherVote]") + Options.msg.get("msg.11"));
          }
        }
      } else {
        String replaceCommand = Options.msg.get("msg.22");
        replaceCommand =
            replaceCommand.replace(
                "[COMMAND]", CommandListener.getCommand("/wv help (Page)").getColoredCommand());
        cs.sendMessage(Options.msg.get("[WeatherVote]") + replaceCommand);
      }
    }
    return true;
  }
}
