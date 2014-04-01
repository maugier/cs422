import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.commons.io.IOUtils;

public class JSON_LangCount {
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
		    }
		 }
	    
	   /* public static class Map2 extends Mapper<LongWritable, Text, Text, IntWritable> {
	        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	          
	            Text ln =new Text();
	            ln.set("1");
	            context.write(ln, new IntWritable(1));
	        }
	    }
	 
	    public static class Reduce2 extends Reducer<Text, IntWritable, Text, IntWritable> {
	        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
	        	int sum = 0;
	        	Iterator<IntWritable> iterator = values.iterator();
	        	for (IntWritable val : values){
	        		sum+=val.get();
	        	}
	        	context.write(new Text("Output:"), new IntWritable(sum));
	        }
	    }*/
	        
	/* public static class JsonParsing {  
	
	 }*/
	 
	    public static void main(String[] args) throws Exception {
	    	 InputStream is = JSON_LangCount.class.getClassLoader().getResourceAsStream("sample.txt");
		        String jsonTxt = IOUtils.toString(is);
		        
		        JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );        
		        /*double coolness = json.getDouble( "coolness" );
		        int altitude = json.getInt( "altitude" );
		        JSONObject pilot = json.getJSONObject("pilot");*/
		        String lang = json.getString("lang");
		        //String lastName = pilot.getString("lastName");
		        
		       /* System.out.println( "Coolness: " + coolness );
		        System.out.println( "Altitude: " + altitude );
		        System.out.println( "Pilot: " + lastName );*/
		        System.out.println("language" + lang );
	    	//JsonParsing obj = new JsonParsing();
	    	
	    	/*Configuration conf = new Configuration();
	        //Configuration conf1 = new Configuration();
	        
	        Job job = new Job(conf, "FindingTriangleRelation");
	        job.setJarByClass(JSON_LangCount.class);
	        job.setMapOutputKeyClass(Text.class);
	        job.setMapOutputValueClass(IntWritable.class);
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(IntWritable.class);
	 
	        job.setMapperClass(Map.class);
	        job.setReducerClass(Reduce.class);
	 
	        job.setInputFormatClass(TextInputFormat.class);
	        job.setOutputFormatClass(TextOutputFormat.class);
	        
	        FileInputFormat.setInputPaths(job, new Path(args[0]));
	        FileOutputFormat.setOutputPath(job, new Path(args[1]));
	        
	        job.waitForCompletion(true);*/
	        
	        /*Job jobnew = new Job(conf1, "FindingTriangleRelationSum");
	        jobnew.setJarByClass(JSON_LangCount.class);
	        jobnew.setMapOutputKeyClass(Text.class);
	        jobnew.setMapOutputValueClass(IntWritable.class);
	        jobnew.setOutputKeyClass(Text.class);
	        jobnew.setOutputValueClass(IntWritable.class);
	 
	        jobnew.setMapperClass(Map2.class);
	        jobnew.setReducerClass(Reduce2.class);
	 
	        jobnew.setInputFormatClass(TextInputFormat.class);
	        jobnew.setOutputFormatClass(TextOutputFormat.class);
	        
	        
	        FileInputFormat.setInputPaths(jobnew, new Path("/std063/temp"));
	        FileOutputFormat.setOutputPath(jobnew, new Path(args[1]));
	        
	        
	        jobnew.waitForCompletion(true);*/
	        

	    }       
	}


