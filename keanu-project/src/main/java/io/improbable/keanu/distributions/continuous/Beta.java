package io.improbable.keanu.distributions.continuous;


import java.util.Random;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import static org.apache.commons.math3.special.Gamma.*;

/**
 * Computer Generation of Statistical Distributions
 * by Richard Saucier
 * ARL-TR-2168 March 2000
 * 5.1.2 page 14
 */

public class Beta {

    private Beta() {
    }

    /**
     * @param alpha location
     * @param beta shape
     * @param xMin minimum x
     * @param xMax source of randomness
     * @param random source of randomness
     * @return a random number from the Beta distribution
     */
    public static double sample(double alpha, double beta, double xMin, double xMax, Random random) {
        double y1 = Gamma.sample(0.0, 1.0, alpha, random);
        double y2 = Gamma.sample(0.0, 1.0, beta, random);

        if (alpha < beta) {
            return xMax - (xMax - xMin) * y2 / (y1 + y2);
        } else {
            return xMin + (xMax - xMin) * y1 / (y1 + y2);
        }
    }

    public static double pdf(double alpha, double beta, double x) {
        double denominator = gamma(alpha) * gamma(beta) / gamma(alpha + beta);
        return pow(x, alpha - 1) * pow(1 - x, beta - 1) / denominator;
    }

    public static Diff dPdf(double alpha, double beta, double x) {
        double gammaAgammaB = gamma(alpha) * gamma(beta);
        double gammaAplusB = gamma(alpha + beta);
        double dPdx = -((pow(x, alpha - 2) * pow(1 - x, beta - 2) * (alpha * (x - 1) + (beta - 2) * x + 1) * gammaAplusB) / gammaAgammaB);

        double pow1minusXToTheBminus1 = pow(1 - x, beta - 1);
        double powXToTheAminus1 = pow(x, alpha - 1);
        double diagammaAplusB = digamma(alpha + beta);
        double commonToDaAndDb = powXToTheAminus1 * pow1minusXToTheBminus1 * gammaAplusB;
        double dPdAlpha = commonToDaAndDb * (diagammaAplusB + log(x) - digamma(alpha)) / gammaAgammaB;
        double dPdBeta = commonToDaAndDb * (diagammaAplusB + log(1 - x) - digamma(beta)) / gammaAgammaB;

        return new Diff(dPdAlpha, dPdBeta, dPdx);
    }

    public static double logPdf(double alpha, double beta, double x) {
        double betaFunction = logGamma(alpha) + logGamma(beta) - logGamma(alpha + beta);
        return (alpha - 1) * Math.log(x) + (beta - 1) * Math.log(1 - x) - betaFunction;
    }

    public static Diff dlnPdf(double alpha, double beta, double x) {
        double dPdx = ((alpha - 1) / x) - ((beta - 1) / (1 - x));
        double dPda = digamma(alpha + beta) - digamma(alpha) + Math.log(x);
        double dPdb = digamma(alpha + beta) - digamma(beta) + Math.log(1 - x);

        return new Diff(dPda, dPdb, dPdx);
    }

    public static class Diff {
        public final double dPdAlpha;
        public final double dPdBeta;
        public final double dPdx;

        public Diff(double dPdAlpha, double dPdBeta, double dPdx) {
            this.dPdAlpha = dPdAlpha;
            this.dPdBeta = dPdBeta;
            this.dPdx = dPdx;
        }
    }

}
