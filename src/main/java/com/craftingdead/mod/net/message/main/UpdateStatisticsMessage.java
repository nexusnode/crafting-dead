package com.craftingdead.mod.net.message.main;

import lombok.Data;

@Data
public class UpdateStatisticsMessage {

  private final int daysSurvived;
  private final int zombiesKilled;
  private final int playersKilled;
  private final int water;
  private final int maxWater;
}
