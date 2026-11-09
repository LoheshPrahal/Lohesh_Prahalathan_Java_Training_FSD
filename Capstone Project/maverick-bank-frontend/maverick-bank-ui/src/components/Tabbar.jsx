function TabBar({ tabs, active, onChange }) {
    return (
        <ul className="nav nav-pills mb-4 flex-wrap gap-1">
            {
                tabs.map((t) => (
                    <li className="nav-item" key={t.key}>
                        <button className={"nav-link " + (active === t.key ? "active" : "bg-white")} onClick={() => onChange(t.key)}>
                            <i className={"bi " + t.icon + " me-1"}></i>{t.label}
                        </button>
                    </li>
                ))
            }
        </ul>
    )
}

export default TabBar
