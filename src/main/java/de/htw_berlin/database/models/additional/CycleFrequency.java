package de.htw_berlin.database.models.additional;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CycleFrequency {

    public final static int MASK_MONDAY     = 0b00000001;
    public final static int MASK_TUESDAY    = 0b00000010;
    public final static int MASK_WEDNESDAY  = 0b00000100;
    public final static int MASK_THURSDAY   = 0b00001000;
    public final static int MASK_FRIDAY     = 0b00010000;
    public final static int MASK_SATURDAY   = 0b00100000;
    public final static int MASK_SUNDAY     = 0b01000000;
    private static final int[] MASKS_DAYS_ALL = { MASK_MONDAY, MASK_TUESDAY, MASK_WEDNESDAY, MASK_THURSDAY, MASK_FRIDAY, MASK_SATURDAY, MASK_SUNDAY };

    public final static int MASK_END_CYCLE  = 0b10000000;



    private int value;

    /**
     * Gets the mask wich corresponds to today's weekday
     * @return mask of today's weekday
     */
    public static int getTodayMask() {
        return switch (LocalDateTime.now().getDayOfWeek()) {
            case MONDAY -> MASK_MONDAY;
            case TUESDAY -> MASK_TUESDAY;
            case WEDNESDAY -> MASK_WEDNESDAY;
            case THURSDAY -> MASK_THURSDAY;
            case FRIDAY -> MASK_FRIDAY;
            case SATURDAY -> MASK_SATURDAY;
            case SUNDAY -> MASK_SUNDAY;
        };
    }

    public CycleFrequency(int... days) {
        value = Arrays.stream(days).reduce(MASK_END_CYCLE, (a, b) -> a | b);
    }

    @Override
    public String toString() {
        return Arrays.stream(CycleFrequency.MASKS_DAYS_ALL)
                .boxed()
                .filter(i -> (i & value) > 0)
                .map(CycleFrequency::getDayString)
                .reduce("Frequency{ ", (a, b) -> a + b + "; ") + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CycleFrequency that) {
            return this.value == that.value;
        } else {
            return false;
        }
    }

    public String toBinaryString() {
        return Integer.toBinaryString(value);
    }

    public static CycleFrequency fromBinaryString(String binary) throws NumberFormatException {
        return new CycleFrequency(Integer.parseInt(binary, 2));
    }

    public static int countDaysAWeek(CycleFrequency frequency) {
        return countDaysAWeek(frequency, 0);
    }

    public static int countDaysAWeek(CycleFrequency frequency, int week) {
        int f = frequency.value >> week * 8;
        int counter = 0;
        for (int maskDay : MASKS_DAYS_ALL) {
            if ((f & maskDay) > 0)
                counter++;
        }
        return counter;
    }

    /**
     * Returns string representation of dayMask
     * @param mask daymask. e.gl {@link CycleFrequency#MASK_THURSDAY}
     * @return string representation of weekday
     */
    public static String getDayString(int mask) {
        if (Arrays.stream(MASKS_DAYS_ALL).noneMatch(day -> mask == day)) {
            return "";
        }
        return switch (mask) {
            case MASK_MONDAY -> "Monday";
            case MASK_TUESDAY -> "Tuesday";
            case MASK_WEDNESDAY -> "Wednesday";
            case MASK_THURSDAY -> "Thursday";
            case MASK_FRIDAY -> "Friday";
            case MASK_SATURDAY -> "Saturday";
            case MASK_SUNDAY -> "Sunday";
            default -> "NULL";
        };
    }
}
