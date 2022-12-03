package edu.pitt.cs;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.*;
import org.mockito.*;
//import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CoffeeMakerQuestTest {

	CoffeeMakerQuest cmq;
	Player player;
	ArrayList<Room> rooms;

	@Before
	public void setup() {
		// 0. Turn on bug injection for Player and Room.
		Config.setBuggyPlayer(true);
		Config.setBuggyRoom(true);

		// 1. Create a Player with no items (no coffee, no cream, no sugar)
		// and assign to player.
		player = Mockito.mock(Player.class);
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(false);
		Mockito.when(player.getInventoryString()).thenReturn("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n");

		// 2. Create 6 rooms as specified in rooms.config and add to rooms list.
		rooms = new ArrayList<>();

		Room r1 = Mockito.mock(Room.class);
		Mockito.when(r1.getFurnishing()).thenReturn("Quaint sofa");
		Mockito.when(r1.getAdjective()).thenReturn("Small");
		Mockito.when(r1.getItem()).thenReturn(Item.CREAM);
		Mockito.when(r1.getNorthDoor()).thenReturn("Magenta");
		Mockito.when(r1.getSouthDoor()).thenReturn(null);
		rooms.add(r1);

		Room r2 = Mockito.mock(Room.class);
		Mockito.when(r2.getFurnishing()).thenReturn("Sad record player");
		Mockito.when(r2.getAdjective()).thenReturn("Funny");
		Mockito.when(r2.getItem()).thenReturn(Item.NONE);
		Mockito.when(r2.getNorthDoor()).thenReturn("Beige");
		Mockito.when(r2.getSouthDoor()).thenReturn("Massive");
		rooms.add(r2);

		Room r3 = Mockito.mock(Room.class);
		Mockito.when(r3.getFurnishing()).thenReturn("Tight pizza");
		Mockito.when(r3.getAdjective()).thenReturn("Refinanced");
		Mockito.when(r3.getItem()).thenReturn(Item.COFFEE);
		Mockito.when(r3.getNorthDoor()).thenReturn("Dead");
		Mockito.when(r3.getSouthDoor()).thenReturn("Smart");
		rooms.add(r3);

		Room r4 = Mockito.mock(Room.class);
		Mockito.when(r4.getFurnishing()).thenReturn("Flat energy drink");
		Mockito.when(r4.getAdjective()).thenReturn("Dumb");
		Mockito.when(r4.getItem()).thenReturn(Item.NONE);
		Mockito.when(r4.getNorthDoor()).thenReturn("Vivacious");
		Mockito.when(r4.getSouthDoor()).thenReturn("Slim");
		rooms.add(r4);

		Room r5 = Mockito.mock(Room.class);
		Mockito.when(r5.getFurnishing()).thenReturn("Beautiful bag of money");
		Mockito.when(r5.getAdjective()).thenReturn("Bloodthirsty");
		Mockito.when(r5.getItem()).thenReturn(Item.NONE);
		Mockito.when(r5.getNorthDoor()).thenReturn("Purple");
		Mockito.when(r5.getSouthDoor()).thenReturn("Sandy");
		rooms.add(r5);

		Room r6 = Mockito.mock(Room.class);
		Mockito.when(r6.getFurnishing()).thenReturn("Perfect air hockey table");
		Mockito.when(r6.getAdjective()).thenReturn("Rough");
		Mockito.when(r6.getItem()).thenReturn(Item.SUGAR);
		Mockito.when(r6.getNorthDoor()).thenReturn(null);
		Mockito.when(r6.getSouthDoor()).thenReturn("Minimalist");
		rooms.add(r6);

		// 3. Create Coffee Maker Quest game using player and rooms, and assign to cmq.
		cmq = CoffeeMakerQuest.createInstance(player, rooms);
	}

	@After
	public void tearDown() {
		cmq = null;
		player = null;
		rooms = null;
	}

	/**
	 * Test case for String getInstructionsString().
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.getInstructionsString().
	 * Postconditions: Return value is " INSTRUCTIONS (N,S,L,I,D,H) > ".
	 * </pre>
	 */
	@Test
	public void testGetInstructionsString() {
		assertEquals(" INSTRUCTIONS (N,S,L,I,D,H) > ", cmq.getInstructionsString());
	}

	/**
	 * Test case for Room getCurrentRoom().
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.getCurrentRoom().
	 * Postconditions: Return value is rooms.get(0).
	 * </pre>
	 */
	@Test
	public void testGetCurrentRoom() {
		assertEquals(cmq.getCurrentRoom(),rooms.get(0));
	}

	/**
	 * Test case for void setCurrentRoom(Room room) and Room getCurrentRoom().
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.setCurrentRoom(rooms.get(2)).
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.setCurrentRoom(rooms.get(2)) is true. 
	 *                 Return value of cmq.getCurrentRoom() is rooms.get(2).
	 * </pre>
	 */
	@Test
	public void testSetCurrentRoom() {
		assertTrue(cmq.setCurrentRoom(rooms.get(2)));
		assertEquals(rooms.get(2), cmq.getCurrentRoom());
	}

	/**
	 * Test case for boolean areDoorsPlacedCorrectly() when check succeeds.
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.areDoorsPlacedCorrectly().
	 * Postconditions: Return value of cmq.areDoorsPlacedCorrectly() is true.
	 * </pre>
	 */
	@Test
	public void testAreDoorsPlacedCorrectly() {
//		assertEqual(rooms.get(0).getNorthDoor(),"");
		assertTrue(cmq.areDoorsPlacedCorrectly());
	}

	/**
	 * Test case for boolean areDoorsPlacedCorrectly() when check fails.
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 *                rooms.get(3) is modified so that it has no south door.
	 * Execution steps: Call cmq.areDoorsPlacedCorrectly().
	 * Postconditions: Return value of cmq.areDoorsPlacedCorrectly() is false.
	 * </pre>
	 */
	@Test
	public void testAreDoorsPlacedCorrectlyMissingSouthDoor() {
		Room r3 = rooms.get(3);
		Mockito.when(r3.getSouthDoor()).thenReturn(null);
		assertFalse(cmq.areDoorsPlacedCorrectly());
	}

	/**
	 * Test case for boolean areRoomsUnique() when check fails.
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 *                rooms.get(2) is modified so that its adjective is modified to "Small".
	 * Execution steps: Call cmq.areDoorsPlacedCorrectly().
	 * Postconditions: Return value of cmq.areRoomsUnique() is false.
	 * </pre>
	 */

	@Test
	public void testAreRoomsUniqueDuplicateAdjective() {
		Room r2= rooms.get(2);
		Mockito.when(r2.getAdjective()).thenReturn("Small");
		assertFalse(cmq.areRoomsUnique());
	}

	/**
	 * Test case for String processCommand("I").
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.processCommand("I").
	 * Postconditions: Return value is "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n".
	 * </pre>
	 */
	@Test
	public void testProcessCommandI() {
		assertEquals("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n", cmq.processCommand("I"));
	}

	/**
	 * Test case for String processCommand("l").
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.processCommand("l").
	 * Postconditions: Return value is "There might be something here...\nYou found some creamy cream!\n".
	 *                 Item.CREAM has been added to the Player.
	 * </pre>
	 */
	@Test
	public void testProcessCommandLCream() {
		assertEquals("There might be something here...\nYou found some creamy cream!\n", cmq.processCommand("l"));
		Mockito.verify(player).addItem(Item.CREAM);
	}

	@Test
	public void testProcessCommandLCoffee() {
		Mockito.when(rooms.get(0).getItem()).thenReturn(Item.COFFEE);
		assertEquals("There might be something here...\nYou found some caffeinated coffee!\n", cmq.processCommand("L"));
		Mockito.verify(player).addItem(Item.COFFEE);
	}

	@Test
	public void testProcessCommandLSugar() {
		Mockito.when(rooms.get(0).getItem()).thenReturn(Item.SUGAR);
		assertEquals("There might be something here...\nYou found some sweet sugar!\n", cmq.processCommand("l"));
		Mockito.verify(player).addItem(Item.SUGAR);
	}

	@Test
	public void testProcessCommandLNone() {
		Mockito.when(rooms.get(0).getItem()).thenReturn(Item.NONE);
		assertEquals("You don't see anything out of the ordinary.", cmq.processCommand("L"));
		Mockito.verify(player).addItem(Item.NONE);
	}

	/**
	 * Test case for String processCommand("n").
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 *                cmq.setCurrentRoom(rooms.get(3)) has been called.
	 * Execution steps: Call cmq.processCommand("n").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("n") is "".
	 *                 Return value of cmq.getCurrentRoom() is rooms.get(4).
	 * </pre>
	 */
	@Test
	public void testProcessCommandN() {
		cmq.setCurrentRoom(rooms.get(3));
		assertEquals(cmq.processCommand("n"),"");
		assertEquals(cmq.getCurrentRoom(),rooms.get(4));
	}

	/**
	 * Test case for String processCommand("s").
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.processCommand("s").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("s") is "A door in that direction does not exist.\n".
	 *                 Return value of cmq.getCurrentRoom() is rooms.get(0).
	 * </pre>
	 */
	@Test
	public void testProcessCommandS() {
		assertEquals("A door in that direction does not exist.\n", cmq.processCommand("s"));
		assertEquals(cmq.getCurrentRoom(),rooms.get(0));
	}

	/**
	 * Test case for String processCommand("D").
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 * Execution steps: Call cmq.processCommand("D").
	 *                  Call cmq.isGameOver().
	 * Postconditions: Return value of cmq.processCommand("D") is "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n".
	 *                 Return value of cmq.isGameOver() is true.
	 * </pre>
	 */
	@Test
	public void testProcessCommandDLose() {
		assertEquals("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n",
			cmq.processCommand("D"));
		assertTrue(cmq.isGameOver());
	}

	@Test
	public void testProcessCommandH() {
		assertEquals("\nN - Go north\nS - go south\nL - Look and collect any items in the room\nI - Show inventory of items collected\nD - Drink coffee made from items in inventory\n",
			cmq.processCommand("H"));
	}

	/**
	 * Test case for String processCommand("D").
	 * 
	 * <pre>
	 * Preconditions: Player, rooms, and cmq test fixture been created.
	 *                Player has all 3 items (coffee, cream, sugar).
	 * Execution steps: Call cmq.processCommand("D").
	 *                  Call cmq.isGameOver().
	 * Postconditions: Return value of cmq.processCommand("D") is "You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n\nYou drink the beverage and are ready to study!\nYou win!\n".
	 *                 Return value of cmq.isGameOver() is true.
	 * </pre>
	 */
	@Test
	public void testProcessCommandDWin() {
		Mockito.when(player.checkCoffee()).thenReturn(true);
		Mockito.when(player.checkCream()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(true);
		Mockito.when(player.getInventoryString()).thenReturn("You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n");

		assertEquals("You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n\nYou drink the beverage and are ready to study!\nYou win!\n",
			cmq.processCommand("D"));
		assertTrue(cmq.isGameOver());
	}

	@Test
	public void testProcessCommandSpecialChar() {
		assertEquals("What?\n", cmq.processCommand("@"));
	}

	@Test
	public void testProcessDrinkAllFalse() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(false);
		assertEquals("\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n", 
			getProcessDrinkMethod().invoke(cmq));
	}

	@Test
	public void testProcessDrinkCoffee() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Mockito.when(player.checkCoffee()).thenReturn(true);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(false);
		assertEquals("\nWithout cream, you get an ulcer and cannot study.\nYou lose!\n", 
			getProcessDrinkMethod().invoke(cmq));
	}

	@Test
	public void testProcessDrinkCream() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(false);
		assertEquals("\nYou drink the cream, but without caffeine, you cannot study.\nYou lose!\n", 
			getProcessDrinkMethod().invoke(cmq));
	}

	@Test
	public void testProcessDrinkSugar() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(true);
		assertEquals("\nYou eat the sugar, but without caffeine, you cannot study.\nYou lose!\n", 
			getProcessDrinkMethod().invoke(cmq));
	}

	@Test
	public void testProcessDrinkCreamSugar() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(true);
		assertEquals("\nYou drink the sweetened cream, but without caffeine you cannot study.\nYou lose!\n", 
			getProcessDrinkMethod().invoke(cmq));
	}

	@Test
	public void testProcessDrinkCoffeeCream() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Mockito.when(player.checkCoffee()).thenReturn(true);
		Mockito.when(player.checkCream()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(false);
		assertEquals("\nWithout sugar, the coffee is too bitter. You cannot study.\nYou lose!\n", 
			getProcessDrinkMethod().invoke(cmq));
	}

	@Test
	public void testProcessCoffeeSugar() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Mockito.when(player.checkCoffee()).thenReturn(true);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(true);
		assertEquals("\nWithout cream, you get an ulcer and cannot study.\nYou lose!\n", 
			getProcessDrinkMethod().invoke(cmq));
	}

	@Test
	public void testGetHelpString() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		assertEquals("\nN - Go north\nS - go south\nL - Look and collect any items in the room\nI - Show inventory of items collected\nD - Drink coffee made from items in inventory\n",
			getHelpStringMethod().invoke(cmq));
	}

	private Method getProcessDrinkMethod() throws NoSuchMethodException {
		Method method = CoffeeMakerQuestImpl.class.getDeclaredMethod("processDrink");
		method.setAccessible(true);
		return method;
	}

	private Method getHelpStringMethod() throws NoSuchMethodException {
		Method method = CoffeeMakerQuestImpl.class.getDeclaredMethod("getHelpString");
		method.setAccessible(true);
		return method;
	}

}