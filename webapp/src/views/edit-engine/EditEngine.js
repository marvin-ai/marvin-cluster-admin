
import React, { Component } from 'react';
import { connect } from 'react-redux';
import BreadCrumb from '../../Breadcrumb';
import ButtonDownload from '../../components/ButtonDownload'
import ButtonJson from '../../components/ButtonJson'

class EditEngine extends Component {
  componentWillMount() {
    //this.props.getListEngines();
  }
  render() { 
    return(
      <div>
        <BreadCrumb location={this.props.location} />
        <div className="card">
          <h3>Risk classifier</h3>
          <ul>
            <li>
              <strong>Name:</strong> Risk classifier
            </li>
            <li>
              <strong>Version:</strong> V.0.1
            </li>
            <li>
              <strong>Status:</strong> Shutdown
            </li>
            <li>
              <strong>Endpoint:</strong> http://www.marvin.com/engine-traning-first
            </li>
          </ul>
          <div className="card">
            <h3>Published</h3>
            <ul>
              <li><strong>Training10</strong></li>
              <li>
                Params: 
                <ButtonJson url="http://www.google.com" /> 
              </li>
              <li>
                Intal data set:
                <ButtonDownload url="http://www.google.com" /> 
              </li>
              <li>
                Data set:
                <ButtonDownload url="http://www.google.com" /> 
              </li>
              <li>
                Metrics:
                <ButtonDownload url="http://www.google.com" /> 
              </li>
              <li>
                Model:
                <ButtonDownload url="http://www.google.com" /> 
              </li>
              <li>Endpoint: http://www.marvin.com/engine-traning-first</li>
            </ul>
          </div>
          <h3>Training history</h3>

          <div className="all-timeline col-md-7">
            <div className="timeline-term white gap-bottom20">
                16/10/2017
            </div>
            <div className="timeline-description">
              <div className="card-timeline">
                <div className="card-timeline-header">
                  <h4>Training 10</h4>
                  <ul className="card-infos">
                    <li>
                      01:35 <i className="fa fa-clock-o" aria-hidden="true"></i>
                    </li>
                    <li>
                      Params: <ButtonJson url="http://www.google.com" /> 
                    </li>
                  </ul>
                </div>
                <div className="card-timeline-body">
                  <h4>Pipeline</h4>

                  <div className="wizard-bar">
                      <div className="step-box">
                        <div>
                          <span className="step">1</span>
                          <span className="step-name">Acquisitor</span>
                        </div>
                      </div>
                      <div className="step-box">
                        <div>
                          <span className="step not-active">2</span>
                          <span className="step-name">Tpreparator</span>
                        </div>
                      </div>
                      <div className="step-box">
                        <div>
                          <span className="step not-active">3</span>
                          <span className="step-name">Trainer</span>
                        </div>
                      </div>
                      <div className="step-box">
                        <div>
                          <span className="step not-active">4</span>
                          <span className="step-name">Evaluator</span>
                        </div>
                      </div>
                  </div>

                  <div className="card-timeline-footer">
                    <p className="text-footer">Trainer, Lorem Ipsum is simply dummy text of the printing and typesetting industry.</p>
                    Model: <ButtonDownload url="http://www.google.com" /> 
                    <a className="button small button-round" href="#">Deploy</a>
                  </div>
                </div>
              </div>
            </div>
            <div className="timeline-description">
            </div>
        </div>

        </div>
      </div>
    );
  }
};

export default connect((state) => ({
}), { })(EditEngine);