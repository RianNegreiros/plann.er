import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import './App.css';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './secure/dashboard/Dashboard';
import OrderItems from './secure/orders/OrderItems';
import Orders from './secure/orders/Orders';
import ProductCreate from './secure/products/ProductCreate';
import ProductEdit from './secure/products/ProductEdit';
import Products from './secure/products/Products';
import Profile from './secure/profile/Profile';
import RedirectToDashboard from './secure/RedirectToDashboard';
import Roles from './secure/roles/Role';
import RoleCreate from './secure/roles/RoleCreate';
import RoleEdit from './secure/roles/RoleEdit';
import UserCreate from './secure/users/UserCreate';
import UserEdit from './secure/users/UserEdit';
import Users from './secure/users/Users';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Route path={'/'} exact component={RedirectToDashboard}/>
        <Route path={'/dashboard'} exact component={Dashboard}/>
        <Route path={'/profile'} component={Profile} exact/>
        <Route path={'/login'} component={Login}/>
        <Route path={'/register'} component={Register}/>
        <Route path={'/users'} component={Users} exact/>
        <Route path={'/users/create'} component={UserCreate}/>
        <Route path={'/users/:id/edit'} component={UserEdit}/>
        <Route path={'/roles'} component={Roles} exact/>
        <Route path={'/roles/create'} component={RoleCreate}/>
        <Route path={'/roles/:id/edit'} component={RoleEdit}/>
        <Route path={'/products'} component={Products} exact/>
        <Route path={'/products/create'} component={ProductCreate}/>
        <Route path={'/products/:id/edit'} component={ProductEdit}/>
        <Route path={'/orders'} component={Orders} exact/>
        <Route path={'/orders/:id'} component={OrderItems} exact/>
      </BrowserRouter>
    </div>
  );
}

export default App;
