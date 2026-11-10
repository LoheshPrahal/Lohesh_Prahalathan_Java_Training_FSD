import { Route, Routes } from "react-router"
import ObjectsDashboard from './components/ObjectsDashboard'
import PageNotFound from "./components/PageNotFound"
import ObjectAdd from "./components/ObjectAdd"

function App() {

  return (
    <Routes>
      <Route path="/" element={<ObjectsDashboard />}/>
      <Route path="/add" element={<ObjectAdd />} />
      <Route path="*" element={<PageNotFound />} />
    </Routes>
  )
}

export default App
