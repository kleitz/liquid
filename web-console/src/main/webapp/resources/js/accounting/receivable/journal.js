
var JOURNALS = [
  {order: 'SA100001', qtyOfBox: '2', paidAmt: '1030.29'},
  {order: 'SA100021', qtyOfBox: '3', paidAmt: '1060.29'},
  {order: 'SA100051', qtyOfBox: '4', paidAmt: '1090.29'}
]

var orderId = getParameterByName('orderId');  
var IntlMixin = ReactIntl.IntlMixin;
var FormattedMessage = ReactIntl.FormattedMessage;

var JournalModal = React.createClass({
  render: function() {
    return ('');
  }
})

var JournalButton = React.createClass({
  handleClick: function() {
    console.log(this.props.journal)
    $("#qtyOfBox").val(this.props.journal.qtyOfBox)
  },

  render: function() {
    return (
      <a href="#" data-toggle="modal" data-target="#crjModal" id={this.props.id} onClick={this.handleClick}>
        <span className={"glyphicon glyphicon-" + this.props.icon + " text-muted"} aria-hidden="true"></span>
      </a>
    );
  }
})

var JournalRow = React.createClass({
  render: function() {
    return ( 
      <tr>
        <td>{this.props.index + 1}</td>
        <td>{this.props.journal.order.orderNo}</td> 
        <td>{this.props.journal.qtyOfBox}</td> 
        <td>{this.props.journal.revenue}</td> 
        <td>{moment(this.props.journal.recognizedAt).format('YYYY-MM-DD')}</td> 
        <td>{this.props.journal.receivedAmt}</td> 
        <td>{moment(this.props.journal.receivedAt).format('YYYY-MM-DD')}</td> 
        <td>{this.props.journal.invoiceNo}</td> 
        <td>{this.props.journal.invoicedAmt}</td> 
        <td>{moment(this.props.journal.invoicedAt).format('YYYY-MM-DD')}</td> 
        <td><JournalButton icon="edit" id={"edit-" + this.props.index} journal={this.props.journal} /></td>
      </tr>);
  }
})

var JournalsTableBody = React.createClass({
  mixins: [IntlMixin],

  getInitialState: function() {
    return {data: []};
  },

  componentDidMount: function() {
    $.get(this.props.source, function(result) {
      if (this.isMounted()) {
        this.setState({
          data: result
        });
      }
    }.bind(this));
  },

  render: function() {
    var rows = [];
    this.state.data.forEach(function(journal, index) {
      rows.push(<JournalRow journal={journal} index={index} />);
    }) 
    // FIXME - sum all crj. 
    // rows.push(<JournalRow journal={...} />);
    return (
      <table className="table table-striped table-hover table-condensed table-bordered table-16">
        <thead>
          <tr>
            <th>#</th>
            <th><FormattedMessage message={this.getIntlMessage('order')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('qty_of_box')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('revenue')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('recognized_at')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('received_amt')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('received_at')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('invoice_no')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('invoiced_amt')} /></th>
            <th><FormattedMessage message={this.getIntlMessage('invoiced_at')} /></th>
            <th><JournalButton icon="plus" /></th>
          </tr>
        </thead>

        <tbody>{rows}</tbody>
      </table>
    );
  }
})

React.render(
  <JournalsTableBody source={'/api/receivable/journal?orderId=' + orderId} {...i18n} />,
  document.getElementById('journals')
);
