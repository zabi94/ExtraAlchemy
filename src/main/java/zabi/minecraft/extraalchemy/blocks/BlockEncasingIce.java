package zabi.minecraft.extraalchemy.blocks;

import java.util.Random;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEncasingIce extends BlockBreakable {

	public BlockEncasingIce(Material materialIn) {
        super(Material.ICE, false);
        this.setDefaultSlipperiness(0.98f);
        this.setCreativeTab(null);
        this.setHardness(0.5F).setLightOpacity(3).setUnlocalizedName("encasing_ice");
	}

	@Override
	public SoundType getSoundType() {
		return SoundType.GLASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	public int quantityDropped(Random random) {
        return 0;
    }

	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		if (!worldIn.isRemote) {
			worldIn.scheduleUpdate(pos, state.getBlock(), (3+(worldIn.rand.nextInt(7)))*20);
		}
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
            worldIn.setBlockToAir(pos);
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (entity instanceof EntityShulker || entity instanceof EntityEnderman) return; //anything that can tp basically
		entity.setInWeb();
		entity.posX = entity.prevPosX;
		entity.posY = entity.prevPosY;
		entity.posZ = entity.prevPosZ;
		entity.rotationPitch = entity.prevRotationPitch;
		entity.rotationYaw = entity.prevRotationYaw;
		entity.setSprinting(false);
		entity.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
	}
}
