import { useEffect, useState } from "react"
import { ACCOUNT_TYPES, authHeader, getErrorMessage } from "../utility/helper"
import { API_SERVER_URL_DEV } from "../../../config"
import axios from "axios"
import Message from "../Message"

function OpenAccount({onSuccess}) {
    const [accountType, setAccountType] = useState("")
    const [branchId, setBranchId] = useState("")
    const [branches, setBranches] = useState([])
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getBranches = async () => {
            try{
                const response = await axios.get(API_SERVER_URL_DEV + '/api/branch/all')
                setBranches(response.data)
            }catch(err){
                setErrMsg(getErrorMessage(err))
            }
        }
        getBranches()
    }, [])

    const openAccount = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'accountType': accountType,
            'branchId': branchId
        }
        try{
            await axios.post(API_SERVER_URL_DEV + '/api/account/add', obj, authHeader())
            setSuccessMsg("Account request is sent. The account can be used once a bank employee approves it.")
            setAccountType("")
            setBranchId("")
            onSuccess()
        }catch(err){
            setErrMsg(getErrorMessage(err))
        }
    }


    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-plus-circle me-2"></i>Open a new account</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />
                <form onSubmit={(e) => openAccount(e)}>
                    <div className="row">
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Account type</label>
                            <select className="form-select" required value={accountType} onChange={(e) => setAccountType(e.target.value)}>
                                <option value="">Select</option>
                                {
                                    ACCOUNT_TYPES.map((t) => (
                                        <option key={t} value={t}>{t}</option>
                                    ))
                                }
                            </select>
                        </div>
                        <div className="col-md-8 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Branch</label>
                            <select className="form-select" required value={branchId} onChange={(e) => setBranchId(e.target.value)}>
                                <option value="">Select</option>
                                {
                                    branches.map((b) => (
                                        <option key={b.id} value={b.id}>{b.name} - {b.city} ({b.ifscCode})</option>
                                    ))
                                }
                            </select>
                        </div>
                    </div>
                    <p className="text-muted small">Your name, address, date of birth, Aadhaar and PAN are already with the bank.</p>
                    <button type="submit" className="btn btn-primary">Open account</button>
                </form>
            </div>
        </div>
    )
}

export default OpenAccount