import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import NavBar from "../Navbar";
import TabBar from "../Tabbar";
import MyAccounts from "./MyAccounts";
import Beneficiaries from "./Beneficiaries";
import Loans from "./Loans";
import Profile from "./Profile";
import CustomerTransactions from "./CustomerTransactions";



function CustomerDashboard(){
    const navigate = useNavigate()
    let params = useParams()
    const [active, setActive] = useState('accounts')

    const allowed = params.token === localStorage.getItem('token') && localStorage.getItem('role') === 'CUSTOMER'

    useEffect(() => {
        if(!allowed){
            navigate("/no-access")
        }
    }, [allowed, navigate])

    const tabs = [
        {key: 'accounts', label: 'Accounts', icon: 'bi-wallet2'},
        { key: 'transactions', label: 'Transactions', icon: 'bi-arrow-left-right' },
        { key: 'beneficiaries', label: 'Beneficiaries', icon: 'bi-people' },
        { key: 'loans', label: 'Loans', icon: 'bi-cash-coin' },
        { key: 'profile', label: 'Profile', icon: 'bi-person' }
    ]

    if(!allowed){
        return null
    }

    return(
        <div className="bg-light min-vh-100 pb-5">
            <NavBar />
            <div className="container">
                <TabBar tabs={tabs} active={active} onChange={setActive} />

                {active === 'accounts' && <MyAccounts />}
                {active === 'transactions' && <CustomerTransactions />}
                {active === 'beneficiaries' && <Beneficiaries />}
                {active === 'loans' && <Loans />}
                {active === 'profile' && <Profile />}
            </div>
        </div>
    )
}

export default CustomerDashboard
