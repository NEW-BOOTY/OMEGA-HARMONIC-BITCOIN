/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.ledger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Block {
    private final int index;
    private final Instant timestamp;
    private final String previousHash;
    private final List<String> transactions;
    private final BigInteger reward;
    private final long nonce;
    private final String hash;

    public Block(int index, Instant timestamp, String previousHash, List<String> transactions, BigInteger reward, long nonce) throws NoSuchAlgorithmException {
        this.index = index;
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.transactions = new ArrayList<>(transactions);
        this.reward = reward;
        this.nonce = nonce;
        this.hash = computeHash();
    }

    private String computeHash() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'computeHash'");
    }

    String Block() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String data = index + timestamp.toString() + previousHash + transactions.toString() + reward + nonce;
        byte[] hashBytes = digest.digest(data.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public int getIndex() { return index; }
    public Instant getTimestamp() { return timestamp; }
    public String getPreviousHash() { return previousHash; }
    public List<String> getTransactions() { return transactions; }
    public BigInteger getReward() { return reward; }
    public long getNonce() { return nonce; }
    public String getHash() { return hash; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block block)) return false;
        return index == block.index &&
                nonce == block.nonce &&
                Objects.equals(timestamp, block.timestamp) &&
                Objects.equals(previousHash, block.previousHash) &&
                Objects.equals(transactions, block.transactions) &&
                Objects.equals(reward, block.reward) &&
                Objects.equals(hash, block.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, timestamp, previousHash, transactions, reward, nonce, hash);
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
