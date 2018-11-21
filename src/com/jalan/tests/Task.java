package com.jalan.tests;

import java.util.Random;

import com.jalan.cksock.SockService;

class Operation {
	public final static int PLUS = 1;
	public final static int MINUS = 2;
	public final static int DIVIDE = 3;
	public final static int MULTIPLY = 4;
	
	int operation;
	
	String symbol;
	
	int num1;
	int num2;
	Integer result;
	
	public Operation() {}
	public Operation(int operation, int num1, int num2) {
		this.num1 = num1;
		this.num2 = num2;
		this.operation = operation;
		
	}
	
	public String toString() {
		return "Operation: " + num1 + " " + symbol + " " + num2 + " = " + (result != null ? result : "?"); 
	}
}

public class Task implements Runnable{

	public SockService service;
	private Random rand = new Random();
	
	private boolean up = true;
	
	public Task(SockService service) {
		this.service = service;
	}
	
	public void run() {
		int cycles = 0;
		while(up) {

			Operation operation = new Operation(getRandomInt(1, 4), getRandomInt(5, 1500), getRandomInt(5, 1500));
			calcOperation(operation);
			
			service.sendDataPlz(operation.toString());
			
			try {
				Thread.sleep(getRandomInt(60, 60*5));
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			cycles++;
			
			if(cycles >= 10) {
				up = false;
			}
		}
	}
	
	public Operation calcOperation(Operation operation) {
		switch(operation.operation) {
			case Operation.PLUS: {
				operation.symbol = "+";
				
				operation.result = operation.num1 + operation.num2;
				break;
			}
			
			case Operation.MINUS: {
				operation.symbol = "-";
				
				operation.result = operation.num1 - operation.num2;
				break;
			}
			
			case Operation.DIVIDE: {
				operation.symbol = "/";
				
				operation.result = operation.num1 / operation.num2;
				break;
			}
			
			case Operation.MULTIPLY: {
				operation.symbol = "*";
				
				operation.result = operation.num1 * operation.num2;
				break;
			}
		}
		
		return operation;
	}
	
	public static int getRandomInt(int min, int max) {
		  return (int)Math.floor(Math.random() * (max - min + 1) + min);
	}
	
}
