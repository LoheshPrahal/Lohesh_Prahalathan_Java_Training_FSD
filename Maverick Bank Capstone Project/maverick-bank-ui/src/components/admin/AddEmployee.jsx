import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const branchApi = API_SERVER_URL_DEV + '/api/branch/all'
const apiUrl = API_SERVER_URL_DEV + '/api/employee/add'

function AddEmployee({ onSuccess }) {
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [branchId, setBranchId] = useState("")
    const [branches, setBranches] = useState([])
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    useEffect(() => {
        const getBranches = async () => {
            try {
                const response = await axios.get(branchApi)
                setBranches(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getBranches()
    }, [])

    const add = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'name': name,
            'email': email,
            'username': username,
            'password': password,
            'branchId': branchId
        }
        try {
            await axios.post(apiUrl, obj, authHeader())
            setSuccessMsg("Bank employee is added.")
            setName("")
            setEmail("")
            setUsername("")
            setPassword("")
            setBranchId("")
            onSuccess()
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-person-plus me-2"></i>Add a bank employee</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />
                <form onSubmit={(e) => add(e)}>
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Name</label>
                            <input type="text" className="form-control" required value={name} onChange={(e) => setName(e.target.value)} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Email</label>
                            <input type="email" className="form-control" required value={email} onChange={(e) => setEmail(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Username</label>
                            <input type="text" className="form-control" required minLength={3} maxLength={15} value={username} onChange={(e) => setUsername(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Password</label>
                            <input type="password" className="form-control" required minLength={8} value={password} onChange={(e) => setPassword(e.target.value)} />
                        </div>
                        <div className="col-md-4 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Branch</label>
                            <select className="form-select" required value={branchId} onChange={(e) => setBranchId(e.target.value)}>
                                <option value="">Select</option>
                                {
                                    branches.map((b) => (
                                        <option key={b.id} value={b.id}>{b.name} - {b.city}</option>
                                    ))
                                }
                            </select>
                        </div>
                    </div>
                    <button type="submit" className="btn btn-primary">Add bank employee</button>
                </form>
            </div>
        </div>
    )
}

export default AddEmployee
