import { useState } from "react"
import { formatDateTime, formatMoney, statusBadge } from "../../utility/helper"

// Redux removed - the list of transactions comes from the parent component
function TransactionList({ list }) {
    const moneyIn = ['DEPOSIT', 'TRANSFER_IN', 'LOAN_DISBURSEMENT']

    const [page, setPage] = useState(0)
    const [previousList, setPreviousList] = useState(list)
    const size = 5

    // A new list (another filter or account) starts again from the first page
    if (list !== previousList) {
        setPreviousList(list)
        setPage(0)
    }

    const totalPages = Math.ceil(list.length / size)
    const shown = list.slice(page * size, page * size + size)

    return (
        <div className="card">
            <div className="card-header">
                Transactions
            </div>
            <div className="card-body">
                <div className="table-responsive">
                    <table className="table table-hover align-middle">
                        <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Date</th>
                                <th scope="col">Account</th>
                                <th scope="col">Type</th>
                                <th scope="col">Description</th>
                                <th scope="col" className="text-end">Amount</th>
                                <th scope="col">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                shown.map((t, index)=>(
                                    <tr key={t.transactionId}>
                                        <th scope="row">{page * size + index + 1}</th>
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

                {/* <!-- Pagination --> */}
                {
                    list.length > 0 &&
                    <nav aria-label="Page navigation">
                        <ul className="pagination justify-content-center">
                            <li className={"page-item " + (page === 0 ? "disabled" : "")}>
                                <button className="page-link" onClick={() => setPage(page - 1)} disabled={page === 0}>Previous</button>
                            </li>
                            {
                                Array.from({ length: totalPages }, (e, index) => (
                                    <li key={index} className={"page-item " + (page === index ? "active" : "")}>
                                        <button className="page-link" onClick={() => setPage(index)}>{index + 1}</button>
                                    </li>
                                ))
                            }
                            <li className={"page-item " + (page >= totalPages - 1 ? "disabled" : "")}>
                                <button className="page-link" onClick={() => setPage(page + 1)} disabled={page >= totalPages - 1}>Next</button>
                            </li>
                        </ul>
                        <p className="text-center mb-0">{`Page ${page + 1} of ${totalPages} total pages`}</p>
                        <p className="text-center text-muted mb-0">{`Showing ${shown.length} records from total ${list.length} records`}</p>
                    </nav>
                }
            </div>
        </div>
    )
}

export default TransactionList
