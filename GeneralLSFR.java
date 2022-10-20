package lfsr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneralLSFR {
	
	public static final ArrayList<String> register = new ArrayList<>();			//initial list from user input : "010101"
	public static final ArrayList<Integer> integerRegister = new ArrayList<>();	//converted list to integer form : 010101
	public static final ArrayList<Boolean> booleanRegister = new ArrayList<>();	//converted list to boolean form : false,true,false,true
	public static final ArrayList<Integer> taps = new ArrayList<>();			//indexes of tap-sequences for booleanRegister 
	
	public static final ArrayList<Boolean> xorResults = new ArrayList<>();
	public static final ArrayList<Integer> xorResultsInteger = new ArrayList<>();
	
	public static final Scanner in = new Scanner(System.in);  
	
	//boolean method taking register and taps lists, and size of taps list as parameters
	static boolean xorRegister(ArrayList<Boolean> booleanRegister,
			ArrayList<Integer> taps, int n) {
		
		//initialise variable as anything
		boolean xor = false;
		
		//iterate over list of register's booleans and XOR 
		for(int i=0; i<n; i++) {
			xor = xor ^ booleanRegister.get(taps.get(i));
		}
		
		//return least significant bit (output)
		return xor;
	}
	
	//public method to initialise Register initial state from user input
	public static void initialiseRegister() {
	    System.out.println("Enter initial state of Register: ");
	    String initialState = in.nextLine();	//take string input of bits
	    
	    //split at every empty delimiter and store in String array
	    String[] initialStateArray = initialState.split("");
	    
	    //enhanced loop to add each bit to register List
	    for(String character : initialStateArray)
	    	register.add(character);
	    
	    //convert String register List to Integer List: {"0", "1", "1"} -> {0,1,1}
	    for(String integers : register){
	        integerRegister.add(Integer.parseInt(integers.trim()));
	    }
	}
	
	//public method to convert to final list form (boolean)
	public static void convertRegisterToBoolean() {
		for(int i=0; i< integerRegister.size(); i++) {
	    	if(integerRegister.get(i).equals(0)) {
	    		booleanRegister.add(false);
	    		
	    	}
	    	else {
	    		booleanRegister.add(true);
	    	}
	    }
	}
	
	//public method to determine XOR tap sequence
	public static void tapSequence() {
		System.out.println("How many XOR taps?");
	    int nOfTaps = in.nextInt();
	    
	    //simple testing to ensure taps !> register
	    if(nOfTaps > integerRegister.size()) {
	    	System.out.println("Not possible to have more taps than bits");
	    }
	    
	    //sequentially ask for each tap's index (from i=0) then add indexes to tap-sequence List
	    for(int i=1; i<= nOfTaps; i++) {
	    	System.out.println("Enter tap " + i + "'s position:" );
	    	int tap = in.nextInt();
	    	
	    	taps.add(tap);
	    }
	    
	    
	}
	
	//main entry point
	public static void main(String [] args) throws IOException{
		//BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\alter\\eclipse-workspace\\FundamentalCybersecurity\\src\\lfsr\\output.txt")));

		File file = new File("C:\\Users\\alter\\eclipse-workspace\\FundamentalCybersecurity\\src\\lfsr\\output.csv");
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("output");
        bw.newLine();
		
		ArrayList<Integer> booleanRegisterBinaryForm = new ArrayList<>(); 	//List for outputting in integer form 
		
		//run public methods
	    initialiseRegister();
	    System.out.println("Register Size: " + integerRegister.size() + " bits");
	    convertRegisterToBoolean();
	    tapSequence();
	    
	    //parameter for determining number of iterations over register XOR operation
	    int n = taps.size();
	    
	    //output initial result of S0 
	    System.out.println("OUTPUT: " + xorRegister(booleanRegister, taps, n));
	    
	    
	    //iteration for the next bitstream (in this case 30 times) by shifting register to the right and
	    //replacing i=0 with XOR operation result
	    System.out.println("Next bitstream (including initial state):");
	    for(int i=0; i<=29; i++) {
			boolean tapXor = xorRegister(booleanRegister, taps, n);
			
			//iterate over every boolean and add it to [0,1,1,0] register for output form
			for(boolean binaries : booleanRegister) {
				if(binaries == true) {
					booleanRegisterBinaryForm.add(1);
				}
				else {
					booleanRegisterBinaryForm.add(0);
				}
			}
			
			//System.out.println((i+1) + " : " + booleanRegister);
			System.out.println((i+1) + " : " + booleanRegisterBinaryForm + tapXor);
			xorResults.add(tapXor);
			
			if(tapXor == true) {
				xorResultsInteger.add(1);
			}
			else {
				xorResultsInteger.add(0);
			}
			
			//reset the register 
			booleanRegisterBinaryForm.removeAll(booleanRegisterBinaryForm);
			
			
			
			for(int j = booleanRegister.size() - 1; j>0; j--) {
				booleanRegister.set(j, booleanRegister.get(j-1));
			}
			booleanRegister.set(0, tapXor);
			
		}
	    System.out.println(xorResults);
	    System.out.println(xorResultsInteger);
	    
	    for(int i=0; i<xorResultsInteger.size(); i++) {
	    	bw.write(xorResultsInteger.get(i));
	    	bw.newLine();
	    }
	    
	    bw.close();
	    fw.close();
	   
	   
	}

}
