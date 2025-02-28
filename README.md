# 📌 Accident Tracker

Welcome to my **Distributed Systems Project**

👤 **Owner:** Gabriel Leonardo Martins de Oliveira  

---

## 📂 Folder Structure

### 📚 Libraries
- 🛠 **gson-2.10.1** → Library for working with JSON
- 🔐 **jBCrypt-0.4.1** → Library for hashing and securely storing passwords in the database
- 🗄️ **mongo-java-driver** → Connector for MongoDB database

### 🏗️ Classes
- **Server** → Runs the server
- **ServerTreatment** → Handles server functions
- **Client** → Runs the client
- **User** → Entity class representing the user, executing functions and calling the database connection
- **UserDB** → Handles communication with the database
- **Database** → Establishes database connection
- **CaesarCrypt** → Encrypts passwords for secure transmission between client and server

---

## 🗄️ Database
The database used is **MongoDB**. The connector is located in the `libs` folder. 

✅ **Setup Instructions:**
1. Download and install **MongoDB**.
2. Create a collection named **`projeto-sd`**.
3. Ensure the correct database setup to avoid connection errors.

---

## 🚀 Running the Application
To start the project:

▶️ **Run the Server:** `src/server/AppServer.java`

▶️ **Run the Client:** `src/client/AppClient.java`

💡 **Tip:** Ensure the database is correctly configured before starting the server.
