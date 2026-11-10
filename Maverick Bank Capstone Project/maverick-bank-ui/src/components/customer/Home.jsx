import { useEffect, useState } from "react"
import axios from "axios"
import { Link } from "react-router"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatMoney, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"
import DonutChart from "../DonutChart"

const apiUrl = API_SERVER_URL_DEV + '/api/account/my'

function Home() {
    const [accounts, setAccounts] = useState([])
    const [errMsg, setErrMsg] = useState("")

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
    }, [])

    // Only the active accounts have a balance which can be used
    const activeAccounts = accounts.filter((a) => a.accountStatus === 'ACTIVE')
    const totalBalance = activeAccounts.reduce((sum, a) => sum + Number(a.balance), 0)
    const waiting = accounts.filter((a) => a.accountStatus === 'PENDING').length

    return (
        <div className="container-fluid">
            <Message errMsg={errMsg} />
            <div className="row mb-4">
                <div className="col-md-4 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">My accounts</div>
                            <div className="h3 fw-bold mb-0">{accounts.length}</div>
                        </div>
                    </div>
                </div>
                <div className="col-md-4 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Total balance</div>
                            <div className="h3 fw-bold mb-0">{formatMoney(totalBalance)}</div>
                        </div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Accounts waiting for approval</div>
                            <div className="h3 fw-bold mb-0">{waiting}</div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row">
                <div className="col-md-6 mb-4">
                    <div className="card">
                        <div className="card-header">
                            Balance in each account
                        </div>
                        <div className="card-body">
                            {
                                totalBalance > 0 ?
                                    <DonutChart title="Balance" labels={activeAccounts.map((a) => a.accountNumber)} values={activeAccounts.map((a) => Number(a.balance))} />
                                    :
                                    <p className="text-muted mb-0">There is no balance yet. Deposit some money in the Transactions page.</p>
                            }
                        </div>
                    </div>
                </div>
                <div className="col-md-6 mb-4">
                    <div className="card">
                        <div className="card-header">
                            My accounts
                        </div>
                        <div className="card-body">
                            <table className="table">
                                <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Account number</th>
                                        <th scope="col">Type</th>
                                        <th scope="col">Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        accounts.map((a, index) => (
                                            <tr key={a.accountId}>
                                                <th scope="row">{index + 1}</th>
                                                <td>{a.accountNumber}</td>
                                                <td>{a.accountType}</td>
                                                <td><span className={"badge " + statusBadge(a.accountStatus)}>{a.accountStatus}</span></td>
                                            </tr>
                                        ))
                                    }
                                </tbody>
                            </table>
                            <Link to="/customer/accounts" className="btn btn-primary">Go to my accounts</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Home
