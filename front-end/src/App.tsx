import { Route, Routes } from "react-router"
import Home from "./pages/home/Home"
import About from "./pages/about/About"

function App(){
  return (
    <Routes>
      <Route path="/" element={<Home/>} />
      <Route path="/about" element={<About/>} />
      <Route path="/contact" element={<div>Contact</div>} />
      <Route path="*" element={<div>404 Not Found</div>} />
    </Routes>
  )
}

export default App
