// Redux for the admin module - the lists of the admin screens are kept in the store
const initialState={
    customers: [],
    employees: [],
    branches: [],
    loans: [],
    admins: []
}
const AdminReducer =(state = initialState,action)=>{
    switch(action.type){
        case 'FETCH_CUSTOMERS':
            return {
                ...state, 
                customers : action.payload
            }
        case 'FETCH_EMPLOYEES':
            return {
                ...state, 
                employees : action.payload
            }
        case 'FETCH_BRANCHES':
            return {
                ...state, 
                branches : action.payload
            }
        case 'FETCH_LOANS':
            return {
                ...state, 
                loans : action.payload
            }
        case 'FETCH_ADMINS':
            return {
                ...state, 
                admins : action.payload
            }
        case 'CLEAR_ADMIN_DATA' :
            return initialState
              
        default: 
            return state
    }

}

export default AdminReducer
