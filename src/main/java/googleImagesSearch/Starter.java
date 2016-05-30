package googleImagesSearch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mashape.unirest.http.exceptions.UnirestException;

public class Starter {

	public static void main(String[] args) throws UnirestException, IOException {
		
		File file=new File("430915.jpg");
		FileInputStream stream=new FileInputStream(new File("430915.jpg"));
		final byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		stream.close();
		
		String name=file.getName();
		
		System.out.println("This is file name"+name);
		
//		HttpResponse<String> response = Unirest.post("https://images.google.com//searchbyimage/upload")
//				  .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundaryY9ddncd4JktnN0F6")
//				  .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//				  .header("Upgrade-Insecure-Requests", "1")
//				  .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
//				  //.header("", "")
//				  //.header("", "")
//				  .header("cache-control", "no-cache")
//				 // .body("-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"encoded_image\"; filename=\""+file.getName()+"\"\r\nContent-Type: false\r\n\r\n\r\n-----011000010111000001101001--")
//				 
//				  .body("------WebKitFormBoundaryY9ddncd4JktnN0F6\r\nContent-Disposition: form-data; name=\"encoded_image\"; filename=\"430915.jpg\"\r\nContent-Type: image/jpg\r\n\r\n\r\n------WebKitFormBoundaryY9ddncd4JktnN0F6\r\nContent-Disposition: form-data; name=\"hl\"\r\n\r\nen\r\n------WebKitFormBoundaryY9ddncd4JktnN0F6--\r\n")
//				  .asString();
//		
//		
//		System.out.println(response.getStatus());
//		System.out.println(response.getHeaders());
//		
//		System.out.println(response.getStatusText());
//		System.out.println(response.getStatusText());
//		List<String> data=new ArrayList<String>();
//		
//		
//		Headers head=response.getHeaders();
//		
//		data=head.get("Location");
//		
//		System.out.println(data);
//		
//		String targetUrl=trimUrl(data.get(0));
//		
//		HttpResponse<String> page=Unirest.get(targetUrl)
//				//.header("content-type", "text/html; charset=UTF-8")
//				//.header("cache-control", "no-cache")
//				.asString();
//		
//		System.out.println(page.getStatusText());
//		System.out.println(page.getStatus());
//		System.out.println(page.getBody());
//		
//		InputStream input=new FileInputStream(file);
//		
//		Connection.Response responsee = Jsoup
//		        .connect("https://images.google.com//searchbyimage/upload")
//		        .method(Connection.Method.POST)
//		        .timeout(50000)
//		        //.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//		        //.header("Referer","https://www.google.com/")
//		        //.header("Origin","https://www.google.com/")
//		       // .header("Upgrade-Insecure-Requests","1")
//		        .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
//		        .header("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryY9ddncd4JktnN0F6")
//		        .data("encoded_image",name,input)
//		        .followRedirects(true)
//		        .execute();
//
//		System.out.println(responsee.header("Location"));
//		System.out.println(responsee.statusCode());
//		
		Document document1=Jsoup.connect("https://images.google.com//searchbyimage?image_url=http://images.all-free-download.com/images/graphiclarge/daisy_pollen_flower_220533.jpg")
				.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				//.header("Referer","https://images.google.com/")
				.header("Upgrade-Insecure-Requests","1")
				.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
				.get();
		
		BufferedWriter wr1=new BufferedWriter(new FileWriter("index2.html"));
		wr1.write(document1.toString());
		wr1.close();
		//System.out.println(document);
		
		
		Document document=Jsoup.connect("https://www.google.com/search?tbs=sbi:AMhZZiv-4xYDdmUe60QNB_1XuePkqmDc5i97c7NAaf5NaNdNQjXMZxrnKvpGwG2kRUJnX4wX48gJm4Dyt1ceXBi02iFKU8lt8tZM-Ggsy6B0ww6b7Y7yP5wndSDdnl6WRizcnnZv2ENe8JP80nrMdGcEmgg4wBcLZXPeRXNaXEerwPB1sO3IpIHtP3apHEMWTBBIMFncEgIG6wGhpFldmjOnCDFpzGriA7AmAQmOB5OPjUuSC5K4s-9X5Teo0OgvgEma7-8YJzP6v8a5DD8s-1NQL1fbtyp3TZkIzJnsezs4UBT0X32s_1rBKc7oxhSrNNHoWLyTSeH2CgsDoRuYhUFoygNL6W35s5WHWzzUJOxt6yV1h6jiFZmcC73ATTaM4zOPOCMIgYtxjro3-gzZy3ranQlNr_1MzfPe1Spb_1xHSfZru7SuRhaWMauZ3jwWLxBqRMeJ0gb8xsNRdrDjaQUmkRkRrZFbidCSRmg-KyVqK-bB4kfLaq1ddGYOuW_1t1_1wxep7wLmQF7o-ftOspLVesbnFROKJnJ6F3PTUdB4aqic1o3HJfHUYCOsPdo0kKcki02jo5Cw8uFIFtBusmc7XwHFQxJnMtFWWhLhPBM8jDpj1KJ-TnsUhhD7mwVjxgWxmYyNs466ehaxJqRV_1y7rFP5LF1DgRzetvMe9KQgN7OnwFIX_14CX3JUBJUOxDftGXdBzIkIILbeOLeqqmGklsG2k9JFaUbX3wihmGJ_1vU70BskwYcsudnx4_171ypcE9xBqj92Bn5wip6jfMbhqbC6hqJhZc7VZAvSmXL0xnH-WbvqnfWu784Uokw9VTHI7MO8Y4LDZ1YG5KvKQzvJ-_1C8rjon5EBw7-ZI5kHmEDXgEnuQPt5QppO46v2WtqjEj-IgkX1b_1uF69Fc5Z_1ovYMQbHp86eo4jkm-_12oiRMymF-oX245X2YF4yoa0PFz2f-fwHYJ0YfirHbAZQK1zJP1gAOQilo7Pw2Ns1lIqrly7szBLZ_1wp0NIuk5u72nv3eltRSjOKOR368vaNVQXNv0TiJCuQFaZpflHO5S7KsHLSGR4vmv22_1M5j4f4loVwajnMZ6zrCwlXm6R-eS6GAwu1kWQXYFPoEAp08vOHdYZRcrx2ajzNQ7RKHVnq-IOZBnc6Xy-XoRAgx2NH69R85V5LVfI_1TInANlymwmghejKpDeDB7pKgnE_1ywri6EtexuY8e25tbcO7pzFA6iXBT&hl=en")
				.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				//.header("Referer","https://images.google.com/")
				.header("Upgrade-Insecure-Requests","1")
				.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
				.get();
		
		BufferedWriter wr=new BufferedWriter(new FileWriter("index.html"));
		wr.write(document.toString());
		wr.close();
		//System.out.println(document);
	}

	
	public  static Document getResult(String url) throws IOException
	{
		Document document1=Jsoup.connect("https://images.google.com//searchbyimage?image_url="+url)
	//http://images.all-free-download.com/images/graphiclarge/daisy_pollen_flower_220533.jpg")
				.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				//.header("Referer","https://images.google.com/")
				.header("Upgrade-Insecure-Requests","1")
				.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
				.get();
		
		BufferedWriter wr1=new BufferedWriter(new FileWriter("index2.html"));
		wr1.write(document1.toString());
		wr1.close();
		//System.out.println(document);
		return document1;
	}
	
	
	private static String trimUrl(String data) {
		
		data=data.replace("[","");
		data=data.replace("]","");
		System.out.println(data);
		return data;
	}

}
