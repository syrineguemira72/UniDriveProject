<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.chart.CategoryAxis?>
<?import javafx.scene.chart.LineChart?>
<?import javafx.scene.chart.NumberAxis?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ChoiceBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.TableColumn?>
<?import javafx.scene.control.TableView?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.AnchorPane?>

<AnchorPane prefHeight="601.0" prefWidth="796.0" stylesheets="@Css/styleAdmin.css" xmlns="http://javafx.com/javafx/23.0.1" xmlns:fx="http://javafx.com/fxml/1" fx:controller="edu.unidrive.controllers.AideAdmin">
    <children>
        <!-- Currency TextField -->
        <TextField fx:id="currencyField" layoutX="129.0" layoutY="130.0" prefHeight="26.0" prefWidth="197.0" promptText="Currency" styleClass="text-field" />

        <!-- Description TextField -->
        <TextField fx:id="descriptionField" layoutX="129.0" layoutY="166.0" prefHeight="26.0" prefWidth="197.0" promptText="Description" styleClass="text-field" />

        <!-- Montant TextField -->
        <TextField fx:id="montantField" layoutX="129.0" layoutY="202.0" prefHeight="26.0" prefWidth="197.0" promptText="Montant" styleClass="text-field" />

        <!-- Association ChoiceBox -->
        <ChoiceBox fx:id="associationChoiceBox" layoutX="129.0" layoutY="238.0" prefHeight="26.0" prefWidth="197.0" styleClass="choice-box" />

        <!-- Labels -->
        <Label layoutX="27.0" layoutY="135.0" prefHeight="18.0" prefWidth="90.0" styleClass="label" text="Currency :" />
        <Label layoutX="27.0" layoutY="171.0" prefHeight="18.0" prefWidth="90.0" styleClass="label" text="Description :" />
        <Label layoutX="27.0" layoutY="207.0" prefHeight="18.0" prefWidth="74.0" styleClass="label" text="Montant :" />
        <Label layoutX="27.0" layoutY="243.0" prefHeight="18.0" prefWidth="90.0" styleClass="label" text="Association :" />

        <!-- Buttons -->
        <Button layoutX="50.0" layoutY="290.0" mnemonicParsing="false" onAction="#ajouterAideaction" styleClass="button" text="Ajouter Aide" />
        <Button layoutX="176.0" layoutY="290.0" mnemonicParsing="false" onAction="#updateAction" styleClass="update-button" text="Update" />
        <Button layoutX="302.0" layoutY="290.0" mnemonicParsing="false" onAction="#deleteSelectedRow" styleClass="delete-button" text="Delete" />
        <Button layoutX="14.0" layoutY="14.0" mnemonicParsing="false" onAction="#goToBack" styleClass="back-button" text="Back" />
        <Button layoutX="732.0" layoutY="14.0" mnemonicParsing="false" onAction="#goToAnotherPage" styleClass="button" text="Next" />

        <!-- Search -->
        <Label layoutX="32.0" layoutY="333.0" styleClass="label" text="Search :" />
        <TextField fx:id="searchField" layoutX="94.0" layoutY="326.0" prefHeight="26.0" prefWidth="218.0" promptText="Search" styleClass="text-field" />

        <!-- Aide Table -->
        <TableView fx:id="aideTable" layoutX="61.0" layoutY="380.0" prefHeight="246.0" prefWidth="674.0">
            <columns>
                <TableColumn fx:id="idColumn" prefWidth="45.0" text="ID">
                    <cellValueFactory>
                        <PropertyValueFactory property="id" />
                    </cellValueFactory>
                </TableColumn>
                <TableColumn fx:id="currencyColumn" prefWidth="86.0" text="Currency">
                    <cellValueFactory>
                        <PropertyValueFactory property="currency" />
                    </cellValueFactory>
                </TableColumn>
                <TableColumn fx:id="descriptionColumn" prefWidth="203.0" text="Description">
                    <cellValueFactory>
                        <PropertyValueFactory property="description" />
                    </cellValueFactory>
                </TableColumn>
                <TableColumn fx:id="montantColumn" prefWidth="100.0" text="Montant">
                    <cellValueFactory>
                        <PropertyValueFactory property="montant" />
                    </cellValueFactory>
                </TableColumn>
                <TableColumn fx:id="createdAtColumn" prefWidth="120.0" text="Created At">
                    <cellValueFactory>
                        <PropertyValueFactory property="createdAt" />
                    </cellValueFactory>
                </TableColumn>
                <TableColumn fx:id="associationColumn" prefWidth="120.0" text="Association">
                    <cellValueFactory>
                        <PropertyValueFactory property="associationId" />
                    </cellValueFactory>
                </TableColumn>
            </columns>
        </TableView>

        <!-- Montant Chart -->
        <LineChart fx:id="montantChart" layoutX="348.0" layoutY="69.0" prefHeight="258.0" prefWidth="433.0">
            <xAxis>
                <CategoryAxis fx:id="xAxis" />
            </xAxis>
            <yAxis>
                <NumberAxis fx:id="yAxis" />
            </yAxis>
        </LineChart>
    </children>
</AnchorPane>
