
function EtherpadJS(element)  {
	/**/
   console.log("Create the Div element");
   var passage = 0;
   var divClassName ="ace_layer ace_text-layer";
   var nbr = document.getElementsByClassName(divClassName).length;
   console.log("There is "+nbr+" Elements of "+ divClassName);
   
   this.div = document.createElement("div");    
   // this.div.className = "etherpaddiv";
   this.div.className = "ace_layer ace_text-layer";
   this.div.setAttribute("id","etherpaddiv");
   
 
 /*  */
/*
	
	
*/
/*
   alert(document.getElementsByClassName("ace_layer ace_text-layer").length)
	this.div = document.getElementsByClassName('ace_layer ace_text-layer')[0];
   console.log("Good! The document is ");
   this.div[0].setAttribute("id","etherpaddiv");
    console.log("Get the dic id "+ this.div[0].getAttribute("id"));
   */
   
   var userId ='';
   var padId='';

   element.appendChild(this.div);
   this.div.innerHTML = "";
   function loadScript(url, callback)
   {
	   console.log("(etherpad.js) Call from -->     loadScript");
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

	   console.log("(etherpad.js) Call from -->     myPrettyCode");
	   (function( $ ){

		   $.fn.pad = function( options ) {
		     var settings = {
		       //'host'              : 'http://beta.etherpad.org',
		    	'host'              : 'http://127.0.0.1:9001',
		       'baseUrl'           : '/p/',
		       'showControls'      : false,
		       'showChat'          : true,
		       'showLineNumbers'   : true,
		      // 'userName'          : 'admin',
		       'userName'          : userId,
		       'lang'              : 'en',
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

	   
//	    $('#etherpaddiv').pad({'padId':'SKLAB','height':500,'showChat':'true', 'showLineNumbers':'true'}); 
//	    $('#etherpaddiv').pad({'padId':'SKLAB','height':500,'showChat':'true'}); 
	   // $('#etherpaddiv').pad({'padId':padId,'height':500,'showChat':'true'});
	    $('#etherpaddiv').pad({'padId':padId,'height':800,'showChat':'true'});

	    

	};

	//   loadScript("http://code.jquery.com/jquery-latest.min.js",   myPrettyCode);
   this.appendInfo = function(text, user)  { 
	   console.log("(etherpad.js) Call from -->    appendInfo ");
	     // The most basic example
	   	userId = user;
	    console.log("Test in appendInfo user Id (etherpad.js): "+ userId);
	   // 
	 //  loadScript("http://code.jquery.com/jquery-latest.min.js",   myPrettyCode);
       this.div.innerHTML = this.div.innerHTML
        + "<p class='info'>"+ text +"</p>";

     
	   

   };
   
this.setText = function(text, user, pad)  { 
	 console.log("(etherpad.js) Call from -->     setText");
	 
	 nbr = document.getElementsByClassName(divClassName).length;
	 if(nbr > 1 ){
		 
		 console.log(" There are 2, let's delete it ");
		 this.div = document.getElementsByClassName(divClassName)[1]; 
		 console.log("Got it ");
		 this.div.parentNode.removeChild(this.div);
		 console.log(" Deleted it");
		 this.div = document.getElementsByClassName(divClassName)[0]; 	
		 console.log(" Got the second");
		 passage = 1;
	 }
	 
	 if(passage == 1 ){
		 console.log("-------------------_>>>>>>-- Il faut modifier ici ");
		 this.div = document.getElementsByClassName(divClassName)[0]; 
		 var elt  = document.getElementsByClassName("ace_content")[0]; 
		 //element.appendChild(this.div);
		 elt.appendChild(this.div);
		 // this.div.innerHTML = "";
		 passage = 2;
	 }
	 
	 if(passage ==2){
		 console.log("------------------->>>>>>-- Il faire une translation ici ");
		 var eltText = document.getElementsByClassName(divClassName)[0];
		 var textValue = eltText.innerText || eltText.textContent;
		 console.log("------------------------------ Content  - ");
		 console.log("The content length is: "+textValue.length);
		 console.log("------------------------------ End Content - ");
	 }
	   
	   nbr = document.getElementsByClassName(divClassName).length;
	   console.log("It remaiins "+nbr+" Elements of "+ divClassName);
	   
	 // The most basic example
	 userId = user;
	 padId = pad;
	 //this.div.innerHTML = "";
	 console.log("Test in setText -> (etherpad.js) user Id : "+ userId+ " and pad : "+pad);
	 
	 loadScript("http://code.jquery.com/jquery-latest.min.js",   myPrettyCode);
     this.div.innerHTML = this.div.innerHTML
      + "<p class='info'>"+ text +"</p>";

     console.log("Set the Text from (etherpad.js)");
     

     //var nbr = document.getElementsByClassName("gr__localhost").length;
     var nbr = document.getElementsByClassName(divClassName).length;
     console.log("There is Elements: "+nbr);
     
 };
 
 
 this.createAndMergeEditors = function(text, user, pad)  { 
	 console.log("(etherpad.js) Call from -->     createAndMergeEditors");
     // The most basic example
 userId = user;
 padId = pad;
 console.log("Test in createMergeEditors -> (etherpad.js) user Id : "+ userId+ " and pad : "+pad);
 
 	console.log("Get the Div element with class name: ace_layer ace_text-layer");
 	var nbr = document.getElementsByClassName("ace_layer ace_text-layer").length;
    console.log("There is "+nbr +" Elements with class name = ace_layer ace_text-layer");
	this.div = document.getElementsByClassName('ace_layer ace_text-layer')[0];   
//	this.div.setAttribute("id","etherpaddiv");
 
	

 console.log("Set the Text in the inserted Etherpad Editor from (etherpad.js)");
 
 loadScript("http://code.jquery.com/jquery-latest.min.js",   myPrettyCode);
	this.div.innerHTML = this.div.innerHTML
+ "<p class='info'>"+ text +"</p>";

 
};
    
   this.appendErr = function(text)  {  
	   console.log("(etherpad.js) Call from -->     appendErr");
       this.div.innerHTML = this.div.innerHTML
        + "<p class='err'>ERROR: "+ text +"</p>";
   };
    
   this.appendWarn = function(text)  {
	   console.log("(etherpad.js) Call from -->     appendWarn");
       this.div.innerHTML = this.div.innerHTML
        + "<p class='warn'>WARN: "+ text +"</p>";
   };
                
   this.clearAll = function()  { 
	   console.log("(etherpad.js) Call from -->     clearAll");
       this.div.innerHTML = "";
   };
   
}



