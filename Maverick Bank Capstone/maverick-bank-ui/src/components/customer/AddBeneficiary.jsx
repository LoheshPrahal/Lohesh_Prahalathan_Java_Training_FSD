import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const branchApi = API_SERVER_URL_DEV + '/api/branch/all'
const apiUrl = API_SERVER_URL_DEV + '/api/beneficiary/add'

function AddBeneficiary({ onSuccess }) {
    const [accountName, setAccountName] = useState("")
    const [accountNumber, setAccountNumber] = useState("")
    const [branchId, setBranchId] = useState("")
    const [branches, setBranches] = useState([])
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getBranches = async () => {
            try {
                const response = await axios.get(branchApi)
                setBranches(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getBranches()
    }, [])

    // On selecting the branch, its IFSC code is shown
    const selectedBranch = branches.find((b) => String(b.id) === branchId)

    const add = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'accountName': accountName,
            'accountNumber': accountNumber,
            'branchId': branchId
        }
        try {
            await axios.post(apiUrl, obj, authHeader())
            setSuccessMsg("Beneficiary is added.")
            setAccountName("")
            setAccountNumber("")
            setBranchId("")
            onSuccess()
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-person-plus me-2"></i>Add a beneficiary</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />
                <form onSubmit={(e) => add(e)}>
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Account name</label>
                            <input type="text" className="form-control" required value={accountName}
                                onChange={(e) => setAccountName(e.target.value)} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Account number</label>
                            <input type="text" className="form-control" required value={accountNumber} pattern="[0-9]{12}" maxLength={12} title="12 digit account number"
                                onChange={(e) => setAccountNumber(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Bank</label>
                            <input type="text" className="form-control" value="Maverick Bank" disabled readOnly />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Branch</label>
                            <select className="form-select" required value={branchId} onChange={(e) => setBranchId(e.target.value)}>
                                <option value="">Select</option>
                                {
                                    branches.map((b) => (
                                        <option key={b.id} value={b.id}>{b.name} - {b.city}</option>
                                    ))
                                }
                            </select>
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">IFSC code</label>
                            <input type="text" className="form-control" value={selectedBranch ? selectedBranch.ifscCode : ""} disabled readOnly />
                        </div>
                    </div>
                    <button type="submit" className="btn btn-primary">Add beneficiary</button>
                </form>
            </div>
        </div>
    )
}

export default AddBeneficiary
