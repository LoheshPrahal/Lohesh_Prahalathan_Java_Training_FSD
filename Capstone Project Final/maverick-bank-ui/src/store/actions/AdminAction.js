import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader } from "../../utility/helper"

const customerApi = API_SERVER_URL_DEV + '/api/customer'
const employeeApi = API_SERVER_URL_DEV + '/api/employee'
const branchApi = API_SERVER_URL_DEV + '/api/branch'
const loanApi = API_SERVER_URL_DEV + '/api/loan'
const adminApi = API_SERVER_URL_DEV + '/api/admin'

// Customers
export const getCustomers = ()=>async(dispatch)=>{

    const response = await axios.get(customerApi + '/all', authHeader())

    dispatch({
        type: 'FETCH_CUSTOMERS',
        payload : response.data
    })
}

// The next two APIs do not send the saved customer back, so the list is fetched again
export const updateCustomer = (id, customer)=>async(dispatch)=>{

    await axios.put(customerApi + '/' + id, customer, authHeader())
    await dispatch(getCustomers())
}

export const deactivateCustomer = (id)=>async(dispatch)=>{

    await axios.put(customerApi + '/deactivate/' + id, {}, authHeader())
    await dispatch(getCustomers())
}

// Bank employees
export const getEmployees = ()=>async(dispatch)=>{

    const response = await axios.get(employeeApi + '/all', authHeader())

    dispatch({
        type: 'FETCH_EMPLOYEES',
        payload : response.data
    })
}

// The next three APIs do not send the saved employee back, so the list is fetched again
export const addEmployee = (employee)=>async(dispatch)=>{

    await axios.post(employeeApi + '/add', employee, authHeader())
    await dispatch(getEmployees())
}

export const updateEmployee = (id, employee)=>async(dispatch)=>{

    await axios.put(employeeApi + '/' + id, employee, authHeader())
    await dispatch(getEmployees())
}

export const deactivateEmployee = (id)=>async(dispatch)=>{

    await axios.put(employeeApi + '/deactivate/' + id, {}, authHeader())
    await dispatch(getEmployees())
}

// Branches (the list of the branches is open for everyone, so no token is sent)
export const getBranches = ()=>async(dispatch)=>{

    const response = await axios.get(branchApi + '/all')

    dispatch({
        type: 'FETCH_BRANCHES',
        payload : response.data
    })
}

export const addBranch = (branch)=>async(dispatch)=>{

    await axios.post(branchApi + '/add', branch, authHeader())
    await dispatch(getBranches())
}

// Loans
export const getLoans = ()=>async(dispatch)=>{

    const response = await axios.get(loanApi + '/all', authHeader())

    dispatch({
        type: 'FETCH_LOANS',
        payload : response.data
    })
}

export const addLoan = (loan)=>async(dispatch)=>{

    await axios.post(loanApi + '/add', loan, authHeader())
    await dispatch(getLoans())
}

// Admins
export const getAdmins = ()=>async(dispatch)=>{

    const response = await axios.get(adminApi + '/all', authHeader())

    dispatch({
        type: 'FETCH_ADMINS',
        payload : response.data
    })
}

// Used at logout, so that the next user never sees the lists of the previous user
export const clearAdminData = ()=>(dispatch)=>{
    dispatch({
        type: 'CLEAR_ADMIN_DATA'
    })
}
