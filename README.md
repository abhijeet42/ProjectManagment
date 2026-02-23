# 🚀 ProjectManagment

## 📊 AI-Inspired Task Scheduling with Revenue Prediction

A Java + PostgreSQL based Project Management System that integrates  
**Linear Regression + Moving Average** to predict next week’s revenue and assist smarter scheduling decisions.

This project goes beyond CRUD.  
It introduces data-driven prioritization using forecasting logic.

---

# 🎯 Features

- Add, Delete, View, and Update Projects
- Store Weekly Revenue Data
- Predict Next Week Revenue
- Smart Scheduling Based on Forecast
- Reset Database with ID Restart Support
- Error Percentage Calculation for Accuracy Check

---

# 🛠 Tech Stack

## Backend
- Java
- JDBC

## Database
- PostgreSQL

## Prediction Model
- Linear Regression
- Moving Average

---

## 🏗 Project Structure

```text
com.promanage
│
├── model
│   └── Project.java
│
├── dao
│   ├── ProjectDAO.java
│   ├── WeeklyRevenueDAO.java
│   └── ScheduleDAO.java
│
├── service
│   └── PredictionService.java
│
├── Main.java
└── DBConnection.java
```

---

# 🧩 Core Components

## 1️⃣ DBConnection.java
Handles database connectivity using JDBC.  
Centralizes PostgreSQL connection management.

---

## 2️⃣ ProjectDAO.java

Responsible for:
- addProject()
- deleteProject()
- getAllProjects()
- updateStatus()
- resetAllProjects()


### 🔁 Reset Operation
```sql
TRUNCATE TABLE project RESTART IDENTITY CASCADE;
```
This operation:
- Deletes all records  
- Resets ID sequence to 1  
- Maintains foreign key integrity  

---

## 3️⃣ WeeklyRevenueDAO.java

Responsibilities:

- Inserts weekly revenue  
- Fetches revenue history  
- Supplies data to `PredictionService`  

---

## 4️⃣ PredictionService.java (Core Logic)

This class implements the forecasting engine.

It combines two techniques:

---

### 🔹 Moving Average

**Formula:**
MA = (Sum of last N weeks revenue) / N


**Purpose:**

- Smooths short-term fluctuations  
- Reduces noise in volatile data  

---

### 🔹 Linear Regression

**Formula:**
y = a + bx


Where:

- `y` = predicted revenue  
- `x` = week number  
- `b` = slope (trend direction)  
- `a` = intercept  


**Purpose:**

- Detects upward or downward revenue trend  
- Captures long-term growth pattern  

---

### 🔹 Final Prediction Strategy
Final Prediction = Trend (Linear Regression) + Stability (Moving Average)


This hybrid approach:

- Works well for small datasets  
- Reduces overfitting  
- Improves stability  

---

# 📅 Scheduling Decision Logic

After generating prediction:
If Predicted Revenue > Current Revenue


→ Future projects may generate higher revenue  
→ Adjust scheduling priority accordingly  

Else:

→ Focus on completing current projects  

This makes scheduling data-driven instead of assumption-based.

---

# 🗄 Database Schema

## Project Table

- id  
- name  
- revenue  
- deadline  
- status  

## WeeklyRevenue Table

- id  
- week_no  
- revenue  

---

# 📊 Accuracy Measurement

**Error Percentage Formula:**
Error % = |Actual - Predicted| / Actual × 100

Lower error percentage indicates better model performance.

---

# ⚙ How To Run

### 1️⃣ Clone Repository
git clone https://github.com/abhijeet42/ProjectManagment

### 2️⃣ Create PostgreSQL Database
```
CREATE DATABASE prmanage;
```

### 3️⃣ Configure DBConnection.java

Update:

- Database URL  
- Username  
- Password  

### 4️⃣ Run Main Class

---

# 🚀 Future Improvements

- Implement ARIMA Forecasting  
- Add REST API Layer  
- Create Web Dashboard  
- Add Graph Visualization  
- Implement Authentication & Roles  
- Improve Prediction Weight Optimization  

---

# 👨‍💻 Author

Abhijeet
