import React from 'react';


const ButtonDownload = props => {
  return (
    <a href={props.url} className="button-json">
      View JSON <i className="fa fa-eye" aria-hidden="true"></i>
    </a>
  );
};
export default ButtonDownload;