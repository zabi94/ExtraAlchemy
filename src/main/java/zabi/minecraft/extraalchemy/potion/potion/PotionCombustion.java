package zabi.minecraft.extraalchemy.potion.potion;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionCombustion extends PotionBase {

	public static int tickController = 0;
	
	public PotionCombustion(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "combustion");
		this.setIconIndex(5, 1);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		
		boolean tickFrequent = tickController%10==1;
		boolean tickRare = ((amp>0)?(tickController%40):(tickController))==1;

		if (tickRare) {
			e.setFire(5);
			if (e instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) e;
				if (p.isSpectator()) return;
				if (!p.getEntityWorld().isRemote) {
					if (p.getHeldItemMainhand()!=null && !FurnaceRecipes.instance().getSmeltingResult(p.getHeldItemMainhand()).isEmpty() ) {
						ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(p.getHeldItemMainhand());
						stack.setCount(1);

						if (p.inventory.addItemStackToInventory(stack)) {
							p.getHeldItemMainhand().setCount(p.getHeldItemMainhand().getCount()-1);
							p.inventory.markDirty();
						}
					}

					if (p.getHeldItemOffhand()!=null && FurnaceRecipes.instance().getSmeltingResult(p.getHeldItemOffhand()).isEmpty() ) {
						ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(p.getHeldItemOffhand());
						stack.setCount(1);
						if (p.inventory.addItemStackToInventory(stack)) {
							p.getHeldItemOffhand().setCount(p.getHeldItemOffhand().getCount()-1);
							p.inventory.markDirty();;
						}
					}
				}
			}
		}
		//Taken from mods.railcraft.common.items.firestone.FirestoneTickHandler class
		// --<COPY>
		if (tickFrequent && !e.getEntityWorld().isRemote && e.getEntityWorld().getGameRules().getBoolean("doFireTick")) {
			Random rnd = e.getRNG();
			int x = (int) Math.round(e.posX) - 5 + rnd.nextInt(12);
			int y = (int) Math.round(e.posY) - 5 + rnd.nextInt(12);
			int z = (int) Math.round(e.posZ) - 5 + rnd.nextInt(12);
			if (y < 1) y = 1;
			if (y > e.getEntityWorld().getActualHeight()) y = e.getEntityWorld().getActualHeight() - 2;
			BlockPos pos = new BlockPos(x, y, z);
			if (canBurn(e.getEntityWorld(), pos)) e.getEntityWorld().setBlockState(pos, Blocks.FIRE.getDefaultState());
		}
		// --</COPY>
	}
	
	//Taken from mods.railcraft.common.items.firestone.FirestoneTickHandler class
	//	--<COPY>
	private boolean canBurn(World world, BlockPos pos) {
        if (!world.isAirBlock(pos)) return false;
        for (EnumFacing side : EnumFacing.VALUES) {
            BlockPos offset = pos.offset(side);
            if (!world.isAirBlock(pos.offset(side))) {
                Block block = world.getBlockState(offset).getBlock();
                if (block != Blocks.FIRE) return true;
            }
        }
        return false;
    }
	//	--</COPY>


	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	public static class PotionCombustionHandler {
		
		@SubscribeEvent
		public void onTick(TickEvent.ServerTickEvent evt) {
			tickController++;
			tickController=tickController%80;
		}
	}
	
	

}
