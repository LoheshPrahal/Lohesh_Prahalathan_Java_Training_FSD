import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { deactivateEmployee, getEmployees, updateEmployee } from "../../store/actions/AdminAction"
import { getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"
import AddEmployee from "./AddEmployee"

function EmployeeManage() {
    // Redux - the list of bank employees comes from the store
    const employees = useSelector(state=>state.employees)
    const [editId, setEditId] = useState(null)
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    const dispatch = useDispatch()

    useEffect(() => {
        const fetchEmployees = async () => {
            try {
                await dispatch(getEmployees())
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        fetchEmployees()
    }, [dispatch])

    const startEdit = (e) => {
        setErrMsg("")
        setSuccessMsg("")
        setEditId(e.employeeId)
        setName(e.name)
        setEmail(e.email)
    }

    const update = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'name': name,
            'email': email
        }
        try {
            // The store fetches the list of bank employees again
            await dispatch(updateEmployee(editId, obj))
            setSuccessMsg("Bank employee is updated.")
            setEditId(null)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    const deactivate = async (employee) => {
        if (!window.confirm("Deactivate " + employee.name + "? The bank employee will not be able to login.")) {
            return
        }
        setErrMsg("")
        setSuccessMsg("")
        try {
            await dispatch(deactivateEmployee(employee.employeeId))
            setSuccessMsg("Bank employee is deactivated.")
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div>
            <Message errMsg={errMsg} successMsg={successMsg} />

            <div className="card shadow-sm border-0 rounded-4 mb-4">
                <div className="card-body p-4">
                    <h5 className="fw-bold mb-3"><i className="bi bi-person-badge me-2"></i>Bank employees</h5>
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Branch</th>
                                    <th>Status</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    employees.map((e) => (
                                        <tr key={e.employeeId}>
                                            <td>{e.name}</td>
                                            <td>{e.username}</td>
                                            <td>{e.email}</td>
                                            <td>{e.branchName}</td>
                                            <td><span className={"badge " + statusBadge(e.status)}>{e.status}</span></td>
                                            <td className="text-end text-nowrap">
                                                <button className="btn btn-sm btn-outline-primary me-2" onClick={() => startEdit(e)}>Edit</button>
                                                <button className="btn btn-sm btn-outline-danger" disabled={e.status !== 'ACTIVE'} onClick={() => deactivate(e)}>Deactivate</button>
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {employees.length === 0 && <p className="text-muted mb-0">No bank employees yet.</p>}
                </div>
            </div>

            {
                editId !== null &&
                <div className="card shadow-sm border-0 rounded-4 mb-4">
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">Edit bank employee</h5>
                        <form onSubmit={(e) => update(e)}>
                            <div className="row">
                                <div className="col-md-6 mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Name</label>
                                    <input type="text" className="form-control" required value={name} onChange={(e) => setName(e.target.value)} />
                                </div>
                                <div className="col-md-6 mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Email</label>
                                    <input type="email" className="form-control" required value={email} onChange={(e) => setEmail(e.target.value)} />
                                </div>
                            </div>
                            <button type="submit" className="btn btn-primary me-2">Save</button>
                            <button type="button" className="btn btn-secondary" onClick={() => setEditId(null)}>Cancel</button>
                        </form>
                    </div>
                </div>
            }

            <AddEmployee />
        </div>
    )
}

export default EmployeeManage
