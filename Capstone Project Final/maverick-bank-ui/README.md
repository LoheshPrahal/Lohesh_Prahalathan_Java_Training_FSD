# Maverick Bank UI

React + Bootstrap frontend of the Maverick Bank backend (same structure as the helpdesk UI).

## Run

1. Start the backend (`maverick-bank`, port 8081).
2. In this folder:

```
npm install
npm run dev
```

3. Open http://localhost:5173

The address of the backend is in `config.js`. The backend controllers have `@CrossOrigin(origins = "http://localhost:5173")`, so change that if you use another port.

## First time setup (empty database)

1. Create the first admin with the API `POST /api/admin/add` (Postman collection, request 0.1).
2. Login as the admin: add the **branches** and the **loans**, then add a **bank employee** (needs a branch).
3. A customer registers on the Sign up page (it also opens the first account, with a branch).
4. Login as the bank employee and approve the account. After that the customer can use the account.

## Screens

Every role has a dashboard with a `Navbar`, a welcome message and an `Outlet`, the pages are child routes (same as the executive screens of the helpdesk UI).

| Role | Routes | Pages |
|---|---|---|
| Customer | `/customer` | Home (balance chart), Accounts, Transactions, Beneficiaries, Loans, Profile |
| Bank employee | `/employee` | Home (waiting work, loan chart), Accounts (approval), Account Search, Close Requests, Customer Search (`/employee/customer/:id`), Transactions, Loans, Reports |
| Admin | `/admin` | Home (customer and employee charts), Customers, Bank Employees, Branches, Loans, Admins |

Public routes: `/` (login) and `/customer/signup`.

Redux is used only for the transactions (`src/store`), the rest of the pages keep their data in the component state.
