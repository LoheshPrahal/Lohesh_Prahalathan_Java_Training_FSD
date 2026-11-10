import { useState } from "react"
import LoanList from "./LoanList"
import ApplyLoan from "./ApplyLoan"
import MyLoans from "./MyLoans"

function Loans() {
    const [selectedLoan, setSelectedLoan] = useState(null)
    const [refresh, setRefresh] = useState(0)

    return (
        <div>
            <LoanList onApply={(loan) => setSelectedLoan(loan)} />
            {
                selectedLoan &&
                <ApplyLoan loan={selectedLoan} onClose={() => setSelectedLoan(null)} onSuccess={() => setRefresh(refresh + 1)} />
            }
            <MyLoans refresh={refresh} />
        </div>
    )
}

export default Loans
