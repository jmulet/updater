/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

/**
 *
 * @author Josep
 */
public class BeanModuleVersion {
    protected String version;
    protected String fileName;
    protected String whatsnew;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getWhatsnew() {
        return whatsnew;
    }

    public void setWhatsnew(String whatsnew) {
        this.whatsnew = whatsnew;
    }
    
    @Override
    public String toString()
    {
         return "\n\t ...version="+version+" file="+fileName+" history="+whatsnew;
    }
}
