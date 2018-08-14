import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class PageAllpostLogic {
	static ObjectMapper mapper = new ObjectMapper();

	 
	public static void main(String[] args) throws Exception {
		String fileName="src/main/resources/read_data/page_all_post.json";
		String excelfilename="src/main/resources/write_data/example.xls";
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
			// CSVUtils.writeLine(csv_writer, Arrays.asList( "ID", "post", "created date", "updated date", "head line", "type", "link", "perm link", " full picture url", "share count", "like count", " image", "full link", "cid code"));
			 Integer count=1;
			 /* Create a Workbook and Worksheet */
             HSSFWorkbook my_workbook = new HSSFWorkbook();
             HSSFSheet my_sheet = my_workbook.createSheet("MyBanner"); 
			 for(PageAllPostModel p: datanode.getData())
			 {
				 if(null!=p.getFull_picture())
				 {
					 Row row = my_sheet.createRow(count);
					 	
					 //first column
			            row.createCell(0)
			                    .setCellValue(1);

			            row.createCell(1)
			                    .setCellValue(p.getId());
		                /* Add Picture to workbook and get a index for the picture */
		                int my_picture_id = my_workbook.addPicture(fetchFile(p.getFull_picture()), Workbook.PICTURE_TYPE_JPEG);
		                /* Close Input Stream */
		                /* Create the drawing container */
		                HSSFPatriarch drawing = my_sheet.createDrawingPatriarch();
		                /* Create an anchor point */
		                ClientAnchor my_anchor = new HSSFClientAnchor();
		                /* Define top left corner, and we can resize picture suitable from there */
		                //set second column
		                my_anchor.setCol1(10);
		                my_anchor.setRow1(count);  
		                
		                /* Invoke createPicture and pass the anchor point and ID */
		                HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
		                /* Call resize method, which resizes the image */
		                my_picture.resize();  
				 }
		                count++;
			 }
			 FileOutputStream out = new FileOutputStream(new File(excelfilename));
             my_workbook.write(out);
             out.close();
				System.out.println(excelfilename + " written successfully");
			 
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
	public static  byte[] fetchFile(String url) throws IOException 
	{

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(
	            new ByteArrayHttpMessageConverter());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

	    HttpEntity<String> entity = new HttpEntity<String>(headers);

	    ResponseEntity<byte[]> response = restTemplate.exchange(
	    		url,
	            HttpMethod.GET, entity, byte[].class, "1");

	    if (response.getStatusCode() == HttpStatus.OK) 
	    {
	        Files.write(Paths.get("google.png"), response.getBody());
	    }
	    return response.getBody();
	}

}
