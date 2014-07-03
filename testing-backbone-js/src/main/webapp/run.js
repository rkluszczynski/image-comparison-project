var curl;
(function () {

	curl({
		main: 'hello',
		packages: {
			// Your application's package
			hello: { location: 'hello' },
			// Third-party packages
			curl: { location: '/webapp/public/curl/dist/curl' },
			jquery: { location: '/webapp/public/jquery/dist/jquery', main: '.' },
			Backbone: { location: '/webapp/public/backbone-amd/backbone', main: '.' },
			underscore: { location: '/webapp/public/lodash/dist/lodash', main: '.' }
		}
	});

}());
