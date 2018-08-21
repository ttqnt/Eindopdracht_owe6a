package virusapplicatie;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karin Geertse
 */
public class VirusLogica {
    private HashMap<Integer, Virus> virusMap;
    private HashMap<String, ArrayList<Integer>> hostMap;
    private SortedSet<Virus> vList1;
    private SortedSet<Virus> vList2;
    
    public VirusLogica(String file){
        virusMap = new HashMap<>();
        hostMap = new HashMap<>(); 
        readInput(file);
    }
    
    private void readInput(String file){
        try{
            //File file = new File("/home/karin/virushostdb.tsv");
            URL virusDb = new URL(file);
            BufferedReader inFile;
            
            String line;
            String hostId;
            String hostName;
            
            try{
                inFile = new BufferedReader(new InputStreamReader(virusDb.openStream()));
                inFile.readLine();
                
                while ((line = inFile.readLine()) != null){
                    String[] columns = line.split("\t");

                    try{
                        hostId = columns[7];
                        hostName = columns[7] + " (" + columns[8] + ")";
                    } catch (ArrayIndexOutOfBoundsException e){
                        hostId = "0";
                        hostName = "(Unknown)";
                    }

                    String[] classif = columns[2].split(" ");
                    addToVMap(columns[0], columns[1], hostId, classif[1]);
                    addToHMap(hostName, columns[0]); 
                    
                }
                inFile.close();
            }catch (IOException ex) {
                Logger.getLogger(VirusLogica.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch (MalformedURLException ex) {
            Logger.getLogger(VirusLogica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void addToVMap(String i, String s, String h, String c){
        ArrayList<Integer> hList;
        Integer id = new Integer(i);
        Integer host = new Integer(h);
                 
        if (!virusMap.containsKey(id))
            hList = new ArrayList<>();
        else 
           hList = virusMap.get(id).getHosts();
        
        if (!h.equals("0"))
            hList.add(host);        
        virusMap.put(id, new Virus(id, s, hList, c));
    }
    
    private void addToHMap(String h, String i){
        ArrayList<Integer> hList;
        Integer id = new Integer(i);
        
        if (!hostMap.containsKey(h))
            hList = new ArrayList<>();
        else 
            hList = hostMap.get(h);
        hList.add(id);
        hostMap.put(h, hList);
    }
    
    public void createViruslist(int i){
        //switch case ;  1 = viruslijst1,  2 viruslijst2;
        
    }
    
    public SortedSet getVLijst1(){
        return vList1;
    }
    
    public SortedSet getVLijst2(){
        return vList2;
    }
    
    public void createIntersect(){
        
    }
}
