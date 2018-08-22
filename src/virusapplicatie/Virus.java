package virusapplicatie;

import java.util.*;

/**
 * Class for Virus Objects to be stored and sorted by VirusApplicatie.
 * 
 * @author Karin Geertse
 */
public class Virus implements Comparable {
    private int id;
    private String soort;
    private String classificatie;
    private ArrayList <Integer> hostList;
    
    /**
     * Constructor.
     * 
     * @param int virus_id tax.
     * @param String species.
     * @param hList ArrayList of hostId's.
     * @param String viral classification.
     */
    public Virus(int i, String s, ArrayList hList, String c){
        setId(i);
        setSoort(s);
        setClassification(c);
        createHostList(hList);
    }
    
    /**
     * Initializes id parameter.
     * @param i 
     */
    private void setId(int i){
        id = i;
    }
    
    /**
     * Initializes soort parameter.
     * @param i 
     */
    private void setSoort(String s){
        soort = s;
    }
    
    /**
     * Initializes ArrayList hostList parameter.
     * @param i 
     */
    private void createHostList(ArrayList hList){
        hostList = hList;
    }
    
    /**
     * Initializes classificatie parameter.
     * @param i 
     */
    private void setClassification(String c){
        classificatie = c;
    }
    
    /**
     * @return returns int virus_id;
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return returns String species.
     */
    public String getSoort(){
        return soort;
    }
    
    /**
     * @return returns String classification.
     */
    public String getClassification(){
        return classificatie;
    }
    
    /**
     * @return returns ArrayList containing Integer host_id's.
     */
    public ArrayList getHosts(){
        return hostList;
    }
 
    /**
     * @return returns size of ArrayList hostList.
     */
    public int hostTotal(){
        return hostList.size();
    }

    @Override
    public int compareTo(Object o) {
        Virus v = (Virus) o;
        return this.id - v.id;
    }
    
    @Override
    public boolean equals(Object o){
        Virus v = (Virus) o;
        return this.id == v.id;
    }

    @Override
    public int hashCode(){
        return this.soort.hashCode() + this.id;
    }
}
