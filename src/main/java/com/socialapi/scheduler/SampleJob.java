package com.socialapi.scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import com.capitalone.socialApiFb.model.ApiResponseMessage;
import com.capitalone.socialApiFb.model.Comments;
import com.capitalone.socialApiFb.model.PagePostImpressions;
import com.capitalone.socialApiFb.model.Post;
import com.capitalone.socialApiFb.model.PostAllData;
import com.capitalone.socialApiFb.model.Posts;
import com.capitalone.socialApiFb.model.SharedPosts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleJob extends QuartzJobBean {
	private static final Logger log = LoggerFactory.getLogger(SampleJob.class);
	@Autowired
	private  Environment env;
	private String name;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	ObjectMapper mapper = new ObjectMapper();
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
		PostAllData alldata=savepostimpressions();
		
		
		// below method code will fetch data for previous day on dailybasis
		preparedataperpostDailybasis();
	}
	// this method gets data on life time basis
	public PostAllData savepostimpressions()
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
				alldata.getAllimpressionsData().addAll(impressions.getData());
				//allimpressions.addAll(impressions.getData());
				// getting comments for each posts
				log.info("getting commens data ");
				url=env.getProperty("base.url")+p.getId()+"/comments?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")+"&summary=1";
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				Comments comments=mapper.convertValue(node, Comments.class);
				alldata.getAllcommentsData().add(comments);
				log.info("comment data is :"+comments);
				// getting shared posts data 
				url=env.getProperty("base.url")+p.getId()+"/sharedposts?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")+"&summary=1";
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				SharedPosts sharedposts=mapper.convertValue(node, SharedPosts.class);
				if(!sharedposts.getData().isEmpty())
				{
					log.info("sharedposts data is :"+sharedposts);
					alldata.getAllsharedpostsData().add(sharedposts);
				}

				
		}
		
		log.info("impressions are: "+alldata);
		
				try {
					log.info("saving data in a file: postimpressions_lifetime");
					String filename="src/main/resources/postimpressions_lifetime_"+pageId+".json";
		Path newFilePath = Paths.get(filename);
		File file = new File(filename);
		if (file.exists() && file.isFile())
		  {
			log.info("filealready exists by name "+filename);
			log.info("deleteing file");
		  file.delete();
		  }
		Files.createFile(newFilePath);
		//Object to JSON in file
		//ClassLoader classLoader = getClass().getClassLoader();
		
		//JsonNode savenode=mapper.convertValue(, JsonNode.class);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file,alldata);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mapper.writeValue(new File("c:\\file.json"), );
	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    log.info("output is "+node.toString());
	    ApiResponseMessage resp= new ApiResponseMessage(4, "success", mapper.convertValue(alldata, JsonNode.class));
	log.info("inside getPerDay END");
	return alldata;
	
	}
	
	// comments will be per day post will be liftime 
	// as we need to  get the date comments per day on anypost
	public void preparedataperpostDailybasis()
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
		
		for(Post p: posts.getData())
		{
			
				// getting comments for each posts
				log.info("getting commens data ");
				url=env.getProperty("base.url")+p.getId()+"/comments?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")
						+env.getProperty("sincedate")
						+env.getProperty("untildate")
						+"&summary=1";
				
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				Comments comments=mapper.convertValue(node, Comments.class);
				alldata.getAllcommentsData().add(comments);
				log.info("comment data is :"+comments);
				// getting shared posts data 
				url=env.getProperty("base.url")+p.getId()+"/sharedposts?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")+env.getProperty("sincedate")
						+env.getProperty("untildate")+"&summary=1";
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				SharedPosts sharedposts=mapper.convertValue(node, SharedPosts.class);
				if(!sharedposts.getData().isEmpty())
				{
					log.info("sharedposts data is :"+sharedposts);
					alldata.getAllsharedpostsData().add(sharedposts);
				}
				
		}
		
		log.info("impressions are: "+alldata);
		
				try {
					log.info("saving data in a file: postimpressions_lifetime");
					String filename="src/main/resources/allpost_data_perday_"+dateFormat2.format(Calendar
							.getInstance().getTime())+pageId+".json";
		Path newFilePath = Paths.get(filename);
		File file = new File(filename);
		if (file.exists() && file.isFile())
		  {
			log.info("filealready exists by name "+filename);
			log.info("deleteing file");
		  file.delete();
		  }
		Files.createFile(newFilePath);
		//Object to JSON in file
		//ClassLoader classLoader = getClass().getClassLoader();
		
		//JsonNode savenode=mapper.convertValue(, JsonNode.class);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file,alldata);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mapper.writeValue(new File("c:\\file.json"), );
	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    log.info("output is "+node.toString());
	    ApiResponseMessage resp= new ApiResponseMessage(4, "success", mapper.convertValue(alldata, JsonNode.class));
	log.info("inside getPerDay END");
		
		
	}

}



