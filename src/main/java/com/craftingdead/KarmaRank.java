package com.craftingdead;

public enum KarmaRank {

	HERO_1(1000), HERO_2(2500), HERO_3(4000), HERO_4(7000), NONE(0), BANDIT_1(-1000), BANDIT_2(-2500), BANDIT_3(-4000),
	BANDIT_4(-7000);

	private final int minimumKarma;

	KarmaRank(int minimumScore) {
		this.minimumKarma = minimumScore;
	}

	public static KarmaRank getKarmaRank(int karma) {
		for (KarmaRank rank : KarmaRank.values()) {
			if (rank.minimumKarma > 0 && karma >= rank.minimumKarma) {
				return rank;
			} else if (rank.minimumKarma < 0 && karma <= rank.minimumKarma) {
				return rank;
			}
		}
		return KarmaRank.NONE;
	}

}
