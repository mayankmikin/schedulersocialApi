package com.socialapi.scheduler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Test {
	static String timeStamp = "2014-12-14T18:23:17+0000";
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
	static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) throws IOException {
		/*try {
			System.out.println("Unix timestamp: " + dateFormat.parse(timeStamp).getTime());
		System.out.println(dateFormat2.format(new Date(dateFormat.parse(timeStamp).getTime())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String csvFile ="src/main/resources/some.csv";
        FileWriter writer = new FileWriter(csvFile);

        List<Developer> developers = Arrays.asList(
                new Developer("mkyong", new BigDecimal(120500), 32),
                new Developer("zilap", new BigDecimal(150099), 5),
                new Developer("ultraman", new BigDecimal(99999), 99)
        );

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Name", "Salary", "Age"));
		
        for (Developer d : developers) {

            List<String> list = new ArrayList<>();
            list.add(d.getName());
            list.add(d.getSalary().toString());
            list.add(String.valueOf(d.getAge()));

            CSVUtils.writeLine(writer, list);

			//try custom separator and quote. 
			//CSVUtils.writeLine(writer, list, '|', '\"');
        }

        writer.flush();
        writer.close();*/
		checkCSVreader();
	}
	
	
	public static void checkCSVreader()
	{
		
	}
	
	public static Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    return cal.getTime();
	}

}
