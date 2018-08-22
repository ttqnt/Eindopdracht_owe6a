package virusapplicatie;

import java.io.*;
import java.util.*;
import java.net.*;
        
/**
 *  Class contains the datastructures, sorting methods of VirusApplicatie.
 *  Handles reading of files, storing and sorting data.
 * 
 * @author Karin Geertse
 */
public class VirusLogica {
    private HashMap<Integer, Virus> virusMap;
    private HashMap<String, ArrayList<Integer>> hostMap;
    private SortedSet<Virus> vList1;
    private SortedSet<Virus> vList2;
    private SortedSet<Virus> intersection;
    private String classificationSelect = "(none)";
    
    /**
     * 
     * @param input String user input of direct path to file or URL.
     * @throws InvalidInputException custom exception for handeling invalid filepaths or URLS.
     */
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
    
    /**
     * Opens URL, reads lines and passes data to addToVMap() and addToHashMap() methods.
     * 
     * @param file String of URL.
     * @throws MalformedURLException
     */
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

                String classif = getViralClass(columns[2]);
                addToVMap(columns[0], columns[1], hostId, classif);
                addToHMap(hostName, columns[0]);                     
            }
            inFile.close();
        }catch (IOException e) {
            throw new MalformedURLException(); 
        }
    }
    
    /**
     * Opens file, reads lines and passes data to addToVMap() and addToHashMap() methods.
     * 
     * @param path absolute path to file.
     * @throws FileNotFoundException 
     */
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
                
                String classif = getViralClass(columns[2]);
                addToVMap(columns[0], columns[1], hostId, classif);
                addToHMap(hostName, columns[0]);                     
            } inFile.close();
        }catch (IOException ex) {
            throw new FileNotFoundException(); 
        }        
    }
    
    /**
     * Method for determining a uniform classification parameter for Virus objects.
     * 
     * @param s String of raw classification data.
     * @return returns String of a uniform classification parameter.
     */
    private String getViralClass(String s){
        final String[] VIRALCLASS = {"ssDNA", "ssRNA", "dsDNA", "dsRNA", "Retro", "Satellites", "Viroid"};
        String read = "Others";
        for (String c : VIRALCLASS){
            if (s.contains(c))
                read = c;
        }
        if (read.equals("Retro")){
            read = "Retrovirus";
        }
        if (read.equals("Satellites")){
            read = "Satellite/Virophage";
        }
        return read;
    }
    
    /**
     * Method for storing Virus objects as values to their key virus_tax_id in HashMap.
     * 
     * @param i String virus_tax_id.
     * @param s String soort.
     * @param h String host_id.
     * @param c String classification.
     */
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
    
    /**
     * Method for storing an ArrayList of virus_tax_ids as value to their corresponding key host in HashMap.
     * 
     * @param h String host
     * @param i String i virus_tax_id
     */
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
    
    /**
     * Creates intersection of two SortedSets and passes this set to the intersection parameter.
     * 
     * @param a SortedSet to be compared.
     * @param b SortedSet to be compared.
     */
    private void createIntersect(SortedSet a, SortedSet b){
        intersection = new TreeSet<>();
        SortedSet<Virus> intersect;
        
        if(a.size() < b.size()){
            intersect = new TreeSet<>(a);
            intersect.retainAll(b);
        } else {
            intersect = new TreeSet<>(b);
            intersect.retainAll(a);            
        }        
        intersection = intersect;
    }
    
    /**
     * @return returns HashMap hostMap.
     */
    public HashMap getHostMap(){
        return hostMap;
    }
    
    /**
     * Adjusts classificationSelect parameter in correspondence with selection by user.
     * @param c 
     */
    public void setClassSelect(String c){
        classificationSelect = c;
    }
    
    /**
     * Method for generating output corresponding with viral classification selection by user.
     * 
     * @param s String viral classification selected.
     * @param i int identify which SortedSet needs to be returned to which JTextArea.
     * @return returns String of output to be parsed into JTextArea.
     */
    public String virusClassSelect(String s, int i){ 
        String output = "";
        
        switch (i) {
            case 1:  i = 1;  
                if (vList1 != null){
                    for(Virus v : vList1){
                        if (v.getClassification().equals(s))
                            output += v.getId() + "\n";
                    }
                }
                break;
            case 2:  i = 2;
                if (vList2 != null){
                    for(Virus v : vList2){
                        if (v.getClassification().equals(s))
                            output += v.getId() + "\n";
                    }
                }
                break;
            case 3: i = 3;
                if (intersection != null){
                    for (Virus v : intersection){
                        if (v.getClassification().equals(s))
                            output += v.getId() + "\n";
                    }
                }                    
        }
        return output;
    }
    
    /**
     * Method for creating a SortedSet of Virus objects that share a selected host.
     * 
     * @param host String selected host.
     * @param i int identify which SortedSet needs to be created.
     */
    public void createVirusList(String host, int i){
        SortedSet <Virus> vList = new TreeSet<>();
        
        if (!classificationSelect.equals("(none)")){
            for (Integer virusId : hostMap.get(host)){
                if (virusMap.get(virusId).getClassification().equals(classificationSelect))
                    vList.add(virusMap.get(virusId));                    
            }            
        } else{
            for (Integer virusId : hostMap.get(host))
                vList.add(virusMap.get(virusId));
        }
        
        switch (i) {
            case 1:  i = 1;                
                vList1 = vList; 
                if (twoSets())
                    createIntersect(vList1, vList2);
                break;
            case 2:  i = 2;
                vList2 = vList;
                if (twoSets())
                    createIntersect(vList1, vList2);
                break;
        } 
    }
    
    /**
     * Method for parsing Virus object id's from SortedSet.
     * @param i int identify which SortedSet needs to be parsed into corresponding JTextArea.
     * @return returns output to be parsed into JTextArea.
     */
    public String getVList(int i){        
        String output = "";
        switch (i) {
            case 1:  i = 1;                
                for (Virus v : vList1)
                    output += v.getId() + "\n";
                break;
            case 2:  i = 2;
                for (Virus v : vList2)
                    output += v.getId() + "\n";
                break;
            case 3:  i = 3;
                if (twoSets())
                    for (Virus v : intersection)
                        output += v.getId() + "\n";            
                break;
        }
        return output;
    }
    
    /**
     * @return returns true if both SortedSets vList1 and vList2 are initialized.
     */
    public boolean twoSets(){
        boolean bool = false;
        if (vList1 != null && vList2 != null)
            bool = true;
        return bool;
    }
    
    /**
     * @return returns SortedSet intersection.
     */
    public SortedSet getIntersection(){
        return intersection;
    }
    
    /**
     * Method for adjusting the sortingalgorithm of Virus objects in SortedSets in correspondence to user choice.
     * @param i int identify which sortingalgorithm to be implemented; 
     * 0 for sorting by virus_tax id(default)
     * 1 for sorting based on classification.
     * 2 for sorting based on number of hosts.
     */
    public void changeSort(int i){
        SortedSet<Virus> sort1 = new TreeSet <>();
        SortedSet<Virus> sort2 = new TreeSet <>();
        SortedSet<Virus> sorti = new TreeSet <>();
        if (i != 0){        
            switch (i) {
                case 1:  i = 1;                
                    sort1 = new TreeSet <>(new compareClassification());
                    sort2 = new TreeSet <>(new compareClassification());
                    sorti = new TreeSet <>(new compareClassification());
                    break;
                case 2:  i = 2;
                    sort1 = new TreeSet <>(new compareHostNumber());
                    sort2 = new TreeSet <>(new compareHostNumber());
                    sorti = new TreeSet <>(new compareHostNumber());
                    break;
            }
        }
        
        if (vList1 != null){
            for (Virus v1 : vList1)
                sort1.add(v1);
            vList1 = sort1;
        }
        
        if (vList2 != null){
            for (Virus v2 : vList2)
                sort2.add(v2);
            vList2 = sort2;
        }
        
        if (intersection != null){
            for (Virus v3 : intersection)
                sorti.add(v3);
            intersection = sorti;
        }        
    }
     
    /**
     * Overidden comparator for sorting Virus objects based on classification.
     */
    class compareClassification implements Comparator<Virus>{
        @Override
        public int compare(Virus v1, Virus v2){
            int result = v1.getClassification().compareTo(v2.getClassification());
            if (result == 0)
                result = v1.compareTo(v2);
            return result;
        }
    }
    /**
     * Overidden comparator for sorting Virus objects based on number of hosts.
     */
    class compareHostNumber implements Comparator<Virus>{
        @Override
        public int compare(Virus v1, Virus v2){
            int result = v1.hostTotal() - v2.hostTotal();
            if (result == 0)
                result = v1.compareTo(v2);
            return result;
        }
    }
}