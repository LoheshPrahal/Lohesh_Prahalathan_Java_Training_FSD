import { useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, CITIES, getErrorMessage } from "../../utility/helper"
import Message from "../Message"

const apiUrl = API_SERVER_URL_DEV + '/api/branch/add'

function AddBranch({ onSuccess }) {
    const [name, setName] = useState("")
    const [address, setAddress] = useState("")
    const [city, setCity] = useState("")
    const [ifscCode, setIfscCode] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    const add = async (e) => {
        e.preventDefault()
        setErrMsg("")
        setSuccessMsg("")
        let obj = {
            'name': name,
            'address': address,
            'city': city,
            'ifscCode': ifscCode
        }
        try {
            await axios.post(apiUrl, obj, authHeader())
            setSuccessMsg("Branch is added.")
            setName("")
            setAddress("")
            setCity("")
            setIfscCode("")
            onSuccess()
        }
        catch (err) {
            setErrMsg(getErrorMessage(err))
        }
    }

    return (
        <div className="card shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3"><i className="bi bi-plus-circle me-2"></i>Add a branch</h5>
                <Message errMsg={errMsg} successMsg={successMsg} />
                <form onSubmit={(e) => add(e)}>
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Branch name</label>
                            <input type="text" className="form-control" required value={name} onChange={(e) => setName(e.target.value)} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">Address</label>
                            <input type="text" className="form-control" required value={address} onChange={(e) => setAddress(e.target.value)} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">City</label>
                            <select className="form-select" required value={city} onChange={(e) => setCity(e.target.value)}>
                                <option value="">Select</option>
                                {
                                    CITIES.map((c) => (
                                        <option key={c} value={c}>{c}</option>
                                    ))
                                }
                            </select>
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label small fw-semibold text-secondary">IFSC code</label>
                            <input type="text" className="form-control" required pattern="[A-Z]{4}0[A-Z0-9]{6}" maxLength={11} title="Example: MAVB0000001" value={ifscCode}
                                onChange={(e) => setIfscCode(e.target.value.toUpperCase())} />
                        </div>
                    </div>
                    <button type="submit" className="btn btn-primary">Add branch</button>
                </form>
            </div>
        </div>
    )
}

export default AddBranch
