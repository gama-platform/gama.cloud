
function LogJS(element)  {
	
   this.div = document.createElement("div");    
   this.div.className = 'logjs';    
   element.appendChild(this.div); 
   this.div.innerHTML = "";
   
			

   this.appendInfo = function(text)  {        
   
       
       this.div.innerHTML = this.div.innerHTML
        + "<p class='info'>"+ text +"</p>";
        

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

			