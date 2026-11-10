import { useEffect, useState } from "react"
import { useDispatch } from "react-redux"
import { clearTransactions, getAllTransactions, getTransactionsByAccount } from "../../store/actions/TransactionAction"
import { getErrorMessage } from "../../utility/helper"
import Message from "../Message"
import Stat from "../transaction/Stat"
import TransactionFilter from "../transaction/TransactionFilter"
import TransactionList from "../transaction/TransactionList"

function EmployeeTransactions() {
    // all - the transactions of all the account holders, account - the transactions of one account
    const [mode, setMode] = useState("all")
    const [accountInput, setAccountInput] = useState("")
    const [accountNumber, setAccountNumber] = useState("")
    const [errMsg, setErrMsg] = useState("")

    const dispatch = useDispatch()

    useEffect(() => {
        if (mode === 'all') {
            dispatch(getAllTransactions('', '', '', 20)).catch((err) => setErrMsg(getErrorMessage(err)))
        }
        else if (accountNumber) {
            dispatch(getTransactionsByAccount(accountNumber, '', '', '', 10)).catch((err) => setErrMsg(getErrorMessage(err)))
        }
        else {
            dispatch(clearTransactions())
        }
    }, [mode, accountNumber, dispatch])

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
                    <Stat />
                    <TransactionFilter mode={mode} accountNumber={accountNumber} />
                    <TransactionList />
                </div>
            }
        </div>
    )
}

export default EmployeeTransactions
