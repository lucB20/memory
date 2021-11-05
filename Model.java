import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;
/**
 * Modele contenant toutes les données du jeu
 * @author BURLET luc
 */
public class Model {

    private ImageIcon[] ordre; //images mélangées
    private ImageIcon[] images;// images des cases
    private Boolean[] find; // liste de l'état des "carte"
    private float maxTime; //temps de fin de partie
    private float[] bestTimes; // meilleur temps
    private ImageIcon back; // arrière des cases
    private ImageIcon piege; // image pièges
    private Boolean isPlaying; //etat de la partie
    private int nbEssais;// nombre d'essais
    private int taille; // taille de la grille
    private String cheminScore; // chemin des fichiers contenant les scores
    private static String cheminData="data/";//chemin du ficher contenant les données

    /**
     * constructeur du model a partir de la taille de la grille
     * @param taille tailles de la grille
     */
    public Model(int taille){
        this.taille=taille;
        this.maxTime=0;
        this.isPlaying =false;
        int nbCases=taille*taille;
        this.images=new ImageIcon[(nbCases/2)];
        // récupération des images en fonction du nombre de cases
        try{
            File folder=new File(cheminData+"images");
            File[] fileEntry=folder.listFiles();
            // tris de images pour que l'ordre soit toujour le meme
            Arrays.sort(fileEntry);
            for (int i=0;i<nbCases/2;i++) {
                this.images[i]=new ImageIcon(new ImageIcon(cheminData+"images/" +fileEntry[i].getName()).getImage().getScaledInstance(600/taille,600/taille, Image.SCALE_DEFAULT));
            }
            this.piege=new ImageIcon(new ImageIcon(cheminData+"warn.jpeg").getImage().getScaledInstance(600/taille,600/taille, Image.SCALE_DEFAULT));
            this.back=new ImageIcon(new ImageIcon(cheminData+"cross.jpeg").getImage().getScaledInstance(600/taille,600/taille, Image.SCALE_DEFAULT));
        }catch(Exception e){ e.printStackTrace(); }
        // innitialisation du chemimn des scores et du nombre d'essais en fonstions de la taille de la grille
        this.bestTimes=new float[3];
        switch (taille){
            case 3:
                this.nbEssais=2;
                this.cheminScore= cheminData+"bestScore3";
                break;
            case 4:
                this.nbEssais=3;
                this.cheminScore= cheminData+"bestScore4";
                break;
            case 5:
                this.nbEssais=6;
                this.cheminScore= cheminData+"bestScore5";
                break;
        }
        //récupération des scores dans leurs fichiers
        try{
            int cpt=0 ;
            BufferedReader br = new BufferedReader(new FileReader(this.cheminScore));
            String s;
            while ((s = br.readLine()) != null) {
                this.bestTimes[cpt]=Float.parseFloat(s);
                cpt++;
            }
            br.close();
        }
        catch (Exception e){ e.printStackTrace(); }

        // innitialisation et mélange des images et de leur état
        this.ordre=new ImageIcon[nbCases];
        this.find=new Boolean[nbCases];
        Vector v=new Vector();
        for(int i=0;i<nbCases/2*2;i++)
            v.add(this.images[(int) (i % (nbCases / 2))]);
        if (nbCases%2!=0){
            v.add(this.piege);
        }
        for(int i=0;i<ordre.length;i++){
            int rand= (int) (Math.random()*v.size());
            this.ordre[i]=(ImageIcon)(v.elementAt(rand));
            // on vérifie si la case est une case piège si oui on initialise son état a trouvé
            // car elle n'a pas de paire
            if(this.ordre[i].equals(this.piege)) {
                this.find[i] = true;
            }
            else
                this.find[i]=false;
            v.removeElementAt(rand);
        }
    }

    /**
     * ordres images mélangées qui apparaitons dans la grille
     * @return liste d'image icons
     */
    public ImageIcon[] getOrdre() {
        return this.ordre;
    }

    /**
     * donne de temps maximum de la partie
     * @return temps Float
     */
    public float getMaxTime() {
        return this.maxTime;
    }

    /**
     * change de le temps de fin de la partie
     * @param maxTime temps : Float
     */
    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * donne la liste des meilleurs temps
     * @return liste des meilleur temps en float
     */
    public float[] getBestTimes() {
        return this.bestTimes;
    }

    /**
     * donne l'image servant de dos aux images
     * @return image Icon du dos des cartes
     */
    public ImageIcon getBack() {
        return this.back;
    }

    /**
     * donne la taille de la grille
     * @return taille int
     */
    public int getTaille() {
        return this.taille;
    }

    /**
     * donne l'état de la partie
     * @return true si lancer false si non
     */
    public Boolean getPlaying() {
        return this.isPlaying;
    }

    /**
     * change l'état de la partie
     * @param gagner état de la partie
     */
    public void setPlaying(Boolean gagner) {
        this.isPlaying = gagner;
    }

    /**
     * donne le nombre d'essais restant au joueur
     * @return nombre d'essais
     */
    public int getNbEssais() {
        return this.nbEssais;
    }

