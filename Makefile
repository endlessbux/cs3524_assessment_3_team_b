all:
	make clean
	make mud

clean:
	rm -f cs3524/solutions/mud/*.class


mud:
	javac cs3524/solutions/mud/Edge.java
	javac cs3524/solutions/mud/MUD.java
	javac cs3524/solutions/mud/Vertex.java
	javac cs3524/solutions/mud/GameImplementation.java
	javac cs3524/solutions/mud/ServerMainline.java
	javac cs3524/solutions/mud/StubImplementation.java
	javac cs3524/solutions/mud/MUDGame.java