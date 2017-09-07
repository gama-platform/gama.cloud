(function() {
   'use strict';
 
   rap.registerTypeHandler("webopengl.WebGLComposite", {
 
       factory : function(properties) {
           return new webopengl.WebGLComposite(properties);
       },
 
       destructor : "destroy",
 
       properties : [ "abc" ],
 
       events : [],
 
       methods : [ 'appendWarn', 'appendErr', 'appendInfo', 'clearAll' ]
 
   });
 
   if (!window.webopengl) {
       window.webopengl = {};
   }
 
 
   // Constructor
   webopengl.WebGLComposite = function(properties) {
 
       bindAll(this, [ "layout", "onReady", "onSend", "onRender", "onChange" ]);// @custom
       this.parent = rap.getObject(properties.parent);
       this.element = document.createElement("div");
       this.parent.append(this.element);
       this.parent.addListener("Resize", this.layout);
 
       this.webgljs = new WebGLJS(this.element);
        
    
       // Render interface
       rap.on("render", this.onRender);
   };
 
   webopengl.WebGLComposite.prototype = {
 
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
           this.webgljs.appendErr(text);
       },
 
       appendWarn : function(json) {  
           var text= json["text"];
           this.webgljs.appendWarn(text);
       },
        
       appendInfo : function(json) {  
           var text= json["text"];
           this.webgljs.appendInfo(text);
       },
        
       clearAll : function()  {  
           this.webgljs.clearAll();
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