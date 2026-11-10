import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/admin/all'

function AdminList() {
    const [admins, setAdmins] = useState([])
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getAdmins = async () => {
            try {
                const response = await axios.get(apiUrl, authHeader())
                setAdmins(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getAdmins()
    }, [])

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
