package zabi.minecraft.extraalchemy.blocks;

import java.util.Random;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEncasingIce extends BlockBreakable {

	public BlockEncasingIce(Material materialIn) {
        super(Material.ICE, false);
        this.slipperiness = 0.98F;
        this.setCreativeTab(null);
        this.setTickRandomly(true);
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
		return EnumPushReaction.NORMAL;
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
            worldIn.setBlockToAir(pos);
    }
}
