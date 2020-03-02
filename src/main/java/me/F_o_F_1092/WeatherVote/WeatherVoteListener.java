package me.F_o_F_1092.WeatherVote;

import me.F_o_F_1092.WeatherVote.VotingPlayers.VotePlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class WeatherVoteListener {

  static ArrayList<WeatherVote> weatherVotes = new ArrayList<WeatherVote>();

  static void addWeatherVote(WeatherVote weatherVote) {
    weatherVotes.add(weatherVote);
  }

  public static void removeWeatherVote(String worldName) {
    weatherVotes.remove(getVoteing(worldName));
  }

  public static WeatherVote getVoteing(String worldName) {
    for (WeatherVote weatherVote : weatherVotes) {
      if (weatherVote.getWorldName().equals(worldName)) {
        return weatherVote;
      }
    }

    return null;
  }

  public static boolean isVoting(String worldName) {
    return getVoteing(worldName) != null;
  }

  boolean hasVote(WeatherVote weatherVote, UUID uuid) {

    for (VotePlayer votePlayer : weatherVote.getVotePlayers()) {
      if (votePlayer.getPlayerId().equals(uuid)) {
        return true;
      }
    }

    return false;
  }

  VotePlayer getVote(WeatherVote weatherVote, UUID uuid) {

    for (VotePlayer votePlayer : weatherVote.getVotePlayers()) {
      if (votePlayer.getPlayerId().equals(uuid)) {
        return votePlayer;
      }
    }

    return null;
  }

  public static Economy getVault() {
    return Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
  }

  public static boolean isVaultInUse() {
    return Options.vault && Options.price > 0.00;
  }
}
