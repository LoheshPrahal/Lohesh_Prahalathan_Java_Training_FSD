import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatDateTime, formatMoney, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const pendingApi = API_SERVER_URL_DEV + '/api/loan/application/pending'
const approvedApi = API_SERVER_URL_DEV + '/api/loan/application/approved'
// Repayment - loans with nothing more to pay
const repaidApi = API_SERVER_URL_DEV + '/api/loan/application/repaid'

function LoanApplications() {
    const [pending, setPending] = useState([])
    const [approved, setApproved] = useState([])
    const [repaid, setRepaid] = useState([])
    const [credit, setCredit] = useState(null)
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getApplications = async () => {
            try {
                const response = await axios.get(pendingApi, authHeader())
                setPending(response.data)
                const approvedResponse = await axios.get(approvedApi, authHeader())
                setApproved(approvedResponse.data)
                const repaidResponse = await axios.get(repaidApi, authHeader())
                setRepaid(repaidResponse.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getApplications()
    }, [refresh])

    // Creditworthiness is checked with the inbound and outbound transactions of the account
    const checkCredit = async (application) => {
        setErrMsg("")
        try {
            const response = await axios.get(API_SERVER_URL_DEV + '/api/transaction/summary/' + application.accountId, authHeader())
            setCredit({ ...response.data, customerName: application.customerName, applicationId: application.applicationId })
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    // status is APPROVED or REJECTED
    const review = async (id, status) => {
        setErrMsg("")
        setSuccessMsg("")
        try {
            await axios.put(API_SERVER_URL_DEV + '/api/loan/application/review/' + id, {}, { ...authHeader(), params: { status: status } })
            setSuccessMsg("Application is " + status.toLowerCase() + ".")
            setCredit(null)
            setRefresh(refresh + 1)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    // The approved loan is credited to the account of the customer
    const disburse = async (id) => {
        setErrMsg("")
        setSuccessMsg("")
        try {
            await axios.put(API_SERVER_URL_DEV + '/api/loan/application/disburse/' + id, {}, authHeader())
            setSuccessMsg("Loan is disbursed to the account of the customer.")
            setRefresh(refresh + 1)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    // Repayment - the employee closes the loan which is fully repaid
    const close = async (id) => {
        setErrMsg("")
        setSuccessMsg("")
        try {
            await axios.put(API_SERVER_URL_DEV + '/api/loan/application/close/' + id, {}, authHeader())
            setSuccessMsg("Loan is closed.")
            setRefresh(refresh + 1)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div>
            <Message errMsg={errMsg} successMsg={successMsg} />

            {/* <!-- Applications to review --> */}
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-hourglass-split me-2"></i>Applications to review</h5>
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Customer</th>
                                    <th>Account</th>
                                    <th>Loan</th>
                                    <th className="text-end">Amount</th>
                                    <th>Purpose</th>
                                    <th>Applied on</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    pending.map((a) => (
                                        <tr key={a.applicationId}>
                                            <td>{a.customerName}</td>
                                            <td>{a.accountNumber}</td>
                                            <td>{a.loanType} ({a.interestRate} %, {a.tenureMonths} months)</td>
                                            <td className="text-end">{formatMoney(a.requestedAmount)}</td>
                                            <td>{a.purpose}</td>
                                            <td>{formatDateTime(a.applicationDate)}</td>
                                            <td className="text-end text-nowrap">
                                                <button className="btn btn-sm btn-outline-primary me-2" onClick={() => checkCredit(a)}>Check credit</button>
                                                <button className="btn btn-sm btn-success me-2" onClick={() => review(a.applicationId, 'APPROVED')}>Approve</button>
                                                <button className="btn btn-sm btn-outline-danger" onClick={() => review(a.applicationId, 'REJECTED')}>Reject</button>
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {pending.length === 0 && <p className="text-muted mb-0">No application is waiting for a decision.</p>}

                    {
                        credit &&
                        <div className="alert alert-info mt-3 mb-0">
                            <div className="fw-semibold">Credit check - {credit.customerName} (account {credit.accountNumber})</div>
                            Total inbound: <span className="text-success fw-semibold">{formatMoney(credit.totalInbound)}</span>,
                            total outbound: <span className="text-danger fw-semibold">{formatMoney(credit.totalOutbound)}</span>
                        </div>
                    }
                </div>
            </div>

            {/* <!-- Approved applications, waiting for the disbursement --> */}
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-check-circle me-2"></i>Approved - waiting for the disbursement</h5>
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Customer</th>
                                    <th>Account</th>
                                    <th>Loan</th>
                                    <th className="text-end">Amount</th>
                                    <th>Reviewed on</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    approved.map((a) => (
                                        <tr key={a.applicationId}>
                                            <td>{a.customerName}</td>
                                            <td>{a.accountNumber}</td>
                                            <td>{a.loanType}</td>
                                            <td className="text-end">{formatMoney(a.requestedAmount)}</td>
                                            <td>{a.reviewedDate}</td>
                                            <td className="text-end">
                                                <button className="btn btn-sm btn-primary" onClick={() => disburse(a.applicationId)}>Disburse</button>
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {approved.length === 0 && <p className="text-muted mb-0">No approved loan is waiting.</p>}
                </div>
            </div>

            {/* <!-- Loans with the remaining amount zero, waiting to be closed --> */}
            <div className="card shadow-sm border-0 rounded-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-patch-check me-2"></i>Fully repaid - waiting to be closed</h5>
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Customer</th>
                                    <th>Account</th>
                                    <th>Loan</th>
                                    <th className="text-end">Loan amount</th>
                                    <th className="text-end">Remaining amount</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    repaid.map((a) => (
                                        <tr key={a.applicationId}>
                                            <td>{a.customerName}</td>
                                            <td>{a.accountNumber}</td>
                                            <td>{a.loanType}</td>
                                            <td className="text-end">{formatMoney(a.requestedAmount)}</td>
                                            <td className="text-end">{formatMoney(a.remainingAmount)}</td>
                                            <td className="text-end">
                                                <button className="btn btn-sm btn-success" onClick={() => close(a.applicationId)}>Close loan</button>
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {repaid.length === 0 && <p className="text-muted mb-0">No fully repaid loan is waiting to be closed.</p>}
                </div>
            </div>
        </div>
    )
}

export default LoanApplications
