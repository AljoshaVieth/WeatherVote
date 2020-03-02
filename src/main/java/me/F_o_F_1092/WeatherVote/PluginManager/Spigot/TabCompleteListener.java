package me.F_o_F_1092.WeatherVote.PluginManager.Spigot;

import me.F_o_F_1092.WeatherVote.PluginManager.Command;
import me.F_o_F_1092.WeatherVote.PluginManager.CommandListener;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener
    extends me.F_o_F_1092.WeatherVote.PluginManager.TabCompleteListener {

  public static List<String> completeTab(CommandSender cs, String startCommand) {

    List<String> strings = new ArrayList<>();

    for (Command command : CommandListener.getAllCommands()) {
      if (!command.getCommandString().equals(CommandListener.getMainCommand())) {
        if (command.getPermission() == null || cs.hasPermission(command.getPermission())) {
          strings.add(getNextArg(startCommand, command.getCommandString()));
        }
      }
    }

    return strings;
  }
}
