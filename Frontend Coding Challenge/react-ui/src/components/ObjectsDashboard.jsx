import { useEffect, useState } from 'react'
import ObjectList from './ObjectList'
import axios from 'axios'
import { Link } from 'react-router'
import { addedObjects } from "./ObjectAdd"



function ObjectsDashboard() {

    let [objects, setObjects] = useState([]);
    const [page, setPage] = useState(1)
    // const [orderBy, setOrderBy] = useState("ASC")
    const pageSize = 9

    useEffect(() => {
        const getAllObjects = async () => {
            try {
                const resp = await axios.get('https://api.restful-api.dev/objects')
                setObjects([...resp.data, ...addedObjects])

            } catch (err) {
                console.log(err)
            }
        }
        getAllObjects()

    }, [])

    const sortObjects = (orderBy) => {
        const sorted = [...objects].sort((a, b) => {

            if (a.data === null || a.data?.price == null) return 1;
            if (b.data === null || b.data?.price == null) return -1;

            if (orderBy === 'ASC') {
                return Number(a.data.price) - Number(b.data.price);
            }

            return Number(b.data.price) - Number(a.data.price);
        })

        setObjects(sorted)
    }

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

    return (
        <div className="container mt-4">
            <div className="d-flex justify-content-between align-items-center">
                <h3>Objects</h3>
                <Link className="btn btn-primary" to="/add">+ Add Object</Link>
            </div>
            <hr />
            <select className="form-select" onChange={(e) => sortObjects(e.target.value)}>
                <option value="ASC">ASC</option>
                <option value="DESC">DESC</option>
            </select>

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
                            <li key={index} className={"page-item " + (index + 1 === page ? "active" : "")}>
                                <button className="page-link" onClick={() => setPage(index + 1)}>
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