package me.F_o_F_1092.WeatherVote;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * The <i>Utils</i> class. TODO: Document.
 *
 * @author Jab
 */
public class Utils {

  /**
   * Formats text using the '&' alternative color code.
   *
   * @param strings The Strings to format.
   * @return Returns a list of formatted strings. If the list is null, null is returned. If the list
   *     is empty, a empty list is returned.
   */
  public static List<String> color(List<String> strings) {
    return color('&', strings);
  }

  /**
   * Formats text using the given alternative color code.
   *
   * @param strings The Strings to format.
   * @param code The alternative color code to use.
   * @return Returns a list of formatted strings. If the list is null, null is returned. If the list
   *     is empty, a empty list is returned.
   */
  public static List<String> color(char code, List<String> strings) {

    if (strings == null) {
      return null;
    } else if (strings.isEmpty()) {
      return new ArrayList<>();
    }

    List<String> list = new ArrayList<>();
    for (String line : strings) {
      list.add(color(code, line));
    }
    return list;
  }

  /**
   * Formats text using the '&' alternative color code.
   *
   * @param string The String to format.
   * @return Returns a formatted string. If the string is null, null is returned. If the string is
   *     empty, a empty string is returned.
   */
  public static String color(String string) {
    return color('&', string);
  }

  /**
   * Formats text using the given alternative color code.
   *
   * @param string The String to format.
   * @param code The alternative color code to use.
   * @return Returns a formatted string. If the string is null, null is returned. If the string is
   *     empty, a empty string is returned.
   */
  public static String color(char code, String string) {

    if (string == null) {
      return null;
    } else if (string.isEmpty()) {
      return "";
    }

    return ChatColor.translateAlternateColorCodes(code, string);
  }
}
