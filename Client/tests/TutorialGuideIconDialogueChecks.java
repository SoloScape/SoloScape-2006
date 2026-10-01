package unpackaged;

public final class TutorialGuideIconDialogueChecks {
    public static void main(String[] args) {
        Widget guide = new Widget();
        guide.anInt2084 = 244 << 16;
        guide.anInt2009 = 2;
        guide.anInt2026 = 0xFFFF;
        require(Class20.isGuideIconDialogueWidget(guide),
                "Guide sentinel was not recognized on the NPC four-line portrait slot");

        Widget ordinaryNpc = new Widget();
        ordinaryNpc.anInt2084 = 244 << 16;
        ordinaryNpc.anInt2009 = 2;
        ordinaryNpc.anInt2026 = 946;
        require(!Class20.isGuideIconDialogueWidget(ordinaryNpc),
                "Ordinary NPC head was mistaken for the Guide icon");

        guide.anInt2084 = 243 << 16;
        require(!Class20.isGuideIconDialogueWidget(guide),
                "Guide sentinel leaked into the wrong dialogue group");
        System.out.println("Tutorial Guide icon dialogue checks passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
