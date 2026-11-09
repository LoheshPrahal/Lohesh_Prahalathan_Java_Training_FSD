import { useNavigate } from "react-router"

function NavBar() {
    const navigate = useNavigate()
    const logout = () => {
        localStorage.clear()
        navigate("/")
    }

    return (
        <nav className="navbar navbar-dark bg-primary shadow-sm mb-4">
            <div className="container">
                <span className="navbar-brand fw-bold"><i className="bi bi-bank2 me-2"></i>Maverick Bank</span>
                <div className="d-flex align-items-center">
                    <span className="navbar-text text-white me-3">
                        <i className="bi bi-person-circle me-1"></i>{localStorage.getItem('username')}
                        <span className="badge text-bg-light ms-2">{localStorage.getItem('role')}</span>
                    </span>
                    <button className="btn btn-outline-light btn-sm" onClick={() => logout()}>Logout</button>
                </div>
            </div>
        </nav>
    )
}

export default NavBar
