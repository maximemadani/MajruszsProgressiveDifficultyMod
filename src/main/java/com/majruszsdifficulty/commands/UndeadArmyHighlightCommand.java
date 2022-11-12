package com.majruszsdifficulty.commands;

import com.majruszsdifficulty.undeadarmy.UndeadArmy;
import com.mlib.annotations.AutoInstance;

@AutoInstance
public class UndeadArmyHighlightCommand extends UndeadArmyCommand {
	public UndeadArmyHighlightCommand() {
		super( "highlight", "highlighted", UndeadArmy::highlightArmy );
	}
}
