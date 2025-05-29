/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 * Unauthorized distribution or reproduction is strictly prohibited.
 * For licensing inquiries, contact: PAY_ME@MY.COM
 */

package dev.royal.cryptoverse;

import dev.royal.cryptoverse.apw.APWPoissonMiner;
import dev.royal.cryptoverse.omega.OmegaHarmonicIssuer;

import java.math.BigInteger;

public final class CryptoEngineLauncher {
    public static void main(String[] args) {
        System.out.println("\n--- BITCOIN PRIME ENHANCER ---");
        APWPoissonMiner miner = new APWPoissonMiner();
        double probability = miner.getPoissonProbability(6, 0.15);
        System.out.printf("Poisson mining probability (6 blocks, 15%% hashrate): %.6f\n", probability);

        System.out.println("\n--- OMEGA HARMONIC ENGINE ---");
        OmegaHarmonicIssuer omega = new OmegaHarmonicIssuer();
        BigInteger reward = omega.computeBlockReward(500, 2.3);
        System.out.println("Block reward for epoch 500 with q=2.3: " + reward);
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
