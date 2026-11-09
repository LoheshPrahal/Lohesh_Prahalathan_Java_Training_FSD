import { useSelector } from "react-redux"
import { formatDateTime, formatMoney, statusBadge } from "../utility/helper"

function TransactionList() {
    const list = useSelector(state=>state.transactions)
    const moneyIn = ['DEPOSIT', 'TRANSFER_IN', 'LOAN_DISBURSEMENT']

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-list-ul me-2"></i>Transactions</h5>
                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Account</th>
                                <th>Type</th>
                                <th>Description</th>
                                <th className="text-end">Amount</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                list?.map((t)=>(
                                    <tr key={t.transactionId}>
                                        <td>{formatDateTime(t.transactionDate)}</td>
                                        <td>{t.accountNumber}</td>
                                        <td>
                                            <span className={"badge " + (moneyIn.includes(t.transactionType) ? "text-bg-success" : "text-bg-danger")}>
                                                {t.transactionType.replace('_', ' ')}
                                            </span>
                                        </td>
                                        <td>{t.description}</td>
                                        <td className={"text-end fw-semibold " + (moneyIn.includes(t.transactionType) ? "text-success" : "text-danger")}>
                                            {moneyIn.includes(t.transactionType) ? "+" : "-"}{formatMoney(t.amount)}
                                        </td>
                                        <td><span className={"badge " + statusBadge(t.transactionStatus)}>{t.transactionStatus}</span></td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
                {list.length === 0 && <p className="text-muted mb-0">No transactions found.</p>}
            </div>
        </div>
    )
}

export default TransactionList
