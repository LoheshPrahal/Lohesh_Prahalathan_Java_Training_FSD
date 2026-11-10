import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { authHeader, getErrorMessage } from "../../utility/helper"
import Message from "../Message"
import DonutChart from "../DonutChart"

function Home() {
    const [customers, setCustomers] = useState([])
    const [employees, setEmployees] = useState([])
    const [branches, setBranches] = useState(0)
    const [loans, setLoans] = useState(0)
    const [errMsg, setErrMsg] = useState("")

    useEffect(() => {
        const getNumbers = async () => {
            try {
                const customerResponse = await axios.get(API_SERVER_URL_DEV + '/api/customer/all', authHeader())
                setCustomers(customerResponse.data)
                const employeeResponse = await axios.get(API_SERVER_URL_DEV + '/api/employee/all', authHeader())
                setEmployees(employeeResponse.data)
                const branchResponse = await axios.get(API_SERVER_URL_DEV + '/api/branch/all')
                setBranches(branchResponse.data.length)
                const loanResponse = await axios.get(API_SERVER_URL_DEV + '/api/loan/all', authHeader())
                setLoans(loanResponse.data.length)
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getNumbers()
    }, [])

    // Customers for each status
    const activeCustomers = customers.filter((c) => c.status === 'ACTIVE').length
    const inactiveCustomers = customers.length - activeCustomers

    // Bank employees for each branch
    const branchNames = [...new Set(employees.map((e) => e.branchName))]
    const employeesInBranch = branchNames.map((name) => employees.filter((e) => e.branchName === name).length)

    return (
        <div className="container-fluid">
            <Message errMsg={errMsg} />
            <div className="row mb-4">
                <div className="col-md-3 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Customers</div>
                            <div className="h3 fw-bold mb-0">{customers.length}</div>
                        </div>
                    </div>
                </div>
                <div className="col-md-3 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Bank employees</div>
                            <div className="h3 fw-bold mb-0">{employees.length}</div>
                        </div>
                    </div>
                </div>
                <div className="col-md-3 mb-3 mb-md-0">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Branches</div>
                            <div className="h3 fw-bold mb-0">{branches}</div>
                        </div>
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Loans</div>
                            <div className="h3 fw-bold mb-0">{loans}</div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row">
                <div className="col-md-6 mb-4">
                    <div className="card">
                        <div className="card-header">
                            Customers for each status
                        </div>
                        <div className="card-body">
                            {
                                customers.length > 0 ?
                                    <DonutChart title="Customers" labels={['ACTIVE', 'INACTIVE']} values={[activeCustomers, inactiveCustomers]} />
                                    :
                                    <p className="text-muted mb-0">No customers yet.</p>
                            }
                        </div>
                    </div>
                </div>
                <div className="col-md-6 mb-4">
                    <div className="card">
                        <div className="card-header">
                            Bank employees for each branch
                        </div>
                        <div className="card-body">
                            {
                                employees.length > 0 ?
                                    <DonutChart title="Bank employees" labels={branchNames} values={employeesInBranch} />
                                    :
                                    <p className="text-muted mb-0">No bank employees yet.</p>
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Home
