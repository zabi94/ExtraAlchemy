package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.LibMod;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandMixin extends LockableContainerBlockEntity implements SidedInventory, Tickable {

	@Shadow private int fuel;
	
	private static final Identifier HEAT_SOURCE_TAG = LibMod.id("heat_source");
	private static final Identifier HEAT_CONDUCTOR_TAG = LibMod.id("heat_conductor");
	
	protected BrewingStandMixin(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void checkFireUnderneath(CallbackInfo ci) {
		if (!world.isClient && ModConfig.INSTANCE.enableBrewingStandFire) {
			if (fuel < 20 && world.getTime() % 40 == 0 && isHeated(world, pos)) {
				fuel++;
				this.markDirty();
			}
		}
	}
	
	public boolean isHeated(World world, BlockPos pos) {
		BlockState oneBelow = world.getBlockState(pos.down());
		BlockState twoBelow = world.getBlockState(pos.down(2));
		return isHeatSource(oneBelow) || (isTransmissiveBlock(oneBelow) && isHeatSource(twoBelow));
	}

	public boolean isHeatSource(BlockState state) {
		return BlockTags.getContainer().getOrCreate(HEAT_SOURCE_TAG).contains(state.getBlock());
	}
	
	public boolean isTransmissiveBlock(BlockState state) {
		return BlockTags.getContainer().getOrCreate(HEAT_CONDUCTOR_TAG).contains(state.getBlock());
	}
	
}
