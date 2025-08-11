import React, { useState, useEffect } from 'react';
import CryptoPriceList from './components/CryptoPriceList';
import TradePanel from "./components/TradePanel";
import './App.css';

function App() {
  const [prices, setPrices] = useState({});

 
  async function fetchPricesFromAPI() {
    try {
      const response = await fetch("http://localhost:8080/api/prices"); 
      if (!response.ok) {
        throw new Error("Failed to fetch prices");
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Error fetching prices:", error);
      return {};
    }
  }


  useEffect(() => {
    async function updatePrices() {
      const data = await fetchPricesFromAPI();
      setPrices(data);
    }

    updatePrices();

    const interval = setInterval(() => {
      updatePrices();
    }, 30000); 

    return () => clearInterval(interval);
  }, []);

 
  const handleTradeComplete = async () => {
    const data = await fetchPricesFromAPI();
    setPrices(data);
  };

  return (
    <div className="App">
      <header className="app-header">
        <h1 className="app-title">Crypto Trading Simulator</h1>
      </header>
      <CryptoPriceList prices={prices} setPrices={setPrices} />
      <TradePanel prices={prices} onTradeComplete={handleTradeComplete} />
    </div>
  );
}

export default App;
