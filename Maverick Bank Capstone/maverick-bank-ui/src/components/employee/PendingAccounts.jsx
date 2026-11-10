import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { ACCOUNT_TYPES, authHeader, formatDateTime, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/account/pending'
const branchApi = API_SERVER_URL_DEV + '/api/branch/all'

function PendingAccounts() {
    const [accounts, setAccounts] = useState([])
    // Filter - account type and branch ("" means all)
    const [filterType, setFilterType] = useState("")
    const [filterBranchId, setFilterBranchId] = useState("")
    const [branches, setBranches] = useState([])
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    // The branches for the filter
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

    // The accounts waiting for approval (filtered with the account type and the branch)
    useEffect(() => {
        const getAccounts = async () => {
            try {
                const response = await axios.get(apiUrl, {
                    ...authHeader(),
                    params: { accountType: filterType || undefined, branchId: filterBranchId || undefined }
                })
                setAccounts(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getAccounts()
    }, [refresh, filterType, filterBranchId])

    // The employee validates the details of the customer and approves the account
    const approve = async (id) => {
        setErrMsg("")
        setSuccessMsg("")
        try {
            await axios.put(API_SERVER_URL_DEV + '/api/account/approve/' + id, {}, authHeader())
            setSuccessMsg("Account is approved.")
            setRefresh(refresh + 1)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4 mb-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-hourglass-split me-2"></i>Accounts waiting for approval</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />

                {/* <!-- Filter --> */}
                <div className="row align-items-end">
                    <div className="col-md-4 mb-3">
                        <label className="form-label small fw-semibold text-secondary">Account type</label>
                        <select className="form-select" value={filterType} onChange={(e) => setFilterType(e.target.value)}>
                            <option value="">All</option>
                            {
                                ACCOUNT_TYPES.map((t) => (
                                    <option key={t} value={t}>{t}</option>
                                ))
                            }
                        </select>
                    </div>
                    <div className="col-md-5 mb-3">
                        <label className="form-label small fw-semibold text-secondary">Branch</label>
                        <select className="form-select" value={filterBranchId} onChange={(e) => setFilterBranchId(e.target.value)}>
                            <option value="">All</option>
                            {
                                branches.map((b) => (
                                    <option key={b.id} value={b.id}>{b.name} - {b.city} ({b.ifscCode})</option>
                                ))
                            }
                        </select>
                    </div>
                    <div className="col-md-3 mb-3">
                        <button type="button" className="btn btn-outline-secondary" onClick={() => { setFilterType(""); setFilterBranchId("") }}>Clear filter</button>
                    </div>
                </div>

                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Account number</th>
                                <th>Customer</th>
                                <th>Type</th>
                                <th>Branch</th>
                                <th>Requested on</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                accounts.map((a) => (
                                    <tr key={a.accountId}>
                                        <td>{a.accountNumber}</td>
                                        <td>{a.customerName}</td>
                                        <td>{a.accountType}</td>
                                        <td>{a.branchName}</td>
                                        <td>{formatDateTime(a.openedDate)}</td>
                                        <td className="text-end">
                                            <button className="btn btn-sm btn-success" onClick={() => approve(a.accountId)}>Approve</button>
                                        </td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
                {accounts.length === 0 && <p className="text-muted mb-0">{filterType || filterBranchId ? "No accounts match the filter." : "No account is waiting for approval."}</p>}
            </div>
        </div>
    )
}

export default PendingAccounts
