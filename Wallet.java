/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.wallet;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.UUID;

public final class Wallet {
    private final String id;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private BigDecimal balance;

    public Wallet() throws Exception {
        this.id = UUID.randomUUID().toString();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
        this.balance = BigDecimal.ZERO;
    }

    public String signTransaction(String data) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public boolean verifySignature(String data, String signatureStr) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] sigBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(sigBytes);
    }

    public PublicKey getPublicKey() { return publicKey; }
    public String getId() { return id; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal newBalance) { this.balance = newBalance; }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
