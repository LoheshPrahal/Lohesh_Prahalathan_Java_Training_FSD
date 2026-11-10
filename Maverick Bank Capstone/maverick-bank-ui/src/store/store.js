import { configureStore } from "@reduxjs/toolkit";
import AdminReducer from "./reducers/AdminReducer";
 
// Redux is used only by the admin module (the transaction module uses normal state, like the other components)
export const store = configureStore({
    reducer: AdminReducer
})
