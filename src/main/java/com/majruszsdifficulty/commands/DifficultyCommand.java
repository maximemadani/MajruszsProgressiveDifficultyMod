package com.majruszsdifficulty.commands;

import com.majruszsdifficulty.GameStage;
import com.mlib.commands.Command;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.Vec3;

public class DifficultyCommand extends Command {
	static final String GAME_STAGE_ID = "gamestage";

	protected MutableComponent createGameStageMessage( GameStage.Stage stage, String translationKey ) {
		return Component.translatable( "commands.gamestage." + translationKey, GameStage.getGameStageText( stage ) );
	}

	protected String asVec3i( Vec3 position ) {
		return String.format( "(%d, %d, %d)", ( int )( position.x ), ( int )( position.y ), ( int )( position.z ) );
	}
}
