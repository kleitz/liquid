/*
  CrudTable configuration 
*/
var fields = [
  {name: 'order', type: 'descendant', path: 'orderNo'},
  {name: 'qtyOfBox'},
  {name: 'revenue'},
  {name: 'recognizedAt', type: 'date', pattern: 'YYYY-MM-DD'},
  {name: 'receivedAmt'},
  {name: 'receivedAt', type: 'date', pattern: 'YYYY-MM-DD'},
  {name: 'invoiceNo'},
  {name: 'invoicedAmt'},
  {name: 'invoicedAt', type: 'date', pattern: 'YYYY-MM-DD'}
]
var modalTitle = 'crj'

/* Global variables */
var orderId = getParameterByName('orderId');  

var IntlMixin = ReactIntl.IntlMixin;
var FormattedMessage = ReactIntl.FormattedMessage;

var TextField = React.createClass({
  mixins: [IntlMixin],

  render: function() {    
    return (
      <div className="form-group" key={this.props.key}>
        <label for={this.props.name} className="control-label"><FormattedMessage message={this.getIntlMessage(this.props.name)} /></label>
        <input type="text" className="form-control input-sm" id={this.props.name} name={this.props.name}></input>
      </div>
    );
  }
})

var DateField = React.createClass({
  mixins: [IntlMixin],

  componentDidMount: function() {
    $('#' + this.props.name + 'Picker').dtPicker()
  },
 
  render: function() {
    return (
      <div className="form-group" key={this.props.key}>
        <label for={this.props.name} className="control-label"><FormattedMessage message={this.getIntlMessage(this.props.name)} /></label>
        <div className="input-group input-group-sm date" id={this.props.name + "Picker"}>
          <input type="text" data-date-format="YYYY-MM-DD HH:mm" className="form-control" id={this.props.name} name={this.props.name} />
          <span className="input-group-addon">
            <span className="glyphicon glyphicon-calendar"></span>
          </span>
        </div>        
      </div>
    );
  }
})

var FieldRow = React.createClass({
  render: function() {    
    var component = this
    var columns = []
    this.props.columns.forEach(function(field) {
      var key = field.name
      switch (field.type) {
        case 'date':
          columns.push(
            <div className={"col-xs-" + component.props.width}>
              <DateField key={field.name} name={field.name} />
            </div>
          ) 
          break;
        default:
          columns.push(
            <div className={"col-xs-" + component.props.width}>
              <TextField key={field.name} name={field.name} />
            </div>
          ) 
      } 
    })

    return (
      <div className="row">{columns}</div>
    );
  }
})

var ModalForm = React.createClass({
  mixins: [IntlMixin],

  render: function() {    
    var width = 2
    var rows = []
    var quotient = Math.floor(fields.length / width) 
    var remainder = fields.length % width 

    for (var i = 0; i < quotient; i++) {
      var columns = []
      for (var j = 0; j < width; j++) {
        columns.push(fields[i * width + j])
      } 
      rows.push(<FieldRow columns={columns} width={12 / width} />)
    }

    if (remainder > 0) {
      var columns = []
      for (var j = 0; j < remainder; j++) {
        columns.push(fields[quotient * width + j])
      } 
      rows.push(<FieldRow columns={columns} width={12 / width} />)
    }
    
    return (
      <div className="modal fade" id="modalForm" tabindex="-1" role="dialog" aria-labelledby="hostModalLabel" aria-hidden="true">
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 className="modal-title" id="modalTitle"></h4>
            </div>
            <div className="modal-body">
              {rows}
            </div>
            <div className="modal-footer">
              <button type="button" className="btn btn-default" data-dismiss="modal"><FormattedMessage message={this.getIntlMessage('close')} /></button>
              <button type="button" className="btn btn-success"><FormattedMessage message={this.getIntlMessage('save')} /></button>
            </div>
          </div>
        </div>
      </div>    
    ); 
  } 
})

// Update or Create
var UpdateButton = React.createClass({
  mixins: [IntlMixin],

  handleClick: function() {
    var component = this
    
    if(this.props.row)
      fields.forEach(function(field) {
        $('#' + field.name).val(component.props.row[field.name]); 
      })

    var message = 'add'
    if (this.props.type == 'update') {
      message = 'update' 
    }
    $('#modalTitle').text(this.getIntlMessage(message) + this.getIntlMessage(modalTitle))    
  },

  render: function() {
    var icon = 'plus';
    if (this.props.type == 'update') {
      icon = 'edit'
    }
    
    return (
      <a href="#" data-toggle="modal" data-target="#modalForm" id={this.props.id} onClick={this.handleClick}>
        <span className={"glyphicon glyphicon-" + icon + " text-muted"} aria-hidden="true"></span>
      </a>
    );
  }
})

var DescendantCell = React.createClass({
  render: function() {
    function getDescendantProperty(obj, path) {
      var descendantProps = path.split(".");
      while(descendantProps.length && (obj = obj[descendantProps.shift()]));
      return obj;
    } 
    var value = getDescendantProperty(this.props.value, this.props.path)
    return (
      <td key={this.props.key}><span>{value}</span></td>
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
        case 'descendant':
          cells.push(<DescendantCell key={key} value={component.props.row[field.name]} path={field.path} />) 
          break;
        case 'icon':
          var color = field.color[component.props.row[field.name]]
          cells.push(<IconCell key={key} icon={field.icon} color={color} />) 
          break;
        default:
          cells.push(<td key={key}>{component.props.row[field.name]}</td>)
      } 
    })
    cells.push(<td key='plus'><UpdateButton type="update" row={this.props.row} /></td>)  
    /*cells.push(<td key='minus'><DeleteButton host={this.props.host} /></td>)*/     
    
    return ( 
      <tr>{cells}</tr>
    );
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
    heads.push(<th key='plus'><UpdateButton type='add' /></th>)

    var rows = [];
    this.state.data.forEach(function(row, index) {
      rows.push(<CrudRow key={index} row={row} index={index} />);
    }) 

    // FIXME - sum all crj. 
    // rows.push(<JournalRow journal={...} />);
    return (
      <div>
        <table className="table table-striped table-hover table-condensed table-bordered table-16">
          <thead>
            <tr>{heads}</tr>
          </thead>
  
          <tbody>{rows}</tbody>
        </table>
        <ModalForm />
      </div>
    );
  }
})

React.render(
  <CrudTable source={'/api/receivable/journal?orderId=' + orderId} {...i18n} />,
  document.getElementById('journals')
);
