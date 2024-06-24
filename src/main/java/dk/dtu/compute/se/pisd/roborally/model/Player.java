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
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * Represents a player in the RoboRally game. Each player has a set of programming
 * registers and command cards, a current position on the board, and a direction
 * they are facing. This class manages the player's state, including their name,
 * color, and the cards they have been dealt.
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;
    final public static int NO_UPGRADES = 3;
    final public static int NO_UPGRADE_INV = 6;


    final transient public Board board;
    @Expose
    private String name;
    @Expose
    private String color;
    @Expose
    private  Space space;
    @Expose
    private Heading heading = SOUTH;
    @Expose
    private CommandCardField[] program;
    @Expose
    private CommandCardField[] cards;

    private CommandCardField[] upgrades;
    @Expose
    private CommandCardField[] upgradeInv;
    private int index = 0;
    @Expose
    public double distance;
    @Expose
    public Phase phase;

    private int energy; // New attribute for energy level

    /**
     * Constructs a new Player with the specified board, color, and name.
     * Initializes the player's program and command card fields.
     * @param board the board on which the player is playing
     * @param color the color representing the player
     * @param name the name of the player
     */
    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;
        this.energy = 100; // Initialize with a default energy level


        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }

        upgrades = new CommandCardField[3];
        for (int i = 0; i < upgrades.length; i++) {
             upgrades[i] = new CommandCardField(this);
        }

        upgradeInv  = new CommandCardField[6];
        for (int i = 0; i < upgradeInv.length; i++) {
            upgradeInv[i] = new CommandCardField(this);
        }



    }

    /**
     * Gets the name of the player.
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player and notifies observers of the change.
     * @param name the new name of the player
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Gets the color representing the player.
     * @return the player's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color representing the player and notifies observers of the change.
     * @param color the new color of the player
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * Gets the current space the player is occupying on the board.
     * @return the player's current space
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Sets the player's current space on the board and notifies observers of the change.
     * @param space the new space for the player
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }

        }
    }

    /**
     * Gets the current heading (direction) of the player.
     * @return the player's current heading
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the heading (direction) of the player and notifies observers of the change.
     * @param heading the new heading for the player
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Gets the programming field at the specified index.
     * @param i the index of the programming field
     * @return the programming field at the specified index
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Gets the command card field at the specified index.
     * @param i the index of the command card field
     * @return the command card field at the specified index
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public CommandCardField getUpgradeField(int i) {
        return upgrades[i];
    }
    public CommandCardField getUpgradeInv(int i) {
        return upgradeInv[i];
    }

    public void incrementIndex() {
        index++;
    }

    public void move(int n) {
        if(n == 0) {
            return;
        } else {
            move(n - 1);
        }

        Space nextSpace = switch (heading) {
            case SOUTH -> board.getSpace(space.x, space.y + 1);
            case NORTH -> board.getSpace(space.x, space.y - 1);
            case WEST  -> board.getSpace(space.x - 1, space.y);
            case EAST  -> board.getSpace(space.x + 1, space.y);
        };

        if(nextSpace.getType() == ActionField.WALL)
            return;
        else if(nextSpace.getType() == ActionField.CHECKPOINT)
            incrementIndex();

        setSpace(nextSpace);
    }

    public int getIndex() {
        return index;
    }

    public void changeEnergy(int amount) {
        this.energy += amount;
        this.energy = Math.max(0, this.energy); // Ensure energy does not go below zero
        notifyChange(); // Notify observers of change
    }

    // Get current energy level
    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
        notifyChange();  // Notifying observers about the change
    }
}
