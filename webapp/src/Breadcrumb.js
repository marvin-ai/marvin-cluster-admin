import React from 'react';
import breadcrumbData from './json/breadcrumbData.json';

const BreadCrumb = props => {
  const path = props.location.pathname.split('/')[1];
  return(
    <div>
      <div className="pull-left">
        <h2 className="subtitle">{breadcrumbData[path]}</h2>
        <nav aria-label="You are here:" role="navigation">
          <ul className="breadcrumbs">
            <li><a href="#">Home</a></li>
            <li>
              <span className="show-for-sr">Current: </span> {breadcrumbData[path]}
            </li>
          </ul>
        </nav>
      </div>
      <a className="button button-round pull-right" href="#">Create Engine</a>
    </div>
  )
};

export default BreadCrumb;