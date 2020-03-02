package me.F_o_F_1092.WeatherVote.VotingGUI;

import me.F_o_F_1092.WeatherVote.Options;
import me.F_o_F_1092.WeatherVote.PluginManager.ServerLog;
import me.F_o_F_1092.WeatherVote.WeatherVote;
import me.F_o_F_1092.WeatherVote.WeatherVote.Weather;
import me.F_o_F_1092.WeatherVote.WeatherVoteListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static me.F_o_F_1092.WeatherVote.Utils.color;

public class VotingGUIPlayer {

  protected UUID uuid;
  protected String worldName;

  protected VotingGUIPlayer(UUID uuid, String worldName) {
    this.uuid = uuid;
    this.worldName = worldName;
  }

  UUID getUUID() {
    return this.uuid;
  }

  String getWorldName() {
    return this.worldName;
  }

  protected void openVoteingGUI() {
    Inventory voteGUI;
    ItemStack voteGUIItemOne;
    ItemStack voteGUIItemTwo;

    if (!WeatherVoteListener.isVoting(this.worldName)) {
      try {
        voteGUI = Bukkit.createInventory(null, 9, Options.msg.get("votingInventoryTitle.1"));
      } catch (Exception e) {
        voteGUI =
            Bukkit.createInventory(
                null, 9, color("Åòf[Åò9ÅòlWÅòbÅòlVÅòf] ") + Options.msg.get("color.2") + "Voting...");

        ServerLog.err("The Voting-Inventory text caused a problem. [" + e.getMessage() + "]");
      }

      voteGUIItemOne = new ItemStack(Material.YELLOW_CONCRETE, 1);
      ItemMeta voteGUIItemOneMeta = voteGUIItemOne.getItemMeta();
      voteGUIItemOneMeta.setDisplayName("ÅòeÅòl" + Options.msg.get("text.1"));
      voteGUIItemOne.setItemMeta(voteGUIItemOneMeta);

      voteGUIItemTwo = new ItemStack(Material.BLUE_CONCRETE, 1);
      ItemMeta voteGUIItemTwoMeta = voteGUIItemTwo.getItemMeta();
      voteGUIItemTwoMeta.setDisplayName("Åò9Åòl" + Options.msg.get("text.2"));
      voteGUIItemTwo.setItemMeta(voteGUIItemTwoMeta);
    } else {
      WeatherVote WeatherVote = WeatherVoteListener.getVoteing(this.worldName);

      try {
        String replace = Options.msg.get("votingInventoryTitle.2");
        if (WeatherVote.getWeather() == Weather.SUN) {
          replace = replace.replace("[WEATHER]", Options.msg.get("text.1"));
        } else {
          replace = replace.replace("[WEATHER]", Options.msg.get("text.2"));
        }
        voteGUI = Bukkit.createInventory(null, 9, replace);

      } catch (Exception e) {
        if (WeatherVote.getWeather() == Weather.SUN) {
          voteGUI =
              Bukkit.createInventory(
                  null, 9, "Åòf[Åò9ÅòlWÅòbÅòlVÅòf] " + Options.msg.get("color.2") + "Sunny");
        } else {
          voteGUI =
              Bukkit.createInventory(
                  null, 9, "Åòf[Åò9ÅòlWÅòbÅòlVÅòf] " + Options.msg.get("color.2") + "Rainy");
        }

        ServerLog.err("The Voring-Inventory text caused a problem. [" + e.getMessage() + "]");
      }

      voteGUIItemOne = new ItemStack(Material.LIME_CONCRETE, 1);
      ItemMeta voteGUIItemOneMeta = voteGUIItemOne.getItemMeta();
      voteGUIItemOneMeta.setDisplayName("ÅòaÅòl" + Options.msg.get("text.3"));
      voteGUIItemOne.setItemMeta(voteGUIItemOneMeta);

      voteGUIItemTwo = new ItemStack(Material.RED_CONCRETE, 1);
      ItemMeta voteGUIItemTwoMeta = voteGUIItemTwo.getItemMeta();
      voteGUIItemTwoMeta.setDisplayName("ÅòcÅòl" + Options.msg.get("text.4"));
      voteGUIItemTwo.setItemMeta(voteGUIItemTwoMeta);
    }

    ItemStack blockedItem = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
    ItemMeta blockedItemMeta = voteGUIItemOne.getItemMeta();
    blockedItemMeta.setDisplayName(" ");
    blockedItem.setItemMeta(blockedItemMeta);

    voteGUI.setItem(0, blockedItem);
    voteGUI.setItem(1, voteGUIItemOne);
    voteGUI.setItem(2, voteGUIItemOne);
    voteGUI.setItem(3, voteGUIItemOne);
    voteGUI.setItem(4, blockedItem);
    voteGUI.setItem(5, voteGUIItemTwo);
    voteGUI.setItem(6, voteGUIItemTwo);
    voteGUI.setItem(7, voteGUIItemTwo);
    voteGUI.setItem(8, blockedItem);

    Bukkit.getPlayer(this.uuid).openInventory(voteGUI);
  }

  void closeInventory() {
    Bukkit.getPlayer(this.uuid).closeInventory();
  }
}
