/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Josep
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {
        try 
        {
//            String latestVersion = RemoteUpdater.getLatestVersion();
//            System.out.println(latestVersion);
//            System.out.println(RemoteUpdater.getWhatsNew());
//            System.out.println(RemoteUpdater.getModulesRepo());
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
