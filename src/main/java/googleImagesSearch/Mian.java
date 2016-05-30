package googleImagesSearch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Mian {

	public static void main(String[] args) throws MalformedURLException ,IOException
	{
		
		
		File file=new File("430915.jpg");
		String site="https://images.google.com//searchbyimage/upload";
		
		HttpURLConnection connection=null;
		DataOutputStream dos=null;
		DataInputStream inStream=null;
		
		
		String lineEnd="rn";
		String twoHyphens="--";
		String boundary="*****";
		byte[] buffer;
        int maxBufferSize = 1*1024*1024;
		int bytesRead,bytesAvailable,bufferSize;
		
		String result;
		
		//////////////  Client Request ///////////////
		
		FileInputStream fileInputStream=new FileInputStream(file);
		URL url=new URL(site);
		
		connection=(HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Connection","Keep-Alive");
		connection.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
		
		dos=new DataOutputStream(connection.getOutputStream());
		
		dos.writeBytes(twoHyphens+boundary+lineEnd);
		dos.writeBytes("Content-Disposition: form-data; name=encoded_image;"+"filename="+file.getName()+lineEnd);
        dos.writeBytes(lineEnd);
        
        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];
        // read file and write it into form...
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while (bytesRead > 0)
        {
         dos.write(buffer, 0, bufferSize);
         bytesAvailable = fileInputStream.available();
         bufferSize = Math.min(bytesAvailable, maxBufferSize);
         bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }
        // send multipart form data necesssary after file data...
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        // close streams
        System.out.println("File is written");
        fileInputStream.close();
        dos.flush();
        dos.close();
        
        Map<String, List<String>> map = connection.getHeaderFields();
    	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
    		System.out.println("Key : " + entry.getKey() + 
                     " ,Value : " + entry.getValue());
    	}
        
    	System.out.println(connection.getResponseCode());
    	
      //------------------ read the SERVER RESPONSE
        try {
              inStream = new DataInputStream ( connection.getInputStream() );
              String str;
 
              while (( str = inStream.readLine()) != null)
              {
                System.out.println(str);
              }
              inStream.close();
              
              System.out.println(connection.getHeaderFields());
        }
        catch (IOException ioex){
        	System.out.println("Debug error: " + ioex.getMessage());
        }
	}
	
	
}
