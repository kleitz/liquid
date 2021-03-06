/*
  CrudTable 
*/
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

var SelectField = React.createClass({
  mixins: [IntlMixin],

  getInitialState: function() {
    return {data: []};
  },

  componentDidMount: function() {
    console.log("options are exist", 'options' in this.props.field)
    if('options' in this.props.field) {
      if (this.isMounted()) {
        this.setState({
          data: this.props.field.options
        });
      }
    }
    else { 
      console.log("url", this.props.field.url)
      $.get(this.props.field.url, function(result) {
        if (this.isMounted()) {
          this.setState({
            data: result
          });
        }
      }.bind(this));
    }
  },

  render: function() {    
    var options = []    
    this.state.data.forEach(function(option) {
      options.push(<option value={option.id}>{option.name}</option>)
    })
    
    return (
      <div className="form-group" key={this.props.key}>
        <label for={this.props.name} className="control-label"><FormattedMessage message={this.getIntlMessage(this.props.name)} /></label>
        <select className="form-control input-sm" id={this.props.name} name={this.props.name}>
          {options} 
        </select>
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
        case 'select':
          columns.push(
            <div className={"col-xs-" + component.props.width}>
              <SelectField id={field.name} name={field.name} field={field} />
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

  handleSubmit: function(e) {
    component = this
    e.preventDefault();
    var data = JSON.stringify($('#crudForm').serializeObject());
    $.ajax({
      type: "POST",
      url: this.props.definition.modal.url,
      contentType: "application/json",
      data: data,
      dataType: "text"
    }). 
    done(function(data) {
      $('#crudModal').modal('hide')
      component.props.onRowSubmit()
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
          <form id="crudForm" onSubmit={this.handleSubmit}>
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 className="modal-title" id="crudModalTitle"></h4>
              </div>
              <div className="modal-body">{rows}</div>
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
    this.props.definition.modal.fields.forEach(function(field) {
      if('default' in field)
        $('#' + field.name).val(field['default']); 
      else {
        if(field.type == 'date') {
          $('#' + field.name).val(moment().format(field.pattern)); 
        }
      }
    })   
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
          if(field.value == 'descendant') {
            var value = getDescendantProperty(component.props.row, field.pattern)
            $('#' + field.name).val(value); 
          }
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
    
    if("modal" in this.props.definition) {
      cells.push(<td key='plus'><UpdateButton definition={this.props.definition} row={this.props.row} /></td>)  
    }
    
    return ( 
      <tr>{cells}</tr>
    );
  }
})

var CrudTable = React.createClass({
  mixins: [IntlMixin],

  refresh: function() {
    $.get(this.props.definition.source, function(result) {
      console.log("get: ", result)
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
    console.log('componentDidMount')
    component = this
    this.refresh()

    $('#crudModal').on('hidden.bs.modal', function (e) {
      component.props.definition.columns.forEach(function(column) {
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

    var modal = <div /> 
    if("modal" in this.props.definition) {
      heads.push(<th key='plus'><AddButton definition={component.props.definition} /></th>)
      modal = <ModalForm definition={this.props.definition} onRowSubmit={this.handleRowSubmit} />
    }
    console.log("modal:", modal)

    var rows = [];
    console.log("state:", this.state)
    this.state.data.forEach(function(row, index) {
      rows.push(<TableRow definition={component.props.definition} key={index} row={row} index={index} />);
    }) 
    
    if (typeof SumRow === "undefined") 
      console.log("SumRow is undefined.")
    else
      rows.push(<SumRow data={this.state.data} definition={component.props.definition} />);
    
    if (typeof DeductionRow === "undefined") 
      console.log("DecuctionRow is undefined.")
    else
      rows.push(<DeductionRow data={this.state.data} definition={component.props.definition} />);

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
        {modal}
      </div>
    );
  }
})

