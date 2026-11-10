import { configureStore } from "@reduxjs/toolkit";
import TransactionReducer from "./reducers/TransactionReducer";
 
export const store = configureStore({
    reducer: TransactionReducer
})
