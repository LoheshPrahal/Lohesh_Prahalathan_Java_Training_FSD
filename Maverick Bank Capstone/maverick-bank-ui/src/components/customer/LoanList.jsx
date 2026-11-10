import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatMoney, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/loan/all'

function LoanList({ onApply }) {
    const [loans, setLoans] = useState([])
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getLoans = async () => {
            try {
                const response = await axios.get(apiUrl, authHeader())
                setLoans(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getLoans()
    }, [])

    return (
        <div className="card shadow-sm border-0 rounded-4 mb-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-cash-coin me-2"></i>Loans offered by the bank</h5>
                <Message errMsg={errMsg} />
                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Loan</th>
                                <th className="text-end">Loan amount</th>
                                <th className="text-end">Rate of interest</th>
                                <th className="text-end">Tenure</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                loans.map((l) => (
                                    <tr key={l.id}>
                                        <td>{l.loanType}</td>
                                        <td className="text-end">{formatMoney(l.minAmount)} - {formatMoney(l.maxAmount)}</td>
                                        <td className="text-end">{l.interestRate} %</td>
                                        <td className="text-end">{l.tenureMonths} months</td>
                                        <td className="text-end">
                                            <button className="btn btn-sm btn-primary" onClick={() => onApply(l)}>Apply</button>
                                        </td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
                {loans.length === 0 && <p className="text-muted mb-0">No loans are offered right now.</p>}
            </div>
        </div>
    )
}

export default LoanList
