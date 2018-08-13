import React from 'react';
import { Link } from 'react-router';

const MainHeader = props => {
  return (
    <span>
      <header className="header">
        <div className="container">
          <Link to="/" className="brand">
            <img src="./images/logo_marvin.png" alt="Marvin" />
          </Link>
        </div>
      </header>
    </span>
  );
};
export default MainHeader;
