package traitement;
import java.io.File;
import java.io.FilenameFilter;
/**
 * 
 * @author dbinet
 *
 *classe permettant de récuperer un groupement de fichier finissant par la meme extension
 *
 */
public class Filter {

    public File[] finder( String dirName,String extension){
    	File dir = new File(dirName);

    	return dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(extension); }
    	} );

    }

}