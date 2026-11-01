package com.rs2.model.combat;

import com.rs2.model.Entity;
import com.rs2.model.combat.CombatManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;

public final class DeathCleanupTask
extends TickTask {
    private final Entity defeatedEntity;
    private final Entity killer;
    // Record this when the fatal hit occurs. A Castle Wars match can end
    // before the scheduled death cleanup runs, removing the player from the
    // active match and otherwise turning a safe death into a regular one.
    private final boolean castleWarsDeath;

    public DeathCleanupTask(int value2, Entity entity, Entity entity2) {
        super(value2);
        this.defeatedEntity = entity;
        this.killer = entity2;
        this.castleWarsDeath = entity instanceof Player
                && CastleWarsManager.isInGame((Player) entity);
    }

    @Override
    public final void execute() {
        CombatManager.finishDeath(this.defeatedEntity, this.killer, true, this.castleWarsDeath);
        this.stop();
    }
}

