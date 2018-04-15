/**
 * @Generated
 */
(function ()  {
//	'use strict';
	console.log("loaded --> EtherGaml.js");
	rap.registerTypeHandler("ummisco.gama.participative.EtherpadBasicText", {
		
		factory : function(properties) {
			return new ummisco.gama.participative.EtherpadBasicText(properties);
		},
		
		destructor : "destroy",
		properties : [ "abc" , "url", "text", "editable", "status", "annotations", "scope", "proposals", "font", "dirty", "markers", "background"],
		events : ["Modify", "TextChanged", "Save", "FocusIn", "FocusOut", "Selection", "CaretEvent", "ContentAssist"],
		methods : ["appendWarn", "appendErr", "appendInfo", "setText", "createAndMergeEditors", "clearAll", "setSelection", "addMarker", "removeMarker", "clearMarkers", "insertText", "removeText", "setProposals", "moveCursorFileStart","moveCursorFileEnd"]
	});
	
	if (!window.o7planning) {
		console.log("function  --> window.o7planning");
	       window.o7planning = {};
	}
	
	
	rwt.qx.Class.define("ummisco.gama.participative.EtherpadBasicText", {
		extend :org.dslforge.styledtext.BasicText,
		construct : function(properties) {
			
			this.base(arguments, properties);
			
			console.log("function  --> construct :");
			//--
			bindAll(this, [ "layout", "onReady", "onSend", "onRender", "onChange" ]);// @custom
			//var divClassName ="ace_content"; //"ace_layer ace_text-layer";
			var divClassName ="ace_layer ace_text-layer";
			var nbr = document.getElementsByClassName(divClassName).length;
			console.log("====>>>===>>  There is "+nbr+" Elements of "+ divClassName);
			this.parent = rap.getObject(properties.parent);
			if(nbr=0){
				   this.element = document.createElement("div");
				}else{
				//this.element  = document.getElementsByClassName(divClassName).[0];
				this.element = document.createElement("div");
			}
			//this.element.className = divClassName;
			this.parent.append(this.element);
		    this.parent.addListener("Resize", this.layout);
		    this.etherpadjs = new EtherpadJS(this.element);
		      // Render interface
		    rap.on("render", this.onRender);
		   // this.base(arguments, properties);
			//--
			
		},
		members : {
			createEditor : function() {
				var basePath = 'rwt-resources/src-js/org/dslforge/styledtext/ace';
				ace.require("ace/config").set("basePath", basePath);
				var workerPath = 'rwt-resources/src-js/ummisco/gama/participative/ace';
				ace.require("ace/config").set("workerPath", workerPath);
				var themePath = 'rwt-resources/src-js/ummisco/gama/participative/ace';
				ace.require("ace/config").set("themePath", themePath);
				var modePath = 'rwt-resources/src-js/ummisco/gama/participative/ace';
				ace.require("ace/config").set("modePath", modePath);
				var editor = this.editor = ace.edit(this.element);
				if (editor != null) {
					var editable = this.editable;
					var guid = this.url;
		        	var self = this;
					ace.config.loadModule("ace/ext/language_tools", function (module) {
						editor.getSession().setMode("ace/mode/gaml");
						editor.getSession().id = self.url;	
			        	editor.setOptions({
				            enableBasicAutocompletion: true,
				            enableTextCompleter: false,
				            enableKeyWordCompleter: false,
				            enableSnippets: false,
						    useWorker: true,
			            });	
			        	self.langTools = ace.require("ace/ext/language_tools");
						self.serverCompleter = {
							getMode: function() {
								return editor.getSession().getMode();
							},
							getCompletions: function(editor, session, pos, prefix, callback) {
								self.onCompletionRequest(pos, prefix, callback);
							},
							getDocTooltip: function(item) {
						    	item.docHTML = ["<div class=\"ace_line\" style=\"height:12px\"><span class=\"", self.typeToIcon(item.meta),"\">&nbsp;</span><span class=\"ace_\">","<b>", item.caption, "</b>","</span><span class=\"ace_rightAlignedText\"></span></div>", "<hr></hr>", item.meta].join("");
							}
						};
			        	editor.setOption("enableServerCompleter", self.serverCompleter);
			        	self.completers = editor.completers;
						editor.setTheme("ace/theme/gaml");
						editor.setShowPrintMargin(false);
						editor.setBehavioursEnabled(true);
						editor.setWrapBehavioursEnabled(true);
						editor.setReadOnly(!editable);
						editor.setFontSize(12);
						editor.$blockScrolling = Infinity;
			        });
					//Session settings
					var session = editor.getSession();
					editor.getSession().setUseWrapMode(true);
				    editor.getSession().setTabSize(4);
				    editor.getSession().setUseSoftTabs(true);
					editor.getSession().getUndoManager().reset();
					//Text hover
					var TokenTooltip = ace.require("ace/ext/tooltip").TokenTooltip;
					editor.tokenTooltip = new TokenTooltip(editor);
					//Annotations
					if (this.annotations==null)	this.annotations=[];
					//Index
				 	index = this.scope;
				 	proposals = this.proposals;
				 	if (this.useSharedWorker) {
						if (typeof SharedWorker == 'undefined') {
								console.log("Your browser does not support Javascript shared workers, as a consequence some features will be disabled."
								+ "For a full-featured user experience, the following browsers are supported: Chrome, Firefox, Safari.");
							} else {
							var filePath = 'rwt-resources/src-js/org/dslforge/styledtext/global-index.js';
							var httpURL = this.computeWorkerPath(filePath);
							var worker = this.worker = new SharedWorker(httpURL);
							if (this.ready) {
								editor.on("change", function(event) {
									worker.port.postMessage({
										message: editor.getValue(),
								        guid: guid,
								        index: index
								    });
							    });
							}
							worker.port.onmessage = function(e) {
							 	index = e.data.index;
						    };
						}
				 	}
					editor.on("focus", function() {
				 		self.onFocus();
				 	});
				 	editor.on("blur", function() {
				 		self.onBlur();
				 	});
				 	editor.on("input", function() {
						if (!editor.getSession().getUndoManager().isClean())
							self.onModify();
				 	});
				 	editor.getSession().getSelection().on('changeCursor', function() {
				 	    self.onChangeCursor();
				 	});
					editor.commands.addCommand({
						name: 'saveFile',
						bindKey: {win: 'Ctrl-S', mac: 'Command-S', sender: 'editor|cli'},
						exec: function(env, args, request) {
							self.onSave();
						}
					});
				}
				//Done
		        this.onReady();
			},
			setScope : function(scope) {
				this.base(arguments, scope);
			},
			onCompletionRequest : function(pos, prefix, callback) {
				this.base(arguments, pos, prefix, callback);
			},
			setProposals : function(proposals) {
				this.proposals = proposals;
			},
			onFocus: function() {
				this.base(arguments);
			},
			onBlur: function() {
				this.base(arguments);
			},
			destroy : function() {
				this.base(arguments);
				this.langTools.resetOptions(this.editor);
			}
			
			//-------------------------------------
			
			,

		       onChange : function() {
		            //alert('1');
		       },
		 
		       onReady : function() {
		    	   //alert('2');
		       },
		 
		      
		       // Render interface in Client
		       onRender : function() {
		           if (this.element.parentNode) {
		               rap.off("render", this.onRender);
		                
		               rap.on("render", this.onRender);
		               rap.on("send", this.onSend);
		              
		               //alert('3');
		           }
		           //alert('4');
		       },
		 
		       //  
		       onSend : function() {
		    	   //alert('5');
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
		           //alert('11');
		       },
		        
		       appendInfo : function(json) {  
		           var text= json["text"];
		           var userId= json["userId"];
		           this.etherpadjs.appendInfo(text, userId);
		           //alert('12');
		       },
		       
		       setText : function(json) {  
		           var text= json["text"];
		           var userId= json["userId"];
		           var padId= json["padId"];
		           this.etherpadjs.setText(text, userId, padId);
		           //alert('13');
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
		           //alert('14');
		       }
			
			// -----------------------------------
		}
	});
	//-----------------------------------
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
		   //alert('17');
	       window.setTimeout(function() {
	           func.apply(context);
	       }, 0);
	   };
	
	//---------------------------------
	
}());
