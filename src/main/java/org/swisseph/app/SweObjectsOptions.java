/*
 * Copyright (C) By the Author
 * Author    Yura Krymlov
 * Created   2019-12
 */

package org.swisseph.app;

import org.swisseph.api.ISweAyanamsa;
import org.swisseph.api.ISweHouseSystem;
import org.swisseph.api.ISweObjectsOptions;

import static org.swisseph.api.ISweConstants.CH_VS;
import static org.swisseph.app.SweAyanamsa.getLahiri;
import static org.swisseph.app.SweAyanamsa.getTrueSpica;
import static swisseph.SweConst.SE_SIDM_USER;

/**
 * @author Yura Krymlov
 * @version 1.1, 2019-12
 */
public class SweObjectsOptions implements ISweObjectsOptions {
    private static final long serialVersionUID = 461264153326654053L;

    public static final ISweObjectsOptions LAHIRI_TRADITIONAL = new Builder().ayanamsa(getLahiri()).build();
    public static final ISweObjectsOptions LAHIRI_CITRAPAKSA = new Builder().ayanamsa(getTrueSpica()).build();

    protected final ISweHouseSystem houseSystem;
    protected final ISweAyanamsa ayanamsa;
    protected final boolean trueNode;
    protected final int calcFlags;
    protected final int riseSetFlags;
    protected final double initialJulianDay, initialAyanamsa;

    /**
     * @param initialJulianDay - initial julian day (reference date)
     * @param initialAyanamsa  - initial ayanamsa (at reference date)
     */
    protected SweObjectsOptions(ISweAyanamsa ayanamsa, ISweHouseSystem houseSystem, boolean trueNode,
                                int calcFlags, double initialJulianDay, double initialAyanamsa,
                                int riseSetFlags) {
        this.initialJulianDay = initialJulianDay;
        this.initialAyanamsa = initialAyanamsa;
        this.riseSetFlags = riseSetFlags;
        this.houseSystem = houseSystem;
        this.calcFlags = calcFlags;
        this.trueNode = trueNode;
        this.ayanamsa = ayanamsa;
    }

    @Override
    public ISweHouseSystem houseSystem() {
        return houseSystem;
    }

    @Override
    public ISweAyanamsa ayanamsa() {
        return ayanamsa;
    }

    /**
     * true or mean node to use for RA/KE calculation
     */
    @Override
    public boolean trueNode() {
        return trueNode;
    }

    /**
     * Reference date (Julian day), if sid_mode is SE_SIDM_USER
     */
    @Override
    public double initialJulianDay() {
        return initialJulianDay;
    }

    /**
     * Initial ayanamsha at t0, if sid_mode is SE_SIDM_USER
     */
    @Override
    public double initialAyanamsa() {
        return initialAyanamsa;
    }

    /**
     * Special preset of flags for planets calculation
     */
    @Override
    public int calcFlags() {
        return calcFlags;
    }

    /**
     * This is a flag to swe_rise_trans()
     */
    @Override
    public int riseSetFlags() {
        return riseSetFlags;
    }

    @Override
    public ISweObjectsOptions clone() throws CloneNotSupportedException {
        return (ISweObjectsOptions) super.clone();
    }

    @Override
    public String toString() {
        return new StringBuilder(64)
                .append(ayanamsa()).append(CH_VS)
                .append(houseSystem()).append(CH_VS)
                .append(calcFlags()).append(CH_VS)
                .append(trueNode()).append(CH_VS)
                .append(riseSetFlags())
                .toString();
    }

    public static final class Builder {
        private ISweHouseSystem houseSystem = SweHouseSystem.byDefault();
        private int calcFlags = DEFAULT_SS_TRUEPOS_NONUT_SPEED_FLAGS;
        private ISweAyanamsa ayanamsa = SweAyanamsa.byDefault();
        private int riseSetFlags = DEFAULT_SS_RISE_SET_FLAGS;
        private double initialJulianDay, initialAyanamsa;
        private boolean trueNode;

        public Builder options(ISweObjectsOptions options) {
            this.initialJulianDay = options.initialJulianDay();
            this.initialAyanamsa = options.initialAyanamsa();
            this.riseSetFlags = options.riseSetFlags();
            this.houseSystem = options.houseSystem();
            this.calcFlags = options.calcFlags();
            this.trueNode = options.trueNode();
            this.ayanamsa = options.ayanamsa();
            return this;
        }

        public Builder riseSetFlags(int flags) {
            this.riseSetFlags = flags;
            return this;
        }

        /**
         * true or mean node to use for RA/KE calculation
         */
        public Builder trueNode(boolean trueNode) {
            this.trueNode = trueNode;
            return this;
        }

        /**
         * Special preset of flags for planets calculation
         */
        public Builder calculationFlags(int flags) {
            this.calcFlags = flags;
            return this;
        }

        public Builder ayanamsa(ISweAyanamsa ayanamsa) {
            this.ayanamsa = ayanamsa;
            return this;
        }

        public Builder houseSystem(ISweHouseSystem houseSystem) {
            this.houseSystem = houseSystem;
            return this;
        }

        /**
         * Reference date (Julian day) if sid_mode is SE_SIDM_USER
         */
        public Builder initialJulianDay(double initJulianDay) {
            if (ayanamsa.fid() == SE_SIDM_USER) {
                this.initialJulianDay = initJulianDay;
            }
            return this;
        }

        /**
         * Initial ayanamsha if sid_mode is SE_SIDM_USER
         */
        public Builder initialAyanamsa(double initAyanamsa) {
            if (ayanamsa.fid() == SE_SIDM_USER) {
                this.initialAyanamsa = initAyanamsa;
            }
            return this;
        }

        public ISweObjectsOptions build() {
            return new SweObjectsOptions(ayanamsa, houseSystem, trueNode, calcFlags,
                    initialJulianDay, initialAyanamsa, riseSetFlags);
        }
    }
}
