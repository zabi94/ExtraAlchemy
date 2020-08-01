package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.Tickable;
import zabi.minecraft.extraalchemy.config.ModConfig;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandMixin extends LockableContainerBlockEntity implements SidedInventory, Tickable {

	@Shadow private int fuel;
	
	protected BrewingStandMixin(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void checkFireUnderneath(CallbackInfo ci) {
		if (!world.isClient && ModConfig.INSTANCE.enableBrewingStandFire && isHeatSource(world.getBlockState(pos.down()))) {
			if (fuel < 20 && world.getTime() % 40 == 0) {
				fuel++;
				this.markDirty();
			}
		}
	}

	public boolean isHeatSource(BlockState state) {
		if (state.getMaterial() == Material.FIRE || state.getMaterial() == Material.LAVA || state.getBlock() == Blocks.MAGMA_BLOCK) {
			return true;
		}
		return false;
	}
	
}
