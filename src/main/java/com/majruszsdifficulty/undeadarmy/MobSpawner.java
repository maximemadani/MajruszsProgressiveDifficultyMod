package com.majruszsdifficulty.undeadarmy;

import com.mlib.Random;
import com.mlib.entities.EntityHelper;
import com.mlib.math.VectorHelper;
import com.mlib.time.TimeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

record MobSpawner( UndeadArmy undeadArmy ) implements IComponent {
	@Override
	public void tick() {
		if( !TimeHelper.hasServerTicksPassed( 20 ) || this.undeadArmy.phase.state != Phase.State.WAVE_ONGOING )
			return;

		MobInfo mobInfo = this.getNextMobToSpawn();
		if( mobInfo != null ) {
			Vec3 position = VectorHelper.subtract( VectorHelper.vec3( mobInfo.position ), new Vec3( 0.0, 0.5, 0.0 ) );
			Entity entity = EntityHelper.spawn( mobInfo.type, this.undeadArmy.level, position );
			if( entity == null )
				return;

			mobInfo.uuid = entity.getUUID();
			this.updateWaveHealth( mobInfo );
		}
	}

	@Override
	public void onPhaseChanged() {
		if( this.undeadArmy.phase.state == Phase.State.WAVE_PREPARING ) {
			this.generateMobList();
			this.undeadArmy.phase.healthTotal = 0;
		}
	}

	@Nullable
	private MobInfo getNextMobToSpawn() {
		return this.undeadArmy.mobsLeft.stream()
			.filter( mobDef->mobDef.uuid == null )
			.findFirst()
			.orElse( null );
	}

	private void generateMobList() {
		Config.WaveDef waveDef = this.undeadArmy.config.getWave( this.undeadArmy.currentWave + 1 );
		waveDef.mobDefs.forEach( mobDef->{
			for( int i = 0; i < mobDef.count; ++i ) {
				this.addToPendingMobs( mobDef, false );
			}
		} );
		if( waveDef.boss != null ) {
			this.addToPendingMobs( waveDef.boss, true );
		}
	}

	private void addToPendingMobs( Config.MobDef def, boolean isBoss ) {
		this.undeadArmy.mobsLeft.add( new MobInfo( def, this.getRandomSpawnPosition(), isBoss ) );
	}

	private void updateWaveHealth( MobInfo mobInfo ) {
		this.undeadArmy.phase.healthTotal += mobInfo.getMaxHealth( this.undeadArmy.level );
	}

	private BlockPos getRandomSpawnPosition() {
		int tries = 0;
		int x, y, z;
		do {
			Vec3i offset = this.buildOffset( 30 );
			x = this.undeadArmy.positionToAttack.getX() + offset.getX();
			z = this.undeadArmy.positionToAttack.getZ() + offset.getZ();
			y = this.undeadArmy.level.getHeight( Heightmap.Types.MOTION_BLOCKING, x, z );
		} while( y != this.undeadArmy.level.getHeight( Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z ) && ++tries < 5 );

		return new BlockPos( x, y, z );
	}

	private Vec3i buildOffset( int spawnRadius ) {
		Direction direction = this.undeadArmy.direction;
		int x = direction.z != 0 ? 20 : 10 + direction.x * spawnRadius;
		int y = 0;
		int z = direction.x != 0 ? 20 : 10 + direction.z * spawnRadius;

		return Random.getRandomVector3i( -x, x, -y, y, -z, z );
	}
}
