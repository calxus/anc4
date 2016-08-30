##Network Simulator
Written by: Gordon Adam

Program to simulate the routing tables of a computer network. The gui offers a cl interface in order to manipulate the simulation. All necessary information can be viewed on the command line. Though there is a visual representation offered as well. All instructions for building, executing and commands are given below.

###INSTRUCTIONS
If for any reason you need to build the project please follow the build instructions.
However, the project has already been compiled and placed in a .jar, so to execute the program please follow execute instructions

###BUILD
`javac -cp lib\jgraphx.jar *.java`
`jar cvmf META-INF\MANIFEST.MF NetSim.jar *.class`

###EXECUTE
`java -jar NetSim.jar <Network Description File>'`


###NETWORK DESCRIPTION FILES
`netdesc.txt`
`netdesc_simple.txt`

###COMMANDS
__table__ - _prints the routing table of each node_

__exchange__ - _Each node will swap its routing table with its direct neighbour. Nodes will compare their routing tables with their neighbours to find the shortest path to nodes they are not connected to. If an integer is provided the command will complete that many iterations of routing table swaps._

__remove__ - _takes two arguments, which indicate the link between two nodes. The link indicated will then be removed._

__edit__ - _takes three arguments, first two indicate the link between the two nodes and the third indicates the new cost of the link._

__trace__ - _takes a minimum of two arguments indicating the start node and end node. The command will try to reach the end destination and tell you the cost of travel. if a third argument of an integer is given the program will only travel that many nodes before giving up, if no argument is given default is "16"_

__splithorizon__ - _takes one of two arguments, either "on" or "off". for more information on split horizon visit: https://en.wikipedia.org/wiki/Split_horizon_route_advertisement_