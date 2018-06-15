(function() {
   'use strict';
   console.log("Loaded! --> rap-handler.js");
   rap.registerTypeHandler("o7planning.EtherpadComposite", {
  	   factory : function(properties) {
    	   console.log(" --> rap.registerTypeHandler(o7planning.EtherpadComposite   -> from rap-handler.js");
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
 		   
       ready : false,
 
       onChange : function() {
    	//   console.log('onChanged!');
           
       },
 
       onReady : function() {
       },
 
      
       // Render interface in Client
       onRender : function() {
           if (this.element.parentNode) {
               rap.off("render", this.onRender);
                
               rap.on("render", this.onRender);
               rap.on("send", this.onSend);
           }
       },
 
       //  
       onSend : function() {
       },
 
       destroy : function() {
           rap.off("send", this.onSend);
           try {
               this.element.parentNode.removeChild(this.element);
           } catch (e) {
               try {
                   console
                           .log('error call this.element.parentNode.removeChild(this.element) :'
                                   + e);
               } catch (e) {
               }
           }
       },
 
       layout : function() {
           if (this.ready) {
               var area = this.parent.getClientArea();
               this.element.style.left = area[0] + "px";
               this.element.style.top = area[1] + "px";
               this.editor.resize(area[2], area[3]);
           }
       },
 
       setAbc : function(abc) {
 
       },
 
       appendErr : function(json) {  
           var text= json["text"];
           this.etherpadjs.appendErr(text);
       },
 
       appendWarn : function(json) {  
           var text= json["text"];
           this.etherpadjs.appendWarn(text);
       },
        
       appendInfo : function(json) {  
           var text= json["text"];
           var userId= json["userId"];
           this.etherpadjs.appendInfo(text, userId);
       },
       
       setText : function(json) {  
           var text= json["text"];
           var userId= json["userId"];
           var padId= json["padId"];
           var url=json["url"];
           this.etherpadjs.setText(text, userId, padId,url);
       },
      
       
       createAndMergeEditors : function(json) {  
           var text= json["text"];
           var userId= json["userId"];
           var padId= json["padId"];
           this.etherpadjs.createAndMergeEditors(text, userId, padId);
       },
       
       clearAll : function()  {  
           this.etherpadjs.clearAll();
            
       }
   };
 
   var bind = function(context, method) {
       return function() {
           return method.apply(context, arguments);
       };
   };
 
   var bindAll = function(context, methodNames) {
	   for (var i = 0; i < methodNames.length; i++) {
           var method = context[methodNames[i]];
           context[methodNames[i]] = bind(context, method);
       }
   };
 
   var async = function(context, func) {
	   
       window.setTimeout(function() {
           func.apply(context);
       }, 0);
   };
 
}());