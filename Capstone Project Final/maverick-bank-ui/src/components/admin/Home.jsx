import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { getBranches, getCustomers, getEmployees, getLoans } from "../../store/actions/AdminAction"
import { getErrorMessage } from "../../utility/helper"
import Message from "../Message"
import DonutChart from "../DonutChart"

function Home() {
    // Redux - the numbers of the dashboard come from the store
    const customers = useSelector(state=>state.customers)
    const employees = useSelector(state=>state.employees)
    const branches = useSelector(state=>state.branches)
    const loans = useSelector(state=>state.loans)
    const [errMsg, setErrMsg] = useState("")

    const dispatch = useDispatch()

    useEffect(() => {
        const getNumbers = async () => {
            try {
                await dispatch(getCustomers())
                await dispatch(getEmployees())
                await dispatch(getBranches())
                await dispatch(getLoans())
            }
            catch (err) {
                setErrMsg(getErrorMessage(err))
            }
        }
        getNumbers()
    }, [dispatch])

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
                            <div className="h3 fw-bold mb-0">{branches.length}</div>
                        </div>
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-muted small">Loans</div>
                            <div className="h3 fw-bold mb-0">{loans.length}</div>
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
