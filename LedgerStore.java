/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.ledger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public final class LedgerStore {
    private final Blockchain blockchain;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65536;
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String SECRET_ALGO = "PBKDF2WithHmacSHA256";

    public LedgerStore(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public boolean saveEncrypted(File file, String password) {
        try {
            byte[] iv = generateIV();
            SecretKey key = deriveKey(password.toCharArray(), iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(new CipherOutputStream(fos, cipher))) {
                fos.write(iv);
                oos.writeObject(blockchain.getChain());
            }
            return true;
        } catch (Exception e) {
            System.err.println("Ledger encryption error: " + e.getMessage());
            return false;
        }
    }

    public boolean loadEncrypted(File file, String password) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] iv = new byte[GCM_IV_LENGTH];
            if (fis.read(iv) != GCM_IV_LENGTH) throw new IOException("IV read failed");
            SecretKey key = deriveKey(password.toCharArray(), iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            try (ObjectInputStream ois = new ObjectInputStream(new CipherInputStream(fis, cipher))) {
                @SuppressWarnings("unchecked")
                List<Block> loaded = (List<Block>) ois.readObject();
                if (loaded != null && !loaded.isEmpty()) {
                    Blockchain loadedChain = new Blockchain();
                    for (int i = 1; i < loaded.size(); i++) {
                        loadedChain.addBlock(
                            loaded.get(i).getTransactions(),
                            loaded.get(i).getReward(),
                            loaded.get(i).getNonce()
                        );
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Ledger decryption error: " + e.getMessage());
        }
        return false;
    }

    private SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_ALGO);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
