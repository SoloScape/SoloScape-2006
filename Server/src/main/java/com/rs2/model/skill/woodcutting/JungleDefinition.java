package com.rs2.model.skill.woodcutting;

/** Revision-443 Tai Bwo Wannai jungle stages (four spars, then empty). */
public enum JungleDefinition {
    LIGHT(9010, 10, 32.0, 6281, 128, 240),
    MEDIUM(9015, 20, 55.0, 6283, 96, 210),
    DENSE(9020, 30, 81.0, 6285, 64, 180);

    private final int baseObjectId;
    private final int requiredLevel;
    private final double experience;
    private final int sparItemId;
    private final int chanceLow;
    private final int chanceHigh;

    JungleDefinition(int baseObjectId, int requiredLevel, double experience,
                     int sparItemId, int chanceLow, int chanceHigh) {
        this.baseObjectId = baseObjectId;
        this.requiredLevel = requiredLevel;
        this.experience = experience;
        this.sparItemId = sparItemId;
        this.chanceLow = chanceLow;
        this.chanceHigh = chanceHigh;
    }

    public static JungleDefinition forObjectId(int objectId) {
        for (JungleDefinition definition : values()) {
            if (objectId >= definition.baseObjectId && objectId <= definition.baseObjectId + 4) {
                return definition;
            }
        }
        return null;
    }

    public int getStage(int objectId) {
        return objectId - baseObjectId;
    }

    public boolean isDepleted(int objectId) {
        return getStage(objectId) >= 4;
    }

    public int getBaseObjectId() {
        return baseObjectId;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public double getExperience() {
        return experience;
    }

    public int getSparItemId() {
        return sparItemId;
    }

    public int getChanceLow() {
        return chanceLow;
    }

    public int getChanceHigh() {
        return chanceHigh;
    }
}
