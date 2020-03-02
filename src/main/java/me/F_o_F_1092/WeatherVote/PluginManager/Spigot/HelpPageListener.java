package me.F_o_F_1092.WeatherVote.PluginManager.Spigot;

import me.F_o_F_1092.WeatherVote.Options;
import me.F_o_F_1092.WeatherVote.PluginManager.Command;
import me.F_o_F_1092.WeatherVote.PluginManager.CommandListener;
import me.F_o_F_1092.WeatherVote.PluginManager.HelpMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpPageListener extends me.F_o_F_1092.WeatherVote.PluginManager.HelpPageListener {

  public static void sendMessage(Player player, int page) {
    List<HelpMessage> personalHelpMessages = getAllPersonalHelpMessages(player);
    List<HelpMessage> personalHelpPageMessages =
        getHelpPageMessages(player, personalHelpMessages, page);

    player.sendMessage("");
    JSONMessageListener.send(
        player, getNavBar(personalHelpMessages, personalHelpPageMessages, page));
    player.sendMessage("");
    for (HelpMessage hm : personalHelpPageMessages) {
      JSONMessageListener.send(player, hm.getJsonString());
    }
    player.sendMessage("");

    if (getMaxPlayerPages(personalHelpMessages) != 1) {
      player.sendMessage(Options.msg.get("helpTextGui.4").replace("[PAGE]", (page + 1) + ""));
    }

    JSONMessageListener.send(
        player, getNavBar(personalHelpMessages, personalHelpPageMessages, page));
    player.sendMessage("");
  }

  public static void sendNormalMessage(CommandSender commandSender) {
    commandSender.sendMessage(
        Options.msg.get("color.1")
            + "Åòm----------------Åòr "
            + pluginNametag
            + Options.msg.get("color.1")
            + "Åòm----------------");
    commandSender.sendMessage("");
    for (Command command : CommandListener.getAllCommands()) {
      commandSender.sendMessage(command.getHelpMessage().getNormalString());
    }
    commandSender.sendMessage("");
    commandSender.sendMessage(
        Options.msg.get("color.1")
            + "Åòm----------------Åòr "
            + pluginNametag
            + Options.msg.get("color.1")
            + "Åòm----------------");
  }

  private static List<HelpMessage> getHelpPageMessages(
      Player player, List<HelpMessage> personalHelpMessages, int page) {
    List<HelpMessage> personalHelpPageMessages = new ArrayList<>();

    for (int index = 0; index < maxHelpMessages; index++) {
      if (personalHelpMessages.size() >= (page * maxHelpMessages + index + 1)) {
        personalHelpPageMessages.add(personalHelpMessages.get(page * maxHelpMessages + index));
      }
    }

    return personalHelpPageMessages;
  }

  public static int getMaxPlayerPages(Player player) {
    return (int)
        java.lang.Math.ceil(
            ((double) getAllPersonalHelpMessages(player).size() / (double) maxHelpMessages));
  }

  private static List<HelpMessage> getAllPersonalHelpMessages(Player player) {

    List<HelpMessage> personalHelpMessages = new ArrayList<>();

    for (Command command : CommandListener.getAllCommands()) {
      if (command.getPermission() == null || player.hasPermission(command.getPermission())) {
        personalHelpMessages.add(command.getHelpMessage());
      }
    }

    return personalHelpMessages;
  }
}
