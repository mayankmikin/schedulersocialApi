
package com.socialapi.scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import com.capitalone.socialApiFb.model.CSVReaderUtils;
import com.capitalone.socialApiFb.model.CSVUtils;
import com.capitalone.socialApiFb.model.Comments;
import com.capitalone.socialApiFb.model.PagePostImpression;
import com.capitalone.socialApiFb.model.PagePostImpressions;
import com.capitalone.socialApiFb.model.Post;
import com.capitalone.socialApiFb.model.PostAllData;
import com.capitalone.socialApiFb.model.Posts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

public class SampleJob extends QuartzJobBean {
	private static final Logger log = LoggerFactory.getLogger(SampleJob.class);
	@Autowired
	private  Environment env;
	private String name;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	ObjectMapper mapper = new ObjectMapper();
	 CsvMapper csvmapper = new CsvMapper();
	 RestTemplate restTemplate = new RestTemplate();
	// Invoked if a Job data map entry with that name
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		//System.out.println(String.format("Hello %s!", this.name));
		// this method fetches all data including old values as well 
		//PostAllData alldata=savepostimpressions();
		
		
		// below method code will fetch data for previous day on dailybasis
		try
		{
			preparedataperpostDailybasis();
			savepostimpressions();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	// this method gets data on life time basis
	public PostAllData savepostimpressions() throws Exception
	{
		
		  String url="";
		log.info("inside savepostimpressions START");
		log.info(env.getProperty("base.url"));
		String pageId="";
		pageId=env.getProperty("pageid");
		url=env.getProperty("base.url")+pageId+"/posts?"
				+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
				+env.getProperty("suffixparams");
		log.info("url is "+url);
	    JsonNode node = restTemplate.getForObject(url, JsonNode.class);
	  //Get file from resources folder
	    log.info("posts are: "+node);
		
		Posts posts =mapper.convertValue(node, Posts.class);
		log.info("post data is : "+posts.toString());
	/*	ArrayList<PagePostImpression> allimpressions=new ArrayList<>();*/
		PostAllData alldata= new PostAllData();

		/*lines to save data in csv format
		 * 
		 * 
		 * */
		String impressions_directorypath="src/main/resources/"+"folder_impressions_"+dateFormat2.format(Calendar.getInstance().getTime());
		String impressionsfilename=impressions_directorypath+"/impressions_perpost_data_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+"_"+pageId+".csv";
		Path newFilePath = Paths.get(impressionsfilename);
		Path directory = Paths.get(impressions_directorypath);
		File file = new File(impressionsfilename);
		if (file.exists() && file.isFile())
		  {
			log.info("filealready exists by name "+impressionsfilename);
			log.info("deleteing file");
		  file.delete();
		  }
		Files.createDirectories(directory);
		Files.createFile(newFilePath);
		FileWriter impressions_writer = new FileWriter(impressionsfilename);
		// set columns in csv
		CSVUtils.writeLine(impressions_writer, Arrays.asList("PostID ","Reaction Name","Count Value", "Date"));
		
		HashMap<String, Integer> impressions_new=new HashMap<>();
		for(Post p: posts.getData())
		{
			url=env.getProperty("base.url")+p.getId()+"/insights?"
					+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
					+env.getProperty("methodname")
					+env.getProperty("fbmetrics")+"["+env.getProperty("metrics")+"]"
					+"&period=lifetime"
					+env.getProperty("suffixparams");
					log.info("url is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				log.info("impressionsdata is: "+node.asText().toString());
				PagePostImpressions impressions=mapper.convertValue(node, PagePostImpressions.class);
				
				//allimpressions.addAll(impressions.getData());
				// getting comments for each posts


				for(PagePostImpression po:impressions.getData())
				{
					String val="0";
					if(!po.getValues().get(0).get("value").asText().equals(""))
					{
						val=po.getValues().get(0).get("value").asText();
					}
					CSVUtils.writeLine(impressions_writer, Arrays.asList(p.getId(),po.getName(),val,dateFormat2.format(Calendar.getInstance().getTime())));	
					impressions_new.put(p.getId()+"_"+po.getName(), Integer.parseInt(val));
				}
				
		}
		impressions_writer.flush();
		impressions_writer.close();
		// read old files
		
		// read old file 
		impressions_directorypath="src/main/resources/"+"folder_impressions_"+dateFormat2.format(yesterday());
		impressionsfilename=impressions_directorypath+"/impressions_perpost_data_perday_"+dateFormat2.format(yesterday())+"_"+pageId+".csv";
				
				 HashMap<String, Integer> oldimpressions=CSVReaderUtils.readPostImpressionsFile(impressionsfilename, ",");
				System.out.println("***** old impressions ******");
			
				// calculate delta
				// write the delta
				impressions_directorypath="src/main/resources/"+"folder_impressions_delta_"+dateFormat2.format(Calendar.getInstance().getTime());
				impressionsfilename=impressions_directorypath+"/impressions_perpost_delta_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+"_"+pageId+".csv";
				  directory = Paths.get(impressions_directorypath);
					 newFilePath = Paths.get(impressionsfilename);	
				 file = new File(impressionsfilename);
					if (file.exists() && file.isFile())
					{
					log.info("filealready exists by name "+impressionsfilename);
					log.info("deleteing file");
					file.delete();
					}
					Files.createDirectories(directory);
					Files.createFile(newFilePath);
					//calculating delta and directly writing them into files
					impressions_writer = new FileWriter(impressionsfilename);
					CSVUtils.writeLine(impressions_writer, Arrays.asList("PageID_PostID_ImpressionType", "count","Date"));
				for(Map.Entry<String, Integer> entry:impressions_new.entrySet())
				{
					// data is present in both calculate delta or else write as is
					System.out.println("key: "+entry.getKey());
					if(oldimpressions.containsKey(entry.getKey()))
					{
						String deltacount=Integer.toString(entry.getValue()-oldimpressions.get(entry.getKey()));
						
						System.out.println("delta is: "+deltacount);
						CSVUtils.writeLine(impressions_writer, Arrays.asList(entry.getKey().toString(),deltacount,dateFormat2.format(Calendar.getInstance().getTime())));
					}
					else
					{
						CSVUtils.writeLine(impressions_writer, Arrays.asList(entry.getKey().toString(),entry.getValue().toString(),dateFormat2.format(Calendar.getInstance().getTime())));
					}
					
					
				}
				impressions_writer.flush();
				impressions_writer.close();
				System.out.println("********** old impressions");
				for(Map.Entry<String, Integer> entry:oldimpressions.entrySet())
				{
					System.out.println(entry.getKey());
				}
				System.out.println("saved delta and data file for impressions");
	return alldata;
	
	}

	public void preparedataperpostDailybasis()
	{
		  String url="";
		String pageId="";
		pageId=env.getProperty("pageid");
		url=env.getProperty("base.url")+pageId+"/posts?"
				+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
				+env.getProperty("suffixparams");
	    JsonNode node = restTemplate.getForObject(url, JsonNode.class);
	    //this node contains all posts of a page 
	    
	    try {
			log.info("saving data in a file: postimpressions_lifetime");
			String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
			String filename=directorypath+"/allpost_data_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+pageId+".json";
		//	String filename=directorypath+"/comments_perpost_data_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+pageId+".csv";




		
		Posts posts =mapper.convertValue(node, Posts.class);
		//log.info("post data is : "+posts.toString());
	/*	ArrayList<PagePostImpression> allimpressions=new ArrayList<>();*/
		
		PostAllData alldata= new PostAllData();
		String comments_directorypath="src/main/resources/"+"folder_comments_"+dateFormat2.format(Calendar.getInstance().getTime());
		String commentsfilename=comments_directorypath+"/comments_perpost_data_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+"_"+pageId+".csv";
		Path directory = Paths.get(comments_directorypath);
		Path newFilePath = Paths.get(commentsfilename);
	File file = new File(commentsfilename);
	if (file.exists() && file.isFile())
	{
	log.info("filealready exists by name "+commentsfilename);
	log.info("deleteing file");
	file.delete();
	}
	Files.createDirectories(directory);
	Files.createFile(newFilePath);
		FileWriter comments_writer = new FileWriter(commentsfilename);
		CSVUtils.writeLine(comments_writer, Arrays.asList("PostID ", "Count Value", "Date"));
		HashMap<String, Integer> comments_new=new HashMap<>();	
		for(Post p: posts.getData())
		{
			
				// getting comments for each posts
				log.info("getting comments data ");
				url=env.getProperty("base.url")+p.getId()+"/comments?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")
						+env.getProperty("sincedate")
						+env.getProperty("untildate")
						+"&summary=1";
				
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				Comments comments=mapper.convertValue(node, Comments.class);	
			CSVUtils.writeLine(comments_writer, Arrays.asList(p.getId(),comments.getSummary().get("total_count").asText(),dateFormat2.format(Calendar.getInstance().getTime())));	
			comments_new.put(p.getId(),Integer.parseInt(comments.getSummary().get("total_count").asText()));
		
		}
		comments_writer.flush();
		comments_writer.close();
		log.info("impressions are: "+alldata);
		// read old file 
		 comments_directorypath="src/main/resources/"+"folder_comments_"+dateFormat2.format(yesterday());
		 commentsfilename=comments_directorypath+"/comments_perpost_data_perday_"+dateFormat2.format(yesterday())+"_"+pageId+".csv";
		
		 HashMap<String, Integer> oldcomments=CSVReaderUtils.readCommentsFile(commentsfilename, ",");
		System.out.println("***** old comments ******");
	
		// calculate delta
		// write the delta
		 comments_directorypath="src/main/resources/"+"folder_comments_delta_"+dateFormat2.format(Calendar.getInstance().getTime());
		 commentsfilename=comments_directorypath+"/comments_perpost_delta_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+"_"+pageId+".csv";
		  directory = Paths.get(comments_directorypath);
			 newFilePath = Paths.get(commentsfilename);	
		 file = new File(commentsfilename);
			if (file.exists() && file.isFile())
			{
			log.info("filealready exists by name "+commentsfilename);
			log.info("deleteing file");
			file.delete();
			}
			Files.createDirectories(directory);
			Files.createFile(newFilePath);
			//calculating delta and directly writing them into files
			comments_writer = new FileWriter(commentsfilename);
			CSVUtils.writeLine(comments_writer, Arrays.asList("PostID ", "Count Value", "Date"));
		for(Map.Entry<String, Integer> entry:comments_new.entrySet())
		{
			// data is present in both calculate delta or else write as is
			if(oldcomments.containsKey(entry.getKey()))
			{
				String deltacount=Integer.toString(entry.getValue()-oldcomments.get(entry.getKey()));
				CSVUtils.writeLine(comments_writer, Arrays.asList(entry.getKey().toString(),deltacount,dateFormat2.format(Calendar.getInstance().getTime())));
			}
			else
			{
				CSVUtils.writeLine(comments_writer, Arrays.asList(entry.getKey().toString(),entry.getValue().toString(),dateFormat2.format(Calendar.getInstance().getTime())));
			}
			
			
		}
		comments_writer.flush();
		comments_writer.close();
		System.out.println("saved delta and data file for comments");
				} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	catch (IOException e) {
			e.printStackTrace();
		}
	    log.info("output is "+node.toString());
	    
	log.info("inside getPerDay END");
		
		
	}
	
	public static Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    return cal.getTime();
	}
}



