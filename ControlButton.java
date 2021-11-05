import javax.swing.*;
import  java.awt.event.*;

/**
 * class de controle des boutons "case" de la fenetre
 * @author BURLET luc
 */
public class ControlButton implements ActionListener{
    private Model model; // model sur lequel la classe interagis
    private Fenetre fenetre; // fenetre sur laquel la classe interagis
    private JButton uncover; // premier bouton cliqué par l'utilisateur
    private JButton uncoverTow;// premier bouton cliqué par l'utilisateur
    private int posUncover;//position dans la grille du premier bouton cliquer par l'utilisateur
    private Timer timer;// temposrisations


    /**
     * constructeur qui initialise le model et la fenetre avec lequel le controller interagi
     * @param model model de la grille
     * @param fenetre fenetre de jeu
     */
    public ControlButton(Model model, Fenetre fenetre) {
        this.model = model;
        this.fenetre = fenetre;
    }

    /**
     * temporisation qui permet l'affichage temporaire de la 2ème case
     */
    public void tempoWrong() {
        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(uncoverTow!=null&&uncover!=null) {
                    uncover.setEnabled(true);
                    uncoverTow.setEnabled(true);
                    uncover = null;
                    uncoverTow = null;
                }
                timer.stop();
            }
        });
        timer.start();
    }

    /**
     * la fonction permet d'éviter que le chronomètre ne reste afficher lors de la réinisialisation
     * temosriation de 100 miliseconde (temps d'un cycle du chrono)
     */
    public void tempoReset() {
        timer = new Timer(101, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fenetre.getTemps().setText("0,0");
                timer.stop();
            }
        });
        timer.start();
    }

    /**
     * fonction qui permet de déterminer les actions a réaliser lors des cliques du joueur
     * @param e actionEvent action détectée
     */
    public void actionPerformed(ActionEvent e)
    {
        //tant que 2 cas sont séléctionnée le joueur ne pas en sélectioner une 3ème
        if(this.uncoverTow!=null&&this.uncover!=null){
            return;
        }

        // recherche de la position du bouton dans la grille pui on l'affiche
        int posButton=-1;
        for(int i=0;i<this.fenetre.getTaille()*this.fenetre.getTaille();i++){
            if ((e.getSource())==fenetre.getCases()[i])
                posButton=i;
        }
        this.fenetre.getCases()[posButton].setEnabled(false);

        //on vérifie que le joueur ne soit pas tombé su une case piège
        if (((JButton) e.getSource()).getDisabledIcon().equals(this.model.getPiege())){
            //on regarde si une case a déja été découverte pour la retourer si oui
            if (this.uncover!=null)
                this.uncover.setEnabled(true);
            this. uncover=null;
            //on lance le jeu si il n'est pas lancé
            if (!this.model.getPlaying()){
                this.model.setPlaying(true);
                this.fenetre.getChrono().start();
            }
            return;
        }
        // on lance le jeu s'il n'est pas lancé
        if (!this.model.getPlaying()) {
            this.model.setPlaying(true);
            this.posUncover=posButton;
            this.uncover=(JButton) e.getSource();
            this.fenetre.getChrono().start();
            return;
        //on regarde une case a déja été découverte
        }else if (this.uncover!=null) {
            //on vérifi que les images soient les mêmes
            if (((JButton) e.getSource()).getDisabledIcon().equals(this.uncover.getDisabledIcon())){
                //si oui on met a jour le modèle et on vérifi si toutes les case on étés retounées (si le jeu a été gagner)
                this.model.setFind(posButton,true);
                this.model.setFind(this.posUncover,true);
                this.uncover=null;
                if (this.model.areAllFind()){
                    // si le joueur gagne : on stop le chrono, on actualise les scors et on affiche la fenetre
                    //de victoire avant de réinitialiser le jeu
                    this.fenetre.getChrono().terminate();
                    String time = this.fenetre.getTemps().getText().replace(',', '.');
                    this.model.setMaxTime(Float.parseFloat(time));
                    this.model.updateBestScores();
                    this.fenetre.gagner();
                    reset();
                }
                return;
            }else {
                //si les 2 cases ne sont pas une paire, on décrémente le nombre d'essais on enregistre qu'une 2ème case
                // a été découverte on vérifie si le joueur a perdu si non on actualise le nombre d'assais et on temporise le rétounement
                // des 2 carte
                this.uncoverTow=(JButton) e.getSource();
                this.model.setNbEssais(this.model.getNbEssais()-1);
                if(model.getNbEssais()<0){
                    // si le joueur a perdu on arrète le chronomètre on affiche la fenetre de défaite et on réinitialise le jeu
                    this.fenetre.getChrono().terminate();
                    this.fenetre.perdu();
                    reset();
                    return;
                }
                this.fenetre.setEssais(this.model.getNbEssais()+"");
                tempoWrong();
                return;
            }
        }else {
            // si le joueur n'a pas sélectionner de case on met a jour la première case trouvée et sa position
            this.uncover=(JButton) e.getSource();
            for(int i=0;i<fenetre.getTaille()*fenetre.getTaille();i++){
                if (e.getSource()==fenetre.getCases()[i])
                    this.posUncover=i;
            }
        }


    }

    /**
     * fontion qui permet de réinitialiser le jeu
     * on réinitialise le model, ce controler et l'affichage.
     * on crée aussi un nouveau chronomètre car un ne peu utilser un thread qu'une seul fois
     */
    public void reset(){
        this.fenetre.setChrono(fenetre.getTemps());
        this.uncover=null;
        this.uncoverTow=null;
        this.posUncover=-1;
        this.model.reset(fenetre.getTaille());
        for (int i=0;i<model.getTaille()*model.getTaille();i++){
            this.fenetre.getCases()[i].setDisabledIcon(model.getOrdre()[i]);
            this.fenetre.getCases()[i].setEnabled(true);
        }

        this.fenetre.getEssais().setText(""+this.model.getNbEssais());
        tempoReset();
    }

    /**
     * fontion qui permet de réinitialiser le jeu a partir d'une nouvelle taille
     * on réinitialise le model, ce controler et l'affichage.
     * on crée aussi un nouveau chronomètre car un ne peu utilser un thread qu'une seul fois
     * @param taille nouvelle taille
     */
    public void reset(int taille){
        this.fenetre.setChrono(fenetre.getTemps());
        this.model.reset(taille);
        this.uncover=null;
        this.uncoverTow=null;
        this.posUncover=-1;
        for (int i=0;i<model.getTaille()*model.getTaille();i++){
            this.fenetre.getCases()[i].setEnabled(true);
        }
        this.fenetre.getEssais().setText(""+this.model.getNbEssais());
        tempoReset();
    }

    /**
     * permet de récupérer la class contenant le infomations
     * @return instance de la Classe modelle courante
     */
    public Model getModel() {
        return model;
    }
}

