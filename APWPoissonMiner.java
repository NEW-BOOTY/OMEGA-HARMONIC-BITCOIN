/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 * Unauthorized distribution or reproduction is strictly prohibited.
 * For licensing inquiries, contact: PAY_ME@MY.COM
 */

package dev.royal.cryptoverse.apw;

import java.security.SecureRandom;

public final class APWPoissonMiner {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TARGET_BLOCK_INTERVAL = 600; // 10 minutes

    public double computePhi(double hashrateShare) {
        int prime = getNthPrime((int) Math.floor(1.0 / hashrateShare));
        double theta = getNetworkHashrateDeviation();
        return prime * Math.exp(-theta * theta);
    }

    public double getPoissonProbability(int blocks, double hashrateShare) {
        double lambda = computePhi(hashrateShare) / TARGET_BLOCK_INTERVAL;
        return Math.pow(lambda, blocks) * Math.exp(-lambda) / factorial(blocks);
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

    private int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    private double getNetworkHashrateDeviation() {
        return RANDOM.nextGaussian(); // Simulates network variance
    }
}
