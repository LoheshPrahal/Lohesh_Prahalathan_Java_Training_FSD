import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatDateTime, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/closure/pending'

function ClosureRequests() {
    const [requests, setRequests] = useState([])
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getRequests = async () => {
            try {
                const response = await axios.get(apiUrl, authHeader())
                setRequests(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getRequests()
    }, [refresh])

    // action is approve (the account is closed) or reject
    const process = async (id, action) => {
        setErrMsg("")
        setSuccessMsg("")
        try {
            await axios.put(API_SERVER_URL_DEV + '/api/closure/' + action + '/' + id, {}, authHeader())
            setSuccessMsg(action === 'approve' ? "Account is closed." : "Request is rejected.")
            setRefresh(refresh + 1)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-x-circle me-2"></i>Account close requests</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />
                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Account number</th>
                                <th>Customer</th>
                                <th>Reason</th>
                                <th>Requested on</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                requests.map((r) => (
                                    <tr key={r.requestId}>
                                        <td>{r.accountNumber}</td>
                                        <td>{r.customerName}</td>
                                        <td>{r.reason}</td>
                                        <td>{formatDateTime(r.requestDate)}</td>
                                        <td className="text-end">
                                            <button className="btn btn-sm btn-danger me-2" onClick={() => process(r.requestId, 'approve')}>Close account</button>
                                            <button className="btn btn-sm btn-outline-secondary" onClick={() => process(r.requestId, 'reject')}>Reject</button>
                                        </td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
                {requests.length === 0 && <p className="text-muted mb-0">No close requests.</p>}
            </div>
        </div>
    )
}

export default ClosureRequests
