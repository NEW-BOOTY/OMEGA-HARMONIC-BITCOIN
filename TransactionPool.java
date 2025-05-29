/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.wallet;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public final class TransactionPool {
    private final List<Transaction> pendingTransactions = new ArrayList<>();

    public synchronized void addTransaction(Transaction tx) {
        pendingTransactions.add(tx);
    }

    public synchronized List<Transaction> getPendingTransactions() {
        return new ArrayList<>(pendingTransactions);
    }

    public synchronized void clear() {
        pendingTransactions.clear();
    }

    public synchronized boolean validateTransaction(Transaction tx) {
        return TransactionPoolValidator.validateTransaction(tx);
    }

    public boolean validateTransactionFields(String senderKeyBase64, String receiverKeyBase64, String amount, String signature) {
        try {
            // Attempt to decode and verify fields separately
            PublicKey sender = TransactionPoolValidator.decodePublicKey(senderKeyBase64);
            PublicKey receiver = TransactionPoolValidator.decodePublicKey(receiverKeyBase64);
            new BigDecimal(amount); // Validate numeric form

            return (sender != null && receiver != null && signature != null && !signature.isEmpty());
        } catch (Exception e) {
            System.err.println("[TX Field Validation Failed] " + e.getMessage());
            return false;
        }
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
