
	console.log('Loading Javscript translatror ...');
	
	/*
   var head = document.getElementsByTagName('head')[0];
   var changeScript = document.createElement('script');
   changeScript.type = 'text/javascript';
   changeScript.src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js";
   head.appendChild(changeScript);
   */
//(function () {
        var ev = new $.Event('remove'),   orig = $.fn.remove;
        var evap = new $.Event('append'), origap = $.fn.append;
		var evch = new $.Event('change'), origch = $.fn.change;
        var evck = new $.Event('click'),  origck = $.fn.click;


        $.fn.remove = function () { $(this).trigger(ev);   return orig.apply(this, arguments);   }
        $.fn.append = function () { $(this).trigger(evap); return origap.apply(this, arguments); }
        $.fn.change = function () { $(this).trigger(evch); return origch.apply(this, arguments); }
        $.fn.click = function  () { $(this).trigger(evck); return origck.apply(this, arguments); } 

 //   })();

    $(document).on('append', function (e) { alert('Append occured'); });
    $(document).on('remove', function (e) { alert('Remove occured'); });
    $(document).on('change', function (e) { alert('Change occured'); });
	$(document).on('click', function (e) { alert('Click occured');   });
	
	console.log('Javscript translatror loaded');
	
	
	function changeHtml(selector, html) {
    var elem = $(selector);
    	jQuery.event.trigger('htmlchanging', { elements: elem, content: { current: elem.html(), pending: html} });
    	elem.html(html);
    	jQuery.event.trigger('htmlchanged', { elements: elem, content: html });
	}
	
	$(document).bind('htmlchanging', function (e, data) {
	    //your before changing html, logic goes here
		console.log('Before changing');
	});

	$(document).bind('htmlchanged', function (e, data) {
	    //your after changed html, logic goes here
		console.log('After changing');
	});
	
	
	function translate() {
		console.log('This is from EtherToGaml');
	}