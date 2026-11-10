import { useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatDateTime, formatMoney, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

function AccountSearch() {
    const [accountNumber, setAccountNumber] = useState("")
    const [account, setAccount] = useState(null)
    const [summary, setSummary] = useState(null)
    const [errMsg, setErrMsg] = useState("")

    // Account details with the total inbound and outbound transactions
    const search = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setAccount(null)
        setSummary(null)
        try {
            const response = await axios.get(API_SERVER_URL_DEV + '/api/account/number/' + accountNumber, authHeader())
            setAccount(response.data)
            const summaryResponse = await axios.get(API_SERVER_URL_DEV + '/api/transaction/summary/' + response.data.accountId, authHeader())
            setSummary(summaryResponse.data)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-search me-2"></i>Find an account</h5>
                <Message errMsg={errMsg} />
                <form onSubmit={(e) => search(e)}>
                    <div className="row align-items-end">
                        <div className="col-md-8 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Account number</label>
                            <input type="text" className="form-control" required pattern="[0-9]{12}" maxLength={12} title="12 digit account number" value={accountNumber}
                                onChange={(e) => setAccountNumber(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <button type="submit" className="btn btn-primary w-100">Search</button>
                        </div>
                    </div>
                </form>

                {
                    account &&
                    <dl className="row mb-0 mt-2">
                        <dt className="col-sm-3">Customer</dt>
                        <dd className="col-sm-9">{account.customerName}</dd>
                        <dt className="col-sm-3">Account</dt>
                        <dd className="col-sm-9">{account.accountNumber} ({account.accountType}) <span className={"badge " + statusBadge(account.accountStatus)}>{account.accountStatus}</span></dd>
                        <dt className="col-sm-3">Branch</dt>
                        <dd className="col-sm-9">{account.branchName}, {account.branchAddress} ({account.ifscCode})</dd>
                        <dt className="col-sm-3">Balance</dt>
                        <dd className="col-sm-9">{formatMoney(account.balance)}</dd>
                        <dt className="col-sm-3">Opened on</dt>
                        <dd className="col-sm-9">{formatDateTime(account.openedDate)}</dd>
                        {
                            summary &&
                            <>
                                <dt className="col-sm-3">Total inbound</dt>
                                <dd className="col-sm-9 text-success fw-semibold">{formatMoney(summary.totalInbound)}</dd>
                                <dt className="col-sm-3">Total outbound</dt>
                                <dd className="col-sm-9 text-danger fw-semibold">{formatMoney(summary.totalOutbound)}</dd>
                            </>
                        }
                    </dl>
                }
            </div>
        </div>
    )
}

export default AccountSearch
