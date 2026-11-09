import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../utility/helper"
import Message from "../Message"
import AddBeneficiary from "./AddBeneficiary"


function Beneficiaries() {
    const [beneficiaries, setBeneficiaries] = useState([])
    const [refresh, setRefresh] = useState(0)
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getBeneficiaries = async () => {
            try {
                const response = await axios.get(API_SERVER_URL_DEV + '/api/beneficiary/my', authHeader())
                setBeneficiaries(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getBeneficiaries()
    }, [refresh])

    return (
        <div>
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-people me-2"></i>My beneficiaries</h5>
                    <Message errMsg={errMsg} />
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Account name</th>
                                    <th>Account number</th>
                                    <th>Bank</th>
                                    <th>Branch</th>
                                    <th>IFSC code</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    beneficiaries.map((b) => (
                                        <tr key={b.beneficiaryId}>
                                            <td>{b.accountName}</td>
                                            <td>{b.accountNumber}</td>
                                            <td>Maverick Bank</td>
                                            <td>{b.branchName}</td>
                                            <td>{b.ifscCode}</td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {beneficiaries.length === 0 && <p className="text-muted mb-0">No beneficiaries yet. Add one to transfer money.</p>}
                </div>
            </div>

            <AddBeneficiary onSuccess={() => setRefresh(refresh + 1)} />
        </div>
    )
}

export default Beneficiaries
