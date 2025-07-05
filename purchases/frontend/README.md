# Purchases App (React + TypeScript + Redux)

A  coupon management system for 3 roles: **Admin**, **Company**, and **Customer**. Built using React, TypeScript, Redux Toolkit, and styled with modern UI/UX in mind.

---

## 💡 Features

### 🧑‍💼 Admin&#x20;

* Add / view / delete companies and customers
* View all system stats (total companies, customers)

### 🏢 Company&#x20;

* Add new coupons
* View and manage company-specific coupons
* Update company profile
* Delete coupon purchase history

### 🛍️ Customer&#x20;

* View available coupons
* Purchase coupons
* View personal purchase history
* Profile management

### 🔐 Authentication

* Role-based login (Admin / Company / Customer)
* Auth state managed via Redux
* Secure API token handling

### 💅 UI/UX

* Gradient backgrounds with neon-style buttons
* Personalized dashboard greetings (e.g. "Good morning ☀️")
* Animated feedback popups (e.g. "Successfully deleted")

---

## 🛠️ Tech Stack

| Layer         | Tech                           |
| ------------- | ------------------------------ |
| Frontend      | React + TypeScript             |
| State Mgmt    | Redux Toolkit                  |
| Routing       | React Router                   |
| Forms         | React Hook Form                |
| Styling       | CSS Modules                    |
| Notifications | Custom NotificationService.tsx |

---

## 🧩 Folder Structure

```
src/
├── assets/                     # Static files (images, styles, etc.)
├── Components/                 # Reusable and page-level components
│   ├── AuthorizationPage/
│   ├── CardInfo/
│   ├── Coupon/
│   ├── Form/
│   ├── HomePage/
│   ├── LayoutArea/
│   ├── Login/
│   ├── ModalWindow/
│   ├── MyProfile/
│   ├── NotFound/
│   ├── NotificationService/
│   ├── Routing/
│   ├── SearchBar/
│   └── Users/
├── Config/                     # Configuration constants
├── Models/                     # TypeScript interfaces and types
├── MyDate/                     # Custom reusable date components
├── Redux/                      # Redux store and slices
├── Services/                   # API interaction logic
├── Utils/                      # Utility functions
├── App.tsx                     # Root React component
├── main.tsx                    # Application entry point
├── index.css                   # Global styles
└── vite-env.d.ts               # Vite environment typings
```

## 👨‍💻 Author

**Shlomi Aflalo**
Junior Full Stack Developer — React + Java Spring Boot
[GitHub](https://github.com/shlomiaflalo) | [LinkedIn](https://www.linkedin.com/in/shlomi-aflalo-50237b360/)

> For additional improvements, bugs or questions, feel free to reach out or contribute.

---

## Thank you

---
