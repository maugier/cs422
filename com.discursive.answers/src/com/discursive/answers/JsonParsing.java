package com.discursive.answers;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.json.JSONObject;

public class JsonParsing {
	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();
	        
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	       
	    	String[] tmp=  value.toString().split("(?<=\\G.{3})");
	    	
	    	for(String x: tmp)
	    	{
	    		word.set(x);
	    		context.write(word, one);
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
		InputStream file =  new FileInputStream("G://Education/Android/com.discursive.answers/sample_one.txt");
		Scanner input = new Scanner(file);
		HashMap<BigInteger, String> textMap = new HashMap<BigInteger, String>();

		while(input.hasNext()) {
		    String nextLine = input.nextLine();
		    JSONObject jsonObj = new JSONObject(nextLine);
		    OutputStream fos = new FileOutputStream("input.txt");
		    ObjectOutputStream outputStream = new ObjectOutputStream(fos);
		    
		    if (jsonObj.has("lang") && jsonObj.get("lang").equals("en"))
		    {
		    	Object  value= jsonObj.get("text");
		    	textMap.put(BigInteger.valueOf( (long) jsonObj.get("id")), value.toString());
		    }
		    
		    for (BigInteger key: textMap.keySet())
		   {
			outputStream.writeBytes(textMap.get(key).toString());
			outputStream.writeBytes("\n");
			   }
		  fos.close();
		  
		}
		input.close();
		System.out.println("donee!");
		
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
