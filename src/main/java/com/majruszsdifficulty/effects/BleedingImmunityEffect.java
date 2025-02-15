package com.majruszsdifficulty.effects;

import com.majruszsdifficulty.Registries;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.ModConfigs;
import com.mlib.gamemodifiers.contexts.OnEffectApplicable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

public class BleedingImmunityEffect extends MobEffect {
	public BleedingImmunityEffect() {
		super( MobEffectCategory.BENEFICIAL, 0xff990000 );
	}

	@Override
	public void applyEffectTick( LivingEntity entity, int amplifier ) {}

	@Override
	public void applyInstantenousEffect( @Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entity,
		int amplifier, double health
	) {
		entity.removeEffect( Registries.BLEEDING.get() );
	}

	@Override
	public boolean isDurationEffectTick( int duration, int amplifier ) {
		return false;
	}

	@AutoInstance
	public static class BleedingImmunity {
		public BleedingImmunity() {
			ConfigGroup group = ModConfigs.registerSubgroup( Registries.Groups.DEFAULT )
				.name( "BleedingImmunity" )
				.comment( "Config for Bleeding Immunity effect." );

			OnEffectApplicable.listen( this::cancelBleeding )
				.addCondition( Condition.hasEffect( Registries.BLEEDING_IMMUNITY, data->data.entity ) )
				.addCondition( Condition.predicate( data->data.effect.equals( Registries.BLEEDING.get() ) ) )
				.insertTo( group );
		}

		private void cancelBleeding( OnEffectApplicable.Data data ) {
			data.event.setResult( Event.Result.DENY );
		}
	}
}
