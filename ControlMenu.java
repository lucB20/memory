import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * class de controle du menu des options
 * @author BURLET luc
 */
public class ControlMenu implements ActionListener {
    private Fenetre fenetre;// fenetre source
    private ControlButton controler;// controler source

    /**
     * Consructeur initialisant la fenetre et le controler des bontons (cases)
     * @param fenetre fenetre source
     * @param controlButton controler source
     */
    public ControlMenu(Fenetre fenetre,ControlButton controlButton) {
        this.fenetre = fenetre;
        this.controler=controlButton;
    }

    /**
     * fonction permetant le choix de l'action a réaliser en fonction de l'option cliquée
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        //affiche le scores
        if (e.getSource()==fenetre.getMeilleursScores()){
            fenetre.bestScors();
            return;
        }
        //arrete le chrono et réinitialise le modèle en cas de nouvelle partie
        fenetre.getChrono().terminate();
        controler.reset();
    }
}
