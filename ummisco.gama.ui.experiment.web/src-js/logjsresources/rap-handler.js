(function() {
   'use strict';
 
   rap.registerTypeHandler("o7planning.LogComposite", {
 
       factory : function(properties) {
           return new o7planning.LogComposite(properties);
       },
 
       destructor : "destroy",
 
       properties : [ "abc" ],
 
       events : [],
 
       methods : [ 'appendWarn', 'appendErr', 'appendInfo', 'clearAll' ]
 
   });
 
   if (!window.o7planning) {
       window.o7planning = {};
   }
 
 
   // Constructor
   o7planning.LogComposite = function(properties) {
 
       bindAll(this, [ "layout", "onReady", "onSend", "onRender", "onChange" ]);// @custom
       this.parent = rap.getObject(properties.parent);
       this.element = document.createElement("div");
       this.parent.append(this.element);
       this.parent.addListener("Resize", this.layout);
 
       this.logjs = new LogJS(this.element);
        
    
       // Render interface
       rap.on("render", this.onRender);
   };
 
   o7planning.LogComposite.prototype = {
 
       ready : false,
 
       onChange : function() {
            
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
           this.logjs.appendErr(text);
       },
 
       appendWarn : function(json) {  
           var text= json["text"];
           this.logjs.appendWarn(text);
       },
        
       appendInfo : function(json) {  
           var text= json["text"];
           this.logjs.appendInfo(text);
       },
        
       clearAll : function()  {  
           this.logjs.clearAll();
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