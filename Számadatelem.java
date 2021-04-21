package számadatelem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.io.PrintWriter;

class Covid{
    String date;
    String country;
    int cnew;
    int dnew;
    int act;
    int cdeaths;
    int ddeaths;
     
    @Override
    public String toString(){
        return country + "\t" + dnew + "\t\t" + cnew + "\t\t\t" + act + "\t\t" + ddeaths + "\t\t" + cdeaths;
    }
    
    Covid(String[] a){
        this.date=(a[0]);
        this.country=(a[1]);
        this.cnew=Integer.parseInt(a[2]);
        this.dnew=Integer.parseInt(a[3]);
        this.act=Integer.parseInt(a[4]);
        this.cdeaths=Integer.parseInt(a[5]);
        this.ddeaths=Integer.parseInt(a[6]);
    }
}

public class Számadatelem {
    
    static LinkedHashMap<String, ArrayList<Covid>> database = new LinkedHashMap<>();
    
    public static void feltolt(File f){
        try {
            Scanner s = new Scanner(f, "ISO-8859-2");
            s.nextLine();
            while(s.hasNextLine()){
                Covid co = new Covid(s.nextLine().split(";"));
                if(!database.keySet().contains(co.date))
                    database.put(co.date, new ArrayList<>());
                database.get(co.date).add(co);      
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } 
    }
    
    public static int max(int a, int b){
        if (a>b)
            b=a;
        return b;
    }
    
    public static int min(int a, int b){
        if (a<b)
            b=a;
        return b;
    }
    
    
    public static void vilagKethetenteOssz(File f, File g){
        int vUj, vUjOssz, vUjMax, vUjMin, vAktiv, vAktivMax, vAktivMaxMax, vAktivMaxMin, vHalal, vHalalOssz, vHalalMax, vHalalMin;
        int hUj, hUjOssz, hUjMax, hUjMin, hAktiv, hAktivMax, hAktivMaxMax, hAktivMaxMin, hHalal, hHalalOssz, hHalalMax, hHalalMin;
        int count, mennyiIntervallum;
        vUj = vUjOssz = vUjMax = vAktiv = vAktivMax = vAktivMaxMax =  vHalal = vHalalOssz = vHalalMax = 0;
        vUjMin = vAktivMaxMin = vHalalMin = 1000000000;
        hUj = hUjOssz = hUjMax = hAktiv = hAktivMax = hAktivMaxMax = hHalal = hHalalOssz = hHalalMax = 0;
        hUjMin = hAktivMaxMin = hHalalMin = 1000000000;
        count = mennyiIntervallum = 0;
        String intervallum = "";
        try {
            PrintWriter world = new PrintWriter(f);
            PrintWriter hungary = new PrintWriter(g);
            world.println("Datum"+";"+"Uj esetek szama"+";"+"Aktiv esetek maximuma"+";"+"Elhunytak szama");
            hungary.println("Datum"+";"+"Uj esetek szama"+";"+"Aktiv esetek maximuma"+";"+"Elhunytak szama");
                for (String s : database.keySet()) {
                    for (Covid c : database.get(s)){
                        vUj += c.dnew;
                        vAktiv += c.act;
                        vHalal += c.ddeaths;
                        if(c.country.equals("Hungary")){
                            hUj += c.dnew;
                            hAktiv += c.act;
                            hHalal += c.ddeaths;
                        }
                    }
                    vAktivMax = max(vAktiv,vAktivMax);
                    vAktiv = 0;
                    hAktivMax = max(hAktiv,hAktivMax);
                    hAktiv = 0; 
                    if (count % 14 == 0)
                        intervallum = s;
                    count++;
                    if (count % 14 == 0){
                        world.println(intervallum + "-" + s + ";" + vUj + ";" + vAktivMax + ";" + vHalal);
                        hungary.println(intervallum + "-" + s + ";" + hUj + ";" + hAktivMax + ";" + hHalal);
                        // Világ
                        vUjMax = max(vUj, vUjMax);
                        vAktivMaxMax = max(vAktivMax, vAktivMaxMax);
                        vHalalMax = max(vHalal, vHalalMax);
                        vUjMin = min(vUj, vUjMin);
                        vAktivMaxMin = min(vAktivMax, vAktivMaxMin);
                        vHalalMin = min(vHalal, vHalalMin);
                        vUjOssz += vUj;
                        vHalalOssz += vHalal;
                        //Magyar
                        hUjMax = max(hUj, hUjMax);
                        hAktivMaxMax = max(hAktivMax, hAktivMaxMax);
                        hHalalMax = max(hHalal, hHalalMax);
                        hUjMin = min(hUj, hUjMin);
                        hAktivMaxMin = min(hAktivMax, hAktivMaxMin);
                        hHalalMin = min(hHalal, hHalalMin);
                        hUjOssz += hUj;
                        hHalalOssz += hHalal;
                        //
                        vUj = vAktivMax = vHalal = hUj = hAktivMax = hHalal = 0;
                        mennyiIntervallum++;
                    }
                }
            //Világ
            world.println("\nOsszesen"+";"+vUjOssz+";"+""+";"+vHalalOssz);
            double vuatl = Math.round((double)vUjOssz/mennyiIntervallum*100.0)/100.0;
            double vhatl = Math.round((double)vHalalOssz/mennyiIntervallum*100.0)/100.0;
            world.println("Atlag"+";"+vuatl+";"+""+";"+vhatl);
            world.println("Maximum"+";"+vUjMax+";"+vAktivMaxMax+";"+vHalalMax);
            world.println("Minimum"+";"+vUjMin+";"+vAktivMaxMin+";"+vHalalMin);
            world.flush();
            world.close();
            //Magyar
            hungary.println("\nOsszesen"+";"+hUjOssz+";"+""+";"+hHalalOssz);
            double huatl = Math.round((double)hUjOssz/mennyiIntervallum*100.0)/100.0;
            double hhatl = Math.round((double)hHalalOssz/mennyiIntervallum*100.0)/100.0;
            hungary.println("Atlag"+";"+huatl+";"+""+";"+hhatl);
            hungary.println("Maximum"+";"+hUjMax+";"+hAktivMaxMax+";"+hHalalMax);
            hungary.println("Minimum"+";"+hUjMin+";"+hAktivMaxMin+";"+hHalalMin);
            hungary.flush();
            hungary.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Hiba: " + ex.getMessage());
        }       
    }
    
    public static void egyNap(String s){
    System.out.println(s+"\nOrszág\t\tNapi új esetek\tKumulatív új esetek\tAktív esetek\tNapi halálok\t Kumulatív halálok");
    for (Covid c : database.get(s)) 
            System.out.println(c);
    }
    
    public static void minden(){
        for (String s : database.keySet()) {
            egyNap(s);
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        feltolt(new File("D:\\Egyetem\\BGE PSZK\\Számadatelem\\COVID.csv"));
//      minden();
//      egyNap("2020.02.16");
        File f1 = new File("D:\\Egyetem\\BGE PSZK\\Számadatelem\\vilag.csv");
        File f2 = new File("D:\\Egyetem\\BGE PSZK\\Számadatelem\\magyar.csv");
        vilagKethetenteOssz(f1, f2);
    }   
}
