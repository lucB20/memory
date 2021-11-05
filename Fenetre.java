import javax.swing.*;
import java.awt.*;

/**
 * class d'affichage du jeu
 * @author BURLET luc
 */
public class Fenetre extends JFrame{

    private Chrono chrono; // chronomètre

    private JButton[] cases;// cases réprésentée par des bontons
    private JPanel grid; // grille de jeu

    private JLabel labTemps; // label Temps
    private JLabel temps; // valeur du temps

    private JLabel labEssais; // présentation des essais
    private JLabel essais; // nombre d'eissais


    private JMenuBar menu; // bar de menus

    private JMenu options; // menu des option
    private JMenuItem meilleursScores; // menu item qui affichera les scores
    private JMenuItem nouvellePartie; // menu item qui créera une nouvelle partie

    private JMenu tailles; // menu des tailles
    private JMenuItem trois; // menu item qui créera une nouvelle partie de taille 3*3
    private JMenuItem quatre; // menu item qui créera une nouvelle partie de taille 4*4
    private JMenuItem cinque; // menu item qui créera une nouvelle partie de taille 5*5

    private JPanel principale; // pannet contenant tous les elements
    private JPanel top; // haut du panel principal
    private JPanel essaiPannel; // pannel contenant l'affichage des essais
    private JPanel panelTemps; // pannel contenant l'affichage du temps

    private Model model; // model du jeu
    private ControlButton controlButton; // controler de bouton de la grille
    private ControlMenu controlMenu; // controler du menu des options
    private ControleMenuTaille controleMenuTaille; // controler du menu des tailles

    private  int taille;// taille de la grille

