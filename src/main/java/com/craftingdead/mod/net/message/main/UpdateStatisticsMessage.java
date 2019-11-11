package com.craftingdead.mod.net.message.main;

public class UpdateStatisticsMessage {

  private final int daysSurvived;
  private final int zombiesKilled;
  private final int playersKilled;
  private final int water;
  private final int maxWater;
  private final int stamina;
  private final int maxStamina;

  public UpdateStatisticsMessage(int daysSurvived, int zombiesKilled, int playersKilled, int water,
      int maxWater, int stamina, int maxStamina) {
    this.daysSurvived = daysSurvived;
    this.zombiesKilled = zombiesKilled;
    this.playersKilled = playersKilled;
    this.water = water;
    this.maxWater = maxWater;
    this.stamina = stamina;
    this.maxStamina = maxStamina;
  }

  public int getDaysSurvived() {
    return daysSurvived;
  }

  public int getZombiesKilled() {
    return zombiesKilled;
  }

  public int getPlayersKilled() {
    return playersKilled;
  }

  public int getWater() {
    return water;
  }

  public int getMaxWater() {
    return maxWater;
  }

  public int getStamina() {
    return stamina;
  }

  public int getMaxStamina() {
    return maxStamina;
  }
}
