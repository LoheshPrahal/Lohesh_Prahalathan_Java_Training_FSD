import { useEffect, useState } from "react"
import axios from "axios"
import { useDispatch } from "react-redux"
import { API_SERVER_URL_DEV } from "../../../config"
import { getTransactions } from "../../store/actions/TransactionAction"
import { authHeader, formatMoney, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"
import Stat from "../transaction/Stat"
import AddTransaction from "../transaction/AddTransaction"
import TransactionFilter from "../transaction/TransactionFilter"
import TransactionList from "../transaction/TransactionList"

const accountApi = API_SERVER_URL_DEV + '/api/account/my'
const beneficiaryApi = API_SERVER_URL_DEV + '/api/beneficiary/my'

function CustomerTransactions() {
    const [accounts, setAccounts] = useState([])
    const [beneficiaries, setBeneficiaries] = useState([])
    const [accountId, setAccountId] = useState("")
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")

    const dispatch = useDispatch()

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

    // The last 10 transactions of the selected account
    useEffect(() => {
        if (accountNumber) {
            dispatch(getTransactions(accountNumber, '', '', '', 10)).catch((err) => setErrMsg(getErrorMessage(err)))
        }
    }, [accountNumber, dispatch])

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
                    <Stat />
                    <AddTransaction account={account} beneficiaries={beneficiaries} onDone={() => setRefresh(refresh + 1)} />
                    <TransactionFilter mode="my" accountNumber={account.accountNumber} />
                    <TransactionList />
                </div>
            }
        </div>
    )
}

export default CustomerTransactions
