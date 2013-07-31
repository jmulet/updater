/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

import java.util.ArrayList;
import org.iesapp.util.StringUtils;

/**
 *
 * @author Josep
 */
public class RemoteRepository {
    protected final ArrayList<BeanModulesRepo> modules;
    protected final ArrayList<BeanModulesRepo> plugins;
    protected final ArrayList<BeanDistroRepo> distros;
    
    public RemoteRepository()
    {
        modules = new ArrayList<BeanModulesRepo>();
        plugins = new ArrayList<BeanModulesRepo>();
        distros = new ArrayList<BeanDistroRepo>();
    }

    public ArrayList<BeanModulesRepo> getModules() {
        return modules;
    }

    public ArrayList<BeanModulesRepo> getPlugins() {
        return plugins;
    }

    public ArrayList<BeanDistroRepo> getDistros() {
        return distros;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("IESDIGITAL REPOSITORY.........\n");
        for(BeanDistroRepo dist: distros){
            builder.append("\n").append(dist);
        }
        
        for(BeanModulesRepo dist: modules){
            builder.append("\n").append(dist);
        }
         
        for(BeanModulesRepo dist: plugins){
            builder.append("\n").append(dist);
        }
        return builder.toString();
    }
    
    //Utilities
    public String getLastVersionForModule(String className)
    {
        String last = null;
        for(BeanModulesRepo bmr: modules)
        {
            if(bmr.getClassName().equalsIgnoreCase(className))
            {
                last = bmr.getLatestVersion();
                break;
            }
        }
        return last;
    }
            
    public String getLastVersionForPlugin(String className)
    {
        String last = null;
        for(BeanModulesRepo bmr: plugins)
        {
            if(bmr.getClassName().equalsIgnoreCase(className))
            {
                last = bmr.getLatestVersion();
                break;
            }
        }
        return last;
    }

    public BeanModuleVersion getLastVersionBeanForModule(String className) {
        BeanModuleVersion last = null;
        for(BeanModulesRepo bmr: modules)
        {
            if(bmr.getClassName().equalsIgnoreCase(className))
            {
                last = bmr.getLastVersionBean();
                break;
            }
        }
        return last;
    }

    public BeanModulesRepo getRepoForModule(String className) {
        BeanModulesRepo last = null;
        for(BeanModulesRepo bmr: modules)
        {
            if(bmr.getClassName().equalsIgnoreCase(className))
            {
                last = bmr;
                break;
            }
        }
        return last;
    }
    
    public String getLastDistroVersion()
    {
        String version = "0.0";
        for(BeanDistroRepo bdr: distros)
        {
            if(StringUtils.compare(bdr.getVersion(), version)>0)
            {
                version = bdr.getVersion();
            }
                
        }
        return version;
    }
    
    public BeanDistroRepo getLastDistroBean()
    {
        String version = "0.0";
        BeanDistroRepo pointer = null;
        for(BeanDistroRepo bdr: distros)
        {
            if(StringUtils.compare(bdr.getVersion(), version)>0)
            {
                version = bdr.getVersion();
                pointer = bdr;
            }   
        }
        return pointer;
    }
}
