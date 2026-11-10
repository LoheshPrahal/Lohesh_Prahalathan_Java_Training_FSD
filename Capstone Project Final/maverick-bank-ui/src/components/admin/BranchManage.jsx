import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { getBranches } from "../../store/actions/AdminAction"
import { getErrorMessage } from "../../utility/helper"
import Message from "../Message"
import AddBranch from "./AddBranch"

function BranchManage() {
    // Redux - the list of branches comes from the store
    const branches = useSelector(state=>state.branches)
    const [errMsg, setErrMsg] = useState("")

    const dispatch = useDispatch()

    useEffect(() => {
        const fetchBranches = async () => {
            try {
                await dispatch(getBranches())
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        fetchBranches()
    }, [dispatch])

    return (
        <div>
            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-building me-2"></i>Branches</h5>
                    <Message errMsg={errMsg} />
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Branch</th>
                                    <th>Address</th>
                                    <th>City</th>
                                    <th>IFSC code</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    branches.map((b) => (
                                        <tr key={b.id}>
                                            <td>{b.name}</td>
                                            <td>{b.address}</td>
                                            <td>{b.city}</td>
                                            <td>{b.ifscCode}</td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {branches.length === 0 && <p className="text-muted mb-0">No branches yet. Add the first branch below.</p>}
                </div>
            </div>

            <AddBranch />
        </div>
    )
}

export default BranchManage
