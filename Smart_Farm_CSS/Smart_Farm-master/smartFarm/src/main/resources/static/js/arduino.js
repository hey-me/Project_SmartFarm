function LedControl(action){
    if("WebSocket" in window){//브라우저가 지원하는지 체크후
        //웹소켓을 열고
        var ws = new WebSocket("ws://192.168.210.224:80");
        ws.onopen = function(){
        	
        	if(action== "changeSetting1"){
        		ws.send(action+"/"+$("#ledOnTime").val()+"/"+$("#ledOffTime").val());

        	}else if(action== "changeTransmitDataInterrupt"){
        		ws.send(action+$("#Interrupt").val());

        	}else
        	ws.send(action);
  
        };
        ws.onmessage = function(evt){
			//alert(evt.data);
            ws.close();
        };
        ws.onclose = function(){
        };
    } else {
        alert("웹소켓을 지원하지 않는 브라우저입니다.");
    }
}