    /**
     * change le nombre d'essais restant au joueur
     * @param nbEssais le nombre d'essais restants
     */
    public void setNbEssais(int nbEssais) {
        this.nbEssais = nbEssais;
    }

    /**
     * donne l'image utilisée en tant que case piège
     * @return image icon contenant l'image des cases piège
     */
    public ImageIcon getPiege() {
        return piege;
    }

    /**
     * change l'état d'une case dans le tableau find
     * @param pos position de la case dans la grille
     * @param state état dans le quel faire basculer la case
     */
    public void setFind(int pos,boolean state){
        this.find[pos]=state;
    }



    /**
     * permet de savoir si la grille est gagnée
     * @return true si les valeur de find sont toutes a true
     */
    public boolean areAllFind(){
        for(int i=0;i<find.length;i++){
            if (!find[i]) {
                return false;
            }
        }
        return true;
    }



    /**
     * permet d'actialiser les fichier de score si le score de la partie est meilleurs que ceux stokés
     * ou si les scores stokés n'on pas encor été définits
     */
    public void updateBestScores(){
        //mise a jour du tableau
        if (this.bestTimes[0]>this.maxTime || this.bestTimes[0]==0){
            this.bestTimes[1]=this.bestTimes[0];
            this.bestTimes[2]=this.bestTimes[1];
            this.bestTimes[0]=this.maxTime;
        }else if (this.bestTimes[1]>this.maxTime || this.bestTimes[0]==0){
            this.bestTimes[2]=this.bestTimes[1];
            this.bestTimes[1]=this.maxTime;
        }else if (this.bestTimes[2]>this.maxTime || this.bestTimes[0]==0){
            this.bestTimes[2]=this.maxTime;
        }
        //mise a jour du fichier
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.cheminScore));
            bw.write(""+this.bestTimes[0]);
            bw.newLine();
            bw.write(""+this.bestTimes[1]);
            bw.newLine();
            bw.write(""+this.bestTimes[2]);
            bw.newLine();
            bw.close();
        }catch(Exception e){ e.printStackTrace(); }
    }

    /**
     * permet de remètre le modele en état de début de partie selon une taille
     * @param taille taille de la grille de jeu
     */
    public void reset(int taille){
        this.taille=taille;
        this.maxTime=0;
        this.isPlaying =false;
        int nbCases=taille*taille;
        this.images=new ImageIcon[(nbCases/2)];
        // récupération des images en fonction du nombre de cases
        try{
            File folder=new File(cheminData+"images");
            File[] fileEntry=folder.listFiles();
            // tris de images pour que l'ordre soit toujour le meme
            Arrays.sort(fileEntry);
            for (int i=0;i<nbCases/2;i++) {
                this.images[i]=new ImageIcon(new ImageIcon(cheminData+"images/" +fileEntry[i].getName()).getImage().getScaledInstance(600/taille,600/taille, Image.SCALE_DEFAULT));
            }
            this.piege=new ImageIcon(new ImageIcon(cheminData+"warn.jpeg").getImage().getScaledInstance(600/taille,600/taille, Image.SCALE_DEFAULT));
            this.back=new ImageIcon(new ImageIcon(cheminData+"cross.jpeg").getImage().getScaledInstance(600/taille,600/taille, Image.SCALE_DEFAULT));
        }catch(Exception e){ e.printStackTrace(); }
        // innitialisation du chemimn des scores et du nombre d'essais en fonstions de la taille de la grille
        this.bestTimes=new float[3];
        switch (taille){
            case 3:
                this.nbEssais=2;
                this.cheminScore= cheminData+"bestScore3";
                break;
            case 4:
                this.nbEssais=3;
                this.cheminScore= cheminData+"bestScore4";
                break;
            case 5:
                this.nbEssais=6;
                this.cheminScore= cheminData+"bestScore5";
                break;
        }
        //récupération des scores dans leurs fichiers
        try{
            int cpt=0 ;
            BufferedReader br = new BufferedReader(new FileReader(this.cheminScore));
            String s;
            while ((s = br.readLine()) != null) {
                this.bestTimes[cpt]=Float.parseFloat(s);
                cpt++;
            }
            br.close();
        }
        catch (Exception e){ e.printStackTrace(); }

        // innitialisation et mélange des images et de leur état
        this.ordre=new ImageIcon[nbCases];
        this.find=new Boolean[nbCases];
        Vector v=new Vector();
        for(int i=0;i<nbCases/2*2;i++)
            v.add(this.images[(int) (i % (nbCases / 2))]);
        if (nbCases%2!=0){
            v.add(this.piege);
        }
        for(int i=0;i<ordre.length;i++){
            int rand= (int) (Math.random()*v.size());
            this.ordre[i]=(ImageIcon)(v.elementAt(rand));
            // on vérifie si la case est une case piège si oui on initialise son état a trouvé
            // car elle n'a pas de paire
            if(this.ordre[i].equals(this.piege)) {
                this.find[i] = true;
            }
            else
                this.find[i]=false;
            v.removeElementAt(rand);
        }
    }
}
