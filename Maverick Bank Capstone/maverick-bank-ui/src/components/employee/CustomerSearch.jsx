import { useEffect, useState } from "react"
import axios from "axios"
import { Link } from "react-router"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/customer/all'

function CustomerSearch() {
    const [customers, setCustomers] = useState([])
    const [search, setSearch] = useState("")
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getCustomers = async () => {
            try {
                const response = await axios.get(apiUrl, authHeader())
                setCustomers(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getCustomers()
    }, [])

    const shown = customers.filter((c) =>
        c.name.toLowerCase().includes(search.toLowerCase()) || c.username.toLowerCase().includes(search.toLowerCase()))

    return (
        <div className="container-fluid">
            <div className="card">
                <div className="card-header">
                    Customer Search
                </div>
                <div className="card-body">
                    <Message errMsg={errMsg} />
                    <table className="table">
                        <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Name</th>
                                <th scope="col">Username</th>
                                <th scope="col">Email</th>
                                <th scope="col">Contact</th>
                                <th scope="col">Status</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">
                                    <input type="text" placeholder="search name or username.." className="form-control" value={search}
                                        onChange={(e) => setSearch(e.target.value)} />
                                </th>
                                <th scope="col"></th>
                                <th scope="col"></th>
                                <th scope="col"></th>
                                <th scope="col"></th>
                                <th scope="col"></th>
                            </tr>
                            {
                                shown.map((c, index) => (
                                    <tr key={c.customerId}>
                                        <th scope="row">{index + 1}</th>
                                        <td>{c.name}</td>
                                        <td>{c.username}</td>
                                        <td>{c.email}</td>
                                        <td>{c.contact}</td>
                                        <td><span className={"badge " + statusBadge(c.status)}>{c.status}</span></td>
                                        <td className="text-end">
                                            <Link to={"/employee/customer/" + c.customerId} className="btn btn-sm btn-outline-primary">View</Link>
                                        </td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                    {shown.length === 0 && <p className="text-muted mb-0">No customers found.</p>}
                </div>
            </div>
        </div>
    )
}

export default CustomerSearch
