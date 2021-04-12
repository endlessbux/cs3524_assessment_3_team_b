all:
	make rmishoutclean
	make mudclean
	make mud
	make rmishout

rmishout:
	javac cs3524/solutions/rmishout/ShoutServerImplementation.java
	javac cs3524/solutions/rmishout/ShoutServerMainline.java
	javac cs3524/solutions/rmishout/ShoutClientImplementation.java

mud:
	javac cs3524/solutions/mud/Edge.java
	javac cs3524/solutions/mud/MUD.java
	javac cs3524/solutions/mud/Vertex.java

rmishoutclean:
	rm -f cs3524/solutions/rmishout/*.class

mudclean:
	rm -f cs3524/solutions/mud/*.class