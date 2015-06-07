/* Global variables */
var spId = getParameterByName('spId');  

var definition = {
  source: '/api/payable?spId=' + spId,
  columns: [
    {name: 'appliedAt', type: 'date', pattern: 'YYYY-MM-DD'},
    {name: 'invoiceNo'},
    {name: 'currency'},
    {name: 'serviceProvider', type: 'descendant', pattern: 'charge.sp.name'},
    {name: 'paidAmt'},
    {name: 'paidAt', type: 'date', pattern: 'YYYY-MM-DD'}
  ]
}

var SumRow = React.createClass({
  mixins: [IntlMixin],
  
  render: function() {
    var paidSum = 0
    this.props.data.forEach(function(row) {
      paidSum += row['paidAmt']
    })
    var cells = []
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td></td>)
    cells.push(<td><b><FormattedMessage message={this.getIntlMessage('revenueSum')} /></b></td>)
    cells.push(<td><b>{parseFloat(paidSum).toFixed(2)}</b></td>)
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
