import { NavLink, useNavigate } from "react-router"

function Navbar() {
    const navigate = useNavigate()

    // Redux removed - there is no list in the store to clear at logout
    const logout = () => {
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
                                <NavLink className={linkClass} to="/customer" end>Home</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/customer/accounts">Accounts</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/customer/transactions">Transactions</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/customer/beneficiaries">Beneficiaries</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/customer/loans">Loans</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink className={linkClass} to="/customer/profile">Profile</NavLink>
                            </li>
                        </ul>
                        <button className="btn btn-secondary" onClick={() => logout()}>Logout</button>
                    </div>
                </div>
            </nav>
    )
}

export default Navbar
