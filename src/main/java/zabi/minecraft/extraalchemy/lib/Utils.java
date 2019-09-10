package zabi.minecraft.extraalchemy.lib;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("deprecation")
public class Utils {

	//XP code math taken from 
	//https://raw.githubusercontent.com/SleepyTrousers/EnderIO/bc4a9a24e08ed5f259962b83ae6211371d3bddc4/enderio-base/src/main/java/crazypants/enderio/base/xp/XpUtil.java
	
	//Minecraft, this is bullshit
	private static final Field bis = ReflectionHelper.findField(DragonFightManager.class, "bossInfo", "field_186109_c");

	private static final int MAX_LEVEL = 21862;
	private static final int[] xpmap = new int[MAX_LEVEL + 1];

	private Utils() {}

	public static void teleportThroughDimensions(EntityPlayer player, int dimension, double x, double y, double z) {
		int oldDimension = player.getEntityWorld().provider.getDimension();
		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
		MinecraftServer server = player.getEntityWorld().getMinecraftServer();
		WorldServer worldServer = server.getWorld(dimension);
		DragonFightManager dfm = null;
		if (player.getEntityWorld().provider instanceof WorldProviderEnd) {
			dfm = ((WorldProviderEnd) player.getEntityWorld().provider).getDragonFightManager();
		}
		player.addExperienceLevel(0);
		worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(entityPlayerMP, dimension, new Teleporter(worldServer) {
			@Override
			public void placeInPortal(Entity entityIn, float rotationYaw) {
			}
		});
		player.setPositionAndUpdate(x, y, z);
		if (oldDimension == 1) {
			if (dfm!=null && player instanceof EntityPlayerMP) {
				try {
					BossInfoServer bossInfo= (BossInfoServer) bis.get(dfm);
					bossInfo.removePlayer((EntityPlayerMP) player);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			player.setPositionAndUpdate(x, y, z);
			worldServer.spawnEntity(player);
			worldServer.updateEntityWithOptionalForce(player, false);
		}
	}

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

	public static int getPlayerXP(EntityPlayer player) {
		return (int) (getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
	}

	public static void addPlayerXP(EntityPlayer player, int amount) {
		int experience = Math.max(0, getPlayerXP(player) + amount);
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (float) (experience - expForLevel) / (float) getXpBarCapacity(player.experienceLevel);
	}

}
