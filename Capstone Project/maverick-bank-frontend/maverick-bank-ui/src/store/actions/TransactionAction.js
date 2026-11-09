import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"

export const getTransactions = (accountNumber, fromDate, toDate, transactionType, size)=>async(dispatch)=>{

    const response = await axios.get(`${API_SERVER_URL_DEV}/api/transaction/my/${accountNumber}`, 
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
            params: { fromDate: fromDate || undefined, toDate: toDate || undefined, transactionType: transactionType || undefined, size: size }
        }
    )

    dispatch({
        type: 'FETCH_TRANSACTIONS',
        payload : response.data
    })
}

// Bank employee - transactions of any account
export const getTransactionsByAccount = (accountNumber, fromDate, toDate, transactionType, size)=>async(dispatch)=>{

    const response = await axios.get(`${API_SERVER_URL_DEV}/api/transaction/${accountNumber}`, 
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
            params: { fromDate: fromDate || undefined, toDate: toDate || undefined, transactionType: transactionType || undefined, size: size }
        }
    )

    dispatch({
        type: 'FETCH_TRANSACTIONS',
        payload : response.data
    })
}

// Bank employee - transactions of all the accounts
export const getAllTransactions = (fromDate, toDate, transactionType, size)=>async(dispatch)=>{

    const response = await axios.get(`${API_SERVER_URL_DEV}/api/transaction/all`, 
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
            params: { fromDate: fromDate || undefined, toDate: toDate || undefined, transactionType: transactionType || undefined, size: size }
        }
    )

    dispatch({
        type: 'FETCH_TRANSACTIONS',
        payload : response.data
    })
}

// The next three APIs do not send the saved transaction back, so the list of the account is fetched again
export const deposit = (transaction, accountNumber)=>async(dispatch)=>{
    
    await axios.post(`${API_SERVER_URL_DEV}/api/transaction/deposit`, transaction ,
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        })
    dispatch(getTransactions(accountNumber, '', '', '', 10))
}

export const withdraw = (transaction, accountNumber)=>async(dispatch)=>{
    
    await axios.post(`${API_SERVER_URL_DEV}/api/transaction/withdraw`, transaction ,
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        })
    dispatch(getTransactions(accountNumber, '', '', '', 10))
}

export const transfer = (transaction, accountNumber)=>async(dispatch)=>{
    
    await axios.post(`${API_SERVER_URL_DEV}/api/transaction/transfer`, transaction ,
        {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        })
    dispatch(getTransactions(accountNumber, '', '', '', 10))
}

// Used at logout, so that the next user never sees the list of the previous user
export const clearTransactions = ()=>(dispatch)=>{
    dispatch({
        type: 'CLEAR_TRANSACTIONS'
    })
}
