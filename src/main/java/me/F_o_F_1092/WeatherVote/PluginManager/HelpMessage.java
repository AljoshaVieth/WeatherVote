package me.F_o_F_1092.WeatherVote.PluginManager;

import me.F_o_F_1092.WeatherVote.Options;
import me.F_o_F_1092.WeatherVote.PluginManager.VersionManager.ServerType;

import java.util.ArrayList;
import java.util.List;

public class HelpMessage {

  private String helpMessage;
  private String jsonHelpMessage;

  HelpMessage(String commandString, String commandColloredString, String helpMessage) {
    String shortHelpMessage;

    if (helpMessage.length()
        > (60
            - commandColloredString
                .replace(Options.msg.get("color.2"), "")
                .replace(Options.msg.get("color.1"), "")
                .length()
            - 3)) {
      shortHelpMessage =
          helpMessage.substring(
                  0,
                  (60
                      - commandColloredString
                          .replace(Options.msg.get("color.2"), "")
                          .replace(Options.msg.get("color.1"), "")
                          .length()
                      - 3))
              + "...";
    } else {
      shortHelpMessage = helpMessage;
    }

    List<JSONMessage> jsonMessages = new ArrayList<>();

    JSONMessage jsonHelpCommand = new JSONMessage(commandColloredString);
    jsonHelpCommand.setPreviewCommand(commandString);
    jsonHelpCommand.setHoverText(Options.msg.get("helpTextGui.1"));

    JSONMessage jsonSplitMessage = new JSONMessage(Options.msg.get("color.2") + " \u2771 ");

    JSONMessage jsonHelpMessage = new JSONMessage(shortHelpMessage);
    jsonHelpMessage.setHoverText(helpMessage);

    jsonMessages.add(jsonHelpCommand);
    jsonMessages.add(jsonSplitMessage);
    jsonMessages.add(jsonHelpMessage);

    if (VersionManager.getServerType() == ServerType.BUKKIT) {
      this.jsonHelpMessage =
          me.F_o_F_1092.WeatherVote.PluginManager.Spigot.JSONMessageListener
              .putJSONMessagesTogether(jsonMessages);
    }
    this.helpMessage = Options.msg.get("color.2") + commandColloredString + " §f" + helpMessage;
  }

  public String getJsonString() {
    return this.jsonHelpMessage;
  }

  public String getNormalString() {
    return this.helpMessage;
  }
}
