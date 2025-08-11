import React, { useState, useEffect } from "react";
import {
  buyCrypto,
  sellCrypto,
  resetAccount,
  getUser,
  getUserTransactions,
  getUserHolding,
} from "./cryptoApi"; 
import "./TradePanel.css";

const INITIAL_BALANCE = 10000;
const userId = 1; 

export default function TradePanel({ prices, onTradeComplete }) {
  const [balance, setBalance] = useState(INITIAL_BALANCE);
  const [holdings, setHoldings] = useState({});
  const [transactions, setTransactions] = useState([]);
  const [selectedCrypto, setSelectedCrypto] = useState("");
  const [amount, setAmount] = useState("");
  const [confirmation, setConfirmation] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const price = prices[selectedCrypto]?.USD;
  const qty = parseFloat(amount);

  useEffect(() => {
    async function fetchUserData() {
      try {
        const user = await getUser(userId);
        setBalance(user.balance);

        const transactionsData = await getUserTransactions(userId);
        setTransactions(transactionsData);

        const holdingsData = {};
        for (const crypto of Object.keys(prices)) {
          try {
            const holding = await getUserHolding(userId, crypto);
            holdingsData[crypto] = holding?.quantity || 0;
          } catch {
            holdingsData[crypto] = 0;
          }
        }
        setHoldings(holdingsData);
      } catch (err) {
        setErrorMessage("Failed to load user data");
      }
    }
    fetchUserData();
  }, [prices]);

  const handleAmountChange = (e) => {
    const val = e.target.value;
    setAmount(val);
    setConfirmation("");
    setErrorMessage("");

    const parsed = parseFloat(val);

    if (val === "") {
      setErrorMessage("");
      return;
    }
    if (isNaN(parsed)) {
      setErrorMessage(" Amount must be a valid number.");
      return;
    }
    if (parsed <= 0) {
      setErrorMessage(" Amount must be greater than zero.");
      return;
    }
    setErrorMessage("");
  };

  const refreshUserData = async () => {
    try {
      const user = await getUser(userId);
      setBalance(user.balance);

      const transactionsData = await getUserTransactions(userId);
      setTransactions(transactionsData);

      const holdingsData = {};
      for (const crypto of Object.keys(prices)) {
        try {
          const holding = await getUserHolding(userId, crypto);
          holdingsData[crypto] = holding?.quantity || 0;
        } catch {
          holdingsData[crypto] = 0;
        }
      }
      setHoldings(holdingsData);
    } catch {
      setErrorMessage("Failed to load user data");
    }
  };

  const handleBuy = async () => {
    setConfirmation("");
    setErrorMessage("");

    if (!selectedCrypto) {
      setErrorMessage(" Please select a cryptocurrency.");
      return;
    }
    if (!amount || isNaN(qty) || qty <= 0) {
      setErrorMessage(" Please enter a valid positive amount.");
      return;
    }
    if (!price) {
      setErrorMessage(" Price for selected crypto not available.");
      return;
    }

    try {
      const response = await buyCrypto(userId, {
        cryptoName: selectedCrypto,
        quantity: qty,
        pricePerUnit: price,
      });
      setConfirmation(response);

      await refreshUserData();
      setAmount("");

      if (onTradeComplete) onTradeComplete();
    } catch (error) {
      setErrorMessage(error.response?.data || "Error during purchase");
    }
  };

  const handleSell = async () => {
    setConfirmation("");
    setErrorMessage("");

    if (!selectedCrypto) {
      setErrorMessage(" Please select a cryptocurrency.");
      return;
    }
    if (!amount || isNaN(qty) || qty <= 0) {
      setErrorMessage(" Please enter a valid positive amount.");
      return;
    }
    if (!price) {
      setErrorMessage(" Price for selected crypto not available.");
      return;
    }
    if ((holdings[selectedCrypto] || 0) < qty) {
      setErrorMessage(" Not enough crypto to sell.");
      return;
    }

    try {
      const response = await sellCrypto(userId, {
        cryptoName: selectedCrypto,
        quantity: qty,
        pricePerUnit: price,
      });
      setConfirmation(response);

      await refreshUserData();
      setAmount("");

      if (onTradeComplete) onTradeComplete();
    } catch (error) {
      setErrorMessage(error.response?.data || "Error during sale");
    }
  };

  const handleReset = async () => {
    try {
      await resetAccount(userId, INITIAL_BALANCE);
      setConfirmation("Account reset to initial balance.");
      setErrorMessage("");

      await refreshUserData();
      setAmount("");
      setSelectedCrypto("");

      if (onTradeComplete) onTradeComplete();
    } catch (error) {
      setErrorMessage(error.response?.data || "Error resetting account");
    }
  };

  const cryptoOptions = Object.keys(prices);

  useEffect(() => {
    if (!onTradeComplete) return;
    const interval = setInterval(() => {
      onTradeComplete();
    }, 5000); 

    return () => clearInterval(interval);
  }, [onTradeComplete]);

  return (
    <div className="trade-panel">
      <h2>Trade Panel</h2>
      <p>
        <strong>Balance:</strong> ${balance.toFixed(2)}
      </p>

      <div className="trade-form">
        <select
          value={selectedCrypto}
          onChange={(e) => {
            setSelectedCrypto(e.target.value);
            setConfirmation("");
            setErrorMessage("");
          }}
        >
          <option value="">Select Crypto</option>
          {cryptoOptions.map((c) => (
            <option key={c} value={c}>
              {c}
            </option>
          ))}
        </select>

        <input
          type="number"
          placeholder="Amount"
          value={amount}
          onChange={handleAmountChange}
          min="0"
          step="any"
        />
      </div>

      <div className="trade-buttons">
        <button onClick={handleBuy}>Buy</button>
        <button onClick={handleSell}>Sell</button>
        <button onClick={handleReset}>Reset</button>
      </div>

      {confirmation && <div className="confirmation-message">{confirmation}</div>}
      {errorMessage && <div className="error-message">{errorMessage}</div>}

      <h3>Holdings</h3>
      <ul>
        {Object.entries(holdings).map(([crypto, qty]) => {
          const safeQty = Number(qty) || 0;
          return safeQty > 0 ? (
            <li key={crypto}>
              {crypto}: {safeQty.toFixed(4)}
            </li>
          ) : null;
        })}
      </ul>

      <h3>Transaction History</h3>
      <ul>
        {transactions.map((tx, index) => {
          
          const qty = Number(tx.quantity ?? tx.qty) || 0;
          const price = Number(tx.pricePerUnit ?? tx.price) || 0;
          const total = Number(tx.totalPrice ?? tx.total) || qty * price;
          const crypto = tx.cryptoName ?? tx.crypto ?? "Unknown";
          const type = tx.type ?? tx.action;
          const timestamp = tx.timestamp ?? tx.date ?? new Date().toLocaleString();

          return (
            <li key={index}>
              [{timestamp}] {type} {qty} {crypto} @ ${price.toFixed(2)} = ${total.toFixed(2)}
            </li>
          );
        })}
      </ul>
    </div>
  );
}
