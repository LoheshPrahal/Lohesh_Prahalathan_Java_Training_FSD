import axios from "axios"
import { useState } from "react"
import { useNavigate } from "react-router"

function ObjectAdd() {

    const [name, setName] = useState("")
    const [year, setYear] = useState("")
    const [price, setPrice] = useState("")
    const [cpu, setCpu] = useState("")

    const navigate = useNavigate()

    const add = async (e) => {
        e.preventDefault()

        let obj = {
            'name': name,
            'data': {
                'Year': Number(year),
                'Price': Number(price),
                'CPU': cpu
            }
        }

        try {
            const response = await axios.post('https://api.restful-api.dev/objects', obj)
            console.log(response.data)

            navigate("/")
        } catch (err) {
            console.log(err)
            alert("Could not add the object")
        }
    }

    return (
        <div className="container">
            <h3>Add new Object</h3>

            <form onSubmit={(e) => add(e)}>
                <div className="row">
                    <div className="col-md-6">
                        <div className="mt-4">
                            <label>Name</label>
                            <input type="text" className="form-control"
                                onChange={(e) => setName(e.target.value)} />
                        </div>

                        <div className="mt-4">
                            <label>Year</label>
                            <input type="text" className="form-control"
                                onChange={(e) => setYear(e.target.value)} />
                        </div>

                        <div className="mt-4">
                            <label>Price</label>
                            <input type="text" className="form-control"
                                onChange={(e) => setPrice(e.target.value)} />
                        </div>

                        <div className="mt-4">
                            <label>CPU</label>
                            <input type="text" className="form-control"
                                onChange={(e) => setCpu(e.target.value)} />
                        </div>

                        <div className="mt-4">
                            <button type="submit" className="btn btn-primary">
                                + Add Object
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    )
}

export default ObjectAdd