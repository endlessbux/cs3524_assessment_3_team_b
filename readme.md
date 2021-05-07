# CS3524 Group Assessment

### Requirements:
- #### CGS D3-D1
    - [x] Allow a player to move in at least one direction through console input
    - [x] Print out information from the start location
    - [x] Print out information associated with the new location after a move
- #### CGS C3-C1
    - [x] Make game multi-user
    - [x] Users can move around the MUD world in any direction
    - [x] Users can see other users in the MUD world
    - [x] Users can pick up things in the MUD
    - [x] Users can see a list of other players currently at location
    - [x] Users can see other players' inventories
    - [x] When an item is picked by a user it's removed from location
    - [x] Help command to show what commands are available and how to use them
- #### CGS B3-B1 
    - [x] More than one MUD game is instantiated by the game server
    - [x] User can see what MUD games are currently running on the server
    - [x] User can select a MUD game to join for game playing
    - [x] User can leave a MUD and end playing the MUD game
- #### CGS A5
    - [x] User can issue a command to create a new MUD game
    - [x] User can join, exit a game and join another one
    - [x] User can have multiple games open (game focus)
    - [x] The server restricts the number of MUDs
    - [x] The server restricts the number of users logged onto a MUD
    - [x] Well-organised CLI
- #### CGS A4-A1
    - [ ] Console is refreshed automatically when changes occur
    - [ ] Server handles clients aborting, closing, or leaving game
    - [ ] Players can send messages to each other


# Multi User Dungeon game created using Java RMI

This game was created for the final assignment of CS3524.

## How to launch

1. Run the ‘Makefile’ in Terminal by typing 'make mud'


2. Start the RMI registry with a suitable port such as 50010 'rmiregistry 50010'


3. Open another Terminal and run the ServerMainline file by typing 'java cs3524.solutions.mud.ServerMainline [port] [port]' (The first port should be that of the RMI registry) and the second can be one such as 50019


4. Open a third Terminal window and run the MUD client by typing 'java cs3524.solutions.mud.GameImplementation [hostname] 50010'

## Functions
###### Movement
* User can move around in any direction
###### Location
* Start location of every player is printed out
* Information associated with player new location is displayed
###### Picking
* Players can pick up items
* Items picked up are stored in the users inventory
* Items picked up by a player are removed from the item location
###### Multiplayer MUD
* More than one user can join a MUD game
* Players can see other users in the MUD game
* Players can see other players inventories
* Users can move around in the MUD
* Players can see other players at certain location in a MUD game
###### Multi-MUD Game
* More than 1 MUD game can be instantiated on the server
* Users can see how many MUD games are currently running on the server
* Users can select any MUD game to join
* Users can quit playing a particular MUD game on the server
* Users can create a new MUD game and change MUD game by quitting and rejoining desired game
###### Other
* Users can quit the MUD with the command 'q'
* Users can quit the current game with the command 'c'
* Users can show available commands with the keyword 'h'

    
## Commands
###### move [direction]
* This allows players to change position in the MUD game to supported directions

###### pick [item]
* Allows users to pick up items and store them in the inventory

###### show-user-location CURRENTLY DOES NOTHING
* Prints out current users in the MUD game  
   
###### show-online-players
* Prints out current players on the server

###### show-inventory [playername]
* Prints out the specified players inventory

###### h
* Displays the help interface to assist players with commands

###### n
* Starts a new game and asks for the name of the game you want to create

###### c
* Quits the current game

###### s
* Asks the user to input the name of the game they want to join 
  
###### q
* Quits the MUD and asks the user if they would like to join another game, play again or quit completely    
    
   
