/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.NamedParameterSpec;
import java.util.Base64;

public final class DHKEM_AES256_Stack {
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final String AES_MODE = "AES/GCM/NoPadding";

    public static KeyPair generateX25519KeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("X25519");
        kpg.initialize(new NamedParameterSpec("X25519"));
        return kpg.generateKeyPair();
    }

    public static SecretKey deriveSharedKey(PrivateKey privateKey, PublicKey peerPublicKey) throws Exception {
        KeyAgreement agreement = KeyAgreement.getInstance("X25519");
        agreement.init(privateKey);
        agreement.doPhase(peerPublicKey, true);
        byte[] sharedSecret = agreement.generateSecret();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(sharedSecret);
        return new SecretKeySpec(keyBytes, 0, 32, "AES");
    }

    public static String encrypt(SecretKey key, String message) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_MODE);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] ciphertext = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

        byte[] encrypted = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(SecretKey key, String encryptedData) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(decoded, 0, iv, 0, iv.length);

        Cipher cipher = Cipher.getInstance(AES_MODE);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decrypted = cipher.doFinal(decoded, iv.length, decoded.length - iv.length);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
