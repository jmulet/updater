/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.iesapp.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Josep
 */
public class RemoteUpdater {
    private  String baseURL = "https://raw.github.com/jmulet/iesdigital/master/";
    private  String versionURL = baseURL+"version.xml";
    private  String modulesRepo = baseURL+"repository.xml";
             
    private  String cacheData;
    private  String modulesRepoData;
    private  String versionData;

   /**
    * url or list of urls (at the moment a single url is supported)
    * @param url 
    */ 
    public RemoteUpdater(String url) {
        this.baseURL = url;
    }
    
    public String getLatestVersion() throws Exception, Throwable
    {
        String data = getDataVersion();
        return data.substring(data.indexOf("<version>")+9, data.indexOf("</version>"));
    }
    
    public String getWhatsNew() throws Exception
    {
        String data = getDataVersion();
        return data.substring(data.indexOf("<history>")+9, data.indexOf("</history>"));
    }
    
    public RemoteRepository getModulesRepo()
    {
        RemoteRepository repo = new RemoteRepository();
        try {
            String data = getDataModulesRepo();
            data = StringUtils.BeforeLast(data, "</root>")+"</root>";
            //Parse it with xml dom
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document parse = b.parse(new ByteArrayInputStream(data.getBytes()));
            
            Element distros = (Element) parse.getElementsByTagName("distributions").item(0);
            NodeList distrosList = distros.getElementsByTagName("distribution");
            for(int i=0; i<distrosList.getLength(); i++)
            {
                Node item = distrosList.item(i);
                repo.getDistros().add(new BeanDistroRepo(item));
            }
            
            Element modules = (Element) parse.getElementsByTagName("modules").item(0);
            NodeList moduleList = modules.getElementsByTagName("module");
            for(int i=0; i<moduleList.getLength(); i++)
            {
                Node item = moduleList.item(i);
                NodeList moduleVersionList = ((Element) item).getElementsByTagName("version");
                repo.getModules().add(new BeanModulesRepo(item, moduleVersionList));
            }
            
            Element plugins = (Element) parse.getElementsByTagName("plugins").item(0);
            NodeList pluginList = plugins.getElementsByTagName("plugin");
            for(int i=0; i<pluginList.getLength(); i++)
            {
                Node item = pluginList.item(i);
                NodeList pluginVersionList = ((Element) item).getElementsByTagName("version");
                repo.getPlugins().add(new BeanModulesRepo(item, pluginVersionList));
            }
            
        } catch (Exception ex) {
            Logger.getLogger(RemoteUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
        return repo;
    }
      
    /**
     * Reads file as string from url address
     * @param address
     * @return
     * @throws Exception 
     */
    private String getData(String address) throws Exception
    {
            URL url = new URL(address);
            InputStream html = null;
            html = url.openStream();
            int c = 0;
            StringBuilder buffer = new StringBuilder("");
            while(c != -1){
                c = html.read();
                buffer.append((char) c);
            }
          return buffer.toString();
    }

    private String getDataModulesRepo() throws Exception {
        if(modulesRepoData == null)
        {
            modulesRepoData = getData(modulesRepo);
        }
        return modulesRepoData;
    }

    private String getDataVersion() throws Exception {
        if(versionData == null)
        {
            versionData = getData(versionURL);
        }
        return versionData;
    }

    public void setBaseURLs(String urls) {
        baseURL = urls;
        versionURL = baseURL+"version.xml";
        modulesRepo = baseURL+"repository.xml";
        clearCache();
    }
    
    public void clearCache()
    {
        modulesRepoData = null;
        versionData = null;        
    }

    
    
}
