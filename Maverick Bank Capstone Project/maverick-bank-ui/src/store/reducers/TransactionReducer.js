const initialState={
    transactions: []
}
const TransactionReducer =(state = initialState,action)=>{
    switch(action.type){
        case 'FETCH_TRANSACTIONS':
            return {
                ...state, 
                transactions : action.payload
            }
        case 'CLEAR_TRANSACTIONS' :
            return{
                ...state, 
                transactions : []
            } 
              
        default: 
            return state
    }

}

export default TransactionReducer
