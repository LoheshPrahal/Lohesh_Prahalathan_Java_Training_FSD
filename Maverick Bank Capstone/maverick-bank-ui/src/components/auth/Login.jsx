import { useState } from "react"
import { Link, useLocation, useNavigate } from "react-router"
import { API_SERVER_URL_DEV } from "../../../config"
import axios from "axios"
import { getErrorMessage } from "../../utility/helper"

function Login() {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const apiUrl = API_SERVER_URL_DEV + '/api/auth/login'

    const navigate =  useNavigate()
    const location = useLocation()
    const successMsg = location.state?.message

    const processLogin = async (e) => {
        e.preventDefault()
        // Step 1. Fetch the jwt token
        const token = 'Basic ' + window.btoa(username + ':' + password)
        try {
            const response = await axios.get(apiUrl, {
                headers: { 'Authorization': token }
            })

            // The login API sends the role along with the token
            localStorage.setItem('token', response.data.token)
            localStorage.setItem('username', username)
            localStorage.setItem('role', response.data.role)

            // Based on the role of this user load appropriate dashboard
            switch(response.data.role){
                case 'CUSTOMER' :
                    navigate('/customer')
                    break;
                case 'EMPLOYEE' :
                    navigate('/employee')
                    break;
                case 'ADMIN' :
                    navigate('/admin')
                    break;
                default:
                    setErrMsg('Something went wrong...')
                    break;
            }
        }
        catch (err) {
            if (err?.response?.status === 401 || err?.response?.data === undefined) {
                setErrMsg("Invalid Credentials")
                setErrComment("Check the username and the password. A deactivated user cannot login.")
            }
            else {
                setErrMsg(getErrorMessage(err))
                setErrComment(err?.response?.data?.comments)
            }
        }
    }
    return (
        <div className="bg-light min-vh-100 py-5">
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-12 col-sm-9 col-md-7 col-lg-5 col-xl-4">

                    {/* <!-- Login Card --> */}
                    <div className="card shadow-sm border-0 rounded-4 p-4 p-sm-5 bg-white">
                        <div className="card-body p-0">

                            {/* <!-- Branding / Header --> */}
                            <div className="text-center mb-4">

                                <h1 className="h4 fw-bold text-dark mb-1"><i className="bi bi-bank2 text-primary me-2"></i>Maverick Bank</h1>
                                <p className="text-muted small">Please enter your details to sign in.</p>
                            </div>
                            {/* <!-- Error / Header --> */}
                            <div className="text-center mb-4">

                                <p className="text-success mb-0">
                                    {successMsg}
                                </p>
                                <p className="text-danger fs-5 mb-0">
                                    {errMsg}
                                </p>
                                <p className="text-muted small">
                                    {errComment}
                                </p>
                            </div>

                            {/* <!-- Login Form --> */}
                            <form onSubmit={(e) => processLogin(e)}>
                                {/* <!-- Username Field --> */}
                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-secondary">Username</label>
                                    <input type="text" className="form-control form-control-lg" required
                                        onChange={(e) => setUsername(e.target.value)} />
                                </div>

                                {/* <!-- Password Field --> */}
                                <div className="mb-3">
                                    <div className="d-flex justify-content-between align-items-center">
                                        <label className="form-label small fw-semibold text-secondary">Password</label>
                                        <a href="#" className="small text-decoration-none text-primary fw-semibold">Forgot password?</a>
                                    </div>
                                    <input type="password" className="form-control form-control-lg" placeholder="••••••••" required
                                        onChange={(e) => setPassword(e.target.value)} />
                                </div>

                                {/* <!-- Submit Button --> */}
                                <div className="d-grid mb-3">
                                    <button type="submit" className="btn btn-primary btn-lg fw-semibold"  >Sign in</button>
                                </div>

                                {/* <!-- Footer Link --> */}
                                <div className="text-center">
                                    <p className="small text-muted mb-0">Don't have an account? <Link to="/customer/signup" className="text-primary text-decoration-none fw-semibold">Sign up</Link></p>
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

export default Login
