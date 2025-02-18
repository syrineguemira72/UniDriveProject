package tests;

import Services.ObjetPerduService;
import Services.RecompenseService;
import entites.Objet;
import entites.Recompense;
import tools.MyConnection;

import java.io.IOException;
 import java.util.Scanner;

public class MainClass {
    /*
    extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/desplayObject.fxml")) ;
        Parent root=loader.load() ;
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

     */
    public static void main(String[] args)   {
        MyConnection connection = new MyConnection();
        ObjetPerduService ps = new ObjetPerduService();
        RecompenseService rs = new RecompenseService();
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("*************************************");
            System.out.println("1. Add Object");
            System.out.println("2. Update Object");
            System.out.println("3. Delete Object");
            System.out.println("4. Display Objects");
            System.out.println("5. Add Reward");
            System.out.println("6. Update Reward");
            System.out.println("7. Delete Reward");
            System.out.println("8. Display Rewards");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter object description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter object type: ");
                    String type = scanner.nextLine();
                    System.out.print("Enter object finder: ");
                    String finder = scanner.nextLine();
                    System.out.print("Enter object owner: ");
                    String owner = scanner.nextLine();
                    System.out.print("Enter object status: ");
                    String status = scanner.nextLine();
                    Objet newObj = new Objet(description, 0, type, finder, owner);
                    ps.ajouterObjet(newObj);
                    break;

                case 2:
                    System.out.print("Enter object ID to update: ");
                    int objId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new object description: ");
                    String newDescription = scanner.nextLine();
                    System.out.print("Enter new object type: ");
                    String newType = scanner.nextLine();
                    System.out.print("Enter new object finder: ");
                    String newFinder = scanner.nextLine();
                    System.out.print("Enter new object owner: ");
                    String newOwner = scanner.nextLine();
                    Objet updatedObj = new Objet(newDescription, objId, newType, newFinder, newOwner);
                    ps.modifierObjet(objId, updatedObj);
                    break;

                case 3:
                    System.out.print("Enter object ID to delete: ");
                    int deleteObjId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    ps.supprimerObjet(deleteObjId);
                    break;

                case 4:
                    System.out.println(ps.afficherObjets());
                    break;

                case 5:
                    System.out.print("Enter reward amount: ");
                    int amount = scanner.nextInt();
                    System.out.print("Enter reward object ID: ");
                    int objIdReward = scanner.nextInt();
                    System.out.print("Enter reward finder ID: ");
                    int finderId = scanner.nextInt();
                    Recompense newReward = new Recompense(amount, objIdReward, finderId, 0);
                    rs.ajouterRecompense(newReward);
                    break;

                case 6:
                    System.out.print("Enter reward ID to update: ");
                    int rewardId = scanner.nextInt();
                    System.out.print("Enter new reward amount: ");
                    int newAmount = scanner.nextInt();
                    System.out.print("Enter new reward object ID: ");
                    int newObjIdReward = scanner.nextInt();
                    System.out.print("Enter new reward finder ID: ");
                    int newFinderId = scanner.nextInt();
                    Recompense updatedReward = new Recompense(newAmount, newObjIdReward, newFinderId, rewardId);
                    rs.modifierRecompense(rewardId, updatedReward);
                    break;

                case 7:
                    System.out.print("Enter reward ID to delete: ");
                    int deleteRewardId = scanner.nextInt();
                    rs.supprimerRecompense(deleteRewardId);
                    break;

                case 8:
                    System.out.println(rs.afficherRecompenses());
                    break;

                case 9:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);

        scanner.close();
    }
}

