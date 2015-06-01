/*
  CrudTable configuration 
*/

/* Global variables */
var orderId = getParameterByName('orderId');  

var definition = {
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
    fields: [
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
  }
}

$.fn.serializeObject = function()
{
   var o = {};
   var a = this.serializeArray();
   $.each(a, function() {
       if (o[this.name]) {
           if (!o[this.name].push) {
               o[this.name] = [o[this.name]];
           }
           o[this.name].push(this.value || '');
       } else {
           o[this.name] = this.value || '';
       }
   });
   return o;
};

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
        case 'hidden':
          columns.push(
            <input type="hidden" id={field.name} name={field.name} />
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

  componentDidMount: function() {
    component = this
    // Attach a submit handler to the form
    $("#crudForm").submit(function(event) {
      // Stop form from submitting normally
      event.preventDefault();
      var data = JSON.stringify($('#crudForm').serializeObject());
      $.ajax({
        type: "POST",
        url: "/api/receivable/journal",
        contentType: "application/json",
        data: data,
        dataType: "text"
      }). 
      done(function(data) {
        $('#crudModal').modal('hide')
        component.props.onRowSubmit()
      });
    });
  },

  render: function() {    
    var width = 2
    var rows = []
    
    var counter = 0
    var columns = []
    for(var i = 0; i < this.props.definition.modal.fields.length; i++) {
      columns.push(this.props.definition.modal.fields[i])
      if(this.props.definition.modal.fields[i].type != 'hidden')
        counter++
      if(counter == width || i == this.props.definition.modal.fields.length - 1) {
        rows.push(<FieldRow columns={columns} width={12 / width} />)
        columns = []
        counter = 0
      }
   }

   return (
      <div className="modal fade" id="crudModal" tabindex="-1" role="dialog" aria-labelledby="hostModalLabel" aria-hidden="true">
        <div className="modal-dialog">
          <form id="crudForm">
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 className="modal-title" id="crudModalTitle"></h4>
              </div>
              <div className="modal-body">
                {rows}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-default" data-dismiss="modal"><FormattedMessage message={this.getIntlMessage('close')} /></button>
                <button type="submit" className="btn btn-success"><FormattedMessage message={this.getIntlMessage('save')} /></button>
              </div>
            </div>
          </form>
        </div>
      </div>    
    ); 
  } 
})

var AddButton = React.createClass({
  mixins: [IntlMixin],

  handleClick: function() {
    $('#crudModalTitle').text(this.getIntlMessage('add') + this.getIntlMessage(this.props.definition.modal.title))
    var orderId = getParameterByName('orderId');  
    $('#order').val(orderId); 
    $('#recognizedAt').val(moment().format('YYYY-MM-DD HH:mm'));
    $('#receivedAt').val(moment().format('YYYY-MM-DD HH:mm'));
    $('#invoicedAt').val(moment().format('YYYY-MM-DD HH:mm'));
  },

  render: function() {
    return (
      <a href="#" data-toggle="modal" data-target="#crudModal" onClick={this.handleClick}>
        <span className="glyphicon glyphicon-plus text-muted" aria-hidden="true"></span>
      </a>
    );
  }
})

var UpdateButton = React.createClass({
  mixins: [IntlMixin],

  handleClick: function() {
    var component = this
    
    if(this.props.row)
      this.props.definition.modal.fields.forEach(function(field) {
        if(field.value === undefined)
          $('#' + field.name).val(component.props.row[field.name]); 
        else {
          if(field.value == 'descendant')
            $('#' + field.name).val(component.props.row[field.name][field.pattern]); 
        }
      })
    $('#crudModalTitle').text(this.getIntlMessage('update') + this.getIntlMessage(this.props.definition.modal.title))
  },

  render: function() {
    return (
      <a href="#" data-toggle="modal" data-target="#crudModal" id={this.props.id} onClick={this.handleClick}>
        <span className={"glyphicon glyphicon-edit text-muted"} aria-hidden="true"></span>
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
    var value = getDescendantProperty(this.props.value, this.props.pattern)
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

var TableRow = React.createClass({
  render: function() {
    var component = this

    var cells = []
    cells.push(<td key='index'>{this.props.index + 1}</td>)  
    component.props.definition.columns.forEach(function(column) {
      var key = column.name
      switch (column.type) {
        case 'date':
          cells.push(<DateCell key={key} value={component.props.row[column.name]} pattern={column.pattern} />) 
          break;
        case 'descendant':
          cells.push(<DescendantCell key={key} value={component.props.row} pattern={column.pattern} />) 
          break;
        case 'icon':
          var color = column.color[component.props.row[column.name]]
          cells.push(<IconCell key={key} icon={column.icon} color={color} />) 
          break;
        case 'hidden':
          break;
        default:
          cells.push(<td key={key}>{component.props.row[column.name]}</td>)
      } 
    })
    cells.push(<td key='plus'><UpdateButton definition={this.props.definition} row={this.props.row} /></td>)  
    
    return ( 
      <tr>{cells}</tr>
    );
  }
})

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
    cells.push(<td></td>)

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
    cells.push(<td></td>)

    return ( 
      <tr>{cells}</tr>
    );
  }
})

var CrudTable = React.createClass({
  mixins: [IntlMixin],

  refresh: function() {
    $.get(this.props.definition.source, function(result) {
      if (this.isMounted()) {
        this.setState({
          data: result
        });
      }
    }.bind(this));
  },

  handleRowSubmit: function() {
    this.refresh()
  },

  getInitialState: function() {
    return {data: []};
  },

  componentDidMount: function() {
    this.refresh()
    $('#crudModal').on('hidden.bs.modal', function (e) {
      this.props.definition.columns.forEach(function(column) {
        $('#' + column.name).val(''); 
      })
    })    
  },

  render: function() {
    var component = this

    var heads = []
    heads.push(<th key='#'>#</th>)
    this.props.definition.columns.forEach(function(column) {
      if(column.type != 'hidden')
        heads.push(<th key={column.name}><FormattedMessage message={component.getIntlMessage(column.name)} /></th>)
    })
    heads.push(<th key='plus'><AddButton definition={component.props.definition} /></th>)

    var rows = [];
    this.state.data.forEach(function(row, index) {
      rows.push(<TableRow definition={component.props.definition} key={index} row={row} index={index} />);
    }) 
  
    rows.push(<SumRow data={this.state.data} />);
    rows.push(<DeductionRow data={this.state.data} />);

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
        <ModalForm definition={this.props.definition} onRowSubmit={this.handleRowSubmit} />
      </div>
    );
  }
})

React.render(
  <CrudTable definition={definition} {...i18n} />,
  document.getElementById('crudTable')
);
