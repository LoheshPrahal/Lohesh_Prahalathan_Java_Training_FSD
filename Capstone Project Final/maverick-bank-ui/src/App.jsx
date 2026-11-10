import { Route, Routes } from "react-router"
import Login from "./components/auth/Login"
import SignUp from "./components/customer/SignUp"
import PageNotFound from "./components/PageNotFound"
import CustomerDashboard from "./components/customer/CustomerDashboard"
import CustomerHome from "./components/customer/Home"
import MyAccounts from "./components/customer/MyAccounts"
import CustomerTransactions from "./components/customer/CustomerTransactions"
import Beneficiaries from "./components/customer/Beneficiaries"
import Loans from "./components/customer/Loans"
import Profile from "./components/customer/Profile"
import EmployeeDashboard from "./components/employee/EmployeeDashboard"
import EmployeeHome from "./components/employee/Home"
import PendingAccounts from "./components/employee/PendingAccounts"
import AccountSearch from "./components/employee/AccountSearch"
import ClosureRequests from "./components/employee/ClosureRequests"
import CustomerSearch from "./components/employee/CustomerSearch"
import ViewCustomer from "./components/employee/ViewCustomer"
import EmployeeTransactions from "./components/employee/EmployeeTransactions"
import LoanApplications from "./components/employee/LoanApplications"
import Reports from "./components/employee/Reports"
import AdminDashboard from "./components/admin/AdminDashboard"
import AdminHome from "./components/admin/Home"
import CustomerManage from "./components/admin/CustomerManage"
import EmployeeManage from "./components/admin/EmployeeManage"
import BranchManage from "./components/admin/BranchManage"
import LoanManage from "./components/admin/LoanManage"
import AdminList from "./components/admin/AdminList"

function App() {
return(
  <Routes>
    <Route path="" element={<Login />}/>
    <Route path="customer/signup" element={<SignUp />}/>
    <Route path="customer" element={<CustomerDashboard />}>
      <Route path="" element={<CustomerHome />}/>
      <Route path="accounts" element={<MyAccounts />}/>
      <Route path="transactions" element={<CustomerTransactions />}/>
      <Route path="beneficiaries" element={<Beneficiaries />}/>
      <Route path="loans" element={<Loans />}/>
      <Route path="profile" element={<Profile />}/>
    </Route>
    <Route path="employee" element={<EmployeeDashboard />}>
      <Route path="" element={<EmployeeHome />}/>
      <Route path="accounts" element={<PendingAccounts />}/>
      <Route path="account-search" element={<AccountSearch />}/>
      <Route path="close-requests" element={<ClosureRequests />}/>
      <Route path="customer-search" element={<CustomerSearch />}/>
      <Route path="customer/:id" element={<ViewCustomer />}/>
      <Route path="transactions" element={<EmployeeTransactions />}/>
      <Route path="loans" element={<LoanApplications />}/>
      <Route path="reports" element={<Reports />}/>
    </Route>
    <Route path="admin" element={<AdminDashboard />}>
      <Route path="" element={<AdminHome />}/>
      <Route path="customers" element={<CustomerManage />}/>
      <Route path="employees" element={<EmployeeManage />}/>
      <Route path="branches" element={<BranchManage />}/>
      <Route path="loans" element={<LoanManage />}/>
      <Route path="admins" element={<AdminList />}/>
    </Route>
    <Route path="*" element={<PageNotFound />}/>
  </Routes>
) 
  
}

export default App
