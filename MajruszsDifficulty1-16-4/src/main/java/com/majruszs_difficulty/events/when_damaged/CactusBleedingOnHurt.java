package com.majruszs_difficulty.events.when_damaged;

import com.majruszs_difficulty.ConfigHandler.Config;
import com.majruszs_difficulty.GameState;
import com.majruszs_difficulty.MajruszsHelper;
import com.majruszs_difficulty.effects.BleedingEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;

import javax.annotation.Nullable;

/** Making Cactus inflict bleeding on enemies. */
public class CactusBleedingOnHurt extends WhenDamagedApplyEffectBase {
	public CactusBleedingOnHurt() {
		super( GameState.Mode.NORMAL, false, BleedingEffect.instance );
	}

	/** Checking if all conditions were met. */
	@Override
	protected boolean shouldBeExecuted( @Nullable LivingEntity attacker, LivingEntity target, DamageSource damageSource ) {
		boolean canBeTargetedByBleeding = MajruszsHelper.isHuman( target ) || MajruszsHelper.isAnimal( target );

		return canBeTargetedByBleeding && super.shouldBeExecuted( attacker, target, damageSource );
	}

	@Override
	protected boolean isEnabled() {
		return !Config.isDisabled( Config.Features.CACTUS_BLEEDING );
	}

	@Override
	protected double getChance() {
		return Config.getChance( Config.Chances.CACTUS_BLEEDING );
	}

	@Override
	protected int getDurationInTicks( Difficulty difficulty ) {
		return Config.getDurationInSeconds( Config.Durations.CACTUS_BLEEDING );
	}

	@Override
	protected int getAmplifier( Difficulty difficulty ) {
		return 0;
	}
}