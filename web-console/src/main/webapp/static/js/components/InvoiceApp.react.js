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
  ]
}

var InvoiceApp = React.createClass({

  render: function() {
    return (
      <CrudTable definition={definition} {...i18n} />
    );
  }
});

module.exports = InvoiceApp;
