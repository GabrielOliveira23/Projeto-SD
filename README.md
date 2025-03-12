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

---

## 🖥️ Interfaces

⚙️ **Server:**

![server](https://github.com/user-attachments/assets/7e6a6f82-01b3-433e-ba99-902dedcd09f2)

👤 **Client:**

<table>
  <tr>
    <td><strong>Register</strong><br><img src="https://github.com/user-attachments/assets/7a76e867-6dde-4213-b432-13af47a3a040" width="400"></td> <td><strong>My Incidents</strong><br><img src="https://github.com/user-attachments/assets/8dd1f275-0baa-4d0a-a1e6-8bd86cabf14c" width="500"></td> 
  </tr>
  <tr> <td><strong>Login</strong><br><img src="https://github.com/user-attachments/assets/aae480ba-81f6-4e88-a2f4-157068de9377" width="400"></td> <td><strong>Incidents</strong><br><img src="https://github.com/user-attachments/assets/2e51e8b4-8620-4388-8a3f-0912c5d8b6d4" width="500"></td>
  </tr>
  <tr> <td><strong>Incident Report</strong><br><img src="https://github.com/user-attachments/assets/34c051c5-b0d2-4d9b-a438-5c4c90a5f324" width="400"></td> <td><strong>Home</strong><br><img src="https://github.com/user-attachments/assets/4fb222a0-94ba-4fe7-8866-3de98a41af37" width="500"></td> 
  </tr>
</table>
