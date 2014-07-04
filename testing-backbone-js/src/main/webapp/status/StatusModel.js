define(function(require) {
	var Backbone = require('Backbone');

	return Backbone.Model.extend({
		urlRoot: 'http://localhost:8080/webapp/processing',
		url: function() {
			return this.urlRoot;
		}
	});
});
