import React, { Component } from 'react';
import { connect } from 'react-redux';
import BreadCrumb from '../../Breadcrumb';
import { getListEngines } from './ListEnginesActions';

class ListEngines extends Component {
  componentWillMount() {
    this.props.getListEngines();
  }
  render() { 
    const { location, listEngines } = this.props;
    return(
      <div>
        <BreadCrumb location={location} />
        <div className="card">
          <table className="table-default unstriped">
            <thead>
              <tr>
                <th width="20"><input id="checkbox1" type="checkbox" /></th>
                <th width="50"></th>
                <th>Engines</th>
                <th width="100">Version</th>
                <th width="100">Status</th>
                <th width="100">Endpoint</th>
                <th width="250"></th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  <input id="checkbox1" type="checkbox" />
                </td>
                <td>
                  <span>
                  <input
                    className="toggle-default"
                    type="checkbox"
                    checked={true}
                    id={`status-1`}
                    //onChange={(e) => changeStatusAd()}
                  />
                  <label
                    className="label-toggle"
                    htmlFor={`status-1`}>
                    Toggle
                  </label>
                </span>
                </td>
                <td>Decision trees</td>
                <td>V.1.0</td>
                <td>
                  <i className="fa fa-close icon-status error" aria-hidden="true"></i>
                </td>
                <td>
                  <span className="endpoint">goo.gl/GF3q7894727920983987</span>
                </td>
                <td>
                  <a href="#/edit-engine" className="hollow button secondary">View</a>
                  <a href="" className="hollow button secondary">Delete</a>
                  <a href="#/edit-engine" className="hollow button secondary">Edit</a>
                </td>
              </tr>
              <tr>
                <td>
                  <input id="checkbox1" type="checkbox" />
                </td>
                <td>
                  <span>
                  <input
                    className="toggle-default"
                    type="checkbox"
                    checked={true}
                    id={`status-1`}
                    //onChange={(e) => changeStatusAd()}
                  />
                  <label
                    className="label-toggle"
                    htmlFor={`status-1`}>
                    Toggle
                  </label>
                </span>
                </td>
                <td>Decision trees</td>
                <td>V.1.0</td>
                <td>
                  <i className="fa fa-cloud-upload icon-status success" aria-hidden="true"></i>
                </td>
                <td>
                  <span className="endpoint">goo.gl/GF3q7894727920983987</span>
                </td>
                <td>
                  <a href="#/edit-engine" className="hollow button secondary">View</a>
                  <a href="" className="hollow button secondary">Delete</a>
                  <a href="#/edit-engine" className="hollow button secondary">Edit</a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    );
  }
};

export default connect((state) => ({
  listEngines: state.engines.listEngines,
}), { getListEngines })(ListEngines);
