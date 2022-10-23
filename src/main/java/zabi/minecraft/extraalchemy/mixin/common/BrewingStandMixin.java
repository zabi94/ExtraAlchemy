package zabi.minecraft.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.LibMod;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandMixin extends LockableContainerBlockEntity implements SidedInventory {

	@Shadow private int fuel;
	
	private static final TagKey<Block> HEAT_SOURCE_TAG = TagKey.of(Registry.BLOCK_KEY, LibMod.id("heat_source"));
	private static final TagKey<Block> HEAT_CONDUCTOR_TAG = TagKey.of(Registry.BLOCK_KEY, LibMod.id("heat_conductor"));
	
	protected BrewingStandMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private static void checkFireUnderneath(World world, BlockPos pos, BlockState state, BrewingStandBlockEntity blockEntity, CallbackInfo ci) {
		((BrewingStandMixin) (Object) blockEntity).mixinLogicProxy();
	}

	public void mixinLogicProxy() {
		if (!world.isClient && ModConfig.INSTANCE.enableBrewingStandFire) {

			int maxFuelCapacity = Math.min(20, ModConfig.INSTANCE.brewingStandFireMaxCapacity);
			int delayTicks = 20 * ModConfig.INSTANCE.brewingStandHeatIncrementDelay;
			if (fuel < maxFuelCapacity && world.getTime() % delayTicks == 0 && isHeated(world, pos)) {
				fuel++;
				this.markDirty();
			}
		}
	}
	
	public boolean isHeated(World world, BlockPos pos) {
		BlockState oneBelow = world.getBlockState(pos.down());
		BlockState twoBelow = world.getBlockState(pos.down(2));
		return oneBelow.isIn(HEAT_SOURCE_TAG) || (oneBelow.isIn(HEAT_CONDUCTOR_TAG) && twoBelow.isIn(HEAT_SOURCE_TAG));
	}

}
