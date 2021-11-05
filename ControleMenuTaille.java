import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * class de controle du menu de tailles
 * @author BURLET luc
 */
public class ControleMenuTaille implements ActionListener {
    private Model model;// model source
    private Fenetre fenetre; // fenetre source

    /**
     * contructeur avec la fenetre source
     * @param model model source
     */
    public ControleMenuTaille(Model model,Fenetre fenetre) {
        this.fenetre=fenetre;
        this.model = model;
    }

    /**
     * changement de taille en fontion du bouton cliquer
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==fenetre.getTrois() && fenetre.getTaille()!=3){
            changeSize(3);
        }
        if (e.getSource()==fenetre.getQuatre()&& fenetre.getTaille()!=4){
            changeSize(4);
        }
        if (e.getSource()==fenetre.getCinque()&&fenetre.getTaille()!=5){
            changeSize(5);
        }
    }

    /**
     * permet le changement de taille du jeu a partir de la nouvelle taille
     * @param taille nouvelle taille
     */
    public void changeSize(int taille){
        //réinitialisation de toutes les classes
        this.fenetre.getChrono().terminate();
        this.fenetre.getControlButton().reset(taille);

        //réinitailisation et mise a jour de l'affichage
        this.fenetre.getPrincipale().remove(this.fenetre.getGrid());
        this.fenetre.setTaille(taille);

        this.fenetre.setGrid(new JPanel(new GridLayout(taille,taille)));
        for (int i=0;i<this.model.getTaille()* this.model.getTaille();i++) {
            this.fenetre.getCases()[i] = new JButton();
            this.fenetre.getCases()[i].setIcon(this.model.getBack());
            this.fenetre.getCases()[i].setSize(new Dimension(600/taille,600/taille));
            this.fenetre.getCases()[i].setDisabledIcon(this.model.getOrdre()[i]);
            this.fenetre.getCases()[i].addActionListener(fenetre.getControlButton());
            this.fenetre.getGrid().add(this.fenetre.getCases()[i]);
        }

        this.fenetre.getPrincipale().add(this.fenetre.getGrid(),BorderLayout.CENTER);

    }
}
