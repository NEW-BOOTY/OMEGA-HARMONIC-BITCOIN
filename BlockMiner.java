/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse.miner;

import dev.royal.cryptoverse.ledger.Block;
import dev.royal.cryptoverse.ledger.Blockchain;
import dev.royal.cryptoverse.wallet.Transaction;
import dev.royal.cryptoverse.wallet.TransactionPool;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class BlockMiner {
    private final Blockchain blockchain;
    private final TransactionPool pool;

    public BlockMiner(Blockchain blockchain, TransactionPool pool) {
        this.blockchain = blockchain;
        this.pool = pool;
    }

    public Block mineBlock(BigInteger reward, long nonce) throws NoSuchAlgorithmException {
        List<Transaction> txs = pool.getPendingTransactions();
        List<String> txData = new ArrayList<>();

        for (Transaction tx : txs) {
            txData.add(tx.getSenderPublicKey() + "::" + tx.getReceiverPublicKey()
                    + "::" + tx.getAmount().toPlainString()
                    + "::" + tx.getSignature() + "::" + tx.getTimestamp());
        }

        Block latest = blockchain.getLatestBlock();
        Block newBlock = new Block(
                latest.getIndex() + 1,
                Instant.now(),
                latest.getHash(),
                txData,
                reward,
                nonce
        );

        if (blockchain.addBlock(txData, reward, nonce)) {
            pool.clear();
            return newBlock;
        } else {
            throw new IllegalStateException("Failed to add new block to the chain.");
        }
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
