package googleImagesSearch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/*
 * &safe=active&q=american%20sniper
 */

@RestController
public class SearchController {

	
	//private final static  String BASE_PATH="images/"; //for local
	private final static  String BASE_PATH="/mnt/www/flickwiz.xululabs.us/htdocs/sites/default/files/appimages/"; //for server
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public LinkedList<String> searchImage(@RequestParam ("file") MultipartFile file) throws IOException
	{
		LinkedList<String> movieDetails=new LinkedList<String>();
		String movieId="";
		String movieName="";
		String finaleUrl="";
		String url="";
		String imbdCard=" site:imdb.com";
		String safeCard="&safe=active&q=";
		Scanner input =new Scanner(System.in);
		
		File temp_file=convert(file);
		//String path="https://www.sideshowtoy.com/assets/products/902040-iron-man-mark-17-heartbreaker/lg/902040-iron-man-mark-17-heartbreaker-007.jpg";
		//System.out.println("Enter the URL of the Images...");
		//String path=input.nextLine();
		temp_file=saveImageforUrl(temp_file);
		String name=temp_file.getName();
		System.out.println(name);
		
		
		//System.out.println("http://flickwiz.xululabs.us/sites/default/files/appimages/"+name);
		Document data=Starter.getResult("http://flickwiz.xululabs.us/sites/default/files/appimages/"+name);
		//Document data=Starter.getResult(path);
		
			url=data.location();
			//System.err.println(url);
			Elements e=data.getElementsByClass("_gUb");
			movieName=e.get(0).text().toString();
			//System.err.println(safeCard+movieName+imbdCard);
		
			finaleUrl=url+safeCard+movieName+imbdCard;
			
			
		Elements d=data.select("#rso >.srg > .g:first-child");
		//System.out.println(d.toString());
		Elements links=d.select("a");
		//System.out.println(links.toString());
		String temp=getUrl(links);
		
		


		if(temp=="")
		{
			//Going for 2nd option..
			System.err.println(finaleUrl);
			Document dataWithCard=Starter.secondSearch(finaleUrl);
			Elements dd=dataWithCard.select("#rso >.srg > .g:first-child");
			//System.out.println(d.toString());
			Elements linkhref=dd.select("a");
			//System.out.println(linkhref.toString());		
			
			String tempLink=getUrl(linkhref);
				if(tempLink=="")
				{
					movieDetails.add("NO IMDB DETAILS IN SEARCH");
					System.out.println("File deleted from 2nd IF block "+temp_file.exists());
					System.err.println(temp_file.delete());
					System.out.println("File deleted from 2nd IF block "+new File(file.getOriginalFilename()).exists());
					System.err.println(new File(file.getOriginalFilename()).delete());
					
				}else{
					
			movieId=getMovieId(tempLink);
			movieDetails=Services.getImdbData(movieId);
			
			
			System.out.println("File deleted from 2nd ELSE block "+temp_file.exists()+" ");
			System.err.print(temp_file.delete()+" ");
			System.out.print(new File(file.getOriginalFilename()).exists()+" ");
			System.err.print(new File(file.getOriginalFilename()).delete()+" ");
				}
			
			
			//System.out.println("File deleted"+temp_file.exists());
			//System.out.println(temp_file.delete());
			System.out.println("Output: "+movieDetails.toString());
			return movieDetails;
		}else{
			movieId=getMovieId(temp);
			movieDetails=Services.getImdbData(movieId);
			
		
		System.out.print("File deleted from 1st ELSE block "+temp_file.exists()+" ");
		System.out.print(temp_file.delete()+" ");
		System.out.print(new File(file.getOriginalFilename()).exists()+" ");
		System.err.print(new File(file.getOriginalFilename()).delete()+" ");
		}
		
		System.out.println("Output: "+movieDetails.toString());
		return movieDetails;
	}
	
	
	private String getMovieId(String temp) {
		
		String id="";
	
		System.out.println("The url is :"+temp);
		
		int beginIndex=temp.indexOf("/tt")+1;
		int endIndex=beginIndex+9;
		
		
		
		System.out.print("Start index : "+beginIndex);
		System.out.println(" End index : "+endIndex);
		
		id=temp.substring(beginIndex, endIndex);
		
		System.out.println("The id of the movie is : "+id);
		
		return id;
	}


	private String getUrl(Elements links) {
		String site="";
		String temp="";
		for(int i=0;i<links.size();i++)
		{
			 site=links.get(i).attr("href");
				if(site.contains(("http://www.imdb.com/title")))
					{
						temp=site;
					System.err.println(site);
					return site;
					}else{
						//System.out.println("Does not contain this uurl");
					}
		}
		return temp;
	}


	private static File saveImageforUrl(File file) 
	{
		System.out.println("In the file save method");
		 
		File temp=new File(BASE_PATH+"/temp"+System.currentTimeMillis()+"img.jpg");
		//File temp=new File("temp"+System.currentTimeMillis()+"img.jpg");
		 	BufferedImage image = null;
			try {
				image = ImageIO.read((File) file);
			} catch (IOException e) {
				System.err.println("Error is in Images Reading.");
				e.printStackTrace();
			}
		 	
		 	try {
				ImageIO.write(image, "jpg",temp);
			} catch (IOException e) {
				System.err.println("Error is in Images Writing.");
				e.printStackTrace();
			}
		 	
		return temp;
	}
	
	public File convert(MultipartFile file) 
	{    
		System.out.println("In the file conversion method");
		File convFile = new File(file.getOriginalFilename());
	    try {
			convFile.createNewFile();
		} catch (IOException e) {
			System.err.println("Error is creating new Temp file ");
			e.printStackTrace();
		} 
	    FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		}
		catch (IOException e) {
			System.err.println("Error is converting file.");
			e.printStackTrace();
		} 
	    return convFile;
	}
}
