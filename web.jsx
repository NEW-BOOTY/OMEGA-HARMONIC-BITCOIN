import React from 'react';

export default function MinerPanel() {
  const mineBlock = () => {
    fetch('/api/mine-block', { method: 'POST' })
      .then(res => res.json())
      .then(data => alert('Mined block: ' + data.hash));
  };

  return (
    <div className="bg-gray-700 p-4 rounded-md shadow text-white">
      <h2 className="text-lg mb-2">Mine Block</h2>
      <button className="btn" onClick={mineBlock}>⛏️ Mine Next Block</button>
    </div>
  );
}

import React, { useState } from 'react';

export default function TxForm({ onSubmit }) {
  const [sender, setSender] = useState('');
  const [receiver, setReceiver] = useState('');
  const [amount, setAmount] = useState('');
  const [signature, setSignature] = useState('');

  const submit = () => {
    fetch('/api/submit-tx', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ sender, receiver, amount, signature })
    })
    .then(res => res.json())
    .then(data => onSubmit(data));
  };

  return (
    <div className="bg-gray-800 p-4 rounded-md shadow-md">
      <h2 className="text-white text-lg mb-2">Submit Transaction</h2>
      <input className="input" placeholder="Sender (Base64)" value={sender} onChange={e => setSender(e.target.value)} />
      <input className="input" placeholder="Receiver (Base64)" value={receiver} onChange={e => setReceiver(e.target.value)} />
      <input className="input" placeholder="Amount" value={amount} onChange={e => setAmount(e.target.value)} />
      <input className="input" placeholder="Signature (Base64)" value={signature} onChange={e => setSignature(e.target.value)} />
      <button className="btn mt-2" onClick={submit}>Submit</button>
    </div>
  );
}

import React, { useEffect, useState } from 'react';

export default function TxPoolViewer() {
  const [txs, setTxs] = useState([]);

  useEffect(() => {
    fetch('/api/txpool')
      .then(res => res.json())
      .then(data => setTxs(data));
  }, []);

  return (
    <div className="bg-gray-900 p-4 rounded-md text-white">
      <h2 className="text-xl mb-2">Pending Transactions</h2>
      <ul>
        {txs.map((tx, idx) => (
          <li key={idx}>{tx.txId} → {tx.amount}</li>
        ))}
      </ul>
    </div>
  );
}

import React from 'react';
import TxForm from './components/TxForm';
import TxPoolViewer from './components/TxPoolViewer';
import MinerPanel from './components/MinerPanel';

export default function WebDashboard() {
  return (
    <div className="bg-black min-h-screen p-6 text-white">
      <h1 className="text-2xl font-bold mb-6">OMEGA HARMONIC BITCOIN™ Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <TxForm onSubmit={(res) => alert(res.status === 'accepted' ? '✅ TX Accepted' : '❌ TX Rejected')} />
        <MinerPanel />
        <TxPoolViewer />
      </div>
    </div>
  );
}
