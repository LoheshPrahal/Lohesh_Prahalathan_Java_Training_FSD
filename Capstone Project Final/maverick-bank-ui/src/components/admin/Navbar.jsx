import { NavLink, useNavigate } from "react-router"
import { useDispatch } from "react-redux"
import { clearAdminData } from "../../store/actions/AdminAction"

function Navbar() {
    const navigate = useNavigate()
    const dispatch = useDispatch()

    // Redux for the admin module - the lists in the store are cleared at logout
    const logout = () => {
        dispatch(clearAdminData())
        localStorage.clear()
        navigate("/")
    }

    // The link of the open page is highlighted
    const linkClass = ({ isActive }) => "nav-link" + (isActive ? " active" : "")

    return (
            <nav className="navbar navbar-expand-lg bg-body-tertiary">
                <div className="container-fluid">
                    <span className="navbar-brand"><i className="bi bi-bank2 me-2"></i>Maverick Bank</span>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav me-auto">
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/admin" end>Home</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/admin/customers">Customers</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/admin/employees">Bank Employees</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/admin/branches">Branches</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/admin/loans">Loans</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/admin/admins">Admins</NavLink>
                            </li>
                        </ul>
                        <button className="btn btn-secondary" onClick={() => logout()}>Logout</button>
                    </div>
                </div>
            </nav>
    )
}

export default Navbar
