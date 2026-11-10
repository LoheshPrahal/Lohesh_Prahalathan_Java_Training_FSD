import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { deactivateCustomer, getCustomers, updateCustomer } from "../../store/actions/AdminAction"
import { getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

function CustomerManage() {
    // Redux - the list of customers comes from the store
    const customers = useSelector(state=>state.customers)
    const [editId, setEditId] = useState(null)
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [contact, setContact] = useState("")
    const [address, setAddress] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    const dispatch = useDispatch()

    useEffect(() => {
        const fetchCustomers = async () => {
            try {
                await dispatch(getCustomers())
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        fetchCustomers()
    }, [dispatch])

    const startEdit = (c) => {
        setErrMsg("")
        setSuccessMsg("")
        setEditId(c.customerId)
        setName(c.name)
        setEmail(c.email)
        setContact(c.contact)
        setAddress(c.address)
    }

    // Admin modifies the details of the customer
    const update = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'name': name,
            'email': email,
            'contact': contact,
            'address': address
        }
        try {
            // The store fetches the list of customers again
            await dispatch(updateCustomer(editId, obj))
            setSuccessMsg("Customer is updated.")
            setEditId(null)
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    const deactivate = async (c) => {
        if (!window.confirm("Deactivate " + c.name + "? The customer will not be able to login.")) {
            return
        }
        setErrMsg("")
        setSuccessMsg("")
        try {
            await dispatch(deactivateCustomer(c.customerId))
            setSuccessMsg("Customer is deactivated.")
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
                    <h5 className="fw-bold mb-3"><i className="bi bi-people me-2"></i>Customers</h5>
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Contact</th>
                                    <th>Status</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    customers.map((c) => (
                                        <tr key={c.customerId}>
                                            <td>{c.name}</td>
                                            <td>{c.username}</td>
                                            <td>{c.email}</td>
                                            <td>{c.contact}</td>
                                            <td><span className={"badge " + statusBadge(c.status)}>{c.status}</span></td>
                                            <td className="text-end text-nowrap">
                                                <button className="btn btn-sm btn-outline-primary me-2" onClick={() => startEdit(c)}>Edit</button>
                                                <button className="btn btn-sm btn-outline-danger" disabled={c.status !== 'ACTIVE'} onClick={() => deactivate(c)}>Deactivate</button>
                                            </td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </div>
                    {customers.length === 0 && <p className="text-muted mb-0">No customers yet.</p>}
                </div>
            </div>

            {
                editId !== null &&
                <div className="card shadow-sm border-0 rounded-4">
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">Edit customer</h5>
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
                                <div className="col-md-6 mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Contact</label>
                                    <input type="text" className="form-control" required pattern="[0-9]{10}" maxLength={10} title="10 digit mobile number" value={contact} onChange={(e) => setContact(e.target.value)} />
                                </div>
                                <div className="col-md-6 mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Address</label>
                                    <input type="text" className="form-control" required value={address} onChange={(e) => setAddress(e.target.value)} />
                                </div>
                            </div>
                            <button type="submit" className="btn btn-primary me-2">Save</button>
                            <button type="button" className="btn btn-secondary" onClick={() => setEditId(null)}>Cancel</button>
                        </form>
                    </div>
                </div>
            }
        </div>
    )
}

export default CustomerManage
