package googleImagesSearch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SearchController {

	
	//private final static  String BASE_PATH="images/"; //for local
	private final static  String BASE_PATH="/mnt/www/flickwiz.xululabs.us/htdocs/sites/default/files/appimages/"; //for server
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public LinkedList<String> searchImage(@RequestParam ("file") MultipartFile file) throws IOException
	{
		File temp_file=convert(file);
		String path="http://flickwiz.xululabs.us/sites/default/files/MV5BMTgyNDA1MTkyM15BMl5BanBnXkFtZTgwMDE0MTg5NjE%40._V1_SX640_SY720_.jpg";
		temp_file=saveImageforUrl(temp_file);
		String name=temp_file.getName();
		System.out.println(name);
		
		
		System.out.println("http://flickwiz.xululabs.us/sites/default/files/appimages/"+name);
		Document data=Starter.getResult("http://flickwiz.xululabs.us/sites/default/files/appimages/"+name);
		//Document data=Starter.getResult(path);
		Elements d=data.select("#rso >.srg > .g:first-child");
		//System.out.println(d.toString());
		Elements links=d.select("a");
		//System.out.println(links.toString());
		
		String temp=getUrl(links);
		String movieId=getMovieId(temp);
		
		LinkedList<String> movieDetails=Services.getImdbData(movieId);
		
		System.out.println(movieDetails.toString());
		
		System.out.println("File deleted"+temp_file.exists());
		//System.out.println(temp_file.delete());
		return movieDetails;
	}
	
	
	private String getMovieId(String temp) {
		
		String id="";
	
		System.out.println("The url is :"+temp);
		
		int beginIndex=temp.indexOf("/tt")+1;
		int endIndex=temp.length()-1;
		
		System.out.println("Start index : "+beginIndex);
		System.out.println("End index : "+endIndex);
		id=temp.substring(beginIndex, endIndex);
		
		System.out.println("The id of the movie is : "+id);
		
		return id;
	}


	private String getUrl(Elements links) {
		String site="";
		for(int i=0;i<links.size();i++)
		{
			 site=links.get(i).attr("href");
				if(site.contains(("http://www.imdb.com/title")))
					{
					System.err.println(site);
					return site;
					}else{System.out.println("Does not contain this uurl");}
		}
		return site;
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
