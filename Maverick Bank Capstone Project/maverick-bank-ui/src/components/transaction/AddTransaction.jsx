import { useState } from "react"
import { useDispatch } from "react-redux"
import { deposit, transfer, withdraw } from "../../store/actions/TransactionAction"
import { formatMoney, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

function AddTransaction({ account, beneficiaries, onDone }) {
    const [type, setType] = useState("DEPOSIT")
    const [amount, setAmount] = useState("")
    const [description, setDescription] = useState("")
    const [beneficiaryId, setBeneficiaryId] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    const dispatch = useDispatch()

    const add = async (e)=>{
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'accountId': account.accountId,
            'amount': amount,
            'description': description
        }
        try {
            switch(type){
                case 'DEPOSIT' :
                    await dispatch(deposit(obj, account.accountNumber))
                    break;
                case 'WITHDRAW' :
                    await dispatch(withdraw(obj, account.accountNumber))
                    break;
                case 'TRANSFER' :
                    obj.beneficiaryId = beneficiaryId
                    await dispatch(transfer(obj, account.accountNumber))
                    break;
            }
            setSuccessMsg(formatMoney(amount) + " - " + type.toLowerCase() + " is successful.")
            setAmount("")
            setDescription("")
            setBeneficiaryId("")
            // The balance of the account has changed
            onDone()
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }
    return (
        <div className="card shadow-sm border-0 rounded-4 mb-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-cash-stack me-2"></i>New transaction</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />
                {
                    account.accountStatus !== 'ACTIVE' &&
                    <div className="alert alert-warning py-2">This account is {account.accountStatus}. Transactions are possible only on an ACTIVE account.</div>
                }
                <form onSubmit={(e)=> add(e)}>
                    <fieldset disabled={account.accountStatus !== 'ACTIVE'}>
                        <div className="row">
                            <div className="col-md-3 mb-3">
                                <label className="form-label small fw-semibold text-secondary">Transaction</label>
                                <select className="form-select" value={type} onChange={(e)=> setType(e.target.value)}>
                                    <option value="DEPOSIT">Deposit</option>
                                    <option value="WITHDRAW">Withdraw</option>
                                    <option value="TRANSFER">Transfer</option>
                                </select>
                            </div>
                            <div className="col-md-3 mb-3">
                                <label className="form-label small fw-semibold text-secondary">Amount (₹)</label>
                                <input type="number" className="form-control" required min="0.01" step="0.01" value={amount}
                                    onChange={(e)=> setAmount(e.target.value)}/>
                            </div>
                            {
                                type === 'TRANSFER' &&
                                <div className="col-md-6 mb-3">
                                    <label className="form-label small fw-semibold text-secondary">To (beneficiary)</label>
                                    <select className="form-select" required value={beneficiaryId} onChange={(e)=> setBeneficiaryId(e.target.value)}>
                                        <option value="">Select</option>
                                        {
                                            beneficiaries.map((b)=>(
                                                <option key={b.beneficiaryId} value={b.beneficiaryId}>{b.accountName} - {b.accountNumber}</option>
                                            ))
                                        }
                                    </select>
                                    {beneficiaries.length === 0 && <small className="text-danger">Add a beneficiary first (Beneficiaries tab).</small>}
                                </div>
                            }
                            <div className={type === 'TRANSFER' ? "col-12 mb-3" : "col-md-6 mb-3"}>
                                <label className="form-label small fw-semibold text-secondary">Description (optional)</label>
                                <input type="text" className="form-control" value={description}
                                    onChange={(e)=> setDescription(e.target.value)}/>
                            </div>
                        </div>
                        <button type="submit" className="btn btn-primary">Submit</button>
                    </fieldset>
                </form>
            </div>
        </div>
    )
}

export default AddTransaction
