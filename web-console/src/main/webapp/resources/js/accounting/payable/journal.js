/* Global variables */
var chargeId = getParameterByName('chargeId');  

var definition = {
  source: '/api/payable/journal?chargeId=' + chargeId,
  columns: [
    {name: 'appliedAt', type: 'date', pattern: 'YYYY-MM-DD'},
    {name: 'invoiceNo'},
    {name: 'currency'},
    {name: 'serviceProvider', type: 'descendant', pattern: 'charge.serviceProvider.name'},
    {name: 'paidAmt'},
    {name: 'paidAt', type: 'date', pattern: 'YYYY-MM-DD'}
  ],
  modal: {
    title: 'payable',
    fields: [
      {name: 'appliedAt', type: 'date', pattern: 'YYYY-MM-DD mm:ss'},
      {name: 'invoiceNo'},
      {name: 'currency'},
      {name: 'paidAmt'},
      {name: 'paidAt', type: 'date', pattern: 'YYYY-MM-DD mm:ss'}
    ]
  }
}

React.render(
  <CrudTable definition={definition} {...i18n} />,
  document.getElementById('crudTable')
);
