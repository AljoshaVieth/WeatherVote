package me.F_o_F_1092.WeatherVote.PluginManager;

import java.util.List;

public class JSONMessageListener {

  public static String putJSONMessagesTogether(List<JSONMessage> jsonMessages) {

    StringBuilder jsonString = new StringBuilder("[\"\"");

    for (JSONMessage jsonMessage : jsonMessages) {
      jsonString.append(",").append(jsonMessage.getJsonText());
    }

    jsonString.append("]");

    return jsonString.toString();
  }
}
