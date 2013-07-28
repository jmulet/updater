/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Josep
 */
public class BeanDistroRepo {
    protected String name="";
    protected String version="";
    protected String file="";

    public BeanDistroRepo(Node item) {
        NamedNodeMap attributes = item.getAttributes();
        Node namedItem = attributes.getNamedItem("name");
        Node namedItem1 = attributes.getNamedItem("version");
        Node namedItem2 = attributes.getNamedItem("file");
        if(namedItem!=null)
        {
            this.name = namedItem.getNodeValue();
        }
        if(namedItem1!=null)
        {
            this.version = namedItem1.getNodeValue();
        }
        if(namedItem2!=null)
        {
            this.file = namedItem2.getNodeValue();
        }
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
    @Override
    public String toString()
    {
        return " * DISTRO name="+name+" version="+version+" file="+file;
    }
}
