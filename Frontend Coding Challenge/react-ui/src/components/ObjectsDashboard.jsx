import { useEffect, useState } from 'react'
import ObjectList from './ObjectList'
import axios from 'axios'
import { Link, useSearchParams } from 'react-router'

const PAGE_SIZE = 10

function ObjectsDashboard() {

    const [searchParams, setSearchParams] = useSearchParams()
    const [objects, setObjects] = useState([]);

    useEffect(() => {
        const getAllObjects = async () => {
            try {
                const resp = await axios.get('https://api.restful-api.dev/objects')
                setObjects(resp.data)

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

    const totalPages = Math.max(1, Math.ceil(objects.length / PAGE_SIZE))

    let page = parseInt(searchParams.get("page")) || 1

    if (page < 1) page = 1
    if (page > totalPages) page = totalPages

    const pageObjects = objects.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE)

    const goToPage = (p) => {
        setSearchParams({ page: p })
    }

    return (
        <div className="container mt-4">
            <div className="d-flex justify-content-between align-items-center">
                <h3>Objects</h3>
                <Link className="btn btn-primary" to="/add">+ Add Object</Link>
            </div>

            <hr />

            <ObjectList objects={pageObjects} onDelete={onDelete} />

            <nav aria-label="Page navigation">
                <ul className="pagination">
                    <li className={"page-item" + (page === 1 ? " disabled" : "")}>
                        <button className="page-link" onClick={() => goToPage(page - 1)}>Previous</button>
                    </li>

                    {
                        Array.from({ length: totalPages }, (_, i) => i + 1).map(p => (
                            <li key={p} className={"page-item" + (p === page ? " active" : "")}>
                                <button className="page-link" onClick={() => goToPage(p)}>{p}</button>
                            </li>
                        ))
                    }

                    <li className={"page-item" + (page === totalPages ? " disabled" : "")}>
                        <button className="page-link" onClick={() => goToPage(page + 1)}>Next</button>
                    </li>
                </ul>
            </nav>
        </div>
    )
}

export default ObjectsDashboard