
function EtherpadJS(element)  {
	
   this.div = document.createElement("div");    
   this.div.className = 'etherpaddiv';    
   element.appendChild(this.div);
   this.div.innerHTML = "";
   function loadScript(url, callback)
   {
       // Adding the script tag to the head as suggested before
       var head = document.getElementsByTagName('head')[0];
       var script = document.createElement('script');
       script.type = 'text/javascript';
       script.src = url;

       // Then bind the event to the callback function.
       // There are several events for cross browser compatibility.
       script.onreadystatechange = callback;
       script.onload = callback;

       // Fire the loading
       head.appendChild(script);
   }
   var myPrettyCode = function() {

		$('#etherpaddiv').pad({'padId':'SKLAB'}); // sets the pad id and puts the pad in the div
		


	};
   this.appendInfo = function(text)  {        
	     // The most basic example
	   
	    console.log("test in appendInfo");
	    loadScript("http://code.jquery.com/jquery-latest.min.js",  myPrettyCode);
       this.div.innerHTML = this.div.innerHTML
        + "<p class='info'>"+ text +"</p>";

	   console.log("test appendInfo "+$('#etherpaddiv'));

   };
    
   this.appendErr = function(text)  {  
       this.div.innerHTML = this.div.innerHTML
        + "<p class='err'>ERROR: "+ text +"</p>";
   };
    
   this.appendWarn = function(text)  {        
       this.div.innerHTML = this.div.innerHTML
        + "<p class='warn'>WARN: "+ text +"</p>";
   };
                
   this.clearAll = function()  {  
       this.div.innerHTML = "";
   };
   
}

(function( $ ){
	  $.fn.pad = function( options ) {
	    var settings = {
	      'host'              : 'http://beta.etherpad.org',
	      'baseUrl'           : '/p/',
	      'showControls'      : false,
	      'showChat'          : false,
	      'showLineNumbers'   : false,
	      'userName'          : 'unnamed',
	      'lang'              : '',
	      'useMonospaceFont'  : false,
	      'noColors'          : false,
	      'userColor'         : false,
	      'hideQRCode'        : false,
	      'alwaysShowChat'    : false,
	      'width'             : 100,
	      'height'            : 100,
	      'border'            : 0,
	      'borderStyle'       : 'solid',
	      'toggleTextOn'      : 'Disable Rich-text',
	      'toggleTextOff'     : 'Enable Rich-text',
	      'plugins'           : {},
	      'rtl'               : false
	    };


	    var $self = this;
	    if (!$self.length) return;
	    if (!$self.attr('id')) throw new Error('No "id" attribute');
	    
	    var useValue = $self[0].tagName.toLowerCase() == 'textarea';
	    var selfId = $self.attr('id');
	    var epframeId = 'epframe'+ selfId;
	    // This writes a new frame if required
	    if ( !options.getContents ) {
	      if ( options ) {
	        $.extend( settings, options );
	      }
	      
	      var pluginParams = '';
	      for(var option in settings.plugins) {
	        pluginParams += '&' + option + '=' + settings.plugins[option]
	      }

	      var iFrameLink = '<iframe id="'+epframeId;
	          iFrameLink = iFrameLink +'" name="' + epframeId;
	          iFrameLink = iFrameLink +'" src="' + settings.host+settings.baseUrl+settings.padId;
	          iFrameLink = iFrameLink + '?showControls=' + settings.showControls;
	          iFrameLink = iFrameLink + '&showChat=' + settings.showChat;
	          iFrameLink = iFrameLink + '&showLineNumbers=' + settings.showLineNumbers;
	          iFrameLink = iFrameLink + '&useMonospaceFont=' + settings.useMonospaceFont;
	          iFrameLink = iFrameLink + '&userName=' + settings.userName;
	          if (settings.lang) {
	            iFrameLink = iFrameLink + '&lang=' + settings.lang;
	          }
	          iFrameLink = iFrameLink + '&noColors=' + settings.noColors;
	          iFrameLink = iFrameLink + '&userColor=' + settings.userColor;
	          iFrameLink = iFrameLink + '&hideQRCode=' + settings.hideQRCode;
	          iFrameLink = iFrameLink + '&alwaysShowChat=' + settings.alwaysShowChat;
	          iFrameLink = iFrameLink + '&rtl=' + settings.rtl;
	          iFrameLink = iFrameLink + pluginParams;
	          iFrameLink = iFrameLink +'" style="border:' + settings.border;
	          iFrameLink = iFrameLink +'; border-style:' + settings.borderStyle;
	          iFrameLink = iFrameLink +';" width="' + '100%';//settings.width;
	          iFrameLink = iFrameLink +'" height="' + settings.height; 
	          iFrameLink = iFrameLink +'"></iframe>';
	      
	      
	      var $iFrameLink = $(iFrameLink);
	      
	      if (useValue) {
	        var $toggleLink = $('<a href="#'+ selfId +'">'+ settings.toggleTextOn +'</a>').click(function(){
	          var $this = $(this);
	          $this.toggleClass('active');
	          if ($this.hasClass('active')) $this.text(settings.toggleTextOff);
	          $self.pad({getContents: true});
	          return false;
	        });
	        $self
	          .hide()
	          .after($toggleLink)
	          .after($iFrameLink)
	        ;
	      }
	      else {      
	        $self.html(iFrameLink);
	      }
	    }

	    // This reads the etherpad contents if required
	    else {
	      var frameUrl = $('#'+ epframeId).attr('src').split('?')[0];
	      var contentsUrl = frameUrl + "/export/html";
	      var target = $('#'+ options.getContents);

	      // perform an ajax call on contentsUrl and write it to the parent
	      $.get(contentsUrl, function(data) {
	        
	        if (target.is(':input')) {
	          target.val(data).show();
	        }
	        else {
	          target.html(data);
	        }
	        
	        $('#'+ epframeId).remove();
	      });
	    }
	    
	    
	    return $self;
	  };
	})( jQuery );

