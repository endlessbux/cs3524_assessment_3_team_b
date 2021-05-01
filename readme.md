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
- #### CGS B3-B1 (Valerio)
    - [x] More than one MUD game is instantiated by the game server
    - [x] User can see what MUD games are currently running on the server
    - [x] User can select a MUD game to join for game playing
    - [x] User can leave a MUD and end playing the MUD game
- #### CGS A5 (Cat)
    - [x] User can issue a command to create a new MUD game
    - [x] User can join, exit a game and join another one
    - [ ] User can have multiple games open (game focus)
    - [ ] The server restricts the number of MUDs
    - [ ] The server restricts the number of users logged onto a MUD
    - [ ] Well-organised CLI
- #### CGS A4-A1 (Thomas)
    - [ ] Console is refreshed automatically when changes occur
    - [ ] Server handles clients aborting, closing, or leaving game
    - [ ] Players can send messages to each other

   -###########################################################################
   #### HOW TO LAUNCH
To run the game
•	Run the ‘Makefile’ in machine Terminal 
•	The Makefile contains the ‘make mud’ command which compiles all the files of the game 
o	javac cs3524/solutions/mud/Edge.java
o	javac cs3524/solutions/mud/MUD.java
o	javac cs3524/solutions/mud/Vertex.java
o	javac cs3524/solutions/mud/GameImplementation.java
o	javac cs3524/solutions/mud/ServerMainline.java
o	javac cs3524/solutions/mud/StubImplementation.java
o	javac cs3524/solutions/mud/MUDGame.java
•	Load and start the RMI Registry with suitable port.
•	Open another Terminal and run the ServerMainline.Java file using the command …
•	Open a third Terminal and run the MUD client
################################################################################
####FUNCTIONS 
•	MOVEMENT- 
o	user can move in at least one direction.
o	User can move around in any direction.
o	
•	LOCATION- 
o	Start location of every player is printed out.
o	Information associated with player new location is displayed.
•	PICKING-
o	Players can pick up items.
o	Items picked up are stored up in inventory
o	Item picked up by a player is removed from item location.
o	
•	MULTI-PLAYER MUD-
o	More than one user can join the MUD game.
o	Players can see other users in the MUD game.
o	Players can see other player’s inventories
o	Users can move around in the MUD
o	Players can see other players at certain location in the MUD game.
o	
•	MULTI-MUD GAME-
o	More than 1 MUD game is instantiated on the server.
o	Users can see how many MUD games are currently running on the server.
o	Users can select any MUD game to join.
o	Users can Exit playing a particular MUD game on the server.

