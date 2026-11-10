import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatDateTime, formatMoney, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

const applicationApi = API_SERVER_URL_DEV + '/api/loan/application/my'
// API version 1 - the loans of the logged-in customer (disbursed and closed, with the remaining amount)
const availedApi = API_SERVER_URL_DEV + '/api/v1/loan/application/availed'
// API version 2 - one loan of the logged-in customer, found with the id
const loanApi = API_SERVER_URL_DEV + '/api/v2/loan/application'
const repayApi = API_SERVER_URL_DEV + '/api/loan/application/repay'

function MyLoans({ refresh }) {
    const [applications, setApplications] = useState([])
    const [availed, setAvailed] = useState([])
    // Repayment - the loan which is being repaid and the amount chosen by the customer
    const [repayLoan, setRepayLoan] = useState(null)
    const [amount, setAmount] = useState("")
    const [reload, setReload] = useState(0)
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getLoans = async () => {
            try {
                const response = await axios.get(applicationApi, authHeader())
                setApplications(response.data)
                const availedResponse = await axios.get(availedApi, authHeader())
                setAvailed(availedResponse.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getLoans()
    }, [refresh, reload])

    // Repayment - the latest details of the loan are fetched with the id (API version 2)
    const startRepay = async (id) => {
        setErrMsg("")
        setSuccessMsg("")
        try {
            const response = await axios.get(loanApi + '/' + id, authHeader())
            setRepayLoan(response.data)
            setAmount("")
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    // Repayment - the customer repays any amount, which is not more than the remaining amount
    const repay = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'applicationId': repayLoan.applicationId,
            'amount': amount
        }
        try {
            await axios.post(repayApi, obj, authHeader())
            setSuccessMsg(formatMoney(amount) + " is repaid.")
            setRepayLoan(null)
            setAmount("")
            setReload(reload + 1)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div>
            {/* <!-- Loans which are already credited to the account --> */}
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-check-circle me-2"></i>My availed loans</h5>
                    <Message errMsg={errMsg} successMsg={successMsg} />
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Loan</th>
                                    <th>Account</th>
                                    <th className="text-end">Loan amount</th>
                                    <th className="text-end">Remaining amount</th>
                                    <th className="text-end">Rate of interest</th>
                                    <th className="text-end">Tenure</th>
                                    <th>Applied on</th>
                                    <th>Status</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    availed.map((l) => (
                                        <tr key={l.applicationId}>
                                            <td>{l.loanType}</td>
                                            <td>{l.accountNumber}</td>
                                            <td className="text-end">{formatMoney(l.requestedAmount)}</td>
                                            <td className="text-end">{formatMoney(l.remainingAmount)}</td>
                                            <td className="text-end">{l.interestRate} %</td>
                                            <td className="text-end">{l.tenureMonths} months</td>
                                            <td>{formatDateTime(l.applicationDate)}</td>
                                            <td><span className={"badge " + statusBadge(l.status)}>{l.status}</span></td>
                                            <td className="text-end">
                                                {
                                                    l.status === 'DISBURSED' && Number(l.remainingAmount) > 0 &&
                                                    <button className="btn btn-sm btn-outline-primary" onClick={() => startRepay(l.applicationId)}>Repay</button>
                                                }
                                                {
                                                    l.status === 'DISBURSED' && Number(l.remainingAmount) === 0 &&
                                                    <span className="text-muted small">Fully repaid</span>
                                                }
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {availed.length === 0 && <p className="text-muted mb-0">You have not availed any loan yet.</p>}
                </div>
            </div>

            {/* <!-- Repayment of a loan --> */}
            {
                repayLoan &&
                <div className="card shadow-sm border-0 rounded-4 mb-4">
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">Repay the loan</h5>
                        <dl className="row">
                            <dt className="col-sm-3">Loan</dt>
                            <dd className="col-sm-9">{repayLoan.loanType}</dd>
                            <dt className="col-sm-3">Account</dt>
                            <dd className="col-sm-9">{repayLoan.accountNumber}</dd>
                            <dt className="col-sm-3">Loan amount</dt>
                            <dd className="col-sm-9">{formatMoney(repayLoan.requestedAmount)}</dd>
                            <dt className="col-sm-3">Remaining amount</dt>
                            <dd className="col-sm-9 fw-semibold">{formatMoney(repayLoan.remainingAmount)}</dd>
                        </dl>
                        <p className="text-muted small">The amount is taken from the account of the loan. Once the full amount is repaid, a bank employee will close the loan.</p>
                        <form onSubmit={(e) => repay(e)}>
                            <div className="row">
                                <div className="col-md-4 mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Amount to repay (₹)</label>
                                    <input type="number" className="form-control" required min="0.01" max={repayLoan.remainingAmount} step="0.01" value={amount}
                                        onChange={(e) => setAmount(e.target.value)} />
                                </div>
                            </div>
                            <button type="submit" className="btn btn-primary me-2">Repay</button>
                            <button type="button" className="btn btn-secondary" onClick={() => setRepayLoan(null)}>Cancel</button>
                        </form>
                    </div>
                </div>
            }

            {/* <!-- All the applications --> */}
            <div className="card shadow-sm border-0 rounded-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-file-earmark-text me-2"></i>My loan applications</h5>
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Loan</th>
                                    <th>Account</th>
                                    <th className="text-end">Amount</th>
                                    <th>Purpose</th>
                                    <th>Applied on</th>
                                    <th>Status</th>
                                    <th>Reviewed on</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    applications.map((l) => (
                                        <tr key={l.applicationId}>
                                            <td>{l.loanType}</td>
                                            <td>{l.accountNumber}</td>
                                            <td className="text-end">{formatMoney(l.requestedAmount)}</td>
                                            <td>{l.purpose}</td>
                                            <td>{formatDateTime(l.applicationDate)}</td>
                                            <td><span className={"badge " + statusBadge(l.status)}>{l.status}</span></td>
                                            <td>{l.reviewedDate ? l.reviewedDate : '-'}</td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {applications.length === 0 && <p className="text-muted mb-0">No applications yet.</p>}
                </div>
            </div>
        </div>
    )
}

export default MyLoans
