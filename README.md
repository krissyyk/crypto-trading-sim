# Crypto Trading Simulator

A cryptocurrency trading simulation application with a React frontend and a Java Spring Boot backend.  
Users can buy, sell, and track their crypto holdings with live price updates.


## Features

- React-based user interface  
- Java Spring Boot backend REST API  
- Live cryptocurrency prices  
- Buy and sell cryptocurrencies  
- Track holdings and transaction history  
- Reset account balance  


## Prerequisites

- [Node.js](https://nodejs.org) for the frontend  
- npm (comes with Node.js)  
- [Java JDK](https://adoptium.net) for the backend  
- [Maven](https://maven.apache.org) or Gradle (depending on your build tool) for the backend  



## Running the Application

### Backend (Spring Boot)

1. Open a terminal and navigate to the backend project directory.  
2. Build the backend with Maven (if you use Maven wrapper):  
./mvnw clean package

Or if Maven is installed globally:  
mvn clean package

3. Run the backend application:  
java -jar target/cryptosim-0.0.1-SNAPSHOT.jar
 
4. The backend will start on `http://localhost:8080` by default.

### Frontend (React)

1. Open a terminal and navigate to the frontend project directory.  
2. Install dependencies (only needed once):  
npm install

3. Create a production build:  
npm run build

4. Serve the production build locally (optional):  
npx serve -s build


5. Open the URL provided by the serve command, usually `http://localhost:3000`.


## Important Notes

- Make sure the frontend is configured to communicate with the backend API URL (check environment variables or API configuration).  
- Start the backend before the frontend to ensure API calls work properly.

