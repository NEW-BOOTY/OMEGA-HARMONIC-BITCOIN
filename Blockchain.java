/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.ledger;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class Blockchain {
    private final List<Block> chain = new ArrayList<>();

    public Blockchain() throws NoSuchAlgorithmException {
        this.chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() throws NoSuchAlgorithmException {
        return new Block(
            0,
            Instant.now(),
            "0",
            new ArrayList<>(),
            BigInteger.ZERO,
            0L
        );
    }

    public synchronized Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public synchronized boolean addBlock(List<String> transactions, BigInteger reward, long nonce) {
        try {
            Block latest = getLatestBlock();
            Block newBlock = new Block(
                latest.getIndex() + 1,
                Instant.now(),
                latest.getHash(),
                transactions,
                reward,
                nonce
            );

            if (isValidNewBlock(newBlock, latest)) {
                chain.add(newBlock);
                return true;
            } else {
                System.err.println("Invalid block rejected.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Block creation error: " + e.getMessage());
            return false;
        }
    }

    public synchronized List<Block> getChain() {
        return new ArrayList<>(chain);
    }

    private boolean isValidNewBlock(Block current, Block previous) throws NoSuchAlgorithmException {
        if (previous.getIndex() + 1 != current.getIndex()) return false;
        if (!previous.getHash().equals(current.getPreviousHash())) return false;
        if (!current.getHash().equals(current.computeHash())) return false;
        return true;
    }

    public boolean isValidChain() {
        try {
            for (int i = 1; i < chain.size(); i++) {
                Block current = chain.get(i);
                Block previous = chain.get(i - 1);
                if (!isValidNewBlock(current, previous)) return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("Chain validation error: " + e.getMessage());
            return false;
        }
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
