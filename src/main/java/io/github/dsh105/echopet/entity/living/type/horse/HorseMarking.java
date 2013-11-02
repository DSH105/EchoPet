package io.github.dsh105.echopet.entity.living.type.horse;

public enum HorseMarking {
    NONE(0, 1, 2, 3, 4, 5, 6),
    SOCKS(256, 257, 258, 259, 260, 261, 262),
    WHITE_PATCH(512, 513, 514, 515, 516, 517, 518),
    WHITE_SPOTS(768, 769, 770, 771, 772, 773, 774),
    BLACK_SPOTS(1024, 1025, 1026, 1027, 1028, 1029, 1030);

    private HorseVariant[] a = {HorseVariant.WHITE, HorseVariant.CREAMY, HorseVariant.CHESTNUT, HorseVariant.BROWN, HorseVariant.BLACK, HorseVariant.GRAY, HorseVariant.DARKBROWN};
    private Integer[] b;

    HorseMarking(Integer... i) {
        this.b = i;
    }

    public int getId(HorseVariant v) {
        for (int i = 0; i < HorseVariant.values().length; i++) {
            if (a[i] == v) {
                return b[i];
            }
        }
        return -1;
    }
}