/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the graphical user interface for a player in the RoboRally game,
 * including the programming board and command cards. It allows the player to
 * view and interact with their program and command cards during the game.
 * It observes changes to the player's state and updates the display accordingly.
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayerView extends Tab implements ViewObserver {

    private Player player;
    private VBox top;
    private Label temporaryUpgrade;
    private Label PermanentUpgrade;
    private HBox Upgrade;
    private HBox upgradeLabels;
    private HBox labels;
    private Label programLabel;
    private GridPane programPane;
    private Label cardsLabel;
    private Label energyLabel; // Label to display the player's energy

    private GridPane cardsPane;
    private GridPane upgradesPane;
    private GridPane upgradesInvPane;
    private CardFieldView[] programCardViews;
    private CardFieldView[] cardViews;
    private CardFieldView[] upgradesViews;
    private CardFieldView[] upgradesInvViews;
    private VBox buttonPanel;
    private Button finishButton;
    private Button executeButton;
    private Button stepButton;
    private VBox playerInteractionPanel;
    private GameController gameController;

    /**
     * Constructs a PlayerView for the specified player and game controller.
     * @param gameController The game controller handling game logic.
     * @param player The player this view represents.
     */

    public PlayerView(@NotNull GameController gameController, @NotNull Player player) {
        super(player.getName());
        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        this.setContent(top);
        this.gameController = gameController;
        this.player = player;

        // Initialize the energy label and add to the UI
        energyLabel = new Label("Energy: " + player.getEnergy());
        top.getChildren().add(energyLabel);

        programLabel = new Label("Program");
        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CommandCardField cardField = player.getProgramField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(gameController, cardField, player, "card");
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // XXX  the following buttons should actually not be on the tabs of the individual
        //      players, but on the PlayersView (view for all players). This should be
        //      refactored.

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction( e -> gameController.finishProgrammingPhase());

        executeButton = new Button("Execute Program");
        executeButton.setOnAction( e-> gameController.executePrograms());

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction( e-> gameController.executeRegister());

        buttonPanel = new VBox(finishButton, executeButton, stepButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CommandCardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField, player,"card");
                cardsPane.add(cardViews[i], i, 0);
            }
        }


        Label currentUpgrades = new Label("Current Upgrades");
        upgradesPane = new GridPane();
        upgradesPane.setVgap(2.0);
        upgradesPane.setHgap(2.0);
        upgradesViews = new CardFieldView[Player.NO_UPGRADES];
        for (int i = 0; i < Player.NO_UPGRADES; i++) {
            CommandCardField cardField = player.getUpgradeField(i);
            if (cardField != null) {
                upgradesViews[i] = new CardFieldView(gameController, cardField, player,"upgrade");
                upgradesPane.add(upgradesViews[i], i, 0);
            }
        }

        Label upgradeCards = new Label("Available upgrades");
        upgradesInvPane = new GridPane();
        upgradesInvPane.setVgap(2.0);
        upgradesInvPane.setHgap(2.0);
        upgradesInvViews = new CardFieldView[Player.NO_UPGRADE_INV];
        for (int i = 0; i < Player.NO_UPGRADE_INV; i++) {
            CommandCardField cardField = player.getUpgradeInv(i);
            if (cardField != null) {
                upgradesInvViews[i] = new CardFieldView(gameController, cardField, player,"upgrade");
                upgradesInvPane.add(upgradesInvViews[i], i, 0);
            }
        }

        //CardFieldView tmp = new CardFieldView(gameController,player.PlayerUpgradeTmp);
           // CardFieldView perm = new CardFieldView(gameController,player.PlayerUpgradePerm);

        top.getChildren().add(playerInteractionPanel);
        top.getChildren().add(currentUpgrades);
        top.getChildren().add(upgradesPane);
        top.getChildren().add(upgradeCards);
        top.getChildren().add(upgradesInvPane);
        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

        if (player.board != null) {
            player.board.attach(this);
            update(player.board);
        }

        this.setOnSelectionChanged(event -> {
            if (isSelected()) {
                this.player.board.setCurrentPlayer(this.player);
                this.player.board.getStatusMessage();
                // Perform actions specific to when PlayerView tab is selected
            }
        });

        updateView(player); // Ensure the view is updated with the current player state


    }

    /**
     * Updates the view based on changes to the observed player's state.
     * This method is invoked in response to notifications from the observed subject.
     * @param subject The subject (player) whose state has changed.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == player.board) {
            energyLabel.setText("Energy: " + player.getEnergy());

            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                    CardFieldView cardFieldView = programCardViews[i];
                    if (cardFieldView != null) {
                        if (player.board.getPhase() == Phase.PROGRAMMING) {
                            cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                        } else {
                            if (i < player.board.getStep()) {
                                cardFieldView.setBackground(CardFieldView.BG_DONE);
                            } else if (i == player.board.getStep()) {
                                if (player.board.getCurrentPlayer() == player) {
                                    cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
                                } else if (player.board.getPlayerNumber(player.board.getCurrentPlayer()) > player.board.getPlayerNumber(player)) {
                                    cardFieldView.setBackground(CardFieldView.BG_DONE);
                                } else {
                                    cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                                }
                            } else {
                                cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                            }
                        }
                    }
                }


            if (player.board.getPhase() != Phase.PLAYER_INTERACTION) {
                if (!programPane.getChildren().contains(buttonPanel)) {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS + 2, 0);
                }
                switch (player.board.getPhase()) {
                    case INITIALISATION:
                        finishButton.setDisable(true);
                        // XXX just to make sure that there is a way for the player to get
                        //     from the initialization phase to the programming phase somehow!
                        executeButton.setDisable(false);
                        stepButton.setDisable(true);
                        break;

                    case PROGRAMMING:
                        finishButton.setDisable(false);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        break;

                    case ACTIVATION:
                        finishButton.setDisable(true);
                        executeButton.setDisable(false);
                        stepButton.setDisable(false);
                        break;

                    default:
                        finishButton.setDisable(true);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                }


            } else {
                if (!programPane.getChildren().contains(playerInteractionPanel)) {
                    programPane.getChildren().remove(buttonPanel);
                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
                }
                playerInteractionPanel.getChildren().clear();

                if (player.board.getCurrentPlayer() == player) {
                    // TODO Assignment V3: these buttons should be shown only when there is
                    //      an interactive command card, and the buttons should represent
                    //      the player's choices of the interactive command card. The
                    //      following is just a mockup showing two options
                    Button optionButton = new Button("Option1");
                    optionButton.setOnAction( e -> gameController.notImplemented());
                    optionButton.setDisable(false);
                    playerInteractionPanel.getChildren().add(optionButton);

                    optionButton = new Button("Option 2");
                    optionButton.setOnAction( e -> gameController.notImplemented());
                    optionButton.setDisable(false);
                    playerInteractionPanel.getChildren().add(optionButton);
                }
            }
        }
    }
}
