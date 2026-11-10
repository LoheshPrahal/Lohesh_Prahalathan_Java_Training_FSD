import { useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/transaction/statement'

// Statement - the customer downloads the transactions of the account as a .csv file
function AccountStatement({ accountNumber }) {
    const [fromDate, setFromDate] = useState("")
    const [toDate, setToDate] = useState("")
    const [errMsg, setErrMsg] = useState("")

    const download = async (e) => {
        e.preventDefault()
        setErrMsg("")
        try {
            const response = await axios.get(apiUrl + '/' + accountNumber, {
                ...authHeader(),
                params: { fromDate: fromDate || undefined, toDate: toDate || undefined }
            })
            // The csv text of the server is saved as a file
            const url = window.URL.createObjectURL(new Blob([response.data], { type: 'text/csv' }))
            const link = document.createElement('a')
            link.href = url
            link.download = 'statement-' + accountNumber + '.csv'
            link.click()
            window.URL.revokeObjectURL(url)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4 mb-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-file-earmark-spreadsheet me-2"></i>Account statement</h5>
                <Message errMsg={errMsg} />
                <form onSubmit={(e) => download(e)}>
                    <div className="row align-items-end">
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">From date</label>
                            <input type="date" className="form-control" value={fromDate} max={toDate || undefined}
                                onChange={(e) => setFromDate(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">To date</label>
                            <input type="date" className="form-control" value={toDate} min={fromDate || undefined}
                                onChange={(e) => setToDate(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <button type="submit" className="btn btn-primary w-100">Download statement (.csv)</button>
                        </div>
                    </div>
                    <p className="text-muted small mb-0">Leave the dates empty to get all the transactions of the account.</p>
                </form>
            </div>
        </div>
    )
}

export default AccountStatement
