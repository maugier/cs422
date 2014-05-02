package com.discursive.answers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;


public class NGramCounter {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);

        
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	String input = value.toString();

			    JSONObject jsonObj;
			    Object text="";
            try {
               // Tweet tweet = Tweet.parse(value.toString());
            	jsonObj = new JSONObject(input);
                                
                if (jsonObj.has("lang") && jsonObj.get("lang").equals("en")) {
    					jsonObj = new JSONObject(input);
					 {
					    	text= jsonObj.get("text");
					    	String[] a = text.toString().split("\\s+");
					    	
					    	for(int i=0;i<a.length;i++)
							  {
					    		context.write((new Text(a[i].replaceAll("([\\-.,@!:;+$#=?&%*|_\\[\\]\\\\\\/\\'s$\\)\\(\\\"])",""))), one);
							  }
					    }
				
                }
            } catch (JsonProcessingException | JSONException e) {
                // Parse failure, ignore
            }
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = new Job(conf, "n-gram count");
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(CounterReducer.class);
        job.setCombinerClass(CounterReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setNumReduceTasks(80);
        
        job.waitForCompletion(true);
        

    }
}
