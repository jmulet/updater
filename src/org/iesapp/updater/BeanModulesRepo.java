/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

import java.util.ArrayList;
import org.iesapp.util.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Josep
 */
public class BeanModulesRepo {
    protected String className="";
    protected String name="";
    protected String latestVersion="";
    protected final ArrayList<BeanModuleVersion> moduleVersions = new ArrayList<BeanModuleVersion>();

    BeanModulesRepo(Node item, NodeList moduleVersionList) {
        NamedNodeMap attributes = item.getAttributes();
        Node namedItem = attributes.getNamedItem("class");
        Node namedItem1 = attributes.getNamedItem("name");
        if(namedItem!=null)
        {
            className = namedItem.getNodeValue();
        }
        if(namedItem1!=null)
        {
            name = namedItem1.getNodeValue();
        }
        
        String last="0.0";
        
        for(int i=0; i<moduleVersionList.getLength(); i++)
        {
            Node item1 = moduleVersionList.item(i);
            NamedNodeMap attributes1 = item1.getAttributes();
            Node namedItem3 = attributes1.getNamedItem("name");
            Node namedItem2 = attributes1.getNamedItem("file");
            Node namedItem4 = attributes1.getNamedItem("history");
            BeanModuleVersion beanModuleVersion = new BeanModuleVersion();
            if(namedItem2!=null)
            {
                beanModuleVersion.setFileName(namedItem2.getNodeValue());
            }
            if(namedItem3!=null)
            {
                String ver = namedItem3.getNodeValue();
                beanModuleVersion.setVersion(ver);
                if(StringUtils.compare(ver, last)>0)
                {
                    last = ver;
                }
            }
            if(namedItem4!=null)
            {
                beanModuleVersion.setWhatsnew(namedItem4.getNodeValue());
            }
            moduleVersions.add(beanModuleVersion);
        }
        
        latestVersion =last;
   }
   
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public BeanModuleVersion getLastVersionBean()
    {
        BeanModuleVersion last = null;
        String lastVersion = getLatestVersion();
        for(BeanModuleVersion bean: getModuleVersions())
        {
            if(bean.getVersion().equalsIgnoreCase(lastVersion))
            {
                last = bean;
                break;
            }
        }
        return last;
        
    }
    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public ArrayList<BeanModuleVersion> getModuleVersions() {
        return moduleVersions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("* Repo Module: ");
        builder.append(name).append(" class=").append(className).append(" lastVersion=").append(latestVersion);
        for(BeanModuleVersion b: moduleVersions)
        {
            builder.append(b.toString());
        }
        return builder.toString();
    }
}
