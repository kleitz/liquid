/* Global variables */
var customerId = getParameterByName('customerId');  

var definition = {
  customerId: customerId,
  source: '/api/receivable?customerId=' + customerId,
  columns: [
    {name: 'id', type: 'hidden'},
    {name: 'order', type: 'descendant', pattern: 'order.orderNo'},
    {name: 'qtyOfBox'},
    {name: 'revenue'},
    {name: 'recognizedAt', type: 'date', pattern: 'YYYY-MM-DD'},
    {name: 'goods', type: 'descendant', pattern: 'order.goods.name'},
    {name: 'receivableOrg', type: 'descendant', pattern: 'order.customer.name'},
    {name: 'receivedAmt'},
    {name: 'receivedAt', type: 'date', pattern: 'YYYY-MM-DD'},
    {name: 'invoiceNo'},
    {name: 'invoiceTo', type: 'descendant', pattern: 'order.customer.name'},
    {name: 'invoicedAmt'},
    {name: 'invoicedAt', type: 'date', pattern: 'YYYY-MM-DD'}
  ]
}

React.render(
  <CrudTable definition={definition} {...i18n} />,
  document.getElementById('crudTable')
);
