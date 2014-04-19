package ch.epfl.data.bigdata.markov;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class JsonParsing {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);

        private static final int NGRAM_LENGTH = 3;
  
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            try {
                Tweet tweet = Tweet.parse(value.toString());
                                
                if ("en".equals(tweet.lang)) {
                    int stop = tweet.text.length() - NGRAM_LENGTH + 1;
                    for (int i = 0; i < stop; i++) {
                        context.write((new Text(tweet.text.substring(i, i + NGRAM_LENGTH))), one);
                    }
                }
            } catch (JsonProcessingException e) {
                // Parse failure, ignore
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJobName("n-gram count");

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
