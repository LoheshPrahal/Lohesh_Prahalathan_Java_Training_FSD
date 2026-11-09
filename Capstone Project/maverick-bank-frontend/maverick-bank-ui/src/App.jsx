import { Route, Routes } from "react-router"
import Login from "./components/auth/Login"
import SignUp from "./components/customer/Signup"
import CustomerDashboard from "./components/customer/CustomerDashboard"


function App() {

  return (
    <Routes>
    <Route path="" element={<Login />}/>
    <Route path="customer/signup" element={<SignUp />}/>
    <Route path="customer/:token" element={<CustomerDashboard />}/>
    {/* <Route path="employee/:token" element={<EmployeeDashboard />}/>
    <Route path="admin/:token" element={<AdminDashboard />}/> */}
    {/* <Route path="*" element={<PageNotFound />}/> */}
  </Routes>
  )
}

export default App
