/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */

package dev.royal.cryptoverse;

import dev.royal.cryptoverse.wallet.Transaction;
import dev.royal.cryptoverse.wallet.TransactionPool;
import dev.royal.cryptoverse.wallet.TransactionPoolValidator;
import dev.royal.cryptoverse.apw.APWPoissonMiner;
import dev.royal.cryptoverse.omega.OmegaHarmonicIssuer;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

public final class EngineCLI_Server {

    private static final TransactionPool transactionPool = new TransactionPool();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equalsIgnoreCase("--server")) {
            startJettyServer();
        } else {
            startCommandLine();
        }
    }

    private static void startCommandLine() {
        Scanner scanner = new Scanner(System.in);
        APWPoissonMiner miner = new APWPoissonMiner();
        OmegaHarmonicIssuer issuer = new OmegaHarmonicIssuer();

        System.out.println("=== OMEGA HARMONIC BITCOIN™ CLI ===");

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. View Transaction Pool");
            System.out.println("2. Submit Transaction");
            System.out.println("3. Simulate Mining");
            System.out.println("0. Exit");

            System.out.print("Select> ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    List<Transaction> txs = transactionPool.getPendingTransactions();
                    if (txs.isEmpty()) {
                        System.out.println("No pending transactions.");
                    } else {
                        for (Transaction tx : txs) {
                            System.out.println("TX: " + tx.getId() + " → " + tx.getAmount());
                        }
                    }
                }

                case "2" -> {
                    try {
                        System.out.print("Sender PublicKey (Base64): ");
                        String sender = scanner.nextLine();
                        System.out.print("Receiver PublicKey (Base64): ");
                        String receiver = scanner.nextLine();
                        System.out.print("Amount: ");
                        BigDecimal amount = new BigDecimal(scanner.nextLine());
                        System.out.print("Signature (Base64): ");
                        String signature = scanner.nextLine();

                        Transaction tx = new Transaction(sender, receiver, amount, signature);

                        if (transactionPool.validateTransaction(tx)) {
                            transactionPool.addTransaction(tx);
                            System.out.println("[ACCEPTED] Transaction added: " + tx.getId());
                        } else {
                            System.out.println("[REJECTED] Invalid transaction signature.");
                        }
                    } catch (Exception ex) {
                        System.out.println("[ERROR] " + ex.getMessage());
                    }
                }

                case "3" -> {
                    System.out.print("Blocks to simulate: ");
                    int blocks = Integer.parseInt(scanner.nextLine());
                    System.out.print("Hashrate share (0 < p < 1): ");
                    double p = Double.parseDouble(scanner.nextLine());
                    double result = miner.getPoissonProbability(blocks, p);
                    System.out.printf("Poisson Mining Probability: %.10f\n", result);
                }

                case "0" -> {
                    System.out.println("Exiting.");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void startJettyServer() throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new CryptoServlet()), "/api/*");
        server.start();
        System.out.println("🚀 Jetty Server started at http://localhost:8080/api");
        server.join();
    }

    public static class CryptoServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String path = req.getPathInfo();

            if ("/submit-tx".equals(path)) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }

                    String json = jsonBuilder.toString()
                        .replace("\"", "")
                        .replace("{", "")
                        .replace("}", "");

                    String[] parts = json.split(",");
                    String sender = parts[0].split(":")[1].trim();
                    String receiver = parts[1].split(":")[1].trim();
                    String amount = parts[2].split(":")[1].trim();
                    String signature = parts[3].split(":")[1].trim();

                    if (!transactionPool.validateTransactionFields(sender, receiver, amount, signature)) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("{\"status\":\"rejected\",\"reason\":\"Invalid fields or signature format\"}");
                        return;
                    }

                    Transaction tx = new Transaction(sender, receiver, new BigDecimal(amount), signature);

                    if (transactionPool.validateTransaction(tx)) {
                        transactionPool.addTransaction(tx);
                        resp.getWriter().write("{\"status\":\"accepted\",\"txId\":\"" + tx.getId() + "\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("{\"status\":\"rejected\",\"reason\":\"Invalid signature\"}");
                    }
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Invalid POST endpoint\"}");
            }
        }
    }
}

/*
 * Copyright © 2025 Devin B. Royal.
 * All Rights Reserved.
 */
