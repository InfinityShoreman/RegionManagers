package me.sirsavary.townmanager;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import lib.spywhere.MFS.MFS;
import lib.spywhere.MFS.StorageType;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
 
public class MFSConnector {
        private static Logger log=Logger.getLogger("Minecraft");
        private static MFS mfs=null;
        private static PluginManager pm=null;
       
        public static boolean isConnected(){
                return (mfs!=null);
        }
 
        static public MFS getMFS(PluginManager ipm,String url, String user, String password, StorageType type){
                loadPlugin(ipm);
                pm=ipm;
                if(pm.getPlugin("MFS")!=null){
                        if(!new File("lib",type.getFileName()).exists()){
                                log.info("[MFSConnector] Downloading "+type.getName()+" library from server...");
                                if(!downloadLib(type)){
                                        log.severe("[MFSConnector] Error downloading "+type.getName()+" library.");
                                        return null;
                                }
                                log.info("[MFSConnector] Download successful.");
                        }
                        mfs=(MFS)pm.getPlugin("MFS");
                        mfs.setMFS(url,user,password,type);
                        return mfs;
                }
                return null;
        }
 
        static public MFS getMFS(PluginManager ipm, StorageType type){
                return getMFS(ipm,"","","",type);
        }
 
        static private boolean downloadLib(StorageType type){
                try {
                        if(!new File("lib",type.getFileName()).exists()){
                                if(!type.equals(StorageType.FLATFILE)&&!type.equals(StorageType.YML)){
                                        download("lib",type.getLibraryURL(), type.getFileName());
                                }
                        }
                } catch (IOException e) {
                        return false;
                }
                return true;
        }
 
        static private boolean download(int time){
                try {
                        if(time>=2){return true;}
                        if(time==0){
                                download("plugins",Mirror.Server1.getUrl(), "MFS.jar");
                        }
                        if(time==1){
                                download("plugins",Mirror.Server2.getUrl(), "MFS.jar");
                        }
                } catch (IOException e) {
                        return false;
                }
                return true;
        }
 
        static private void loadPlugin(PluginManager pm){
                if(pm.getPlugin("MFS")==null){
                        try {
                                pm.loadPlugin(new File("plugins","MFS.jar"));
                        } catch (UnknownDependencyException e) {
                                log.severe("[MFSConnector] Error UnknownDependency: "+e.getMessage());
                        } catch (InvalidPluginException e) {
                                log.severe("[MFSConnector] Error InvalidPlugin: "+e.getMessage());
                        } catch (InvalidDescriptionException e) {
                                log.severe("[MFSConnector] Error InvalidDescription: "+e.getMessage());
                        }
                }else{
                        if(!pm.getPlugin("MFS").isEnabled()){
                                pm.enablePlugin(pm.getPlugin("MFS"));
                        }
                }
        }
 
        static public boolean prepareMFS(PluginManager pm){
                if(!new File("plugins","MFS.jar").exists()){
                        log.info("[MFSConnector] Downloading MFS.jar from server...");
                        int m=0;
                        while(!download(m)){
                                m++;
                                if(m<2){
                                        log.info("[MFSConnector] Downloading MFS.jar from Server "+(m+1)+"...");
                                }
                        }
                        if(m>=2){
                                log.severe("[MFSConnector] Error downloading MFS.jar.");
                                return false;
                        }else{
                                log.info("[MFSConnector] Download successful.");
                        }
                }
                log.info("[MFSConnector] Starting MFS...");    
                loadPlugin(pm);
                return true;
        }
 
        private static enum Mirror{
               
                Server1("http://dl.dropbox.com/u/65468988/Plugins/MFS/Stable%20Build/v0.2/MFS.jar"),
                Server2("http://dev.bukkit.org/media/files/590/144/MFS.jar");
 
                private Mirror(String url){
                        this.url=url;
                }
 
                String url;
                public String getUrl() {
                        return this.url;
                }
        }
 
        protected static boolean cancelled;
        public synchronized void cancel()
        {
                cancelled = true;
        }
 
        protected static synchronized void download(String fdr,String location, String filename) throws IOException {
                URLConnection connection = new URL(location).openConnection();
                connection.setUseCaches(false);
                String destination = fdr + File.separator + filename;
                File parentDirectory = new File(destination).getParentFile();
                if (parentDirectory != null) {
                        parentDirectory.mkdirs();
                }
                InputStream in = connection.getInputStream();
                OutputStream out = new FileOutputStream(destination);
                byte[] buffer = new byte[65536];
 
                while (!cancelled)
                {
                        int count = in.read(buffer);
                        if (count < 0) {
                                break;
                        }
                        out.write(buffer, 0, count);
                }
 
                in.close();
                out.close();
        }
}