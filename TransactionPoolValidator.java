/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.wallet;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class TransactionPoolValidator {

    public static boolean validateTransaction(Transaction tx) {
        try {
            PublicKey publicKey = decodePublicKey(tx.getSenderPublicKey());
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(tx.toCanonicalForm().getBytes());

            return signature.verify(Base64.getDecoder().decode(tx.getSignature()));
        } catch (Exception e) {
            System.err.println("[Validation Error] " + e.getMessage());
            return false;
        }
    }

    public static PublicKey decodePublicKey(String base64Key) throws GeneralSecurityException {
        byte[] bytes = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
