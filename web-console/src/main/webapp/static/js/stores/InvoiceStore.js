var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var assign = require('object-assign');

var CHANGE_EVENT = 'change';

var InvoiceStore = assign({}, EventEmitter.prototype, {

  addChangeListener: function(callback) {
    this.on(CHANGE_EVENT, callback);
  },

  removeChangeListener: function(callback) {
    this.removeListener(CHANGE_EVENT, callback);
  },

  emitChange: function() {
    this.emit(CHANGE_EVENT);
  },

  findAll: function() {

  }
});

AppDispatcher.register(function(action) {
  switch(action.actionType) {
    case "ENTERING":

      break;
    case "VOIDING":
    
      break;
    default:
  }
});

module.exports = InvoiceStore;
