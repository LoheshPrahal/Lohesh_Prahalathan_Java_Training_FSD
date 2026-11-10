import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { getAdmins } from "../../store/actions/AdminAction"
import { getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

function AdminList() {
    // Redux - the list of admins comes from the store
    const admins = useSelector(state=>state.admins)
    const [errMsg, setErrMsg] = useState("")

    const dispatch = useDispatch()

    useEffect(() => {
        const fetchAdmins = async () => {
            try {
                await dispatch(getAdmins())
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        fetchAdmins()
    }, [dispatch])

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-shield-lock me-2"></i>Admins</h5>
                <Message errMsg={errMsg} />
                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                admins.map((a) => (
                                    <tr key={a.adminId}>
                                        <td>{a.name}</td>
                                        <td>{a.username}</td>
                                        <td>{a.email}</td>
                                        <td><span className={"badge " + statusBadge(a.status)}>{a.status}</span></td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )
}

export default AdminList
