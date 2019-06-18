package com.craftingdead.mod.message.server;

import lombok.Data;

@Data
public class SUpdateStatisticsMessage {

	private final int daysSurvived;
	private final int zombiesKilled;
	private final int playersKilled;
}
