package com.majruszsdifficulty.items;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnFarmlandTillCheck;
import com.mlib.gamemodifiers.contexts.OnItemAttributeTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Rarity;

public class EnderiumHoeItem extends HoeItem {
	public EnderiumHoeItem() {
		super( CustomItemTier.END, -5, 0.0f, new Properties().rarity( Rarity.UNCOMMON ).fireResistant() );
	}

	@AutoInstance
	public static class IncreaseTillArea {
		static final String ATTRIBUTE_ID = "item.majruszsdifficulty.enderium_hoe.effect";

		public IncreaseTillArea() {
			OnFarmlandTillCheck.listen( OnFarmlandTillCheck.INCREASE_AREA )
				.addCondition( Condition.< OnFarmlandTillCheck.Data >isShiftKeyDown( data->data.player ).negate() )
				.addCondition( Condition.predicate( data->data.itemStack.getItem() instanceof EnderiumHoeItem ) );

			OnItemAttributeTooltip.listen( this::addTooltip )
				.addCondition( Condition.predicate( data->data.item instanceof EnderiumHoeItem ) );
		}

		private void addTooltip( OnItemAttributeTooltip.Data data ) {
			data.add( EquipmentSlot.MAINHAND, Component.translatable( ATTRIBUTE_ID ).withStyle( ChatFormatting.DARK_GREEN ) );
		}
	}
}
