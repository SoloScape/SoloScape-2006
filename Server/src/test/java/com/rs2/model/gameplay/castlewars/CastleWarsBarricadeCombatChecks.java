package com.rs2.model.gameplay.castlewars;

import com.rs2.model.Position;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager.BarricadeState;

/** Regression checks for Castle Wars barricade durability and melee cadence. */
public final class CastleWarsBarricadeCombatChecks {
    public static void main(String[] args) {
        require(CastleWarsEngineeringManager.isBarricadeObject(4421),
                "Saradomin barricade must be attackable");
        require(CastleWarsEngineeringManager.isBarricadeObject(4422),
                "Zamorak barricade must be attackable");
        require(!CastleWarsEngineeringManager.isBarricadeObject(4461),
                "The barricade supply table is not a placed barricade");

        BarricadeState state = new BarricadeState(CastleWarsManager.Team.SARADOMIN,
                new Position(2400, 3100, 0),
                CastleWarsEngineeringManager.BARRICADE_OBJECT_SARADOMIN);
        require(state.getHitpoints() == 50, "New barricades start at 50 HP");
        require(state.takeDamage(0) == 50, "Zero hits must not damage barricades");
        require(state.takeDamage(-10) == 50, "Negative hits must not heal barricades");
        require(state.takeDamage(17) == 33, "Melee damage must reduce barricade HP");
        require(state.takeDamage(33) == 0, "Final hit must destroy the barricade");
        require(state.takeDamage(50) == 0, "Destroyed barricades must not go below zero");

        require(CastleWarsEngineeringManager.barricadeAttackDelayMillis(2) == 1200L,
                "Fast weapon delay must be two game ticks");
        require(CastleWarsEngineeringManager.barricadeAttackDelayMillis(5) == 3000L,
                "Slow weapon delay must be five game ticks");
        require(CastleWarsEngineeringManager.barricadeAttackDelayMillis(0) == 600L,
                "Invalid zero weapon delay must be at least one game tick");

        System.out.println("Castle Wars barricade combat checks passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
