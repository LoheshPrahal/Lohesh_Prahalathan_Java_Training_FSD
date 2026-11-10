import { useEffect, useState } from "react"
import axios from "axios"
import { Link, useParams } from "react-router"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

function ViewCustomer() {
    let params = useParams();
    const [customer, setCustomer] = useState(null)
    const [errMsg, setErrMsg] = useState("")

    // The employee validates all the details of the customer
    useEffect(() => {
        const getCustomer = async () => {
            try {
                const response = await axios.get(API_SERVER_URL_DEV + '/api/customer/' + params.id, authHeader())
                setCustomer(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getCustomer()
    }, [params.id])

    return (
        <div className="container-fluid">
            <div className="card">
                <div className="card-header">
                    Customer details
                </div>
                <div className="card-body">
                    <Message errMsg={errMsg} />
                    {
                        customer &&
                        <dl className="row mb-0">
                            <dt className="col-sm-3">Name</dt>
                            <dd className="col-sm-9">{customer.name} ({customer.gender})</dd>
                            <dt className="col-sm-3">Username</dt>
                            <dd className="col-sm-9">{customer.username}</dd>
                            <dt className="col-sm-3">Email</dt>
                            <dd className="col-sm-9">{customer.email}</dd>
                            <dt className="col-sm-3">Contact</dt>
                            <dd className="col-sm-9">{customer.contact}</dd>
                            <dt className="col-sm-3">Address</dt>
                            <dd className="col-sm-9">{customer.address}</dd>
                            <dt className="col-sm-3">Date of birth</dt>
                            <dd className="col-sm-9">{customer.dateOfBirth} (age {customer.age})</dd>
                            <dt className="col-sm-3">Aadhaar number</dt>
                            <dd className="col-sm-9">{customer.aadhaarNumber}</dd>
                            <dt className="col-sm-3">PAN number</dt>
                            <dd className="col-sm-9">{customer.panNumber}</dd>
                            <dt className="col-sm-3">Status</dt>
                            <dd className="col-sm-9"><span className={"badge " + statusBadge(customer.status)}>{customer.status}</span></dd>
                        </dl>
                    }
                    <Link to="/employee/customer-search" className="btn btn-secondary mt-3">Back</Link>
                </div>
            </div>
        </div>
    )
}

export default ViewCustomer
