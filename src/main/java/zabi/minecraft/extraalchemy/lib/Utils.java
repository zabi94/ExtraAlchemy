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

public class Utils {

	//Minecraft, this is bullshit
	private static final Field bis = ReflectionHelper.findField(DragonFightManager.class, "bossInfo", "field_186109_c");
	
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
	
}
