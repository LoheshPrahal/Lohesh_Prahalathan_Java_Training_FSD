import { useEffect, useState } from 'react'
import ObjectList from './ObjectList'
import axios from 'axios'
import { Link } from 'react-router'

function ObjectsDashboard() {

    const [objects, setObjects] = useState([]);
    const [page, setPage] = useState(1)
    const pageSize = 9

    useEffect(() => {
        const getAllObjects = async () => {
            try {
                const resp = await axios.get('https://api.restful-api.dev/objects')
                setObjects(resp.data)
                console.log(resp)

            } catch (err) {
                console.log(err)
            }
        }
        getAllObjects()
    }, [])

    const onDelete = async (id) => {
        try {
            await axios.delete(`https://api.restful-api.dev/objects/${id}`)
            setObjects(objects.filter(o => o.id !== id))
        } catch (error) {
            console.log(error)
            alert("Could not delete this object")
        }
    }

    const totalPages = Math.max(1, Math.ceil(objects.length / pageSize))

    const pageObjects = objects.slice((page - 1) * pageSize, page * pageSize)

    const onPageChange = (val) => {
        setPage(val)
    }

    return (
        <div className="container mt-4">
            <div className="d-flex justify-content-between align-items-center">
                <h3>Objects</h3>
                <Link className="btn btn-primary" to="/add">+ Add Object</Link>
            </div>

            <hr />

            <ObjectList objects={pageObjects} onDelete={onDelete} />

            <nav>
                <ul className="pagination justify-content-center">

                    <li className="page-item">
                        <button className="page-link" onClick={() => setPage(page - 1)}
                            disabled={page === 1}>Previous</button>
                    </li>

                    {
                        Array.from({ length: totalPages }, (e, index) => (
                            <li key={index} className={"page-item " + (index + 1 === page ? "active": "")}>
                                <button className="page-link" onClick={() => onPageChange(index + 1)}>
                                    {index + 1}
                                </button>
                            </li>
                        ))
                    }

                    <li className="page-item">
                        <button className="page-link" onClick={() => setPage(page + 1)}
                            disabled={page === totalPages}>Next</button>
                    </li>

                </ul>


            </nav>
        </div>
    )
}

export default ObjectsDashboard