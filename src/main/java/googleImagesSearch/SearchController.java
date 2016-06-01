package googleImagesSearch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

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
		
		File temp_file=convert(file);
		String path="http://www.cbssports.com/images/blogs/nike-football.jpg";
		temp_file=saveImageforUrl(temp_file);
		String name=temp_file.getName();
		System.out.println(name);
		
		
		//System.out.println("http://flickwiz.xululabs.us/sites/default/files/appimages/"+name);
		Document data=Starter.getResult("http://flickwiz.xululabs.us/sites/default/files/appimages/"+name);
		//Document data=Starter.getResult(path);
		
			url=data.location();
			System.err.println(url);
			Elements e=data.getElementsByClass("_gUb");
			movieName=e.get(0).text().toString();
			System.err.println(safeCard+movieName+imbdCard);
		
			finaleUrl=url+safeCard+movieName+imbdCard;
			
			
		Elements d=data.select("#rso >.srg > .g:first-child");
		//System.out.println(d.toString());
		Elements links=d.select("a");
		//System.out.println(links.toString());
		
		String temp=getUrl(links);
		
		


		if(temp=="")
		{
			//Going for 2nd option..
			
			Document dataWithCard=Starter.secondSearch(finaleUrl);
			Elements dd=dataWithCard.select("#rso >.srg > .g:first-child");
			//System.out.println(d.toString());
			Elements linkhref=dd.select("a");
			//System.out.println(linkhref.toString());		
			
			String tempLink=getUrl(linkhref);
				if(tempLink=="")
				{
					movieDetails.add("NO IMDB DETAILS IN SEARCH");
					System.out.println("File deleted"+temp_file.exists());
					//System.out.println(temp_file.delete());
					
				}else{
			movieId=getMovieId(tempLink);
			movieDetails=Services.getImdbData(movieId);
			
			
			System.out.println("File deleted"+temp_file.exists());
			//System.out.println(temp_file.delete());
				}
			
			
			System.out.println("File deleted"+temp_file.exists());
			//System.out.println(temp_file.delete());
			return movieDetails;
		}else{
			movieId=getMovieId(temp);
			movieDetails=Services.getImdbData(movieId);
			
		
		System.out.println(movieDetails.toString());
		System.out.println("File deleted"+temp_file.exists());
		//System.out.println(temp_file.delete());
		
		}
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


	private static File saveImageforUrl(File file) throws IOException
	{
		System.out.println("In the file save method");
		 
		File temp=new File(BASE_PATH+"/temp"+System.currentTimeMillis()+"img.jpg");
		//File temp=new File("temp"+System.currentTimeMillis()+"img.jpg");
		 	BufferedImage image=ImageIO.read((File) file);
		 	
		 	ImageIO.write(image, "jpg",temp);
		 	
		return temp;
	}
	
	public File convert(MultipartFile file) throws IOException
	{    
		System.out.println("In the file conversion method");
		File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}
}
