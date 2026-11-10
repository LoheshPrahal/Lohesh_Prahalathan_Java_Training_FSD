import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, formatMoney, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"
import Stat from "../transaction/Stat"
import AddTransaction from "../transaction/AddTransaction"
import AccountStatement from "./AccountStatement"
import TransactionFilter from "../transaction/TransactionFilter"
import TransactionList from "../transaction/TransactionList"

const accountApi = API_SERVER_URL_DEV + '/api/account/my'
const beneficiaryApi = API_SERVER_URL_DEV + '/api/beneficiary/my'
const transactionApi = API_SERVER_URL_DEV + '/api/transaction/my'

function CustomerTransactions() {
    const [accounts, setAccounts] = useState([])
    const [beneficiaries, setBeneficiaries] = useState([])
    // Redux removed - the transactions are kept in the state of this component
    const [transactions, setTransactions] = useState([])
    const [accountId, setAccountId] = useState("")
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")

    // Accounts and beneficiaries of the customer
    useEffect(() => {
        const getAccounts = async () => {
            try {
                const response = await axios.get(accountApi, authHeader())
                setAccounts(response.data)
                // the first account is selected, if none is selected
                setAccountId((current) => current === "" && response.data.length > 0 ? String(response.data[0].accountId) : current)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        const getBeneficiaries = async () => {
            try {
                const response = await axios.get(beneficiaryApi, authHeader())
                setBeneficiaries(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getAccounts()
        getBeneficiaries()
    }, [refresh])

    const account = accounts.find((a) => String(a.accountId) === accountId)
    const accountNumber = account ? account.accountNumber : ""

    // The last 10 transactions of the selected account (fetched again after a new transaction)
    useEffect(() => {
        const getTransactions = async () => {
            try {
                const response = await axios.get(transactionApi + '/' + accountNumber, { ...authHeader(), params: { size: 10 } })
                setTransactions(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        if (accountNumber) {
            getTransactions()
        }
    }, [accountNumber, refresh])

    return (
        <div>
            <Message errMsg={errMsg} />

            {/* <!-- Account selection --> */}
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <div className="row align-items-center">
                        <div className="col-md-6 mb-3 mb-md-0">
                            <label className="form-label small fw-semibold text-secondary">Account</label>
                            <select className="form-select" value={accountId} onChange={(e) => setAccountId(e.target.value)}>
                                {
                                    accounts.map((a) => (
                                        <option key={a.accountId} value={a.accountId}>{a.accountNumber} - {a.accountType}</option>
                                    ))
                                }
                            </select>
                        </div>
                        {
                            account &&
                            <div className="col-md-6">
                                <div className="text-muted small">Balance</div>
                                <div className="h3 fw-bold mb-0">
                                    {formatMoney(account.balance)} <span className={"badge fs-6 " + statusBadge(account.accountStatus)}>{account.accountStatus}</span>
                                </div>
                            </div>
                        }
                    </div>
                </div>
            </div>

            {
                account &&
                <div>
                    <Stat list={transactions} />
                    <AddTransaction account={account} beneficiaries={beneficiaries} onDone={() => setRefresh(refresh + 1)} />
                    {/* <!-- Statement of the account as a csv file --> */}
                    <AccountStatement accountNumber={account.accountNumber} />
                    <TransactionFilter mode="my" accountNumber={account.accountNumber} onResult={setTransactions} />
                    <TransactionList list={transactions} />
                </div>
            }
        </div>
    )
}

export default CustomerTransactions
