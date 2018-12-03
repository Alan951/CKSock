# CKSock
My implementation of Java Native Sockets using Rx and Log4J

## Example
```

public static void main(String [] args) throws IOException, InterruptedException{
	LogManager.getRootLogger().setLevel(Level.OFF);
	SockLogger.autoConfigure(); //Required for the Logger
		
	SockServerService server = new SockServerService(new SockConfig(951)); //Initialize your server socket service
	server.startInComingConnections(); //Start thread for incoming connections
	
	server.getClientMessagesObserver().subscribe((message) -> { //message = MessageWrapper object.
		System.out.println(message); //Print messages incoming from clients
	});
    
	SockService client = new SockService(new SockConfig("127.0.0.1", 951)); //Connection to server with client socket service
	client.sendDataPlz("Hello wolrd!!"); //send message

}

```

#### TODO List
- [ ] Clients can sned message to other(s) client(s) with internal router (actually you need implement it)
