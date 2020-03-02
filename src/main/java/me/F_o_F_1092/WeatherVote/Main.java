package me.F_o_F_1092.WeatherVote;

import me.F_o_F_1092.WeatherVote.PluginManager.*;
import me.F_o_F_1092.WeatherVote.PluginManager.Spigot.UpdateListener;
import me.F_o_F_1092.WeatherVote.PluginManager.VersionManager.BukkitVersion;
import me.F_o_F_1092.WeatherVote.PluginManager.VersionManager.ServerType;
import me.F_o_F_1092.WeatherVote.VotingGUI.VotingGUIListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JavaPlugin {

  static Main plugin;

  public static Main getPlugin() {
    return plugin;
  }

  @Override
  public void onEnable() {
    System.out.println("[WeatherVote] a Plugin by F_o_F_1092");

    plugin = this;

    ServerLog.setPluginTag("Åòf[Åò9WeatherÅòbVoteÅòf]Åò9");
    UpdateListener.initializeUpdateListener(1.42, "1.4.2", 7642);
    UpdateListener.checkForUpdate();

    setup();

    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new EventListener(), this);

    this.getCommand("WeatherVote").setExecutor(new CommandWeatherVote());
    this.getCommand("WeatherVote").setTabCompleter(new CommandWeatherVoteTabCompleter());
  }

  @Override
  public void onDisable() {
    disable();

    System.out.println("[WeatherVote] a Plugin by F_o_F_1092");
  }

  public static void setup() {
    VersionManager.setVersionManager(
        Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3],
        ServerType.BUKKIT,
        false);

    if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
      Options.vault = true;
    }

    File fileConfig = new File("plugins/WeatherVote/Config.yml");
    FileConfiguration ymlFileConfig = YamlConfiguration.loadConfiguration(fileConfig);

    if (!fileConfig.exists()) {

      Options.disabledWorlds.add("world_nether");
      Options.disabledWorlds.add("world_the_end");

      try {
        ymlFileConfig.save(fileConfig);
        ymlFileConfig.set("Version", UpdateListener.getUpdateDoubleVersion());
        ymlFileConfig.set("GameVersion.SetOwn", false);
        ymlFileConfig.set("GameVersion.Version", "v1_13_R2");
        ymlFileConfig.set("ColoredConsoleText", true);
        ymlFileConfig.set("VotingTime", 35);
        ymlFileConfig.set("RemindingTime", 25);
        ymlFileConfig.set("TimeoutPeriod", 15);
        ymlFileConfig.set("UseScoreboard", true);
        ymlFileConfig.set("UseVoteGUI", true);
        ymlFileConfig.set("UseBossBar", true);
        ymlFileConfig.set("UseTitle", true);
        ymlFileConfig.set("CheckForHiddenPlayers", false);
        ymlFileConfig.set("PrematureEnd", true);
        ymlFileConfig.set("Price", 0.00);
        ymlFileConfig.set("RawMessages", true);
        ymlFileConfig.set("DisabledWorld", Options.disabledWorlds);
        ymlFileConfig.set("VotingInventoryMessages", true);
        ymlFileConfig.set("ShowOnlyToPlayersWithPermission", false);
        ymlFileConfig.set("RefundVotingPriceIfVotingFails", true);
        ymlFileConfig.save(fileConfig);
      } catch (IOException e) {
        ServerLog.err("Can't create the Config.yml. [" + e.getMessage() + "]");
      }

      Options.disabledWorlds.clear();
    } else {
      double version = ymlFileConfig.getDouble("Version");

      if (version < UpdateListener.getUpdateDoubleVersion()) {
        try {
          ymlFileConfig.set("Version", UpdateListener.getUpdateDoubleVersion());
          if (version < 1.02) {
            ymlFileConfig.set("VotingInventoryMessages", true);
          }
          if (version < 1.03) {
            ymlFileConfig.set("UseBossBar", true);
            ymlFileConfig.set("UseTitle", true);
          }
          if (version < 1.12) {
            ymlFileConfig.set("CheckForHiddenPlayers", false);
          }
          if (version < 1.3) {
            ymlFileConfig.set("ShowOnlyToPlayersWithPermission", false);
            ymlFileConfig.set("RefundVotingPriceIfVotingFails", true);
            if (ymlFileConfig.contains("UseBossBarAPI")) {
              ymlFileConfig.set("UseBossBar", ymlFileConfig.getBoolean("UseBossBarAPI"));
              ymlFileConfig.set("UseBossBarAPI", null);
            }
            if (ymlFileConfig.contains("UseTitleAPI")) {
              ymlFileConfig.set("UseTitle", ymlFileConfig.getBoolean("UseTitleAPI"));
              ymlFileConfig.set("UseTitleAPI", null);
            }
            ymlFileConfig.set("ColoredConsoleText", true);
          }
          if (version < 1.4) {
            ymlFileConfig.set("GameVersion.SetOwn", false);
            ymlFileConfig.set("GameVersion.Version", "v1_13_R2");
          }
          ymlFileConfig.save(fileConfig);
        } catch (IOException e) {
          ServerLog.err("Can't update the Config.yml. [" + e.getMessage() + "]");
        }
      }
    }

    ServerLog.setUseColoredColores(ymlFileConfig.getBoolean("ColoredConsoleText"));

    if (!ymlFileConfig.getBoolean("GameVersion.SetOwn")) {
      ServerLog.log(
          "ServerType:Åòb "
              + VersionManager.getSetverTypeString()
              + "Åò9, Version:Åòb "
              + VersionManager.getBukkitVersion());
    } else {
      VersionManager.setVersionManager(
          ymlFileConfig.getString("GameVersion.Version"), ServerType.BUKKIT, true);
      ServerLog.log(
          "ServerType:Åòb "
              + VersionManager.getSetverTypeString()
              + "Åò9, Version:Åòb "
              + VersionManager.getBukkitVersion()
              + "Åò9 | Åòb(Self configurated)");
    }

    Options.votingTime = ymlFileConfig.getLong("VotingTime");
    Options.remindingTime = ymlFileConfig.getLong("RemindingTime");
    Options.timeoutPeriod = ymlFileConfig.getLong("TimeoutPeriod");
    Options.useScoreboard = ymlFileConfig.getBoolean("UseScoreboard");
    Options.useVoteGUI = ymlFileConfig.getBoolean("UseVoteGUI");

    if (ymlFileConfig.getBoolean("UseBossBar")) {
      if (VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R1
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R2
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R4
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R1
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R2
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R3) {
        if (Bukkit.getPluginManager().getPlugin("BossBarAPI") != null) {
          Options.useBossBar = true;
        }
      } else {
        Options.useBossBar = true;
      }
    }

    if (ymlFileConfig.getBoolean("UseTitle")) {
      if (VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R1
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R2
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R4
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_7_R3
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R1
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R2
          || VersionManager.getBukkitVersion() == BukkitVersion.v1_8_R3) {
        if (Bukkit.getPluginManager().getPlugin("TitleAPI") != null) {
          Options.useTitle = true;
        }
      } else {
        Options.useTitle = true;
      }
    }

    Options.checkForHiddenPlayers = ymlFileConfig.getBoolean("CheckForHiddenPlayers");
    Options.prematureEnd = ymlFileConfig.getBoolean("PrematureEnd");
    Options.price = ymlFileConfig.getDouble("Price");
    Options.rawMessages = ymlFileConfig.getBoolean("RawMessages");
    Options.disabledWorlds.addAll(ymlFileConfig.getStringList("DisabledWorld"));
    Options.votingInventoryMessages = ymlFileConfig.getBoolean("VotingInventoryMessages");
    Options.showVoteOnlyToPlayersWithPermission =
        ymlFileConfig.getBoolean("ShowOnlyToPlayersWithPermission");
    Options.refundVotingPriceIfVotingFails =
        ymlFileConfig.getBoolean("RefundVotingPriceIfVotingFails");

    File fileStats = new File("plugins/WeatherVote/Stats.yml");
    FileConfiguration ymlFileStats = YamlConfiguration.loadConfiguration(fileStats);

    if (!fileStats.exists()) {
      try {
        ymlFileStats.save(fileStats);
        ymlFileStats.set("Version", UpdateListener.getUpdateDoubleVersion());
        ymlFileStats.set("Date", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        ymlFileStats.set("Sunny.Yes", 0);
        ymlFileStats.set("Sunny.No", 0);
        ymlFileStats.set("Sunny.Won", 0);
        ymlFileStats.set("Sunny.Lost", 0);
        ymlFileStats.set("Rainy.Yes", 0);
        ymlFileStats.set("Rainy.No", 0);
        ymlFileStats.set("Rainy.Won", 0);
        ymlFileStats.set("Rainy.Lost", 0);
        ymlFileStats.set("MoneySpent", 0.00);
        ymlFileStats.save(fileStats);
      } catch (IOException e) {
        ServerLog.err("Can't create the Stats.yml. [" + e.getMessage() + "]");
      }
    } else {
      double version = ymlFileStats.getDouble("Version");

      if (version < UpdateListener.getUpdateDoubleVersion()) {
        try {
          ymlFileStats.set("Version", UpdateListener.getUpdateDoubleVersion());
          ymlFileStats.save(fileStats);
        } catch (IOException e) {
          ServerLog.err("Can't update the Stats.yml. [" + e.getMessage() + "]");
        }
      }
    }

    File fileMessages = new File("plugins/WeatherVote/Messages.yml");
    FileConfiguration ymlFileMessage = YamlConfiguration.loadConfiguration(fileMessages);

    if (!fileMessages.exists()) {
      try {
        ymlFileMessage.save(fileMessages);
        ymlFileMessage.set("Version", UpdateListener.getUpdateDoubleVersion());
        ymlFileMessage.set("[WeatherVote]", "&f[&9Weather&bVote&f] ");
        ymlFileMessage.set("Color.1", "&9");
        ymlFileMessage.set("Color.2", "&b");
        ymlFileMessage.set("Message.1", "You have to be a player, to use this command.");
        ymlFileMessage.set("Message.2", "You do not have the permission for this command.");
        ymlFileMessage.set(
            "Message.3",
            "&b&l[PLAYER]&9 started a new voting for &b&l[WEATHER]&9 weather, vote with &b&l/wv yes&9 or &b&l/wv no&9.");
        ymlFileMessage.set("Message.4", "The voting is disabled in this world.");
        ymlFileMessage.set("Message.5", "There is already a voting in this world.");
        ymlFileMessage.set("Message.6", "There isn't a voting in this world.");
        ymlFileMessage.set("Message.7", "You have already voted.");
        ymlFileMessage.set("Message.8", "You have voted for &b&lYes&9.");
        ymlFileMessage.set("Message.9", "You have voted for &b&lNo&9.");
        ymlFileMessage.set("Message.10", "The plugin is reloading...");
        ymlFileMessage.set("Message.11", "Reloading completed.");
        ymlFileMessage.set("Message.12", "The voting is over, the weather has been changed.");
        ymlFileMessage.set("Message.13", "The voting is over, the weather hasn't been changed.");
        ymlFileMessage.set(
            "Message.14",
            "The voting for &b&l[WEATHER]&9 weather is over in &b&l[SECONDS]&9 seconds.");
        ymlFileMessage.set(
            "Message.15",
            "You have to wait &b&l[SECONDS]&9 more second(s), until you can start a new voting.");
        ymlFileMessage.set(
            "Message.16",
            "There is a new update available for this plugin. &b( https://fof1092.de/WV )&9");
        ymlFileMessage.set("Message.17", "All players have voted.");
        ymlFileMessage.set("Message.18", "You need &b&l[MONEY]$&9 more to start a voting.");
        ymlFileMessage.set("Message.19", "You payed &b&l[MONEY]$&9 to start a voting.");
        ymlFileMessage.set("Message.20", "You opend the voting-inventory.");
        ymlFileMessage.set("Message.21", "Your Voting-Inventory has been closed.");
        ymlFileMessage.set("Message.22", "Try [COMMAND]");
        ymlFileMessage.set("Message.23", "You changed the weather to &b[WEATHER]&9.");
        ymlFileMessage.set("Message.24", "The voting has stopped.");
        ymlFileMessage.set("Message.25", "You stopped the voting.");
        ymlFileMessage.set("Text.1", "Sunny");
        ymlFileMessage.set("Text.2", "Rainy");
        ymlFileMessage.set("Text.3", "Yes");
        ymlFileMessage.set("Text.4", "No");
        ymlFileMessage.set("StatsText.1", "Stats since: ");
        ymlFileMessage.set("StatsText.2", "Money spent: ");
        ymlFileMessage.set("StatsText.3", "Total sunny votes: ");
        ymlFileMessage.set("StatsText.8", "Total rainy votes: ");
        ymlFileMessage.set("StatsText.4", "  Yes votes: ");
        ymlFileMessage.set("StatsText.5", "  No votes: ");
        ymlFileMessage.set("StatsText.6", "  Won: ");
        ymlFileMessage.set("StatsText.7", "  Lost: ");
        ymlFileMessage.set("HelpTextGui.1", "&b[&9Click to use this command&b]");
        ymlFileMessage.set("HelpTextGui.2", "&b[&9Next page&b]");
        ymlFileMessage.set("HelpTextGui.3", "&b[&9Last page&b]");
        ymlFileMessage.set(
            "HelpTextGui.4", "&7&oPage [PAGE]. &7Click on the arrows for the next page.");
        ymlFileMessage.set("HelpText.1", "This command shows you the help page.");
        ymlFileMessage.set("HelpText.2", "This command shows you the info page.");
        ymlFileMessage.set("HelpText.3", "This command shows you the stats page.");
        ymlFileMessage.set("HelpText.4", "This command opens the Voting-Inventory.");
        ymlFileMessage.set("HelpText.5", "This command allows you to start a sun voting.");
        ymlFileMessage.set("HelpText.6", "This command allows you to start a rain voting.");
        ymlFileMessage.set("HelpText.7", "This command allows you to vote for yes or no.");
        ymlFileMessage.set("HelpText.8", "' '");
        ymlFileMessage.set(
            "HelpText.9", "This command is reloading the Config.yml and Messages.yml file.");
        ymlFileMessage.set("HelpText.10", "This command stopps a voting.");
        ymlFileMessage.set("VotingInventoryTitle.1", "&f[&9&lW&bV&f] &bSunny&f/&bRainy");
        ymlFileMessage.set("VotingInventoryTitle.2", "&f[&9&lW&bV&f] &b[WEATHER]&9 Weather");
        ymlFileMessage.set(
            "BossBarMessage",
            "&f[&9&lW&b&lV&f] &9Voting for &b&l[WEATHER]&9 weather (&b&l/wv yes&9 or &b&l/wv no&9)");
        ymlFileMessage.set("TitleMessage.Title.1", "&f[&9&lW&b&lV&f] &b&l[WEATHER]&9 time voting.");
        ymlFileMessage.set(
            "TitleMessage.Title.2", "&f[&9&lW&b&lV&f] &b&l[SECONDS]&9 seconds left.");
        ymlFileMessage.set(
            "TitleMessage.Title.3", "&f[&9&lW&b&lV&f] &9The weather has been changed.");
        ymlFileMessage.set(
            "TitleMessage.Title.4", "&f[&9&lW&b&lV&f] &9The weather hasn't been changed.");
        ymlFileMessage.set("TitleMessage.SubTitle", "&9(&b/wv yes&9 or &b/wv no&9)");
        ymlFileMessage.set(
            "RawMessage.1",
            "[\"\",{\"text\":\"[PLAYER]\",\"color\":\"aqua\",\"bold\":true},{\"text\":\" started a new voting for \",\"color\":\"blue\"},{\"text\":\"[WEATHER]\",\"color\":\"aqua\",\"bold\":true},{\"text\":\" weather, vote with \",\"color\":\"blue\"},{\"text\":\"/wv yes\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/wv yes\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"/wv yes\",\"color\":\"aqua\"}]}}},{\"text\":\" or \",\"color\":\"blue\"},{\"text\":\"/wv no\",\"color\":\"aqua\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/wv no\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"/wv no\",\"color\":\"aqua\",\"bold\":true}]}}},{\"text\":\".\",\"color\":\"blue\"}]");
        ymlFileMessage.save(fileMessages);
      } catch (IOException e) {
        ServerLog.err("Can't create the Messages.yml. [" + e.getMessage() + "]");
      }
    } else {
      double version = ymlFileMessage.getDouble("Version");

      if (version < UpdateListener.getUpdateDoubleVersion()) {
        try {
          ymlFileMessage.set("Version", UpdateListener.getUpdateDoubleVersion());
          if (version <= 1.0) {
            ymlFileMessage.set("VotingInventoryTitle.1", "&f[&9W&bV&f] &bSunny&f/&bRainy");
            ymlFileMessage.set("VotingInventoryTitle.2", "&f[&9W&bV&f] &b[WEATHER]&9");
          }
          if (version < 1.02) {
            ymlFileMessage.set("Message.23", "You changed the weather to &b[WEATHER]&9.");
          }
          if (version < 1.1) {
            ymlFileMessage.set(
                "Message.16",
                "There is a new update available for this plugin. &b( https://fof1092.de/Plugins/WV )&9");
            ymlFileMessage.set(
                "BossBarMessage",
                "&f[&9W&bV&f] &9Voting for &b[WEATHER]&9 weather (&b/wv yes&9 or &b/wv no&9)");
            ymlFileMessage.set("TitleMessage.Title.1", "&f[&9W&bV&f] &b[WEATHER]&9 time voting.");
            ymlFileMessage.set("TitleMessage.Title.2", "&f[&9W&bV&f] &b[SECONDS]&9 seconds left.");
            ymlFileMessage.set(
                "TitleMessage.Title.3", "&f[&9W&bV&f] &9The weather has been changed.");
            ymlFileMessage.set(
                "TitleMessage.Title.4", "&f[&9W&bV&f] &9The weather hasn't been changed.");
            ymlFileMessage.set("TitleMessage.SubTitle", "&9(&b/wv yes&9 or &b/wv no&9)");
          }
          if (version < 1.11) {
            ymlFileMessage.set("HelpTextGui.1", "&b[&9Click to use this command&b]");
            ymlFileMessage.set("HelpTextGui.2", "&b[&9Next page&b]");
            ymlFileMessage.set("HelpTextGui.3", "&b[&9Last page&b]");
            ymlFileMessage.set(
                "HelpTextGui.4", "&7&oPage [PAGE]. &7Click on the arrows for the next page.");
          }
          if (version < 1.2) {
            ymlFileMessage.set("Message.24", "The voting has stopped.");
            ymlFileMessage.set("Message.25", "You stopped the voting.");
            ymlFileMessage.set("HelpText.10", "This command stopps a voting.");
          }
          if (version < 1.3) {
            if (ymlFileMessage.contains("BossBarAPIMessage")) {
              ymlFileMessage.set("BossBarMessage", ymlFileMessage.get("BossBarAPIMessage"));

              ymlFileMessage.set("BossBarAPIMessage", null);
            }
            if (ymlFileMessage.contains("TitleAPIMessage")) {
              ymlFileMessage.set(
                  "TitleMessage.Title.1", ymlFileMessage.getString("TitleAPIMessage.Title.1"));
              ymlFileMessage.set(
                  "TitleMessage.Title.2", ymlFileMessage.getString("TitleAPIMessage.Title.2"));
              ymlFileMessage.set(
                  "TitleMessage.Title.3", ymlFileMessage.getString("TitleAPIMessage.Title.3"));
              ymlFileMessage.set(
                  "TitleMessage.Title.4", ymlFileMessage.getString("TitleAPIMessage.Title.4"));
              ymlFileMessage.set(
                  "TitleMessage.SubTitle", ymlFileMessage.getString("TitleAPIMessage.SubTitle"));

              ymlFileMessage.set("TitleAPIMessage.Title.1", null);
              ymlFileMessage.set("TitleAPIMessage.Title.2", null);
              ymlFileMessage.set("TitleAPIMessage.Title.3", null);
              ymlFileMessage.set("TitleAPIMessage.Title.4", null);
              ymlFileMessage.set("TitleAPIMessage.SubTitle", null);
            }
          }

          ymlFileMessage.save(fileMessages);
        } catch (IOException e) {
          ServerLog.err("Can't update the Messages.yml. [" + e.getMessage() + "]");
        }
      }
    }

    Options.msg.put(
        "[WeatherVote]",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("[WeatherVote]")));
    Options.msg.put(
        "color.1",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.1")));
    Options.msg.put(
        "color.2",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.2")));
    Options.msg.put(
        "msg.1",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.1")));
    Options.msg.put(
        "msg.2",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.2")));
    Options.msg.put(
        "msg.3",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.3")));
    Options.msg.put(
        "msg.4",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.4")));
    Options.msg.put(
        "msg.5",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.5")));
    Options.msg.put(
        "msg.6",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.6")));
    Options.msg.put(
        "msg.7",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.7")));
    Options.msg.put(
        "msg.8",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.8")));
    Options.msg.put(
        "msg.9",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.9")));
    Options.msg.put(
        "msg.10",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.10")));
    Options.msg.put(
        "msg.11",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.11")));
    Options.msg.put(
        "msg.12",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.12")));
    Options.msg.put(
        "msg.13",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.13")));
    Options.msg.put(
        "msg.14",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.14")));
    Options.msg.put(
        "msg.15",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.15")));
    Options.msg.put(
        "msg.16",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.16")));
    Options.msg.put(
        "msg.17",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.17")));
    Options.msg.put(
        "msg.18",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.18")));
    Options.msg.put(
        "msg.19",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.19")));
    Options.msg.put(
        "msg.20",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.20")));
    Options.msg.put(
        "msg.21",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.21")));
    Options.msg.put(
        "msg.22",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.22")));
    Options.msg.put(
        "msg.23",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.23")));
    Options.msg.put(
        "msg.24",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.24")));
    Options.msg.put(
        "msg.25",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.25")));
    Options.msg.put(
        "text.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Text.1")));
    Options.msg.put(
        "text.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Text.2")));
    Options.msg.put(
        "text.3", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Text.3")));
    Options.msg.put(
        "text.4", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Text.4")));
    Options.msg.put(
        "statsText.1",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.1")));
    Options.msg.put(
        "statsText.2",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.2")));
    Options.msg.put(
        "statsText.3",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.3")));
    Options.msg.put(
        "statsText.4",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.4")));
    Options.msg.put(
        "statsText.5",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.5")));
    Options.msg.put(
        "statsText.6",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.6")));
    Options.msg.put(
        "statsText.7",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.7")));
    Options.msg.put(
        "statsText.8",
        ChatColor.translateAlternateColorCodes(
            '&', Options.msg.get("color.1") + ymlFileMessage.getString("StatsText.8")));
    Options.msg.put(
        "helpTextGui.1",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.1")));
    Options.msg.put(
        "helpTextGui.2",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.2")));
    Options.msg.put(
        "helpTextGui.3",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.3")));
    Options.msg.put(
        "helpTextGui.4",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.4")));
    Options.msg.put(
        "votingInventoryTitle.1",
        ChatColor.translateAlternateColorCodes(
            '&', ymlFileMessage.getString("VotingInventoryTitle.1")));
    Options.msg.put(
        "votingInventoryTitle.2",
        ChatColor.translateAlternateColorCodes(
            '&', ymlFileMessage.getString("VotingInventoryTitle.2")));
    Options.msg.put(
        "bossBarMessage",
        ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("BossBarMessage")));
    Options.msg.put(
        "titleMessage.Title.1",
        ChatColor.translateAlternateColorCodes(
            '&', ymlFileMessage.getString("TitleMessage.Title.1")));
    Options.msg.put(
        "titleMessage.Title.2",
        ChatColor.translateAlternateColorCodes(
            '&', ymlFileMessage.getString("TitleMessage.Title.2")));
    Options.msg.put(
        "titleMessage.Title.3",
        ChatColor.translateAlternateColorCodes(
            '&', ymlFileMessage.getString("TitleMessage.Title.3")));
    Options.msg.put(
        "titleMessage.Title.4",
        ChatColor.translateAlternateColorCodes(
            '&', ymlFileMessage.getString("TitleMessage.Title.4")));
    Options.msg.put(
        "titleMessage.SubTitle",
        ChatColor.translateAlternateColorCodes(
            '&', ymlFileMessage.getString("TitleMessage.SubTitle")));
    Options.msg.put("rmsg.1", ymlFileMessage.getString("RawMessage.1"));

    HelpPageListener.initializeHelpPageListener(
        "/WeatherVote help", Options.msg.get("[WeatherVote]"));

    CommandListener.addCommand(
        new Command(
            "/wv help (Page)",
            null,
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.1"))));
    CommandListener.addCommand(
        new Command(
            "/wv info",
            null,
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.2"))));
    CommandListener.addCommand(
        new Command(
            "/wv stats",
            null,
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.3"))));
    if (Options.useVoteGUI) {
      CommandListener.addCommand(
          new Command(
              "/wv",
              null,
              ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.4"))));
    }
    CommandListener.addCommand(
        new Command(
            "/wv sun",
            "WeatherVote.Sun",
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.5"))));
    CommandListener.addCommand(
        new Command(
            "/wv rain",
            "WeatherVote.Rain",
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.6"))));
    CommandListener.addCommand(
        new Command(
            "/wv yes",
            "WeatherVote.Vote",
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.7"))));
    CommandListener.addCommand(
        new Command(
            "/wv no",
            "WeatherVote.Vote",
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.8"))));
    CommandListener.addCommand(
        new Command(
            "/wv stopVoting [World]",
            "WeatherVote.StopVoting",
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.10"))));
    CommandListener.addCommand(
        new Command(
            "/wv reload",
            "WeatherVote.Reload",
            ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.9"))));
  }

  public static void disable() {
    for (World w : Bukkit.getWorlds()) {
      if (Options.useVoteGUI) {
        VotingGUIListener.closeVotingGUIsAtWorld(w.getName());
      }

      if (WeatherVoteListener.isVoting(w.getName())) {
        WeatherVoteListener.getVoteing(w.getName()).stopVoting(true);
      }
    }

    WeatherVoteListener.weatherVotes.clear();
    Options.disabledWorlds.clear();

    CommandListener.clearCommands();
  }
}
