import { formatMoney } from "../../utility/helper"

// Redux removed - the list of transactions comes from the parent component
function Stat({ list }){
    const moneyIn = ['DEPOSIT', 'TRANSFER_IN', 'LOAN_DISBURSEMENT']
    const totalIn = list.filter(t => moneyIn.includes(t.transactionType)).reduce((sum, t) => sum + Number(t.amount), 0)
    const totalOut = list.filter(t => !moneyIn.includes(t.transactionType)).reduce((sum, t) => sum + Number(t.amount), 0)

    return(
        <div className="row mb-4">
            <div className="col-md-4 mb-3 mb-md-0">
                <div className="card shadow-sm border-0 rounded-4">
                    <div className="card-body">
                        <div className="text-muted small">Transactions shown</div>
                        <div className="h4 fw-bold mb-0">{list.length}</div>
                    </div>
                </div>
            </div>
            <div className="col-md-4 mb-3 mb-md-0">
                <div className="card shadow-sm border-0 rounded-4">
                    <div className="card-body">
                        <div className="text-muted small">Money in (inbound)</div>
                        <div className="h4 fw-bold text-success mb-0">{formatMoney(totalIn)}</div>
                    </div>
                </div>
            </div>
            <div className="col-md-4">
                <div className="card shadow-sm border-0 rounded-4">
                    <div className="card-body">
                        <div className="text-muted small">Money out (outbound)</div>
                        <div className="h4 fw-bold text-danger mb-0">{formatMoney(totalOut)}</div>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default Stat
