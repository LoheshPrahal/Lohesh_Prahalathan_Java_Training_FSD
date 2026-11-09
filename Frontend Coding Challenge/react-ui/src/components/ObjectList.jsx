


const getField = (data, name) => {
    if (!data) return null
    const key = Object.keys(data).find(k => k.toLowerCase().includes(name))
    if (key === undefined || data[key] === null || data[key] === "") return null
    return data[key]
}

const show = (value) => {
    return value === null ? <span className="text-muted">N/A</span> : value
}
function ObjectList({objects, onDelete}) {

    return (
        <div className="container">
    <div className="row g-3">
        {
            objects.map((o, index) => {
                const color = getField(o.data, "color")
                const price = getField(o.data, "price")
                const cpu = getField(o.data, "cpu")

                return (
                    <div className="col-12 col-sm-6 col-lg-4" key={index}>
                        <div className="card h-100">
                            <div className="card-body">
                                <h5 className="card-title">{o.name}</h5>

                                <p className="card-text mb-1">
                                    <b>Color: </b>{show(color)}
                                </p>

                                <p className="card-text mb-1">
                                    <b>Price: </b>
                                    {show(price === null ? null : "$" + price)}
                                </p>

                                <p className="card-text mb-1">
                                    <b>CPU: </b>{show(cpu)}
                                </p>
                            </div>

                            <div className="card-footer">
                                <button
                                    className="btn btn-primary btn-sm"
                                    onClick={() => onDelete(o.id)}
                                >
                                    Delete
                                </button>
                            </div>
                        </div>
                    </div>
                )
            })
        }
    </div>
</div>
    )
}

export default ObjectList