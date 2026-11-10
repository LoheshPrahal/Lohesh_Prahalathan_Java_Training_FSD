import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../../utility/helper"
import Message from "../Message"
import Stat from "../transaction/Stat"
import TransactionFilter from "../transaction/TransactionFilter"
import TransactionList from "../transaction/TransactionList"

const apiUrl = API_SERVER_URL_DEV + '/api/transaction'

function EmployeeTransactions() {
    // all - the transactions of all the account holders, account - the transactions of one account
    const [mode, setMode] = useState("all")
    const [accountInput, setAccountInput] = useState("")
    const [accountNumber, setAccountNumber] = useState("")
    // Redux removed - the transactions are kept in the state of this component
    const [transactions, setTransactions] = useState([])
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getTransactions = async () => {
            try {
                if (mode === 'all') {
                    const response = await axios.get(apiUrl + '/all', { ...authHeader(), params: { size: 20 } })
                    setTransactions(response.data)
                }
                else if (accountNumber) {
                    const response = await axios.get(apiUrl + '/' + accountNumber, { ...authHeader(), params: { size: 10 } })
                    setTransactions(response.data)
                }
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getTransactions()
    }, [mode, accountNumber])

    const search = (e) => {
        e.preventDefault()
        setErrMsg("")
        setAccountNumber(accountInput)
    }

    return (
        <div>
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <div className="btn-group mb-3">
                        <button className={"btn " + (mode === 'all' ? "btn-primary" : "btn-outline-primary")} onClick={() => { setErrMsg(""); setMode('all') }}>All accounts</button>
                        <button className={"btn " + (mode === 'account' ? "btn-primary" : "btn-outline-primary")} onClick={() => { setErrMsg(""); setMode('account') }}>One account</button>
                    </div>
                    <Message errMsg={errMsg} />
                    {
                        mode === 'account' &&
                        <form onSubmit={(e) => search(e)}>
                            <div className="row align-items-end">
                                <div className="col-md-8 mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Account number</label>
                                    <input type="text" className="form-control" required pattern="[0-9]{12}" maxLength={12} title="12 digit account number" value={accountInput}
                                        onChange={(e) => setAccountInput(e.target.value)} />
                                </div>
                                <div className="col-md-4 mb-3">
                                    <button type="submit" className="btn btn-primary w-100">Show transactions</button>
                                </div>
                            </div>
                        </form>
                    }
                </div>
            </div>

            {
                (mode === 'all' || accountNumber) &&
                <div>
                    <Stat list={transactions} />
                    <TransactionFilter mode={mode} accountNumber={accountNumber} onResult={setTransactions} />
                    <TransactionList list={transactions} />
                </div>
            }
        </div>
    )
}

export default EmployeeTransactions
