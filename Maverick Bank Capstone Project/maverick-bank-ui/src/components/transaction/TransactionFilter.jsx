import { useState } from "react"
import { useDispatch } from "react-redux"
import { getAllTransactions, getTransactions, getTransactionsByAccount } from "../../store/actions/TransactionAction"
import { getErrorMessage, toInputDate } from "../../utility/helper"
import Message from "../Message"

// mode - my: a customer looks at an own account, account: a bank employee looks at one account, all: a bank employee looks at all the accounts
function TransactionFilter({ mode, accountNumber }) {
    const [fromDate, setFromDate] = useState("")
    const [toDate, setToDate] = useState("")
    const [transactionType, setTransactionType] = useState("")
    const [errMsg, setErrMsg] = useState("")

    const dispatch = useDispatch()

    const fetchList = async (from, to, type, size) => {
        setErrMsg("")
        try {
            switch (mode) {
                case 'my':
                    await dispatch(getTransactions(accountNumber, from, to, type, size))
                    break;
                case 'account':
                    await dispatch(getTransactionsByAccount(accountNumber, from, to, type, size))
                    break;
                default:
                    await dispatch(getAllTransactions(from, to, type, size))
                    break;
            }
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    // Option 1 - the last 10 transactions
    const lastTen = () => {
        setFromDate("")
        setToDate("")
        setTransactionType("")
        fetchList("", "", "", 10)
    }

    // Option 2 - the transactions of the last month
    const lastMonth = () => {
        const today = new Date()
        const monthAgo = new Date()
        const day = monthAgo.getDate()
        monthAgo.setDate(1)
        monthAgo.setMonth(monthAgo.getMonth() - 1)
        // 31st March - 1 month = 28th February (not 3rd March)
        monthAgo.setDate(Math.min(day, new Date(monthAgo.getFullYear(), monthAgo.getMonth() + 1, 0).getDate()))

        setFromDate(toInputDate(monthAgo))
        setToDate(toInputDate(today))
        setTransactionType("")
        fetchList(toInputDate(monthAgo), toInputDate(today), "", 100)
    }

    // Option 3 - the transactions between two dates
    const search = (e) => {
        e.preventDefault()
        fetchList(fromDate, toDate, transactionType, 100)
    }

    return (
        <div className="card shadow-sm border-0 rounded-4 mb-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-funnel me-2"></i>Show transactions</h5>
                <Message errMsg={errMsg} />
                <div className="mb-3">
                    <button type="button" className="btn btn-outline-primary me-2" onClick={() => lastTen()}>Last 10</button>
                    <button type="button" className="btn btn-outline-primary" onClick={() => lastMonth()}>Last month</button>
                </div>
                <form onSubmit={(e) => search(e)}>
                    <div className="row align-items-end">
                        <div className="col-md-3 mb-3">
                            <label className="form-label small fw-semibold text-secondary">From date</label>
                            <input type="date" className="form-control" value={fromDate} max={toDate || undefined}
                                onChange={(e) => setFromDate(e.target.value)} />
                        </div>
                        <div className="col-md-3 mb-3">
                            <label className="form-label small fw-semibold text-secondary">To date</label>
                            <input type="date" className="form-control" value={toDate} min={fromDate || undefined}
                                onChange={(e) => setToDate(e.target.value)} />
                        </div>
                        <div className="col-md-3 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Type</label>
                            <select className="form-select" value={transactionType} onChange={(e) => setTransactionType(e.target.value)}>
                                <option value="">All</option>
                                <option value="DEPOSIT">Deposit</option>
                                <option value="WITHDRAWAL">Withdrawal</option>
                                <option value="TRANSFER_OUT">Transfer out</option>
                                <option value="TRANSFER_IN">Transfer in</option>
                                <option value="LOAN_DISBURSEMENT">Loan disbursement</option>
                            </select>
                        </div>
                        <div className="col-md-3 mb-3">
                            <button type="submit" className="btn btn-primary w-100">Search</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default TransactionFilter
