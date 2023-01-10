import React from "react";

export default function Menu() {
return (
  <nav id="sidebarMenu" className="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
  <div className="position-sticky pt-3 sidebar-sticky">
    <ul className="nav flex-column">
      <li className="nav-item">
        <a className="nav-link active" aria-current="page" href="/">
          <span data-feather="home" className="align-text-bottom"></span>
          Dashboard
        </a>
      </li>
    </ul>
  </div>
</nav>
)
}