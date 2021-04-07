all:
	make rmishoutclean
	make rmishout

rmishout:
	javac cs3524/solutions/rmishout/ShoutServerImplementation.java
	javac cs3524/solutions/rmishout/ShoutServerMainline.java
	javac cs3524/solutions/rmishout/ShoutClientImplementation.java

rmishoutclean:
	rm cs3524/solutions/rmishout/*.class