package zabi.minecraft.extraalchemy.items;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.capability.RingCharge;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.lib.Utils;
import zabi.minecraft.extraalchemy.potion.NoncontinuousEffect;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class ItemPotionRing extends Item implements IBauble {
	
	private static final ArrayList<String> whitelist = Lists.newArrayList();
	private static final ArrayList<String> blacklist = Lists.newArrayList();
	
	static {
		whitelist.add("minecraft:night_vision");
		whitelist.add("minecraft:invisibility");
		whitelist.add("minecraft:leaping");
		whitelist.add("minecraft:strong_leaping");
		whitelist.add("minecraft:fire_resistance");
		whitelist.add("minecraft:swiftness");
		whitelist.add("minecraft:strong_swiftness");
		whitelist.add("minecraft:slowness");
		whitelist.add("minecraft:water_breathing");
		whitelist.add("minecraft:regeneration");
		whitelist.add("minecraft:strong_regeneration");
		whitelist.add("minecraft:strength");
		whitelist.add("minecraft:strong_strength");
		whitelist.add("minecraft:weakness");
		
		whitelist.add("extraalchemy:magnetism_normal");
		whitelist.add("extraalchemy:magnetism_strong");
		whitelist.add("extraalchemy:crumbling_normal");
		whitelist.add("extraalchemy:crumbling_strong");
		whitelist.add("extraalchemy:dislocation_normal");
		whitelist.add("extraalchemy:dislocation_strong");
		whitelist.add("extraalchemy:gravity_normal");
		whitelist.add("extraalchemy:gravity_strong");
		whitelist.add("extraalchemy:hurry_normal");
		whitelist.add("extraalchemy:hurry_strong");
		whitelist.add("extraalchemy:learning_normal");
		whitelist.add("extraalchemy:learning_strong");
		whitelist.add("extraalchemy:leech_normal");
		whitelist.add("extraalchemy:leech_strong");
		whitelist.add("extraalchemy:photosynthesis_normal");
		whitelist.add("extraalchemy:photosynthesis_strong");
		whitelist.add("extraalchemy:reincarnation_normal");
		whitelist.add("extraalchemy:reincarnation_strong");
		whitelist.add("extraalchemy:sails_normal");
		whitelist.add("extraalchemy:sails_strong");
		whitelist.add("extraalchemy:sinking_normal");
		whitelist.add("extraalchemy:sinking_strong");
	}
	
	public ItemPotionRing() {
		this.setMaxStackSize(1);
		this.setCreativeTab(ExtraAlchemy.TAB);
		this.setRegistryName(new ResourceLocation(Reference.MID, "potion_ring"));
		this.setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab) && ModConfig.options.enablePotionRings) {
			whitelist.stream()
				.filter(s -> !blacklist.contains(s))
				.map(s -> new ResourceLocation(s))
				.map(rl -> ForgeRegistries.POTION_TYPES.getValue(rl))
				.filter(pt -> pt != null)
				.filter(pt -> pt.getEffects().size() == 1)
				.forEach(pt -> addPotionRing(items, pt));
		}
	}
	
	private void addPotionRing(NonNullList<ItemStack> items, PotionType pt) {
		ItemStack stack = new ItemStack(this);
		PotionUtils.addPotionToItemStack(stack, pt);
		items.add(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		if (PotionUtils.getPotionFromItem(stack).getEffects().size() != 1) {
			return I18n.format("item.potion_ring_ea.uncraftable.name");
		}
		String potName = I18n.format(PotionUtils.getPotionFromItem(stack).getEffects().get(0).getEffectName());
		String potLvl = I18n.format("potion.potency."+PotionUtils.getPotionFromItem(stack).getEffects().get(0).getAmplifier());
        return I18n.format("item.potion_ring_ea.name", potName, potLvl).trim();
    }

	@Override
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.RING;
	}
	
	@Override
	@Optional.Method(modid = "baubles")
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		IBauble.super.onEquipped(itemstack, player);
		if (!player.world.isRemote && shouldTickAsBauble() && player instanceof EntityPlayer) {
			applyLogic((EntityPlayer) player, itemstack);
		}
	}

	public boolean shouldTickAsBauble() {
		return ModConfig.options.enableBaubleInteraction && Loader.isModLoaded("baubles");
	}

	@Override
	@Optional.Method(modid = "baubles")
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		IBauble.super.onWornTick(itemstack, player);
		if (!player.world.isRemote && shouldTickAsBauble() && player instanceof EntityPlayer && shouldApplyNow(player)) {
			applyLogic((EntityPlayer) player, itemstack);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!entityIn.world.isRemote && !shouldTickAsBauble() && entityIn instanceof EntityPlayer && shouldApplyNow(entityIn)) {
			applyLogic((EntityPlayer) entityIn, stack);
		}
	}
	
	@Override
	@Optional.Method(modid = "baubles")
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		for (int i : getBaubleType(null).getValidSlots()) {
			if (BaublesApi.getBaublesHandler(playerIn).insertItem(i, playerIn.getHeldItem(handIn), false).isEmpty()) {
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}
	
	

	private void applyLogic(EntityPlayer p, ItemStack stack) {
		PotionType type = PotionUtils.getPotionFromItem(stack);
		for (PotionEffect pe: type.getEffects()) {
			int lvl = pe.getAmplifier() + 1;
			if (lvl <= 2) {
				int cost = lvl == 1?ModConfig.options.ringXpConsumptionFirstLevel:(lvl == 2?ModConfig.options.ringXpConsumptionSecondLevel:Integer.MAX_VALUE);
				if (drainXp(p, cost, pe.getPotion())) {
					p.addPotionEffect(new PotionEffect(pe.getPotion(), 120, lvl - 1, false, false));
				}
			}
		}
	}
	
	private boolean shouldApplyNow(Entity p) {
		return p.ticksExisted % 100 == 0;
	}

	private boolean drainXp(EntityPlayer p, int amount, Potion potion) {
		
		if (p.isCreative()) return true;
		if (potion instanceof NoncontinuousEffect && !((NoncontinuousEffect) potion).isEffectActive(p)) {
			return true;
		}
		
		RingCharge rc = p.getCapability(RingCharge.CAPABILITY, null);
		
		if (rc.charges > 0) {
			rc.charges--;
			rc.markDirty((byte) 1);
			return true;
		}
		
		if (amount > Utils.getPlayerXP(p)) {
			return false;
		}
		Utils.addPlayerXP(p, -amount);
		rc.charges = 5;
		return true;
	}
	
	public static void addToWhitelist(String registryName) {
		whitelist.add(registryName);
	}
	
	public static void removeFromWhitelist(String name) {
		blacklist.add(name);
		whitelist.remove(name);
	}
	
	public static void addRecipes() {
		whitelist.stream()
			.filter(s -> !blacklist.contains(s))
			.map(s -> new ResourceLocation(s))
			.map(rl -> ForgeRegistries.POTION_TYPES.getValue(rl))
			.filter(pt -> pt != null)
			.filter(pt -> pt.getEffects().size() == 1)
			.forEach(pt -> addRecipeForType(pt));
	}

	private static void addRecipeForType(PotionType pt) {
		
		ItemStack result = new ItemStack(ModItems.potion_ring);
		PotionUtils.addPotionToItemStack(result, pt);
		
		ItemStack potion = new ItemStack(Items.POTIONITEM);
		PotionUtils.addPotionToItemStack(potion, pt);
		
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(Reference.MID, pt.getRegistryName().toString().replace(':', '_')),
				null, 
				result, 
				new IngredientNBT(potion) {},
				Ingredient.fromItem(ModItems.empty_ring)
		);
	}

}
