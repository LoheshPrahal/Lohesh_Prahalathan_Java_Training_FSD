import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatDateTime, formatMoney, getErrorMessage, statusBadge } from "../utility/helper"
import Message from "../Message"

function MyLoans({ refresh }) {
    const [applications, setApplications] = useState([])
    const [availed, setAvailed] = useState([])
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getLoans = async () => {
            try {
                const response = await axios.get(API_SERVER_URL_DEV + '/api/loan/application/my', authHeader())
                setApplications(response.data)
                const availedResponse = await axios.get(API_SERVER_URL_DEV + '/api/loan/application/availed', authHeader())
                setAvailed(availedResponse.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getLoans()
    }, [refresh])

    return (
        <div>
            {/* <!-- Loans which are already credited to the account --> */}
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-check-circle me-2"></i>My availed loans</h5>
                    <Message errMsg={errMsg} />
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Loan</th>
                                    <th>Account</th>
                                    <th className="text-end">Loan amount</th>
                                    <th className="text-end">Rate of interest</th>
                                    <th className="text-end">Tenure</th>
                                    <th>Applied on</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    availed.map((l) => (
                                        <tr key={l.applicationId}>
                                            <td>{l.loanType}</td>
                                            <td>{l.accountNumber}</td>
                                            <td className="text-end">{formatMoney(l.requestedAmount)}</td>
                                            <td className="text-end">{l.interestRate} %</td>
                                            <td className="text-end">{l.tenureMonths} months</td>
                                            <td>{formatDateTime(l.applicationDate)}</td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {availed.length === 0 && <p className="text-muted mb-0">You have not availed any loan yet.</p>}
                </div>
            </div>

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
