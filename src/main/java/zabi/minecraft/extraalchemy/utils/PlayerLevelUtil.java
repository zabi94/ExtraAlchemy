package zabi.minecraft.extraalchemy.utils;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerLevelUtil {
	
	private static final int MAX_LEVEL = 21862;
	private static final int[] xpmap = new int[MAX_LEVEL + 1];
	
	public static int getExperienceForLevel(int level) {
		if (level <= 0) {
			return 0;
		}
		if (level > MAX_LEVEL) {
			return Integer.MAX_VALUE;
		}
		return xpmap[level];
	}

	static {
		int res = 0;
		for (int i = 0; i <= MAX_LEVEL; i++) {
			if (res < 0) {
				res = Integer.MAX_VALUE;
				Log.e("Internal XP calculation is wrong. Level " + i + " already maxes out.");
			}
			xpmap[i] = res;
			res += getXpBarCapacity(i);
		}
	}

	public static int getXpBarCapacity(int level) {
		if (level >= 30) {
			return 112 + (level - 30) * 9;
		} else {
			return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
		}
	}

	public static int getLevelForExperience(int experience) {
		for (int i = 1; i < xpmap.length; i++) {
			if (xpmap[i] > experience) {
				return i - 1;
			}
		}
		return xpmap.length;
	}

	public static int getPlayerXP(PlayerEntity player) {
		return (int) (getExperienceForLevel(player.experienceLevel) + (player.experienceProgress * player.getNextLevelExperience()));
	}

	public static void addPlayerXP(PlayerEntity player, int amount) {
		int experience = Math.max(0, getPlayerXP(player) + amount);
		player.totalExperience = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float) (experience - expForLevel) / (float) getXpBarCapacity(player.experienceLevel);
	}
}
