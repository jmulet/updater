/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iesapp.util.StringUtils;

public class FileDownloader extends Observable implements Runnable {
    
   public static final int DOWNLOADING =0;
   public static final int PAUSED =1;
   public static final int COMPLETE =2;
   public static final int CANCELLED =3;
   public static final int ERROR =4;
   
   private int status;
   private long downloadedBytes;
   private long size;
   private final static int MAX_BUFFER_SIZE = 1024;
   
   private static boolean isRedirected( Map<String, List<String>> header ) {
      for( String hv : header.get( null )) {
         if(   hv.contains( " 301 " )
            || hv.contains( " 302 " )) return true;
      }
      return false;
   }
    private final String urlLink;
    private File outputFile;
    private int progress;
    private double averageRate = 0.0;
   
   
   public FileDownloader(String urlLink, File outputFile)
   {
       this.urlLink = urlLink;
       this.outputFile = outputFile;
      
       //Check if outputFile already exists
       int id = 1;
       while(this.outputFile.exists())
       {
           String absolutePath = outputFile.getParentFile().getAbsolutePath();
           String fullName = outputFile.getName();
           String ext = StringUtils.AfterLast(fullName, ".");
           String name = StringUtils.BeforeLast(fullName, ".");
           this.outputFile = new File(absolutePath+File.separator+name+"_"+id+"."+ext);
           id +=1;
       }
       
       progress = 0;

   }
   
   public int getStatus()
   {
       return status;
   }
   
   public int getProgress()
   {
       return progress;
   }
   
   public void pause()
   {
       status = PAUSED;
       stateChanged();
   }
   
   public void resume()
   {
       status = DOWNLOADING;
       stateChanged();
       download();
   }
   
   public void cancel()
   {
       status = CANCELLED;
       //reset if user tries to resume again
       downloadedBytes = 0;
       progress = 0;

       stateChanged();
   }
   
   private void error()
   {
       status = ERROR;
       stateChanged();
   }
   
   public void download()
   {
       Thread thread = new Thread(this);
       thread.start();
   }
   
   @Override
   public void run() 
   {
       RandomAccessFile rndFile = null;
       InputStream input = null;
      
       try {
           URL url  = new URL( urlLink );
           HttpURLConnection http = (HttpURLConnection) url.openConnection();
           Map< String, List< String >> header = http.getHeaderFields();
           
           String link = urlLink;
           while( isRedirected( header )) {
               link = header.get( "Location" ).get( 0 );
               url    = new URL( link );
               http   = (HttpURLConnection) url.openConnection();
               header = http.getHeaderFields();
           }
           
           http.connect();
           if(http.getResponseCode()!=200)
           {
               status = ERROR;
               stateChanged();
           }
           
           int contentLength = http.getContentLength();
           if(contentLength<1)
           {
               status = ERROR;
               stateChanged();
           }
           size = contentLength;
           
           rndFile = new RandomAccessFile( outputFile, "rw");
           rndFile.seek(downloadedBytes);
           input  = http.getInputStream();
           
           long start = System.currentTimeMillis();
           
           while(status == DOWNLOADING)
           {
               byte buffer[];
               if(size - downloadedBytes > MAX_BUFFER_SIZE)
               {
                   buffer = new byte[MAX_BUFFER_SIZE];
               } else
               {
                   buffer = new byte[(int) (size-downloadedBytes)];
               }
               
               int read = input.read(buffer);
               if(read==-1)
               {
                   break;
               }
               
               rndFile.write(buffer, 0, read);
               downloadedBytes += read;
               long end = System.currentTimeMillis();
               if(end > start)
               { 
                   // Rate in Bytes / second
                   double currentRate = downloadedBytes * 1000 / (end - start); 
                   averageRate = (averageRate + currentRate)/2;
               }
               
               progress = Math.round(downloadedBytes*100/(1f*size));
               stateChanged();
               
           }
           
           if(status==DOWNLOADING)
           {
               status = COMPLETE;
               stateChanged();
           }
       } catch (Exception ex) {
           status = ERROR;
           stateChanged();
       }
       finally{
           if(rndFile!=null)
           {
               try {
                   rndFile.close();
               } catch (IOException ex) {
                   Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           
           if(input!=null)
           {
               try {
                   input.close();
               } catch (IOException ex) {
                   Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       }
     
   }

    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
    
    
    //Test
    public static void main(String[] args)
    {
        File f = new File("c:\\kk.zip");
        FileDownloader fd = new FileDownloader("https://dl.dropboxusercontent.com/u/92714512/org-iesapp-modules-reserves-2.0.mod", f);
        fd.download();
    }

    public String getFileInfo() {
        return outputFile.getName()+ " "+ displaySize(size);
    }

    public File getOutputFile() {
        return outputFile;
    }

    private String displaySize(long size) {
        if(size<1024)
        {
            return size + " B";
        }
        else if(1024<=size && size<1024*1024)
        {
            return roundDecimals(size/1024f,1) + " KB";
        }
        else if(1024*1024<=size)
        {
            return roundDecimals(size/(1024f*1024f),1) + " MB";
        }
        return size + " B";
    }

    private double roundDecimals(double val, int dec) {
        double pow = Math.pow(10, dec);
        return Math.round(val*pow)/pow;
    }

    public String getTimeRemaining() {
        String output =  "";
        long remainingBytes = size - downloadedBytes;
        long seconds = Math.round( remainingBytes/averageRate );
        if(seconds>0)
        {
            output = StringUtils.displayComplexTime(seconds);
        }
        return output;
    }

    /**
     * Returns average download rate in KB/s
     * @return 
     */
    public double getDownloadRate()
    {
        return roundDecimals(averageRate/1000f,1);
    }
    
}