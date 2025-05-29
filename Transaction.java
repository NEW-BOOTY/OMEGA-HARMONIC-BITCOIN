/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.wallet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public final class Transaction implements Serializable {
    private final String id;
    private final String senderPublicKey;
    private final String receiverPublicKey;
    private final BigDecimal amount;
    private final String signature;
    private final Instant timestamp;

    public Transaction(PublicKey sender, PublicKey receiver, BigDecimal amount, String signature) {
        this.id = UUID.randomUUID().toString();
        this.senderPublicKey = Base64.getEncoder().encodeToString(sender.getEncoded());
        this.receiverPublicKey = Base64.getEncoder().encodeToString(receiver.getEncoded());
        this.amount = amount;
        this.signature = signature;
        this.timestamp = Instant.now();
    }

    public Transaction(String sender, String receiver, BigDecimal amount2, String sig) {
        //TODO Auto-generated constructor stub
    }

    public String getId() { return id; }
    public String getSenderPublicKey() { return senderPublicKey; }
    public String getReceiverPublicKey() { return receiverPublicKey; }
    public BigDecimal getAmount() { return amount; }
    public String getSignature() { return signature; }
    public Instant getTimestamp() { return timestamp; }

    public String toCanonicalForm() {
        return senderPublicKey + receiverPublicKey + amount.toPlainString() + timestamp.toString();
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
