import { useEffect } from "react"
import { Outlet, useNavigate } from "react-router"
import Navbar from "./Navbar"
import Welcome from "./Welcome"

function AdminDashboard() {
    const navigate = useNavigate()

    // Only a logged in admin can open these screens
    const allowed = localStorage.getItem('token') !== null && localStorage.getItem('role') === 'ADMIN'

    useEffect(() => {
        if (!allowed) {
            navigate("/no-access")
        }
    }, [allowed, navigate])

    if (!allowed) {
        return null
    }

    return (
        <div className="container-fluid bg-light min-vh-100 pb-5">
            <div className="row">
                <div className="col-lg-12">
                    <Navbar />
                </div>
            </div>
            <div className="row mt-3">
                <div className="col-lg-12">
                    <div className="alert alert-primary" role="alert">
                        <Welcome />
                    </div>
                </div>
            </div>
            <div className="row">
                <div className="col-lg-12">
                    <Outlet />
                </div>
            </div>
        </div>
    )
}

export default AdminDashboard
