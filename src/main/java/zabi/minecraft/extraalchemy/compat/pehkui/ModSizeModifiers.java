package zabi.minecraft.extraalchemy.compat.pehkui;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModSizeModifiers {
	
	public static final ScaleType GROWING = ScaleRegistries.register(ScaleRegistries.SCALE_TYPES, LibMod.id("growing"), ScaleType.Builder.create().build());
	public static final ScaleType SHRINKING = ScaleRegistries.register(ScaleRegistries.SCALE_TYPES, LibMod.id("shrinking"), ScaleType.Builder.create().build());
	
	private static final ScaleModifier SHRINKING_MOD = ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, LibMod.id("shrinking"), new ScaleModifier() {
		@Override
		public float modifyScale(ScaleData scaleData, float modifiedScale, float delta) {
			return SHRINKING.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
		}
	});
	private static final ScaleModifier GROWING_MOD = ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, LibMod.id("growing"), new ScaleModifier() {
		@Override
		public float modifyScale(ScaleData scaleData, float modifiedScale, float delta) {
			return GROWING.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
		}
	});
	
	public static void registerModifiers() {
		ScaleTypesAdapter.BASE.getDefaultBaseValueModifiers().add(SHRINKING_MOD);
		ScaleTypesAdapter.BASE.getDefaultBaseValueModifiers().add(GROWING_MOD);
		
		GROWING.getScaleChangedEvent().register(ev -> {
			Entity e = ev.getEntity();
			boolean g = e.isOnGround();
			e.calculateDimensions();
			e.setOnGround(g);
			GROWING.getScaleData(e).markForSync(true);
		});
		
		SHRINKING.getScaleChangedEvent().register(ev -> {
			Entity e = ev.getEntity();
			boolean g = e.isOnGround();
			e.calculateDimensions();
			e.setOnGround(g);
			SHRINKING.getScaleData(e).markForSync(true);
		});
	}
	
	
}
