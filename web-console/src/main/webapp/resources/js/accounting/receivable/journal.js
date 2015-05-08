
var JOURNALS = [
    {order: 'SA100001', qtyOfBox: '2', paidAmt: '1030.29'},
    {order: 'SA100021', qtyOfBox: '3', paidAmt: '1060.29'},
    {order: 'SA100051', qtyOfBox: '4', paidAmt: '1090.29'}
]

var JournalRow = React.createClass({
    render: function() {
        return ( 
            <tr>
                <td>{this.props.journal.order}</td>
                <td>{this.props.journal.qtyOfBox}</td>
                <td>{this.props.journal.paidAmt}</td>
            </tr>
        );
    }
})

var JournalsTableBody = React.createClass({
    render: function() {
        var rows = [];
        this.props.journals.forEach(function(journal) {
            rows.push(<JournalRow journal={journal} />);
        }) 
        return (
            <tbody>{rows}</tbody>
        );
    }
})

React.render(
    <JournalsTableBody journals={JOURNALS} />,
    document.getElementById('journals')
);
