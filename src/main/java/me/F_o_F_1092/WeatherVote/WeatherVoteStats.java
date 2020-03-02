package me.F_o_F_1092.WeatherVote;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WeatherVoteStats {

  File fileStats = new File("plugins/WeatherVote/Stats.yml");
  FileConfiguration ymlFileStats = YamlConfiguration.loadConfiguration(fileStats);

  String date = ymlFileStats.getString("Date");

  int sunnyYes = ymlFileStats.getInt("Sunny.Yes");
  int sunnyNo = ymlFileStats.getInt("Sunny.No");
  int sunnyWon = ymlFileStats.getInt("Sunny.Won");
  int sunnyLost = ymlFileStats.getInt("Sunny.Lost");

  int rainyYes = ymlFileStats.getInt("Rainy.Yes");
  int rainyNo = ymlFileStats.getInt("Rainy.No");
  int rainyWon = ymlFileStats.getInt("Rainy.Won");
  int rainyLost = ymlFileStats.getInt("Rainy.Lost");

  double moneySpent = ymlFileStats.getDouble("MoneySpent");

  public WeatherVoteStats() {}

  void setSunnyStats(int yes, int no, boolean won, double moneySpent) {
    this.sunnyYes += yes;
    this.sunnyNo += no;

    if (won) {
      this.sunnyWon++;
    } else {
      this.sunnyLost++;
    }

    this.moneySpent += moneySpent;

    save();
  }

  void setRainyStats(int yes, int no, boolean won, double moneySpent) {
    this.rainyYes += yes;
    this.rainyNo += no;

    if (won) {
      this.rainyWon++;
    } else {
      this.rainyLost++;
    }

    this.moneySpent += moneySpent;

    save();
  }

  void save() {
    if (fileStats.exists()) {
      try {
        ymlFileStats.set("Sunny.Yes", this.sunnyYes);
        ymlFileStats.set("Sunny.No", this.sunnyNo);
        ymlFileStats.set("Sunny.Won", this.sunnyWon);
        ymlFileStats.set("Sunny.Lost", this.sunnyLost);
        ymlFileStats.set("Rainy.Yes", this.rainyYes);
        ymlFileStats.set("Rainy.No", this.rainyNo);
        ymlFileStats.set("Rainy.Won", this.rainyWon);
        ymlFileStats.set("Rainy.Lost", this.rainyLost);
        ymlFileStats.set("MoneySpent", this.moneySpent);
        ymlFileStats.save(fileStats);
      } catch (IOException e1) {

      }
    }
  }

  String getDate() {
    return this.date;
  }

  int getRainyYes() {
    return this.rainyYes;
  }

  int getRainyNo() {
    return this.rainyNo;
  }

  int getRainyWon() {
    return this.rainyWon;
  }

  int getRainyLost() {
    return this.rainyLost;
  }

  int getSunnyYes() {
    return this.sunnyYes;
  }

  int getSunnyNo() {
    return this.sunnyNo;
  }

  int getSunnyWon() {
    return this.sunnyWon;
  }

  int getSunnyLost() {
    return this.sunnyLost;
  }

  int getSunnyVotes() {
    return getSunnyLost() + getSunnyWon();
  }

  int getRainyVotes() {
    return getRainyLost() + getRainyWon();
  }

  int getVotes() {
    return getSunnyVotes() + getRainyVotes();
  }

  double getMoneySpent() {
    return this.moneySpent;
  }
}
