import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;

import com.capitalone.socialApiFb.model.CSVUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class PageAllpostLogic {
	static ObjectMapper mapper = new ObjectMapper();
	public static void main(String[] args) {
		String fileName="src/main/resources/read_data/page_all_post.json";
		//ClassLoader classLoader = PageAllpostLogic.class.getClass().getClassLoader();
		//File file = new File(classLoader.getResource(fileName).getFile());
		JsonNode root=null;
		try {
			
			File file = new File(fileName);
			 root = mapper.readTree(file);
			 System.out.println(root.asText());
			 PageAllPostData datanode=mapper.convertValue(root, PageAllPostData.class);
			 System.out.println(datanode);
			 String csv_directorypath="src/main/resources/"+"write_data";
			 String csvfilename=csv_directorypath+"/sample.csv";
			 Path newFilePath = Paths.get(csvfilename);
				Path directory = Paths.get(csv_directorypath);
				file = new File(csvfilename);
				if (file.exists() && file.isFile())
				  {
					System.out.println("filealready exists by name "+csvfilename);
					System.out.println("deleteing file");
				  file.delete();
				  }
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
			 FileWriter csv_writer = new FileWriter(csvfilename);
			 CSVUtils.writeLine(csv_writer, Arrays.asList("Number ","HeadLine","Post Text", "Image Url","Download Img File","CTA URL","Expanded CTA","TimeStamp","Post Date","ID","Update ID", "Status Link","Image"));
			 Integer count=1;
			 for(PageAllPostModel p: datanode.getData())
			 {
				 CSVUtils.writeLine(csv_writer, 
						 Arrays.asList(count.toString(),
								 (null!=p.getName()?p.getName():""),
								 (null!=p.getMessage()?p.getMessage():""),
								 (null!=p.getPermalink_url().toString()?p.getPermalink_url():""),
								 "Download Img File",
								 (null!=p.getLink()?p.getLink():""),
								 "Expanded CTA",
								 (null!=p.getCreated_time()?p.getCreated_time():""),
								 (null!=p.getCreated_time()?p.getCreated_time():""),
								 (null!=p.getId()?p.getId():""),
								 (null!=p.getPromotable_id()?p.getPromotable_id():""),
								 (null!=p.getPermalink_url()?p.getPermalink_url():""),
								 "Image"));
				 count++;
			 }
			 csv_writer.flush();
			 csv_writer.close();
			 
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void readerUstils(String csvFile)
	{
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	int count=0;
            while ((line = br.readLine()) != null) {
            	if(count>0)
            	{
            		  // use comma as separator
                    String[] lines = line.split(",");
                    System.out.println(line);

            	}
            	count++;
              
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
