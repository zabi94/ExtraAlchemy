package zabi.minecraft.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;

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
		Tag<Block> tagSource = BlockTags.getTagGroup().getTag(HEAT_SOURCE_TAG);
		Tag<Block> tagConductor = BlockTags.getTagGroup().getTag(HEAT_CONDUCTOR_TAG);
		
		if (tagSource == null || tagConductor == null) {
			Log.w("Couldn't find heat related tags, check your resource packs for format errors. Disabling brewing fire in the config");
			ModConfig.INSTANCE.enableBrewingStandFire = false;
			return false;
		}
		
		Block oneBelow = world.getBlockState(pos.down()).getBlock();
		Block twoBelow = world.getBlockState(pos.down(2)).getBlock();
		return tagSource.contains(oneBelow) || (tagConductor.contains(oneBelow) && tagSource.contains(twoBelow));
	}

}
