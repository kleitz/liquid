
var JOURNALS = [
  {order: 'SA100001', qtyOfBox: '2', paidAmt: '1030.29'},
  {order: 'SA100021', qtyOfBox: '3', paidAmt: '1060.29'},
  {order: 'SA100051', qtyOfBox: '4', paidAmt: '1090.29'}
]

var fields = [
  {name: 'order', type: 'nest', child: 'orderNo'},
  {name: 'qtyOfBox'},
  {name: 'revenue'},
  {name: 'recognizedAt', type: 'date', pattern: 'YYYY-MM-DD'},
  {name: 'receivedAmt'},
  {name: 'receivedAt', type: 'date', pattern: 'YYYY-MM-DD'},
  {name: 'invoiceNo'},
  {name: 'invoicedAmt'},
  {name: 'invoicedAt', type: 'date', pattern: 'YYYY-MM-DD'}
]

var orderId = getParameterByName('orderId');  
var IntlMixin = ReactIntl.IntlMixin;
var FormattedMessage = ReactIntl.FormattedMessage;

// Update or Create
var UpdateButton = React.createClass({
  handleClick: function() {
    console.log(this.props.row)
    $("#qtyOfBox").val(this.props.row.qtyOfBox)
  },

  render: function() {
    return (
      <a href="#" data-toggle="modal" data-target="#crjModal" id={this.props.id} onClick={this.handleClick}>
        <span className={"glyphicon glyphicon-" + this.props.icon + " text-muted"} aria-hidden="true"></span>
      </a>
    );
  }
})

var NestCell = React.createClass({
  render: function() {
    return (
      <td key={this.props.key}><span>{this.props.value[this.props.child]}</span></td>
    );
  }
})

var DateCell = React.createClass({
  render: function() {
    return (
      <td key={this.props.key}><span>{moment(this.props.value).format(this.props.pattern)}</span></td>
    );
  }
})

var IconCell = React.createClass({
  render: function() {
    return (
      <td key={this.props.key}><span className={'glyphicon glyphicon-' + this.props.icon + ' ' + this.props.color} aria-hidden="true"></span></td>
    );
  }
})

var CrudRow = React.createClass({
  render: function() {
    var component = this

    var cells = []
    cells.push(<td key='index'>{this.props.index}</td>)  
    fields.forEach(function(field) {
      var key = field.name
      switch (field.type) {
        case 'date':
          cells.push(<DateCell key={key} value={component.props.row[field.name]} pattern={field.pattern} />) 
          break;
        case 'nest':
          cells.push(<NestCell key={key} value={component.props.row[field.name]} child={field.child} />) 
          break;
        case 'icon':
          var color = field.color[component.props.row[field.name]]
          cells.push(<IconCell key={key} icon={field.icon} color={color} />) 
          break;
        default:
          cells.push(<td key={key}>{component.props.row[field.name]}</td>)
      } 
    })
    cells.push(<td key='plus'><UpdateButton icon="edit" row={this.props.row} /></td>)  
    /*cells.push(<td key='minus'><DeleteButton host={this.props.host} /></td>)*/     
    
    return ( 
      <tr>{cells}</tr>
    );
        /*
        <td>{this.props.index + 1}</td>
        <td>{this.props.row.order.orderNo}</td> 
        <td>{this.props.row.qtyOfBox}</td> 
        <td>{this.props.row.revenue}</td> 
        <td>{moment(this.props.row.recognizedAt).format('YYYY-MM-DD')}</td> 
        <td>{this.props.row.receivedAmt}</td> 
        <td>{moment(this.props.row.receivedAt).format('YYYY-MM-DD')}</td> 
        <td>{this.props.row.invoiceNo}</td> 
        <td>{this.props.row.invoicedAmt}</td> 
        <td>{moment(this.props.row.invoicedAt).format('YYYY-MM-DD')}</td> 
        <td><UpdateButton icon="edit" id={"edit-" + this.props.index} row={this.props.row} /></td>
        */
  }
})

var CrudTable = React.createClass({
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
    var component = this

    var heads = []
    heads.push(<th key='#'>#</th>)
    fields.forEach(function(field) {
      heads.push(<th key={field.name}><FormattedMessage message={component.getIntlMessage(field.name)} /></th>)
    })
    heads.push(<th key='plus'><UpdateButton icon='plus' /></th>)

    var rows = [];
    this.state.data.forEach(function(row, index) {
      rows.push(<CrudRow key={index} row={row} index={index} />);
    }) 

    // FIXME - sum all crj. 
    // rows.push(<JournalRow journal={...} />);
    return (
      <table className="table table-striped table-hover table-condensed table-bordered table-16">
        <thead>
          <tr>{heads}</tr>
        </thead>

        <tbody>{rows}</tbody>
      </table>
    );
  }
})

React.render(
  <CrudTable source={'/api/receivable/journal?orderId=' + orderId} {...i18n} />,
  document.getElementById('journals')
);
