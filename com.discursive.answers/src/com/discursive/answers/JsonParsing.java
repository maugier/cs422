package com.discursive.answers;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParsing {
	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();
	        
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
	    	String input = value.toString();
	    	HashMap<BigInteger, String> textMap = new HashMap<BigInteger, String>();
			    JSONObject jsonObj;
			    Object text="";
				try {
					jsonObj = new JSONObject(input);
					 if (jsonObj.has("lang") && jsonObj.get("lang").equals("en"))
					    {
					    	text= jsonObj.get("text");
					    	textMap.put(BigInteger.valueOf( (long) jsonObj.get("id")), text.toString());
					    	for(int i=0;i<text.toString().length()-2;i++)
							  {
							  context.write((new Text(text.toString().substring(i,i+3))), one);
							  }
					    }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	}
	
	 public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

		    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
		    		throws IOException, InterruptedException {
		        int sum = 0;
		        for (IntWritable val : values) {
		            sum += val.get();
		        }
		        context.write(key, new IntWritable(sum));
		        }
		 }
	 
	public static void main(String[] args) throws Exception 
	{
<<<<<<< HEAD
=======
		InputStream file =  new FileInputStream("G://Education/Android/com.discursive.answers/sample_one.txt");
		Scanner input = new Scanner(file);
		HashMap<BigInteger, String> textMap = new HashMap<BigInteger, String>();

		while(input.hasNext()) {
		    String nextLine = input.nextLine();
		    JSONObject jsonObj = new JSONObject(nextLine);
		    OutputStream fos = new FileOutputStream("input.txt");
		    PrintStream outputStream = new PrintStream(fos);
		    
		    if (jsonObj.has("lang") && jsonObj.get("lang").equals("en"))
		    {
		    	Object  value= jsonObj.get("text");
		    	textMap.put(BigInteger.valueOf( (long) jsonObj.get("id")), value.toString());
		    }
		    
		    for (BigInteger key: textMap.keySet())
		   {
			outputStream.println(textMap.get(key));
			   }
		  fos.close();
		  
		}
		input.close();
		System.out.println("donee!");
		
>>>>>>> 95dd14fe9a7c1c462e9681a987ce81176ad3449f
		 Configuration conf = new Configuration();
	        
	        Job job = new Job(conf, "n-gram count");
	    
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	        
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	        
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	        
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	        
	    job.waitForCompletion(true);
	    
	    }
	}
