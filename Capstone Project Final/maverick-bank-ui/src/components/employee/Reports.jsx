import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatDateTime, formatMoney, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

const financialApi = API_SERVER_URL_DEV + '/api/report/financial'
const regulatoryApi = API_SERVER_URL_DEV + '/api/report/regulatory'

function Reports() {
    const [report, setReport] = useState(null)
    const [threshold, setThreshold] = useState("1000000")
    const [regulatory, setRegulatory] = useState(null)
    const [errMsg, setErrMsg] = useState("")

    // Financial performance report
    useEffect(() => {
        const getReport = async () => {
            try {
                const response = await axios.get(financialApi, authHeader())
                setReport(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getReport()
    }, [])

    // Regulatory report - the big transactions
    const getRegulatory = async (e) => {
        e.preventDefault()
        setErrMsg("")
        try {
            const response = await axios.get(regulatoryApi, { ...authHeader(), params: { threshold: threshold, size: 100 } })
            setRegulatory(response.data)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div>
            <Message errMsg={errMsg} />

            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-graph-up me-2"></i>Financial performance</h5>
                    {
                        report &&
                        <div>
                            <div className="row mb-3">
                                <div className="col-md-4 mb-3">
                                    <div className="border rounded-3 p-3">
                                        <div className="text-muted small">Active accounts</div>
                                        <div className="h4 fw-bold mb-0">{report.activeAccounts}</div>
                                    </div>
                                </div>
                                <div className="col-md-4 mb-3">
                                    <div className="border rounded-3 p-3">
                                        <div className="text-muted small">Total balance of the customers</div>
                                        <div className="h4 fw-bold mb-0">{formatMoney(report.totalBalance)}</div>
                                    </div>
                                </div>
                                <div className="col-md-4 mb-3">
                                    <div className="border rounded-3 p-3">
                                        <div className="text-muted small">Loans disbursed</div>
                                        <div className="h4 fw-bold mb-0">{formatMoney(report.totalLoansDisbursed)}</div>
                                    </div>
                                </div>
                                <div className="col-md-4 mb-3 mb-md-0">
                                    <div className="border rounded-3 p-3">
                                        <div className="text-muted small">Total deposits</div>
                                        <div className="h4 fw-bold text-success mb-0">{formatMoney(report.totalDeposits)}</div>
                                    </div>
                                </div>
                                <div className="col-md-4">
                                    <div className="border rounded-3 p-3">
                                        <div className="text-muted small">Total withdrawals</div>
                                        <div className="h4 fw-bold text-danger mb-0">{formatMoney(report.totalWithdrawals)}</div>
                                    </div>
                                </div>
                            </div>
                            <h6 className="fw-bold">Loan applications</h6>
                            {
                                report.loanApplicationsByStatus.map((s) => (
                                    <span key={s.status} className={"badge me-2 fs-6 " + statusBadge(s.status)}>{s.status}: {s.numberOfApplications}</span>
                                ))
                            }
                            {report.loanApplicationsByStatus.length === 0 && <span className="text-muted">No loan applications yet.</span>}
                        </div>
                    }
                </div>
            </div>

            <div className="card shadow-sm border-0 rounded-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-shield-check me-2"></i>Regulatory report</h5>
                    <p className="text-muted small">Transactions of the given amount or more.</p>
                    <form onSubmit={(e) => getRegulatory(e)}>
                        <div className="row align-items-end">
                            <div className="col-md-4 mb-3">
                                <label className="form-label small fw-semibold text-secondary">Amount (₹)</label>
                                <input type="number" className="form-control" required min="0" step="0.01" value={threshold}
                                    onChange={(e) => setThreshold(e.target.value)} />
                            </div>
                            <div className="col-md-3 mb-3">
                                <button type="submit" className="btn btn-primary w-100">Show</button>
                            </div>
                        </div>
                    </form>
                    {
                        regulatory &&
                        <div>
                            <div className="table-responsive">
                                <table className="table table-hover align-middle mb-0">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Account</th>
                                            <th>Type</th>
                                            <th>Description</th>
                                            <th className="text-end">Amount</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            regulatory.map((t) => (
                                                <tr key={t.transactionId}>
                                                    <td>{formatDateTime(t.transactionDate)}</td>
                                                    <td>{t.accountNumber}</td>
                                                    <td>{t.transactionType.replace('_', ' ')}</td>
                                                    <td>{t.description}</td>
                                                    <td className="text-end">{formatMoney(t.amount)}</td>
                                                </tr>
                                            ))
                                        }
                                    </tbody>
                                </table>
                            </div>
                            {regulatory.length === 0 && <p className="text-muted mb-0">No transaction of this amount.</p>}
                        </div>
                    }
                </div>
            </div>
        </div>
    )
}

export default Reports
