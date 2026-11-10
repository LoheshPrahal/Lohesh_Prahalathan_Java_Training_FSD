import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatDateTime, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/account/pending'

function PendingAccounts() {
    const [accounts, setAccounts] = useState([])
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getAccounts = async () => {
            try {
                const response = await axios.get(apiUrl, authHeader())
                setAccounts(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getAccounts()
    }, [refresh])

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
                {accounts.length === 0 && <p className="text-muted mb-0">No account is waiting for approval.</p>}
            </div>
        </div>
    )
}

export default PendingAccounts
