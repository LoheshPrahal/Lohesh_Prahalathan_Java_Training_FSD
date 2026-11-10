import { useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/loan/add'

function AddLoan({ onSuccess }) {
    const [loanType, setLoanType] = useState("")
    const [minAmount, setMinAmount] = useState("")
    const [maxAmount, setMaxAmount] = useState("")
    const [interestRate, setInterestRate] = useState("")
    const [tenureMonths, setTenureMonths] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

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
            await axios.post(apiUrl, obj, authHeader())
            setSuccessMsg("Loan is added.")
            setLoanType("")
            setMinAmount("")
            setMaxAmount("")
            setInterestRate("")
            setTenureMonths("")
            onSuccess()
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
