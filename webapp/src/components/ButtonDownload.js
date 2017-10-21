import React from 'react';


const ButtonDownload = props => {
  return (
    <a href={props.url} className="button-download">
      <i className="fa fa-download" aria-hidden="true"></i>
    </a>
  );
};
export default ButtonDownload;
