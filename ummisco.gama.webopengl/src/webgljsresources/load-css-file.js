//
var cssId = 'webgljs-css-file';  
 
 
if (!document.getElementById(cssId))
{
   var head  = document.getElementsByTagName('head')[0];
   var link  = document.createElement('link');
   link.id   = cssId;
   link.rel  = 'stylesheet';
   link.type = 'text/css';
   link.href = '/rwt-resources/webgljs/webgljs.css';
   link.media = 'all';
   head.appendChild(link);
}