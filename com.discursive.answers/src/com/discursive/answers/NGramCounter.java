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

        //private static int NGRAM_LENGTH = 3;
        
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	String input = value.toString();
	    	//HashMap<BigInteger, String> textMap = new HashMap<BigInteger, String>();
	    	
			    JSONObject jsonObj;
			    Object text="";
            try {
               // Tweet tweet = Tweet.parse(value.toString());
            	jsonObj = new JSONObject(input);
                                
                if (jsonObj.has("lang") && jsonObj.get("lang").equals("en")) {
                   /* int stop = tweet.text.length() - NGRAM_LENGTH + 1;
                    for (int i = 0; i < stop; i++) {
                        context.write((new Text(tweet.text.substring(i, i + NGRAM_LENGTH))), one);
                    }*/
                	
					jsonObj = new JSONObject(input);
					 {
					    	text= jsonObj.get("text");
					    	//textMap.put(BigInteger.valueOf( (long) jsonObj.get("id")), text.toString());
					    	String[] a = text.toString().split("\\s+"); 
					    	for(int i=0;i<a.length;i++)
							  {
					    		context.write((new Text(a[i])), one);
							  }
					    }
				
                }
            } catch (JsonProcessingException | JSONException e) {
                // Parse failure, ignore
            }
        }
    }

    public static void main(String[] args) throws Exception {
        
      /*  if (args.length != 3) {
            throw new Exception("Parameters expected: <input path> <output path> <ngram length>");
        }*/
        
        //Map.NGRAM_LENGTH = Integer.parseInt(args[2]);
        
        Configuration conf = new Configuration();

        //Job job = Job.getInstance(conf);
        Job job = new Job(conf, "n-gram count");
        //job.setJobName("n-gram count");

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
