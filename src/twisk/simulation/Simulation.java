package twisk.simulation;
import twisk.monde.*;
import twisk.outils.KitC;

public class Simulation{
    private KitC kitC;

    public Simulation(){
        kitC = new KitC();
        kitC.creerEnvironnement();

    }

    public native int[] start_simulation(int nbEtapes, int nbGuichets, int nbClients, int[] tabJetonsGuichet);
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
    public native void nettoyage();

    public void simuler(Monde world){

        //Les activités
        Etape act1 = new Activite("Début_du_parc");
        Etape guich = new Guichet("Achat_des_tickets");
        Etape actRes = new ActiviteRestreinte("Visite_du_parc");
        Etape act2 = new Activite("fin_du_parc");
        Etape act3 = new Activite("fin_du_parc2");



        //entrée
        world.aCommeEntree(act1);

        // La suite d'activités
        act1.ajouterSuccesseur(guich);
        guich.ajouterSuccesseur(actRes);
        actRes.ajouterSuccesseur(act2);
        act2.ajouterSuccesseur(act3);

        //Sortie
        world.aCommeSortie(act3);

        String Cworld = world.toC();

        System.out.println(Cworld);
        System.load("/tmp/twisk/libTwisk.so") ; // Ajout séance 6

    }

    public static void main(String[] args) {
        Monde world = new Monde();
        Simulation sim = new Simulation();
        sim.kitC.construireLaLibrairie();
        sim.simuler(world);
        sim.kitC.creerFichier(world.toC());
        sim.kitC.compiler();
    }
}