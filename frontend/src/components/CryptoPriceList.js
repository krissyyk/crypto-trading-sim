import React, { useEffect, useState } from "react";
import "./CryptoPriceList.css";

const cryptos = [
  "XBT", "ETH", "ADA", "DOT", "SOL",
  "LTC", "XRP", "LINK", "BCH", "TRX",
  "ETC", "XMR", "CRV", "ATOM", "UNI",
  "AVAX", "AAVE", "FIL", "XTZ", "ALGO"
];

const cryptoLogos = {
  XBT: "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
  ETH: "https://assets.coingecko.com/coins/images/279/large/ethereum.png",
  ADA: "https://assets.coingecko.com/coins/images/975/large/cardano.png",
  DOT: "https://assets.coingecko.com/coins/images/12171/large/polkadot.png",
  SOL: "https://assets.coingecko.com/coins/images/4128/large/solana.png",
  LTC: "https://assets.coingecko.com/coins/images/2/large/litecoin.png",
  XRP: "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png",
  LINK: "https://assets.coingecko.com/coins/images/877/large/chainlink-new-logo.png",
  BCH: "https://assets.coingecko.com/coins/images/780/large/bitcoin-cash-circle.png",
  TRX: "https://assets.coingecko.com/coins/images/1094/large/tron-logo.png",
  ETC: "https://assets.coingecko.com/coins/images/453/large/ethereum-classic-logo.png",
  XMR: "https://assets.coingecko.com/coins/images/69/large/monero_logo.png",
  CRV: "https://assets.coingecko.com/coins/images/12124/standard/Curve.png?1696511967",
  ATOM: "https://assets.coingecko.com/coins/images/1481/large/cosmos_hub.png",
  UNI: "https://assets.coingecko.com/coins/images/12504/large/uniswap-uni.png",
  AVAX: "https://assets.coingecko.com/coins/images/12559/standard/Avalanche_Circle_RedWhite_Trans.png?1696512369",
  AAVE: "https://assets.coingecko.com/coins/images/12645/large/AAVE.png",
  FIL: "https://assets.coingecko.com/coins/images/12817/standard/filecoin.png?1696512609",
  XTZ: "https://assets.coingecko.com/coins/images/976/standard/Tezos-logo.png?1696502091",
  ALGO: "https://assets.coingecko.com/coins/images/4380/standard/download.png?1696504978",
};

const cryptoNames = {
  XBT: "Bitcoin",
  ETH: "Ethereum",
  ADA: "Cardano",
  DOT: "Polkadot",
  SOL: "Solana",
  LTC: "Litecoin",
  XRP: "XRP",
  LINK: "Chainlink",
  BCH: "Bitcoin Cash",
  TRX: "TRON",
  ETC: "Ethereum Classic",
  XMR: "Monero",
  CRV: "Curve DAO Token",
  ATOM: "Cosmos",
  UNI: "Uniswap",
  AVAX: "Avalanche",
  AAVE: "Aave",
  FIL: "Filecoin",
  XTZ: "Tezos",
  ALGO: "Algorand",
};

export default function CryptoPriceList({ prices = {}, setPrices }) {

  useEffect(() => {
    fetch("http://localhost:8080/api/prices")
      .then(res => res.json() )
      .then(data =>{
        setPrices(data);
      })
      .catch(console.error);

    const ws = new WebSocket("ws://localhost:8080/ws/prices");

    ws.onmessage = (msg) => {
      const data = JSON.parse(msg.data);
      setPrices(data);
    };

    ws.onerror = (err) => {
      console.error("WebSocket error:", err);
    };

    return () => ws.close();
  }, [setPrices]);

  const safePrices = prices || {};

  return (
    <div className="crypto-container">
      <h2>Top 20 Cryptocurrencies (Prices in USD & EUR)</h2>
      <table className="crypto-table">
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Price (USD)</th>
            <th>Price (EUR)</th>
          </tr>
        </thead>
        <tbody>
          {cryptos.map((crypto) => (
            <tr key={crypto}>
              <td>
                <img
                  src={cryptoLogos[crypto]}
                  alt={crypto}
                  className="crypto-logo"
                />
                <span className="crypto-name">
                  {cryptoNames[crypto]} ({crypto})
                </span>
              </td>
              <td>{safePrices[crypto]?.USD ? `$${safePrices[crypto].USD}` : "Loading..."}</td>
              <td>{safePrices[crypto]?.EUR ? `€${safePrices[crypto].EUR}` : "Loading..."}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
