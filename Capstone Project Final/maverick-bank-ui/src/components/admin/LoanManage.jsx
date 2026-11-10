import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { getLoans } from "../../store/actions/AdminAction"
import { formatMoney, getErrorMessage } from "../../utility/helper"
import Message from "../Message"
import AddLoan from "./AddLoan"

function LoanManage() {
    // Redux - the list of loans comes from the store
    const loans = useSelector(state=>state.loans)
    const [errMsg, setErrMsg] = useState("")

    const dispatch = useDispatch()

    useEffect(() => {
        const fetchLoans = async () => {
            try {
                await dispatch(getLoans())
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        fetchLoans()
    }, [dispatch])

    return (
        <div>
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-cash-coin me-2"></i>Loans</h5>
                    <Message errMsg={errMsg} />
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Loan</th>
                                    <th className="text-end">Minimum amount</th>
                                    <th className="text-end">Maximum amount</th>
                                    <th className="text-end">Rate of interest</th>
                                    <th className="text-end">Tenure</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    loans.map((l) => (
                                        <tr key={l.id}>
                                            <td>{l.loanType}</td>
                                            <td className="text-end">{formatMoney(l.minAmount)}</td>
                                            <td className="text-end">{formatMoney(l.maxAmount)}</td>
                                            <td className="text-end">{l.interestRate} %</td>
                                            <td className="text-end">{l.tenureMonths} months</td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {loans.length === 0 && <p className="text-muted mb-0">No loans yet. Add the first loan below.</p>}
                </div>
            </div>

            <AddLoan />
        </div>
    )
}

export default LoanManage
