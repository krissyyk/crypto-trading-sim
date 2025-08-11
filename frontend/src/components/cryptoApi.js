import axios from "axios";

const BASE_URL = "http://localhost:8080/api/users"; 


export async function getUser(userId) {
  const response = await axios.get(`${BASE_URL}/${userId}`);
  return response.data;
}


export async function getUserHolding(userId, cryptoName) {
  const response = await axios.get(`${BASE_URL}/${userId}/holdings/${cryptoName}`);
  return response.data;
}


export async function getUserTransactions(userId) {
  const response = await axios.get(`${BASE_URL}/${userId}/transactions`);
  return response.data;
}


export async function buyCrypto(userId, tradeRequest) {

  const response = await axios.post(`${BASE_URL}/${userId}/buy`, tradeRequest);
  return response.data; 
}


export async function sellCrypto(userId, tradeRequest) {
  
  const response = await axios.post(`${BASE_URL}/${userId}/sell`, tradeRequest);
  return response.data; 
}


export async function resetAccount(userId, startingBalance) {
  const response = await axios.post(`${BASE_URL}/${userId}/reset`, null, {
    params: { startingBalance },
  });
  return response.data; 
}
