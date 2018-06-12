(function() {
   'use strict';
   console.log("Loaded! --> rap-handler.js");
   rap.registerTypeHandler("o7planning.EtherpadComposite", {
   //rap.registerTypeHandler("ummisco.gama.participative.EtherpadBasicText", {
	   factory : function(properties) {
    	   console.log(" --> rap.registerTypeHandler(o7planning.EtherpadComposite   -> from rap-handler.js");
    	// return new ummisco.gama.participative.EtherpadBasicText(properties);
    	 return new o7planning.EtherpadComposite(properties);
       },
 
       destructor : "destroy",
 
       properties : [ "abc" ],
 
       events : [],
 
       methods : [ 'appendWarn', 'appendErr', 'appendInfo', 'setText', 'createAndMergeEditors', 'clearAll' ]
 
   });
 
   if (!window.o7planning) {
       window.o7planning = {};
   }
 
   // Constructor
   //ummisco.gama.participative.EtherpadBasicText = function(properties) {
   o7planning.EtherpadComposite = function(properties) {
 
       bindAll(this, [ "layout", "onReady", "onSend", "onRender", "onChange" ]);// @custom
       this.parent = rap.getObject(properties.parent);
       this.element = document.createElement("div");
       this.parent.append(this.element);
       this.parent.addListener("Resize", this.layout);
 
       this.etherpadjs = new EtherpadJS(this.element);
    
       // Render interface
       rap.on("render", this.onRender);
      
   };
  o7planning.EtherpadComposite.prototype = { 
  //ummisco.gama.participative.EtherpadBasicText = {
		   
       ready : false,
 
       onChange : function() {
    	//   console.log('onChanged!');
           
       },
 
       onReady : function() {
    	  // console.log('--> onReady!');
       },
 
      
       // Render interface in Client
       onRender : function() {
           if (this.element.parentNode) {
               rap.off("render", this.onRender);
                
               rap.on("render", this.onRender);
               rap.on("send", this.onSend);
           //    console.log('--> onRender!');
           }
           //alert('4');
       },
 
       //  
       onSend : function() {
    	//   console.log('--> onSend!');
       },
 
       destroy : function() {
           rap.off("send", this.onSend);
           try {
        	   //alert('6');
               this.element.parentNode.removeChild(this.element);
           } catch (e) {
               try {
                   console
                           .log('error call this.element.parentNode.removeChild(this.element) :'
                                   + e);
               } catch (e) {
               }
           }
           //alert('7');
       },
 
       layout : function() {
           if (this.ready) {
               var area = this.parent.getClientArea();
               this.element.style.left = area[0] + "px";
               this.element.style.top = area[1] + "px";
               this.editor.resize(area[2], area[3]);
               //alert('8');
           }
       },
 
       setAbc : function(abc) {
    	   //alert('9');
       },
 
       appendErr : function(json) {  
           var text= json["text"];
           this.etherpadjs.appendErr(text);
           //alert('10');
       },
 
       appendWarn : function(json) {  
           var text= json["text"];
           this.etherpadjs.appendWarn(text);
       //    console.log('--> appendWarn!'); 
       },
        
       appendInfo : function(json) {  
           var text= json["text"];
           var userId= json["userId"];
           this.etherpadjs.appendInfo(text, userId);
      //     console.log('--> appendInfo!'); 
       },
       
       setText : function(json) {  
           var text= json["text"];
           var userId= json["userId"];
           var padId= json["padId"];
           this.etherpadjs.setText(text, userId, padId);
          // alert('13');
        //   console.log('--> setText!   called with text : '+text); 
        //   console.log('--> setText!   called with userId : '+userId); 
        //   console.log('--> setText!   called with padId : '+padId); 
       },
      
       
       createAndMergeEditors : function(json) {  
           var text= json["text"];
           var userId= json["userId"];
           var padId= json["padId"];
           this.etherpadjs.createAndMergeEditors(text, userId, padId);
           //alert('13');
       },
       
       clearAll : function()  {  
           this.etherpadjs.clearAll();
            
       }
   };
 
   var bind = function(context, method) {
       return function() {
    	   //alert('15');
           return method.apply(context, arguments);
       };
   };
 
   var bindAll = function(context, methodNames) {
	   //alert('16');
       for (var i = 0; i < methodNames.length; i++) {
           var method = context[methodNames[i]];
           context[methodNames[i]] = bind(context, method);
       }
   };
 
   var async = function(context, func) {
	   
       window.setTimeout(function() {
           func.apply(context);
        //   console.log('--> async!');
       }, 0);
   };
 
}());