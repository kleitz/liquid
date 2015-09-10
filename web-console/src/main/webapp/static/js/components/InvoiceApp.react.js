var React = require('react');
var InvoiceStore = require('../stores/InvoiceStore');
var CrudTable = require('./CrudTable.react');

var definition = {
  source: '/api/invoices',
  columns: [
    {name: 'id'},
    {name: 'invoiceNo'},
    {name: 'issuedAt', type: 'date', pattern: 'YYYY-MM-DD'},
    {name: 'description'},
    {name: 'status'}
  ],
  modal: {
    title: 'invoice',
    url: '/api/invoices',
    fields: [
      {name: 'id', type: 'hidden'},
      {name: 'invoiceNo'},
      {name: 'issuedAt', type: 'date', pattern: 'YYYY-MM-DD'},
      {name: 'description'}
    ]
  }
}


/**
 * id
 * label
 * defaultValue
 * placeholder
 */
var TextInput = React.createClass({
  render: function() {
    return (
      <div className="form-group">
        <label htmlFor={this.props.id} className="col-sm-3 control-label">{this.props.label}</label>
        <div className="col-sm-9">
          <input type="text" className="form-control" id={this.props.id} placeholder={this.props.placeholder} />
        </div>
      </div>       
    );
  }
});

var Form = React.createClass({
  render: function() {
    return (
      <form className="form-horizontal">
        <div className="row">
          <div className="col-sm-3">
            <TextInput id="invoiceNo" label="Invoice No." placeholder="Invoice No." />
          </div>       

          <div className="col-sm-3">
            <TextInput id="amount" label="Amount" placeholder="0.00" />
          </div>       
        </div>
      </form> 
    );
  }
});

var InvoiceApp = React.createClass({

  render: function() {
    return (
      <div className="container">
        <Form />
      </div> 
    );
  }
});

module.exports = InvoiceApp;
