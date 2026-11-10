import { useEffect, useState } from "react"
import axios from "axios"
import { Link } from "react-router"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../../utility/helper"
import Message from "../Message"
import DonutChart from "../DonutChart"

function Home() {
    const [accounts, setAccounts] = useState(0)
    const [closures, setClosures] = useState(0)
    const [toReview, setToReview] = useState(0)
    const [toDisburse, setToDisburse] = useState(0)
    const [labels, setLabels] = useState([])
    const [values, setValues] = useState([])
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        // The work which is waiting for the bank employee
        const getWork = async () => {
            try {
                const accountResponse = await axios.get(API_SERVER_URL_DEV + '/api/account/pending', authHeader())
                setAccounts(accountResponse.data.length)
                const closureResponse = await axios.get(API_SERVER_URL_DEV + '/api/closure/pending', authHeader())
                setClosures(closureResponse.data.length)
                const reviewResponse = await axios.get(API_SERVER_URL_DEV + '/api/loan/application/pending', authHeader())
                setToReview(reviewResponse.data.length)
                const disburseResponse = await axios.get(API_SERVER_URL_DEV + '/api/loan/application/approved', authHeader())
                setToDisburse(disburseResponse.data.length)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }

        const getDonutData = async () => {
            try {
                const response = await axios.get(API_SERVER_URL_DEV + '/api/report/financial', authHeader())
                setLabels(response.data.loanApplicationsByStatus.map((s) => s.status))
                setValues(response.data.loanApplicationsByStatus.map((s) => s.numberOfApplications))
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getWork()
        getDonutData()
    }, [])

    return (
        <div className="container-fluid">
            <Message errMsg={errMsg} />
            <div className="row mb-4">
                <div className="col-md-3 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Accounts waiting for approval</div>
                            <div className="h3 fw-bold">{accounts}</div>
                            <Link to="/employee/accounts">Open</Link>
                        </div>
                    </div>
                </div>
                <div className="col-md-3 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Close requests</div>
                            <div className="h3 fw-bold">{closures}</div>
                            <Link to="/employee/close-requests">Open</Link>
                        </div>
                    </div>
                </div>
                <div className="col-md-3 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Loan applications to review</div>
                            <div className="h3 fw-bold">{toReview}</div>
                            <Link to="/employee/loans">Open</Link>
                        </div>
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Approved loans to disburse</div>
                            <div className="h3 fw-bold">{toDisburse}</div>
                            <Link to="/employee/loans">Open</Link>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">
                            Loan applications for each status
                        </div>
                        <div className="card-body">
                            {
                                values.length > 0 ?
                                    <DonutChart title="Loan applications" labels={labels} values={values} />
                                    :
                                    <p className="text-muted mb-0">No loan applications yet.</p>
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Home
