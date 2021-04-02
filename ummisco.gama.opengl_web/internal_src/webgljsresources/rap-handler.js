
(function() {
	'use strict';

	rap.registerTypeHandler("webopengl.WebGLComposite", {

		factory : function(properties) {
			return new webopengl.WebGLComposite(properties);
		},

		destructor : "destroy",

		properties : [ "abc" ],

		events : [],

		methods : [ 'appendWarn', 'appendErr', 'appendInfo', 
			'glDrawElements',  
			'glFrontFace', 
			'glBindBuffer', 
			'glClearColor',
			'glClear',
			'glEnable',
			'glDepthFunc',
			'glBufferData', 
			'glGenFramebuffers',
			'glGenBuffers',
			'glBindFramebuffer',
			'glViewport',
			'glDrawBuffer',
			'glGenRenderbuffers',
			'glRenderbufferStorage',
			'glBindRenderbuffer',
			'glFramebufferRenderbuffer',
			'glEnableVertexAttribArray',
			'glBufferSubData',
			'totalObject',
			'webgl_destroy',
			'clearAll' 
			]

	});

	if (!window.webopengl) {
		window.webopengl = {};
	}

	// Constructor
	webopengl.WebGLComposite = function(properties) {

		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "onChange" ]);// @custom
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("div");

		this.element.style.width = this.parent.getClientArea()[2] + "px";
		this.element.style.height = this.parent.getClientArea()[3] + "px";

		this.parent.append(this.element);
		this.parent.addListener("Resize", this.layout);

		// console.log("this.element.style.width " + this.element.style.width );
		this.webgljs = new WebGLJS(this.parent,this.element);

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

			glDrawElements : function(json) {
				var glTriangles = json["glTriangles"];
				var i = json["i"];
				var glUnsignedInt = json["glUnsignedInt"];
				var j = json["j"];
				
				this.webgljs.glDrawElements(glTriangles, i, glUnsignedInt, j);
				this.layout();
			},
			

			

			glBindBuffer : function(json) {
				var glElementArrayBuffer = json["glElementArrayBuffer"];
				var i = json["i"];

				this.webgljs.glBindBuffer(glElementArrayBuffer,i);
			},

			glRenderbufferStorage : function(json) {
				var glRenderbuffer = json["glRenderbuffer"];
				var glDepthComponent = json["glDepthComponent"];
				var width = json["width"];
				var height = json["height"];

				this.webgljs.glRenderbufferStorage(glRenderbuffer, glDepthComponent, width, height);
			},
			
			
			glEnableVertexAttribArray : function(json) {
				var attributePosition = json["attributePosition"];

				this.webgljs.glEnableVertexAttribArray(attributePosition);
			},

			glViewport : function(json) {
				var i = json["i"];
				var j = json["j"];
				var width = json["width"];
				var height = json["height"];

				this.webgljs.glViewport(i, j, width, height);
			},
			
			
			

			

			glFramebufferRenderbuffer : function(json) {
				var glFramebuffer = json["glFramebuffer"];
				var glDepthAttachment = json["glDepthAttachment"];
				var glRenderbuffer = json["glRenderbuffer"];
				var i = json["i"];
				this.webgljs.glFramebufferRenderbuffer(glFramebuffer, glDepthAttachment, glRenderbuffer, i);
			},
			
			
			glBufferData : function(json) {
				var glElementArrayBuffer = json["glElementArrayBuffer"];
				var numBytes = json["numBytes"];
				var intIdxBuffer = json["intIdxBuffer"];
				var glStaticDraw = json["glStaticDraw"];
				this.webgljs.glBufferData(glElementArrayBuffer, numBytes, intIdxBuffer, glStaticDraw);
			},
			

			glClearColor : function(json) {
				var f = json["f"];
				var g = json["g"];
				var h = json["h"];
				var i = json["i"];

				this.webgljs.glClearColor(f,g,h,i);
			},
			
			glClear : function(json) {
				var i = json["i"];
				this.webgljs.glClear(i);
			},
			

			
			glEnable : function(json) {
				var glDepthTest = json["glDepthTest"];
				this.webgljs.glEnable(glDepthTest);
			},
			

			glDepthFunc : function(json) {
				var glLequal = json["glLequal"];
				this.webgljs.glDepthFunc(glLequal);
			},

			glGenFramebuffers : function(json) {
				var i = json["i"];
				var fBufferArray = json["frameBufferArray"];
				var j = json["j"];
				this.webgljs.glGenFramebuffers(i,fBufferArray,j);
			},


			glGenBuffers : function(json) {
				var i = json["i"];
				var vboHandles = json["vboHandles"];
				var j = json["j"];
				this.webgljs.glGenBuffers(i,vboHandles,j);
			},


			glBindFramebuffer : function(json) {
				var glFramebuffer = json["glFramebuffer"];
				var i = json["i"];
				this.webgljs.glBindFramebuffer(glFramebuffer,i);
			},
			

			glDrawBuffer : function(json) {
				var glColorAttachment0 = json["glColorAttachment0"];
				this.webgljs.glDrawBuffer(glColorAttachment0);
			},

			

			glGenRenderbuffers : function(json) {
				var i = json["i"];
				var dBufferArray = json["depthBufferArray"];
				var j = json["j"];
				this.webgljs.glGenRenderbuffers(i,dBufferArray,j);
			},
			

			

			glBindRenderbuffer : function(json) {
				var glRenderbuffer = json["glRenderbuffer"];
				var i = json["i"];
				this.webgljs.glBindRenderbuffer(glRenderbuffer,i);
			},
			


			

			glBufferSubData : function(json) {
				var glArrayBuffer = json["glArrayBuffer"];
				var offset = json["offset"];
				var i = json["i"];
				
				var fbData = json["fbData"];
				this.webgljs.glBufferSubData(glArrayBuffer, offset, i, fbData);
			},


			totalObject : function(json) {
				var total = json["total"];
				this.webgljs.totalObject(total);
			},

			webgl_destroy : function(json) {
				var text = json["text"];
				this.webgljs.webgl_destroy(text);
			},

			glFrontFace : function(json) {
				var text = json["text"];
				this.webgljs.glFrontFace(text);
			},
			
			appendErr : function(json) {
				var text = json["text"];
				this.webgljs.appendErr(text);
			},

			appendWarn : function(json) {
				var text = json["text"];
				this.webgljs.appendWarn(text);
			},

			appendInfo : function(json) {
				var text = json["text"];
				this.webgljs.appendInfo(text);
			},

			clearAll : function() {
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