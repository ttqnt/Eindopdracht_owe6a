package virusapplicatie;

import java.io.*;
import java.util.*;
import java.net.*;


/**
 *
 * @author Karin Geertse
 */
public class VirusLogica {
    private HashMap<Integer, Virus> virusMap;
    private HashMap<String, ArrayList<Integer>> hostMap;
    private SortedSet<Virus> vList1;
    private SortedSet<Virus> vList2;
    private String classificationSelect = "(none)";
    
    public VirusLogica(String input) throws InvalidInputException {
        virusMap = new HashMap<>();
        hostMap = new HashMap<>(); 
        
        try {
            readUrl(input);
        }
        catch (MalformedURLException e){
            try { 
                readFile(input);
            } catch (FileNotFoundException ex){
                throw new InvalidInputException();
            }
        }
    }
    
    private void readUrl(String file)throws MalformedURLException{
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
                    
                if(columns.length < 8 || columns[7].equals("1")){
                    hostId = "0";
                    hostName = "(Unknown)";
                } else {
                    hostId = columns[7];
                    hostName = columns[7] + " (" + columns[8] + ")";
                }
                
                String[] classif = columns[2].split(" ");
                addToVMap(columns[0], columns[1], hostId, classif[1]);
                addToHMap(hostName, columns[0]);                     
            }
            inFile.close();
        }catch (IOException e) {
            throw new MalformedURLException(); 
        }
    }
    
    private void readFile(String path)throws FileNotFoundException{
        File file = new File(path);
        BufferedReader inFile;
        
        String line;
        String hostId;
        String hostName;
        
        try{
            inFile = new BufferedReader(new FileReader(file));
            inFile.readLine();
            
             while ((line = inFile.readLine()) != null){
                String[] columns = line.split("\t");
                    
                if(columns.length < 8 || columns[7].equals("1")){
                    hostId = "0";
                    hostName = "(Unknown)";
                } else {
                    hostId = columns[7];
                    hostName = columns[7] + " (" + columns[8] + ")";
                }
                
                String[] classif = columns[2].split(" ");
                addToVMap(columns[0], columns[1], hostId, classif[1]);
                addToHMap(hostName, columns[0]);                     
            } inFile.close();
        }catch (IOException ex) {
            throw new FileNotFoundException(); 
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
        
        if (!h.equals("0") && !h.equals("1"))
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
    
    public HashMap getHostMap(){
        return hostMap;
    }
    
    public void setClassSelect(String c){
        classificationSelect = c;
    }
        
    public void createVirusList(String host, int i){
        SortedSet <Virus> vList = new TreeSet<>();
        
        if (!classificationSelect.equals("(none)")){
            for (Integer virusId : hostMap.get(host)){
                if (virusMap.get(virusId).getClassification().equals(host))
                    vList.add(virusMap.get(virusId));                    
            }            
        } else{
            for (Integer virusId : hostMap.get(host))
                vList.add(virusMap.get(virusId));
        }
        
        switch (i) {
            case 1:  i = 1;                
                vList1 = vList; 
                if (vList2 != null)
                    createIntersect();
                break;
            case 2:  i = 2;
                vList2 = vList;
                if (vList1 != null)
                    createIntersect();
                break;
        } 
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
