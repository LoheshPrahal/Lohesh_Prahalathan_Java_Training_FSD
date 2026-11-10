import { useEffect, useState } from "react"
import { Link, useNavigate } from "react-router"
import { API_SERVER_URL_DEV } from "../../../config"
import axios from "axios"
import { ACCOUNT_TYPES, getErrorMessage } from "../../utility/helper"

const signUpApi = API_SERVER_URL_DEV + "/api/customer/add"
const branchApi = API_SERVER_URL_DEV + "/api/branch/all"

function SignUp(){
    const [name, setName] = useState("")
    const [gender, setGender] = useState("")
    const [email, setEmail] = useState("")
    const [contact, setContact] = useState("")
    const [address, setAddress] = useState("")
    const [dateOfBirth, setDateOfBirth] = useState("")
    const [aadhaarNumber, setAadhaarNumber] = useState("")
    const [panNumber, setPanNumber] = useState("")
    const [accountType, setAccountType] = useState("")
    const [branchId, setBranchId] = useState("")
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")

    const [branches, setBranches] = useState([])
    const [errMsg, setErrMsg] = useState("")
    const navigate = useNavigate()

    // The branch list is public, so it is shown before the login
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

    // Password strength: length, upper case letter, number and special character
    const getStrength = () => {
        let score = 0
        if (password.length >= 8) score++
        if (/[A-Z]/.test(password)) score++
        if (/[0-9]/.test(password)) score++
        if (/[^A-Za-z0-9]/.test(password)) score++
        return score
    }
    const strengthLabels = ['Weak', 'Weak', 'Fair', 'Good', 'Strong']
    const strengthColors = ['bg-danger w-25', 'bg-danger w-25', 'bg-warning w-50', 'bg-info w-75', 'bg-success w-100']

    const processSignup =async (e)=>{
        e.preventDefault()
        if (password !== confirmPassword) {
            setErrMsg("Password and confirm password are not same")
            return
        }
        let customerDto ={
            'name': name,
            'gender': gender,
            'email': email,
            'contact': contact,
            'address': address,
            'dateOfBirth': dateOfBirth,
            'aadhaarNumber': aadhaarNumber,
            'panNumber': panNumber,
            'username':username,
            'password':password,
            'accountType': accountType,
            'branchId': branchId
        }
        try{
            await axios.post(signUpApi, customerDto)
            navigate("/", { state: { message: "Registration is successful. Your account will be active once a bank employee approves it." } })
        }
        catch(err){
            setErrMsg(getErrorMessage(err))
        }
    }
    return(
        <div className="bg-light min-vh-100 py-5">
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-12 col-md-10 col-lg-8 col-xl-7">

                    {/* <!-- Sign up Card --> */}
                    <div className="card shadow-sm border-0 rounded-4 p-4 p-sm-5 bg-white">
                        <div className="card-body p-0">

                            {/* <!-- Branding / Header --> */}
                            <div className="text-center mb-4">

                                <h1 className="h4 fw-bold text-dark mb-1"><i className="bi bi-bank2 text-primary me-2"></i>Maverick Bank</h1>
                                <p className="text-muted small">Please enter your details to open an account. Registration also opens your first account.</p>
                            </div>
                            {/* <!-- Error / Header --> */}
                            <div className="text-center mb-4">

                                <p className="text-danger fs-5 mb-0">
                                    {errMsg}
                                </p>
                            </div>

                            {/* <!-- Sign up Form --> */}
                            <form onSubmit={(e) => processSignup(e)}>
                                <div className="row">
                                    {/* <!-- name Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Name</label>
                                        <input type="text" className="form-control" required
                                            onChange={(e) => setName(e.target.value)} />
                                    </div>
                                    {/* <!-- gender Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Gender</label>
                                        <select className="form-select" required onChange={(e) => setGender(e.target.value)}>
                                            <option value="">Select</option>
                                            <option value="MALE">Male</option>
                                            <option value="FEMALE">Female</option>
                                        </select>
                                    </div>
                                    {/* <!-- email Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Email</label>
                                        <input type="email" className="form-control" required
                                            onChange={(e) => setEmail(e.target.value)} />
                                    </div>
                                    {/* <!-- contact Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Contact number</label>
                                        <input type="text" className="form-control" required pattern="[0-9]{10}" maxLength={10} title="10 digit mobile number"
                                            onChange={(e) => setContact(e.target.value)} />
                                    </div>
                                    {/* <!-- address Field --> */}
                                    <div className="col-12 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Address</label>
                                        <input type="text" className="form-control" required
                                            onChange={(e) => setAddress(e.target.value)} />
                                    </div>
                                    {/* <!-- date of birth Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Date of birth</label>
                                        <input type="date" className="form-control" required max={new Date().toISOString().slice(0, 10)}
                                            onChange={(e) => setDateOfBirth(e.target.value)} />
                                    </div>
                                    {/* <!-- aadhaar Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Aadhaar number</label>
                                        <input type="text" className="form-control" required pattern="[0-9]{12}" maxLength={12} title="12 digit Aadhaar number"
                                            onChange={(e) => setAadhaarNumber(e.target.value)} />
                                    </div>
                                    {/* <!-- pan Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">PAN number</label>
                                        <input type="text" className="form-control" required pattern="[A-Z]{5}[0-9]{4}[A-Z]" maxLength={10} title="Example: ABCDE1234F" value={panNumber}
                                            onChange={(e) => setPanNumber(e.target.value.toUpperCase())} />
                                    </div>
                                    {/* <!-- account type Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Account type</label>
                                        <select className="form-select" required onChange={(e) => setAccountType(e.target.value)}>
                                            <option value="">Select</option>
                                            {
                                                ACCOUNT_TYPES.map((t) => (
                                                    <option key={t} value={t}>{t}</option>
                                                ))
                                            }
                                        </select>
                                    </div>
                                    {/* <!-- branch Field --> */}
                                    <div className="col-12 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Branch</label>
                                        <select className="form-select" required onChange={(e) => setBranchId(e.target.value)}>
                                            <option value="">Select</option>
                                            {
                                                branches.map((b) => (
                                                    <option key={b.id} value={b.id}>{b.name} - {b.city} ({b.ifscCode})</option>
                                                ))
                                            }
                                        </select>
                                    </div>
                                    {/* <!-- Username Field --> */}
                                    <div className="col-12 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Username</label>
                                        <input type="text" className="form-control" required minLength={3} maxLength={15}
                                            onChange={(e) => setUsername(e.target.value)} />
                                    </div>
                                    {/* <!-- Password Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Password</label>
                                        <input type="password" className="form-control" placeholder="••••••••" required minLength={8}
                                            onChange={(e) => setPassword(e.target.value)} />
                                        {
                                            password.length > 0 &&
                                            <div>
                                                <div className="progress mt-2">
                                                    <div className={"progress-bar " + strengthColors[getStrength()]}></div>
                                                </div>
                                                <small className="text-muted">Strength: {strengthLabels[getStrength()]}</small>
                                            </div>
                                        }
                                        <small className="text-muted d-block">At least 8 characters. Use upper case letters, numbers and special characters.</small>
                                    </div>
                                    {/* <!-- Confirm Password Field --> */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label small fw-semibold text-secondary">Confirm password</label>
                                        <input type="password" className="form-control" placeholder="••••••••" required
                                            onChange={(e) => setConfirmPassword(e.target.value)} />
                                    </div>
                                </div>

                                {/* <!-- Submit Button --> */}
                                <div className="d-grid mb-3">
                                    <button type="submit" className="btn btn-primary btn-lg fw-semibold"  >Sign up</button>
                                </div>

                                {/* <!-- Footer Link --> */}
                                <div className="text-center">
                                    <p className="small text-muted mb-0">Have an account? <Link to="/" className="text-primary text-decoration-none fw-semibold">Login</Link></p>
                                </div>
                            </form>

                        </div>
                    </div>

                </div>
            </div>
        </div>
        </div>
    )
}

export default SignUp
