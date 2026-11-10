import { useState } from "react"
import { useDispatch } from "react-redux"
import { addLoan } from "../../store/actions/AdminAction"
import { getErrorMessage } from "../../utility/helper"
import Message from "../Message"

function AddLoan() {
    const [loanType, setLoanType] = useState("")
    const [minAmount, setMinAmount] = useState("")
    const [maxAmount, setMaxAmount] = useState("")
    const [interestRate, setInterestRate] = useState("")
    const [tenureMonths, setTenureMonths] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    const dispatch = useDispatch()

    const add = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'loanType': loanType,
            'minAmount': minAmount,
            'maxAmount': maxAmount,
            'interestRate': interestRate,
            'tenureMonths': tenureMonths
        }
        try {
            // The store fetches the list of loans again
            await dispatch(addLoan(obj))
            setSuccessMsg("Loan is added.")
            setLoanType("")
            setMinAmount("")
            setMaxAmount("")
            setInterestRate("")
            setTenureMonths("")
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-plus-circle me-2"></i>Add a loan</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />
                <form onSubmit={(e) => add(e)}>
                    <div className="row">
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Loan name</label>
                            <input type="text" className="form-control" required value={loanType} onChange={(e) => setLoanType(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Minimum amount (₹)</label>
                            <input type="number" className="form-control" required min="1" step="0.01" value={minAmount} onChange={(e) => setMinAmount(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Maximum amount (₹)</label>
                            <input type="number" className="form-control" required min={minAmount || 1} step="0.01" value={maxAmount} onChange={(e) => setMaxAmount(e.target.value)} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Rate of interest (%)</label>
                            <input type="number" className="form-control" required min="0.01" step="0.01" value={interestRate} onChange={(e) => setInterestRate(e.target.value)} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Tenure (months)</label>
                            <input type="number" className="form-control" required min="1" step="1" value={tenureMonths} onChange={(e) => setTenureMonths(e.target.value)} />
                        </div>
                    </div>
                    <button type="submit" className="btn btn-primary">Add loan</button>
                </form>
            </div>
        </div>
    )
}

export default AddLoan
