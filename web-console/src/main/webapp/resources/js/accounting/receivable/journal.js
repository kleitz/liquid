/* Global variables */
var orderId = getParameterByName('orderId');  

var COLUMNS = [
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

var FIELDS = [
  {name: 'id', type: 'hidden'},
  {name: 'order', type: 'hidden', value: 'descendant', pattern: 'id'},
  {name: 'qtyOfBox'},
  {name: 'revenue'},
  {name: 'recognizedAt', type: 'date', pattern: 'YYYY-MM-DD'},
  {name: 'receivedAmt'},
  {name: 'receivedAt', type: 'date', pattern: 'YYYY-MM-DD'},
  {name: 'invoiceNo'},
  {name: 'invoicedAmt'},
  {name: 'invoicedAt', type: 'date', pattern: 'YYYY-MM-DD'}
]

$.get('/api/receivable/journal?orderId=' + orderId, function(data) {
  React.render(
    <CrudTable columns={COLUMNS} data={data} modalTitle="crj" fields={FIELDS} {...i18n} />,
    document.getElementById('crudTable')
  );
}.bind(this));


