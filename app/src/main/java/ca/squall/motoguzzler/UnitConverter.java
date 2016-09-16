package ca.squall.motoguzzler;

import java.math.BigDecimal;

/**
 * Created by charles on 2016-09-16.
 */

public class UnitConverter {

    public static float UNIT_M = 0.621371f;
    public static float UNIT_MPG = 2.352145833f;
    public static float UNIT_G = 0.264172f;

    public static BigDecimal convert(BigDecimal value, float conversionFactor) {
        return value.multiply(new BigDecimal(conversionFactor)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public static BigDecimal revert(BigDecimal value, float conversionFactor) {
        return value.multiply(new BigDecimal(1 / conversionFactor)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }


}
