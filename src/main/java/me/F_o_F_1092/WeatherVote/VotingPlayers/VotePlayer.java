package me.F_o_F_1092.WeatherVote.VotingPlayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VotePlayer {

  private final UUID playerId;
  private final boolean yesNo;

  public VotePlayer(UUID playerId, boolean yesNo) {
    this.playerId = playerId;
    this.yesNo = yesNo;
  }

  public UUID getPlayerId() {
    return this.playerId;
  }

  public Player getPlayer() {
    return Bukkit.getPlayer(this.playerId);
  }

  public boolean getYesNo() {
    return this.yesNo;
  }
}
