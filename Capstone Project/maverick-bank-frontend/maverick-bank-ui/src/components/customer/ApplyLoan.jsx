import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatMoney, getErrorMessage } from "../utility/helper"
import Message from "../Message"


function ApplyLoan({ loan, onClose, onSuccess }) {
    const [accounts, setAccounts] = useState([])
    const [accountId, setAccountId] = useState("")
    const [requestedAmount, setRequestedAmount] = useState("")
    const [purpose, setPurpose] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getAccounts = async () => {
            try {
                const response = await axios.get(API_SERVER_URL_DEV + '/api/account/my', authHeader())
                setAccounts(response.data.filter((a) => a.accountStatus === 'ACTIVE'))
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getAccounts()
    }, [])

    const apply = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'loanId': loan.id,
            'accountId': accountId,
            'requestedAmount': requestedAmount,
            'purpose': purpose
        }
        try {
            await axios.post(API_SERVER_URL_DEV + '/api/loan/apply', obj, authHeader())
            setSuccessMsg("Loan application is sent. A bank employee will review it.")
            setRequestedAmount("")
            setPurpose("")
            onSuccess()
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4 mb-4">
            <div className="card-body p-4">
                <div className="d-flex justify-content-between">
                    <h5 className="fw-bold mb-3">Apply for {loan.loanType}</h5>
                    <button className="btn-close" onClick={() => onClose()}></button>
                </div>
                <Message errMsg={errMsg} successMsg={successMsg} />
                {
                    accounts.length === 0 &&
                    <div className="alert alert-warning py-2">You need an ACTIVE account to apply for a loan.</div>
                }
                <form onSubmit={(e) => apply(e)}>
                    <fieldset disabled={accounts.length === 0}>
                        <div className="row">
                            <div className="col-md-4 mb-3">
                                <label className="form-label small fw-semibold text-secondary">Account (the loan is credited here)</label>
                                <select className="form-select" required value={accountId} onChange={(e) => setAccountId(e.target.value)}>
                                    <option value="">Select</option>
                                    {
                                        accounts.map((a) => (
                                            <option key={a.accountId} value={a.accountId}>{a.accountNumber} - {a.accountType}</option>
                                        ))
                                    }
                                </select>
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="form-label small fw-semibold text-secondary">Amount (₹)</label>
                                <input type="number" className="form-control" required step="0.01" min={loan.minAmount} max={loan.maxAmount} value={requestedAmount}
                                    onChange={(e) => setRequestedAmount(e.target.value)} />
                                <small className="text-muted">{formatMoney(loan.minAmount)} to {formatMoney(loan.maxAmount)}</small>
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="form-label small fw-semibold text-secondary">Purpose</label>
                                <input type="text" className="form-control" required value={purpose}
                                    onChange={(e) => setPurpose(e.target.value)} />
                            </div>
                        </div>
                        <button type="submit" className="btn btn-primary">Submit application</button>
                    </fieldset>
                </form>
            </div>
        </div>
    )
}

export default ApplyLoan
