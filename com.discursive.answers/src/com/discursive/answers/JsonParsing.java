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
	        String line = value.toString();
	        StringTokenizer tokenizer = new StringTokenizer(line);
	        while (tokenizer.hasMoreTokens()) {
	            word.set(tokenizer.nextToken());
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
		        context.write(key, new IntWritable(sum));
		    }
		 }
	 
	public static void main(String[] args) throws Exception 
	{
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
		    
		    
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("input.txt", true)));
		    //out.println("the text");
		    //out.close();
		    
		   for (BigInteger key: textMap.keySet())
		   {
			  /* System.out.print("Key :" + key.toString());
			   System.out.println("      Text :" + textMap.get(key).toString());*/
			   out.print(key);
			   out.print(textMap.get(key).toString());
			  
			   }
		   out.close();
		   
		   /*File mapperfile = new File("input.txt");
		   FileOutputStream f = new FileOutputStream(mapperfile);  
		   ObjectOutputStream s = new ObjectOutputStream(f); 
		   s.writeObject(textMap);
		   s.close();*/
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
