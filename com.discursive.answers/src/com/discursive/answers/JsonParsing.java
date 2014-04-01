package com.discursive.answers;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;





import org.json.JSONObject;





public class JsonParsing {
	
	public static void main(String[] args) throws Exception {
		InputStream file =  new FileInputStream("G://Education/Android/com.discursive.answers/sample_one.txt");
		Scanner input = new Scanner(file);
		HashMap<BigInteger, String> textMap = new HashMap<BigInteger, String>();

		while(input.hasNext()) {
		    //or to process line by line
		    String nextLine = input.nextLine();
		    JSONObject jsonObj = new JSONObject(nextLine);
		    
		    if (jsonObj.has("lang") && jsonObj.get("lang").equals("en"))
		    {
		    	Object  value= jsonObj.get("text");
		    	textMap.put(BigInteger.valueOf( (long) jsonObj.get("id")), value.toString());
		    	
		    }
		    
		    
		   for (BigInteger key: textMap.keySet())
		   {
			   System.out.print("Key :" + key.toString());
			   System.out.println("      Text :" + textMap.get(key).toString());
		   }
		   
		/*    while(itr.hasNext())
		    {
		    	System.out.println(itr.);
		    }
		    */
		    //String tweet= ;
		    //System.out.println(nextLine.split(":"));
    }
		input.close();
		
		}
	

}
