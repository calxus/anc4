Gordon Adam
1107425

If for any reason you need to build the project please follow the build instructions.
However, the project has already been compiled and placed in a .jar, so to execute the program please follow execute instructions

BUILD
=====
javac -cp lib\jgraphx.jar *.java
jar cvmf META-INF\MANIFEST.MF NetSim.jar *.class

EXECUTE
=======
java -jar NetSim.jar <Network Description File>


NETWORK DESCRIPTION FILES
=========================
netdesc.txt
netdesc_simple.txt