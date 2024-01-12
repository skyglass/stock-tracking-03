package net.greeta.stock.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.math.RoundingMode.HALF_UP;

public class CalculationHelper {
    public static final int PRECISION = 2;

    private CalculationHelper() {}


    public static BigDecimal round(BigDecimal value, RoundingMode roundingMode) {
        if (value != null && value.scale() > PRECISION) {
            return value.setScale(PRECISION, roundingMode);
        }
        return value;
    }

    public static BigDecimal round(BigDecimal value) {
        if (value != null && value.scale() > PRECISION) {
            return value.setScale(PRECISION, HALF_UP);
        }
        return value;
    }

    public static double round(double value) {
        return round(BigDecimal.valueOf(value)).doubleValue();
    }


    public static int compare(BigDecimal value1, BigDecimal value2) {
        BigDecimal compareValue1 = Optional.ofNullable(value1).orElse(BigDecimal.ZERO).setScale(2, HALF_UP);
        BigDecimal compareValue2 = Optional.ofNullable(value2).orElse(BigDecimal.ZERO).setScale(2, HALF_UP);
        return compareValue1.compareTo(compareValue2);
    }

    public static boolean equalsToScale2(BigDecimal value1, BigDecimal value2) {
        return compare(value1, value2) == 0;
    }

    public static boolean equalsOrGreaterThan(BigDecimal value1, BigDecimal value2) {
        return compare(value1, value2) >= 0;
    }

    public static boolean moreThanZero(BigDecimal value) {
        return compare(value, BigDecimal.ZERO) > 0;
    }

    public static boolean moreOrEqualToZero(BigDecimal value) {
        return compare(value, BigDecimal.ZERO) >= 0;
    }

    public static boolean lessThanZero(BigDecimal value) {
        return compare(value, BigDecimal.ZERO) < 0;
    }

    public static boolean isEqualToZero(BigDecimal value) {
        return compare(value, BigDecimal.ZERO) == 0;
    }

    public static boolean notEqualsToZero(BigDecimal value) {
        return compare(value, BigDecimal.ZERO) != 0;
    }
}
