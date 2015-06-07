/* Global variables */
var orderId = getParameterByName('orderId');  

var definition = {
  orderId: orderId,
  source: '/api/receivable/journal?orderId=' + orderId,
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
  ],
  modal: {
    title: 'crj',
    url: '/api/receivable/journal',
    fields: [
      {name: 'id', type: 'hidden'},
      {name: 'order', type: 'hidden', value: 'descendant', pattern: 'order.id', default: orderId},
      {name: 'qtyOfBox'},
      {name: 'revenue'},
      {name: 'recognizedAt', type: 'date', pattern: 'YYYY-MM-DD mm:ss'},
      {name: 'receivedAmt'},
      {name: 'receivedAt', type: 'date', pattern: 'YYYY-MM-DD mm:ss'},
      {name: 'invoiceNo'},
      {name: 'invoicedAmt'},
      {name: 'invoicedAt', type: 'date', pattern: 'YYYY-MM-DD mm:ss'}
    ]
  }
}

var SumRow = React.createClass({
  mixins: [IntlMixin],
  
  render: function() {
    var revenueSum = 0
    var receivedSum = 0
    var invoicedSum = 0
    this.props.data.forEach(function(row) {
      revenueSum += row['revenue']
      receivedSum += row['receivedAmt']
      invoicedSum += row['invoicedAmt']
    })
    var cells = []
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td><b><FormattedMessage message={this.getIntlMessage('revenueSum')} /></b></td>)
    cells.push(<td><b>{parseFloat(revenueSum).toFixed(2)}</b></td>)
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td><b><FormattedMessage message={this.getIntlMessage('receivedSum')} /></b></td>)
    cells.push(<td><b>{parseFloat(receivedSum).toFixed(2)}</b></td>)
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td><b><FormattedMessage message={this.getIntlMessage('invoicedSum')} /></b></td>)
    cells.push(<td><b>{parseFloat(invoicedSum).toFixed(2)}</b></td>)
    cells.push(<td></td>)
    if("modal" in this.props.definition) {
      cells.push(<td></td>)
    }

    return ( 
      <tr>{cells}</tr>
    );
  }
})

var DeductionRow = React.createClass({
  mixins: [IntlMixin],
  
  render: function() {
    var revenueSum = 0
    var receivedSum = 0
    var invoicedSum = 0
    this.props.data.forEach(function(row) {
      revenueSum += row['revenue']
      receivedSum += row['receivedAmt']
      invoicedSum += row['invoicedAmt']
    })
    var cells = []
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td><b><FormattedMessage message={this.getIntlMessage('notReceivedRevenue')} /></b></td>)
    cells.push(<td><b>{parseFloat(invoicedSum - receivedSum).toFixed(2)}</b></td>)
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td><b><FormattedMessage message={this.getIntlMessage('notReceivedSum')} /></b></td>)
    cells.push(<td><b>{parseFloat(invoicedSum - receivedSum).toFixed(2)}</b></td>)
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td><b><FormattedMessage message={this.getIntlMessage('notInvoicedSum')} /></b></td>)
    cells.push(<td><b>{parseFloat(revenueSum - invoicedSum).toFixed(2)}</b></td>)
    cells.push(<td></td>)
    if("modal" in this.props.definition) {
      cells.push(<td></td>)
    }

    return ( 
      <tr>{cells}</tr>
    );
  }
})

React.render(
  <CrudTable definition={definition} {...i18n} />,
  document.getElementById('crudTable')
);
