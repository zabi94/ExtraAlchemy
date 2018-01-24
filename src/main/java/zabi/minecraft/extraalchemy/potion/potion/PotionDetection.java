package zabi.minecraft.extraalchemy.potion.potion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.network.packets.PacketHighlightEntity;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionDetection extends PotionBase {

	protected static HashMap<UUID, ArrayList<Integer>> affectedEntities = new HashMap<UUID, ArrayList<Integer>>();
	
	public PotionDetection(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "detection");
		this.setIconIndex(2, 2);
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase e, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(e, attributeMapIn, amplifier);
		this.applyGlowing(e, true);
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase e, int amplifier, double health) {
		super.affectEntity(source, indirectSource, e, amplifier, health);
		this.applyGlowing(e, true);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase e, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(e, attributeMapIn, amplifier);
		this.applyGlowing(e, false);
	}
	
	public void applyGlowing(EntityLivingBase entity, boolean active) {
		if (!entity.getEntityWorld().isRemote && entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			if (affectedEntities.get(player.getUniqueID())==null) affectedEntities.put(player.getUniqueID(), new ArrayList<Integer>());
			if (active) {
				player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(40, 20, 40)).stream()
				.filter(ent -> (ent instanceof EntityLivingBase))
				.filter(ent -> !(ent instanceof EntityPlayer && ((EntityPlayer)ent).isSpectator()))
				.forEach(ent -> {
					affectedEntities.get(player.getUniqueID()).add(ent.getEntityId());
					ExtraAlchemy.network.sendTo(new PacketHighlightEntity(true, ent.getEntityId()), player);
				});

			} else {
				for (Integer id:affectedEntities.get(player.getUniqueID())) {
					Entity e=player.getEntityWorld().getEntityByID(id);
					if (e instanceof EntityLivingBase) {
						ExtraAlchemy.network.sendTo(new PacketHighlightEntity(false, ((EntityLivingBase) e).getEntityId()), player);
					}
				}
				affectedEntities.get(player.getUniqueID()).clear();
			}
		}
	}
	
}
