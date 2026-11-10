import { useEffect } from "react"
import { Link } from "react-router"

function PageNotFound(){
    useEffect(()=>{
        localStorage.clear()
    },[])
    return(
        <div className="container py-5 text-center">
            <h1 className="display-4 fw-bold text-dark">Page not found</h1>
            <p className="text-muted">The page does not exist, or you are not allowed to open it.</p>
            <Link to="/" className="btn btn-primary">Go to Login</Link>
        </div>
    )
}

export default PageNotFound
