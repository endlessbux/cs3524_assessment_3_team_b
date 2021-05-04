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
    - [x] User can have multiple games open (game focus)
    - [x] The server restricts the number of MUDs
    - [x] The server restricts the number of users logged onto a MUD
    - [x] Well-organised CLI
- #### CGS A4-A1 (Thomas)
    - [ ] Console is refreshed automatically when changes occur
    - [ ] Server handles clients aborting, closing, or leaving game
    - [ ] Players can send messages to each other

 ## HOW TO LAUNCH
 To run the game:
-   	Run the ‘Makefile’ in Terminal by typing 'make mud' 
-   	The Makefile contains the ‘make mud’ command which compiles all the files of the game 
       -	javac cs3524/solutions/mud/Edge.java
       -	javac cs3524/solutions/mud/MUD.java
       -	javac cs3524/solutions/mud/Vertex.java
       -	javac cs3524/solutions/mud/GameImplementation.java
       -	javac cs3524/solutions/mud/ServerMainline.java
       -	javac cs3524/solutions/mud/StubImplementation.java
       -	javac cs3524/solutions/mud/MUDGame.java
-   	Load and start the RMI Registry with a suitable port such as 50010.
-   	Open another Terminal and run the ServerMainline file by typing java cs3524.solutions.mud.ServerMainline [port] [port] (The first port should be that of the RMI registry) and the second can be one such as 50019
-   	Open a third Terminal window and run the MUD client by typing java cs3524.solutions.mud.GameImplementation [hostname] 50010

## FUNCTIONS
-   	MOVEMENT- 
      -  	user can move in at least one direction.
      -  	User can move around in any direction.
-   	LOCATION- 
      -  	Start location of every player is printed out.
      -  	Information associated with player new location is displayed.
-   	PICKING-
      -  	Players can pick up items.
      -  	Items picked up are stored up in inventory
      -  	Item picked up by a player is removed from item location.
      -  	Itemsm Picked can also be dropped by a player
-   	MULTI-PLAYER MUD-
      -  	More than one user can join a MUD game.
      -  	Players can see other users in the MUD game.
      -  	Players can see other player’s inventories
      -  	Users can move around in the MUD
      -  	Players can see other players at certain location in a MUD game.
    
-   	MULTI-MUD GAME-
      -	    More than 1 MUD game is instantiated on the server.
      - 	Users can see how many MUD games are currently running on the server.
      - 	Users can select any MUD game to join at a time.
      - 	Users can Exit playing a particular MUD game on the server.
      - 	Users can create new MUD game and change MUD game.
-   	OTHERS
      - 	users can quit the game with the key word “q”.
      - 	Users can use the keyword ”h” to get help messages and commands a player may type in.

    
##### COMMANDS
    -  move (direction)
        -  This allows players to change position in the MUD game to supported directions
    -  pick/drop (item)
        -  Allows users to pick up items and store them in the inventory
    -  show-user-location
        -  Prints out current users in the MUD game      
    -  show-online-players
        -  Prints out current players on the server
    -  show-inventory (playername)
        -  Prints out the specified players inventory
    -  pick ring
        -  Picks ring
    -  h
        -  Displays the help interface to assist players with commands
    -  n
        -  Starts a new game and asks for the name of the game you want to create
    -  s
        -  Asks the user to input the name of the game they want to join   
    -  q
        -  Quits the game and asks the user if they would like to join another game, play again or quit completely    
    
   

