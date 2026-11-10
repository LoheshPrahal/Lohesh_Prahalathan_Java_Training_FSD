import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { ACCOUNT_STATUSES, ACCOUNT_TYPES, authHeader, formatDateTime, formatMoney, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"
import OpenAccount from "./OpenAccount"

const apiUrl = API_SERVER_URL_DEV + '/api/account/my'

function MyAccounts() {
    const [accounts, setAccounts] = useState([])
    const [selected, setSelected] = useState(null)
    const [closeId, setCloseId] = useState(null)
    const [reason, setReason] = useState("")
    // Filter - account type and account status ("" means all)
    const [filterType, setFilterType] = useState("")
    const [filterStatus, setFilterStatus] = useState("")
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    // The accounts of the customer (filtered with the account type and the account status)
    useEffect(() => {
        const getAccounts = async () => {
            try {
                const response = await axios.get(apiUrl, {
                    ...authHeader(),
                    params: { accountType: filterType || undefined, accountStatus: filterStatus || undefined }
                })
                setAccounts(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getAccounts()
    }, [refresh, filterType, filterStatus])

    // Account information of the selected account
    const viewAccount = async (id) => {
        setErrMsg("")
        setSuccessMsg("")
        try {
            const response = await axios.get(apiUrl + '/' + id, authHeader())
            setSelected(response.data)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    // The account is closed by the bank employee, the customer only sends the request
    const sendCloseRequest = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        try {
            await axios.post(API_SERVER_URL_DEV + '/api/closure/add/' + closeId, { 'reason': reason }, authHeader())
            setSuccessMsg("Close request is sent. A bank employee will close the account.")
            setCloseId(null)
            setReason("")
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div>
            <Message errMsg={errMsg} successMsg={successMsg} />

            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-wallet2 me-2"></i>My accounts</h5>

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
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Status</label>
                            <select className="form-select" value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
                                <option value="">All</option>
                                {
                                    ACCOUNT_STATUSES.map((s) => (
                                        <option key={s} value={s}>{s}</option>
                                    ))
                                }
                            </select>
                        </div>
                        <div className="col-md-4 mb-3">
                            <button type="button" className="btn btn-outline-secondary" onClick={() => { setFilterType(""); setFilterStatus("") }}>Clear filter</button>
                        </div>
                    </div>

                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Account number</th>
                                    <th>Type</th>
                                    <th>Branch</th>
                                    <th>IFSC code</th>
                                    <th className="text-end">Balance</th>
                                    <th>Status</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    accounts.map((a) => (
                                        <tr key={a.accountId}>
                                            <td>{a.accountNumber}</td>
                                            <td>{a.accountType}</td>
                                            <td>{a.branchName}</td>
                                            <td>{a.ifscCode}</td>
                                            <td className="text-end">{formatMoney(a.balance)}</td>
                                            <td><span className={"badge " + statusBadge(a.accountStatus)}>{a.accountStatus}</span></td>
                                            <td className="text-end">
                                                <button className="btn btn-sm btn-outline-primary me-2" onClick={() => viewAccount(a.accountId)}>Details</button>
                                                {
                                                    a.accountStatus === 'ACTIVE' &&
                                                    <button className="btn btn-sm btn-outline-danger" onClick={() => setCloseId(a.accountId)}>Close</button>
                                                }
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {accounts.length === 0 && <p className="text-muted mb-0">{filterType || filterStatus ? "No accounts match the filter." : "No accounts yet."}</p>}
                </div>
            </div>

            {/* <!-- Details of the selected account --> */}
            {
                selected &&
                <div className="card shadow-sm border-0 rounded-4 mb-4">
                    <div className="card-body p-4">
                        <div className="d-flex justify-content-between">
                            <h5 className="fw-bold mb-3">Account information</h5>
                            <button className="btn-close" onClick={() => setSelected(null)}></button>
                        </div>
                        <dl className="row mb-0">
                            <dt className="col-sm-3">Name</dt>
                            <dd className="col-sm-9">{selected.customerName}</dd>
                            <dt className="col-sm-3">Account number</dt>
                            <dd className="col-sm-9">{selected.accountNumber} ({selected.accountType})</dd>
                            <dt className="col-sm-3">IFSC code</dt>
                            <dd className="col-sm-9">{selected.ifscCode}</dd>
                            <dt className="col-sm-3">Branch</dt>
                            <dd className="col-sm-9">{selected.branchName}</dd>
                            <dt className="col-sm-3">Branch address</dt>
                            <dd className="col-sm-9">{selected.branchAddress}</dd>
                            <dt className="col-sm-3">Balance</dt>
                            <dd className="col-sm-9">{formatMoney(selected.balance)}</dd>
                            <dt className="col-sm-3">Opened on</dt>
                            <dd className="col-sm-9">{formatDateTime(selected.openedDate)}</dd>
                        </dl>
                    </div>
                </div>
            }

            {/* <!-- Close request --> */}
            {
                closeId !== null &&
                <div className="card shadow-sm border-0 rounded-4 mb-4">
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">Close the account</h5>
                        <p className="text-muted small">The balance must be zero. Withdraw the remaining amount before sending the request.</p>
                        <form onSubmit={(e) => sendCloseRequest(e)}>
                            <div className="mb-3">
                                <label className="form-label small fw-semibold text-secondary">Reason</label>
                                <input type="text" className="form-control" required value={reason}
                                    onChange={(e) => setReason(e.target.value)} />
                            </div>
                            <button type="submit" className="btn btn-danger me-2">Send close request</button>
                            <button type="button" className="btn btn-secondary" onClick={() => setCloseId(null)}>Cancel</button>
                        </form>
                    </div>
                </div>
            }

            <OpenAccount onSuccess={() => setRefresh(refresh + 1)} />
        </div>
    )
}

export default MyAccounts
