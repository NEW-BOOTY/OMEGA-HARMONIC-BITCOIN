/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 * Unauthorized distribution or reproduction is strictly prohibited.
 * For licensing inquiries, contact: PAY_ME@MY.COM
 */

package dev.royal.cryptoverse.omega;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class OmegaHarmonicIssuer {

    public BigInteger computeBlockReward(int epoch, double q) {
        BigDecimal s = computeScarcity(epoch, q);
        BigDecimal omega = computeOmega();
        BigDecimal gamma = gammaFunction(q);

        BigDecimal reward = omega.multiply(gamma).divide(s, BigDecimal.ROUND_HALF_UP);
        return reward.toBigInteger();
    }

    private BigDecimal computeScarcity(int n, double q) {
        int prime = getNthPrime(n);
        double zeta = 0.0;
        for (int i = 1; i <= n; i++) zeta += 1.0 / Math.pow(i, q);
        double scarcity = prime / (Math.log(n * totient(n)) + zeta);
        return BigDecimal.valueOf(scarcity);
    }

    private int totient(int n) {
        int result = n;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                while (n % i == 0) n /= i;
                result -= result / i;
            }
        }
        if (n > 1) result -= result / n;
        return result;
    }

    private int getNthPrime(int n) {
        int count = 0, num = 1;
        while (count < n) {
            num++;
            if (isPrime(num)) count++;
        }
        return num;
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i <= Math.sqrt(num); i++)
            if (num % i == 0) return false;
        return true;
    }

    private BigDecimal computeOmega() {
        double sum = 0.0;
        for (int k = 2; k < 10000; k++)
            sum += 1.0 / (totient(k) * Math.log(k));
        return BigDecimal.valueOf(sum);
    }

    private BigDecimal gammaFunction(double x) {
        double[] p = {
            676.5203681218851, -1259.1392167224028,
            771.32342877765313, -176.61502916214059,
            12.507343278686905, -0.13857109526572012,
            9.9843695780195716e-6, 1.5056327351493116e-7
        };
        int g = 7;
        if (x < 0.5) {
            return BigDecimal.valueOf(Math.PI /
                    (Math.sin(Math.PI * x) * gammaFunction(1 - x).doubleValue()));
        }

        x -= 1;
        double a = 0.99999999999980993;
        for (int i = 0; i < p.length; i++)
            a += p[i] / (x + i + 1);
        double t = x + g + 0.5;
        return BigDecimal.valueOf(Math.sqrt(2 * Math.PI) *
                Math.pow(t, x + 0.5) * Math.exp(-t) * a);
    }
}
