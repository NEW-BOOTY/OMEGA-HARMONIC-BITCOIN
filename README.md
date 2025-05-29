OMEGA HARMONIC BITCOIN™ - Final Deployment Bundle

# 🧬 OMEGA HARMONIC BITCOIN™

> Post-Quantum Cryptocurrency Infrastructure • Prime Harmonic Scarcity • AES-256/DHKEM-Secured • Multi-Network Ready

---
## 🔖 Overview

**OMEGA HARMONIC BITCOIN™** is a high-security, post-Bitcoin cryptographic platform built from scratch in Java. It features:

- 🔐 Adaptive Prime-Weighted Mining Engine (APW-PHD)
- 🧠 Omega Harmonic Block Issuance (ΩHCD)
- 🧱 Modular blockchain core with validated ledger, block mining, and pool mechanics
- 🌐 CLI and RESTful Jetty Server interface
- 🖥️ Dark-themed React Dashboard with real-time network views
- 🔄 Multi-network runtime: `mainnet`, `testnet`, and `devnet`
- 💣 Encrypted persistent storage via AES-256-GCM and X25519-DHKEM

---

## ⚙️ Getting Started

### 1️⃣ Prerequisites

- Java 17+
- PowerShell (for `.ps1` launcher) or Unix shell
- `npm` or `yarn` for Web UI (optional frontend)

---

### 2️⃣ Run the Server

```powershell
# Run the backend server for a given network:
.\run_server.ps1 -network testnet

Valid network options:

mainnet (production)

testnet (sandboxed, isolated)

devnet (local dev only, supports reset)

OMEGA_HARMONIC_BITCOIN_RELEASE/
├── lib/                      # Required .jar dependencies (Jetty, SLF4J, Servlet)
├── out/                      # Compiled class files
├── src/                      # Full modular Java source
│   └── dev/royal/cryptoverse/
│       ├── apw/              # Prime-weighted Poisson miner
│       ├── omega/            # ΩHCD block issuance engine
│       ├── crypto/           # DHKEM + AES-GCM stack
│       ├── ledger/           # Blockchain, Block, LedgerStore
│       ├── wallet/           # Wallet, Transaction, Validator
│       ├── miner/            # BlockMiner
│       ├── CryptoEngineLauncher.java
│       └── EngineCLI_Server.java
├── web/                      # Vite + React dark UI
│   ├── public/
│   └── src/components/
├── run_server.ps1            # Multi-network server launcher
└── README.md                 # You’re reading it


🔐 Security Architecture
Layer	Mechanism
Key Exchange	X25519 DHKEM via NamedParameterSpec
Symmetric	AES-256-GCM (IV + Auth Tag)
Wallet Signing	RSA-4096 + SHA256withRSA
Ledger I/O	Encrypted file store via CipherOutputStream
Transactions	Canonical string signed and validated at entry
Networking	Jetty Server with isolated pools per network

🌐 Web Dashboard Features
Launch via Vite (cd web && npm run dev) or open /dashboard via Jetty.

🔍 View live transaction pool

📝 Submit Base64-signed TXs

⛏️ Initiate mining block synthesis

🔀 Network dropdown: mainnet / testnet / devnet

🌑 Fully dark-themed with brand integration

🧠 Author’s Notes
This system implements novel mathematical primitives:

APW-PHD: Prime-weighted Poisson hashing algorithm

ΩHCD: Omega-based block reward using ζ(q) spectral decay and totient entropy

All modules are 100% original and engineered from scratch for real-world use.


🚀 Final Notes
Run the CLI and Server with --network flags

Use the Jetty endpoints for API/REST

Deploy the Web UI with npm run build and mount under /dashboard

Mine honestly, verify cryptographically, and keep everything modular

Engineered by Devin B. Royal – Cryptographic Infrastructure Architect
