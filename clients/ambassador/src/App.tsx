import { BrowserRouter, Route } from 'react-router-dom';
import './App.css';
import Login from './pages/Login';
import ProductsBackend from './pages/ProductsBackend';
import ProductsFrontend from './pages/ProductsFrontend';
import Profile from './pages/Profile';
import Rankings from './pages/Rankings';
import Register from './pages/Register';
import Stats from './pages/Stats';

function App() {
  return (
    <BrowserRouter>
    <Route path={'/'} exact component={ProductsFrontend}/>
    <Route path={'/backend'} exact component={ProductsBackend}/>
    <Route path={'/login'} component={Login}/>
    <Route path={'/register'} component={Register}/>
    <Route path={'/profile'} component={Profile}/>
    <Route path={'/stats'} component={Stats}/>
    <Route path={'/rankings'} component={Rankings}/>
</BrowserRouter>
  );
}

export default App;
