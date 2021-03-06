/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacrawler;

import com.fileio.crawler.WriteToFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javacrawler.PatternMatcher;

/**
 *
 * @author ubuntu
 */
public class JavaCrawler {

    /**
     * @param args the command line arguments
     */
    public void startCrawler() throws IOException {
        URL url;
        HashSet<String> hsUrl = new HashSet<String>();
        long totalNumberOfCrawledUrl = 0;
        SeedUrl seedObj = new SeedUrl();
        ArrayList<String> urlList = seedObj.getSeedUrl();
        HashSet<String> hsReturned=new HashSet<String>();
        long previousCountOfUrl=0;
        // For each URL in the url List iterating and getting the html and 
        // parsing each line to find a url  
        for (String urls : urlList) {
            try {
                HashSet<String> eachUrlHashSet=new HashSet<String>();
                url = new URL(urls);
                long totalLinksFromEachSite = 0;
                PatternMatcher objOfPatternMatcher = new PatternMatcher();
                System.out.println(url);
                String inputLine;
                // Open connection to the seed page
                URLConnection connObj = url.openConnection();

                // Open a stream and put the contents in the stream
                String workingDir=System.getProperty("user.dir");
                System.out.println("Current Working Directorys is");
                System.out.println(workingDir);
                String fileName = workingDir+"/src/com/crawler/crawledFile.txt";
                System.out.println(fileName);
                BufferedReader bfr = new BufferedReader(new InputStreamReader(connObj.getInputStream()));
                File file = new File(fileName);
                if (!file.exists()) {
                    System.out.println("File Does Not Exists Creating New File");
                    file.createNewFile();
                }
                // Using file writer to write to a file
                //FileWriter fw=new FileWriter(file.getCanonicalFile())
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                previousCountOfUrl=hsReturned.size();
                while ((inputLine = bfr.readLine()) != null) {
                    bw.write(inputLine);
                    //totalLinksFromEachSite += objOfPatternMatcher.ifUrl(inputLine, hsUrl,eachUrlHashSet);
                    //totalNumberOfCrawledUrl += objOfPatternMatcher.ifUrl(inputLine,hsUrl,eachUrlHashSet);
                    // Uncomment to print the complete HTML System.out.println(inputLine);
                    
                    hsReturned=objOfPatternMatcher.ifUrl(inputLine, hsUrl,eachUrlHashSet);
                    
                    
                } // parsing of html file ends
                System.out.println(hsReturned);
                WriteToFile writeToFileObj=new WriteToFile();
                writeToFileObj.writeHashSetToFile(hsReturned);
                totalLinksFromEachSite=hsReturned.size()-previousCountOfUrl;
                System.out.println("Total number of crawled URL for " + urls + "website is " + totalLinksFromEachSite);
                
                bw.close();
                bfr.close();
                System.out.println("Total number of crawled URL is " + hsReturned.size());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  // for ends

    } // function ends

    public static void main(String[] args) throws IOException {
//        String result="";
//        Properties prop=new Properties();
//        String propFileName="config.properties";
//        InputStream inputStream;
//        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
//        prop.load(inputStream);
//        if(inputStream==null)
//        {
//            throw new FileNotFoundException("Property file not found.Please add the config.properties file");
//        }
        JavaCrawler crawlerObj = new JavaCrawler();
        crawlerObj.startCrawler();

    } //function ends
}
