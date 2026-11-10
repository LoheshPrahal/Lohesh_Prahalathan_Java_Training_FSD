import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage, statusBadge } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/customer/profile'

function Profile() {
    const [customer, setCustomer] = useState(null)
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getProfile = async () => {
            try {
                const response = await axios.get(apiUrl, authHeader())
                setCustomer(response.data)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getProfile()
    }, [])

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-person me-2"></i>My profile</h5>
                <Message errMsg={errMsg} />
                {
                    customer &&
                    <dl className="row mb-0">
                        <dt className="col-sm-3">Name</dt>
                        <dd className="col-sm-9">{customer.name}</dd>
                        <dt className="col-sm-3">Username</dt>
                        <dd className="col-sm-9">{customer.username}</dd>
                        <dt className="col-sm-3">Gender</dt>
                        <dd className="col-sm-9">{customer.gender}</dd>
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
            </div>
        </div>
    )
}

export default Profile