    /**
     * contructeur de la fenetre a partir d'un model
     * @param model
     */
    public Fenetre(Model model) {
        this.model=model;
        this.taille=this.model.getTaille();
        this.controlButton=new ControlButton(this.model,this);
        this.controlMenu=new ControlMenu(this,this.controlButton);
        this.controleMenuTaille=new ControleMenuTaille(this.model,this);
        init();
        vue();
        setSize(600,700);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("Memory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * fonstion premettant de récupérer les information
     * du model pour crée la fenetre
     */
    public void init(){
        // création de la grille avec les icons des case dans l'ordre donner par le modèl
        this.grid=new JPanel(new GridLayout(taille,taille));
        this.cases=new JButton[5*5];
        for (int i=0;i<this.cases.length;i++) {
            this.cases[i] = new JButton();
        }
        for (int i=0;i<taille*taille;i++){
            this.cases[i].setIcon(model.getBack());
            this.cases[i].setSize(new Dimension(600/taille,600/taille));
            this.cases[i].setDisabledIcon(model.getOrdre()[i]);
            this.cases[i].addActionListener(this.controlButton);
            this.grid.add(this.cases[i]);
        }
        //initialisation du chrono et du nombre d'essais
        this.temps=new JLabel("0.0");
        setChrono(temps);
        this.essais=new JLabel(""+this.model.getNbEssais());
    }

    /**
     * créeation des élément de le fenetre
     */
    public void vue(){
        //création de la liste des options
        this.options = new JMenu("Options");
        this.meilleursScores = new JMenuItem("meilleurs scores");
        this. nouvellePartie = new JMenuItem("Nouvelle partie");
        this.meilleursScores.addActionListener(this.controlMenu);
        this.nouvellePartie.addActionListener(this.controlMenu);
        this.options.add(this.nouvellePartie);
        this.options.add(this.meilleursScores);
        //création de la liste des tailles
        this.tailles = new JMenu("Tailles");
        this.trois = new JMenuItem("3*3");
        this.quatre = new JMenuItem("4*4");
        this.cinque = new JMenuItem("5*5");
        this.trois.addActionListener(this.controleMenuTaille);
        this.quatre.addActionListener(this.controleMenuTaille);
        this.cinque.addActionListener(this.controleMenuTaille);
        this.tailles.add(this.trois);
        this.tailles.add(this.quatre);
        this.tailles.add(this.cinque);
        //création du menu
        this.menu=new JMenuBar();
        this.menu.add(this.options);
        this.menu.add(this.tailles);

        // création de l'affichage du temps
        this.labTemps=new JLabel("Temps : ");
        panelTemps = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTemps.add(this.labTemps);
        panelTemps.add(this.temps);

        //création de l'affichage haut de la fenetre
        top = new JPanel(new GridLayout(2,1));
        top.add(this.menu);
        top.add(panelTemps);


        //crétion de l'affichage des essais
        this.labEssais=new JLabel("Essais restant : ");
        JPanel panelEssais = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEssais.add(this.labEssais);
        panelEssais.add(this.essais);

        //création de l'affichage bas de la fenetre
        essaiPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        essaiPannel.add(panelEssais);

        //création de l'affichage global
        this.principale = new JPanel(new BorderLayout());
        this.principale.add(top,BorderLayout.NORTH);
        this.principale.add(this.grid,BorderLayout.CENTER);
        this.principale.add(essaiPannel,BorderLayout.SOUTH);

        setContentPane(this.principale);

    }

    /**
     * getter du bouton de taille 3*3 du menu tailles
     * @return 3*3 du menus des tailles
     */
    public JMenuItem getTrois() {
        return trois;
    }
    /**
     * getter du bouton de taille 4*4 du menu tailles
     * @return 4*4 du menus des tailles
     */
    public JMenuItem getQuatre() {
        return quatre;
    }
    /**
     * getter du bouton de taille 5*5 du menu tailles
     * @return 5*5 du menus des tailles
     */
    public JMenuItem getCinque() { return cinque; }

    /**
     * getter des cases
     * @return liste de bonton représantant les cases
     */
    public JButton[] getCases() {
        return this.cases;
    }

    /**
     * getter de l'affichage du temps
     * @return label d'affichage du temps
     */
    public JLabel getTemps() {
        return this.temps;
    }

    /**
     * permet de changer le text du nombre d'essais
     * @param essais
     */
    public void setEssais(String essais) {
        this.essais.setText(essais);
    }

    /**
     * permet de récupérer l'element du menu option qui affiche les scores
     * @return élément de menu
     */
    public JMenuItem getMeilleursScores() {
        return this.meilleursScores;
    }

    /**
     * permet de récuprer la taille de la grille
     * @return taille de la grille
     */
    public int getTaille() {
        return this.taille;
    }


    /**
     * permet de récupérer le Jlabel contenant le nombre d'essais
     * @return Jlabel essais
     */
    public JLabel getEssais() {
        return essais;
    }

    /**
     * permet de récupérer la grille de jeu contenant les boutons
     * @return grille de jeu
     */
    public JPanel getGrid() {
        return grid;
    }

    /**
     * permet de récupérer le Jpanel contenant tous les éléments
     * @return Jpanel principale
     */
    public JPanel getPrincipale() {
        return principale;
    }

    /**
     * donne le controller des cases de jeu
     * @return controller des JButton
     */
    public ControlButton getControlButton() {
        return controlButton;
    }

    /**
     * methode permettant de changer de grille
     * @param grid nouvelle grille
     */
    public void setGrid(JPanel grid) {
        this.grid = grid;
    }

    /**
     * methode permettant le changement de la taille du plateau de jeu
     * @param taille nouvelle taille
     */
    public void setTaille(int taille) {
        this.taille = taille;
    }

    /**
     * renvoie le chronomètre utiliser par le model
     * @return l'instance de chrono utiliser dans la model
     */
    public Chrono getChrono() {
        return this.chrono;
    }

    /**
     * change le chrono ulitiser par le modèle
     * @param label Jlabel sur lequel se fera l'affichage
     */
    public void setChrono(JLabel label){
        this.chrono=new Chrono(label);
    }



    /**
     * affiche la fenetre de défaite au joueur et retourne toute les cases
     */
    public void perdu(){
        for (int i=0;i<this.taille*this.taille;i++){
            this.cases[i].setDisabledIcon(model.getOrdre()[i]);
            this.cases[i].setEnabled(false);
        }
        JOptionPane d = new JOptionPane();
        d.showMessageDialog( this, "vous avez perdu,\n vous aurez plus de chance la prochaine fois", "PERDU",JOptionPane.INFORMATION_MESSAGE);
        JDialog fenErr = d.createDialog(this, "PERDU");
    }

    /**
     * affiche la fenetre de victoire au joueur et retourne toute les cases
     */
    public void gagner(){
        for (int i=0;i<this.taille*this.taille;i++){
            this.cases[i].setDisabledIcon(model.getOrdre()[i]);
            this.cases[i].setEnabled(false);
        }
        JOptionPane d = new JOptionPane();
        d.showMessageDialog( this, "vous avez gagné" +
                "\nvotre score  "+model.getMaxTime()+
                "\n\n Best Scores"+
                "\n 1 - "+this.model.getBestTimes()[0]+
                "\n 2 - "+this.model.getBestTimes()[1]+
                "\n 3 - "+this.model.getBestTimes()[2]
                ,"GAGNE",JOptionPane.INFORMATION_MESSAGE);
        JDialog fenErr = d.createDialog(this, "GAGNE");
    }

    /**
     * affiche les meilleurs scores
     */
    public void bestScors(){
        JOptionPane d = new JOptionPane();
        d.showMessageDialog( this, "Best Scores"+
                        "\n 1 - "+this.model.getBestTimes()[0]+
                        "\n 2 - "+this.model.getBestTimes()[1]+
                        "\n 3 - "+this.model.getBestTimes()[2]
                ,"Best Scores",JOptionPane.INFORMATION_MESSAGE);
        JDialog fenErr = d.createDialog(this, "Best Scores");
    }

}
