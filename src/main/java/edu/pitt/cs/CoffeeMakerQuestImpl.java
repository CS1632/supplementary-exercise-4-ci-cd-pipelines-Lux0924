package edu.pitt.cs;

import java.util.*;

public class CoffeeMakerQuestImpl implements CoffeeMakerQuest {

	private Player player;
	private ArrayList<Room> rooms;
	private int currentRoomIndex;
	private boolean gameOver;

	/**
	 * Constructor. Rooms are laid out from south to north, such that the
	 * first room in rooms becomes the southernmost room and the last room becomes
	 * the northernmost room. Initially, the player is at the southernmost room.
	 * 
	 * @param player Player for this game
	 * @param rooms  List of rooms in this game
	 */
	CoffeeMakerQuestImpl(Player player, ArrayList<Room> rooms) {
		this.player = player;
		this.rooms = rooms;
		this.currentRoomIndex = 0;
		this.gameOver = false;
	}

	/**
	 * Whether the game is over. The game ends when the player drinks the coffee.
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * The method returns success if and only if: 1) Th southernmost room has a
	 * north door only, 2) The northernmost room has a south door only, and 3) The
	 * rooms in the middle have both north and south doors. If there is only one
	 * room, there should be no doors.
	 * 
	 * @return true if check successful, false otherwise
	 */
	public boolean areDoorsPlacedCorrectly() {
		if(rooms.size()==1)
			return (rooms.get(0).getNorthDoor()==null)||(rooms.get(0).getSouthDoor()==null);
		for(int i=1; i<rooms.size()-1; i++){
			if(rooms.get(i).getNorthDoor()==null||rooms.get(i).getSouthDoor()==null)
				return false;
		}
		return rooms.get(0).getSouthDoor()==null&&rooms.get(rooms.size()-1).getNorthDoor()==null;
	}

	/**
	 * Checks whether each room has a unique adjective and furnishing.
	 * 
	 * @return true if check successful, false otherwise
	 */

	public boolean areRoomsUnique() {
		HashSet<String>uniqueAdjective= new HashSet<String>();
		HashSet<String>uniqueFurnishing= new HashSet<String>();
		for(int i=0; i<rooms.size(); i++){
			if(uniqueAdjective.contains(rooms.get(i).getAdjective())||uniqueFurnishing.contains(rooms.get(i).getFurnishing()))
				return false;
			uniqueAdjective.add(rooms.get(i).getAdjective());
			uniqueFurnishing.add(rooms.get(1).getFurnishing());
		}
		return true;
	}

	/**
	 * Returns the room the player is currently in. If location of player has not
	 * yet been initialized with setCurrentRoom, returns null.
	 * 
	 * @return room player is in, or null if not yet initialized
	 */
	public Room getCurrentRoom() {
		if(rooms.get(currentRoomIndex)==null)
			return null;
		return rooms.get(currentRoomIndex);
	}

	/**
	 * Set the current location of the player. If room does not exist in the game,
	 * then the location of the player does not change and false is returned.
	 * 
	 * @param room the room to set as the player location
	 * @return true if successful, false otherwise
	 */
	public boolean setCurrentRoom(Room room) {
		for (int i = 0; i < rooms.size(); i++) {
			Room r = rooms.get(i);
			if (r.equals(room)) {
				currentRoomIndex = i;
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the instructions string command prompt. It returns the following prompt:
	 * " INSTRUCTIONS (N,S,L,I,D,H) > ".
	 * 
	 * @return comamnd prompt string
	 */
	public String getInstructionsString() {
		return " INSTRUCTIONS (N,S,L,I,D,H) > ";
	}

	/**
	 * Processes the user command given in String cmd and returns the response
	 * string. For the list of commands, please see the Coffee Maker Quest
	 * requirements documentation (note that commands can be both upper-case and
	 * lower-case). For the response strings, observe the response strings printed
	 * by coffeemaker.jar. The "N" and "S" commands potentially change the location
	 * of the player. The "L" command potentially adds an item to the player
	 * inventory. The "D" command drinks the coffee and ends the game. Make
	 * sure you use Player.getInventoryString() whenever you need to display
	 * the inventory.
	 * 
	 * @param cmd the user command
	 * @return response string for the command
	 */
	public String processCommand(String cmd) {
		String additionalText = "";
		switch(cmd) {
			case "N":
			case "n":
				if (currentRoomIndex == rooms.size()-1) {
					additionalText = "A door in that direction does not exist.\n";
				} else {
					currentRoomIndex++;
				}
				return additionalText;
			case "S":
			case "s":
				if (currentRoomIndex == 0) {
					additionalText = "A door in that direction does not exist.\n";
				} else {
					currentRoomIndex--;
				}
				return additionalText;
			case "L":
			case "l":
				Item i = rooms.get(currentRoomIndex).getItem();
				if (i == Item.NONE) {
					additionalText = "You don't see anything out of the ordinary.";
				} else if (i == Item.SUGAR) {
					additionalText = "There might be something here...\nYou found some sweet sugar!\n";
				} else if (i == Item.COFFEE) {
					additionalText = "There might be something here...\nYou found some caffeinated coffee!\n";
				} else if (i == Item.CREAM) {
					additionalText = "There might be something here...\nYou found some creamy cream!\n";
				}
				player.addItem(i);
				break;
			case "I":
			case "i":
				additionalText = player.getInventoryString();
				return additionalText;
			case "D":
			case "d":
				gameOver = true;
				return player.getInventoryString() + processDrink();
			case "H":
			case "h":
				additionalText = getHelpString();
				break;
			default:
				additionalText = "What?\n";
				break;
		}
		return additionalText;
	}

	private String processDrink() {
		if (player.checkCoffee() && player.checkCream() && player.checkSugar()) {
			return "\nYou drink the beverage and are ready to study!\nYou win!\n";
		} else if (player.checkCoffee() && player.checkCream() && !player.checkSugar()) {
			return "\nWithout sugar, the coffee is too bitter. You cannot study.\nYou lose!\n";
		} else if (player.checkCoffee()  && !player.checkCream() && player.checkSugar()) {
			return "\nWithout cream, you get an ulcer and cannot study.\nYou lose!\n";
		} else if (!player.checkCoffee() && player.checkCream() && player.checkSugar()) {
			return "\nYou drink the sweetened cream, but without caffeine you cannot study.\nYou lose!\n";
		} else if (player.checkCoffee() && !player.checkCream() && !player.checkSugar()) {
			return "\nWithout cream, you get an ulcer and cannot study.\nYou lose!\n";
		} else if (!player.checkCoffee() && player.checkCream() &&!player.checkSugar()) {
			return "\nYou drink the cream, but without caffeine, you cannot study.\nYou lose!\n";
		} else if (!player.checkCoffee() && !player.checkCream() && player.checkSugar()) {
			return "\nYou eat the sugar, but without caffeine, you cannot study.\nYou lose!\n";
		}
		return "\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n";
	}

	private String getHelpString() {
		return "\nN - Go north\nS - go south\nL - Look and collect any items in the room\nI - Show inventory of items collected\nD - Drink coffee made from items in inventory\n";
	}

}
