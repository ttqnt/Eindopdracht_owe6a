package virusapplicatie;

import java.util.*;

/**
 *
 * @author Karin Geertse
 */
public class Virus implements Comparable {
    private int id;
    private String soort;
    private ArrayList <Integer> hostList = new ArrayList<>();
    private String classificatie;
    
    public Virus(int i, String s, Integer h, String c){
        setId(i);
        setSoort(s);
        setClassification(c);
        addHost(h);
    }
    
    private void setId(int i){
        id = i;
    }
    
    private void setSoort(String s){
        soort = s;
    }
    
    private void addHost(Integer h){
        hostList.add(h);
    }
    
    private void setClassification(String c){
        classificatie = c;
    }
    
    public int getId(){
        return id;
    }
    
    public String getSoort(){
        return soort;
    }
    
    public String getClassification(){
        return classificatie;
    }
    
    public ArrayList getHosts(){
        return hostList;
    }
    
    //public int compareTo(Virus v){
            //return this.id - v.id;
        //}
    
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
        return this.id + this.soort.hashCode();
    }

}
