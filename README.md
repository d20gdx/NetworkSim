# Network Contention Simulator for LoraWAN (1.2 specification).

Scalability is an important consideration for LoRaWAN networks. As part of a research study (Msc disseration) at UCL, this simulation was developed in Java.  The objective of the simulation is to establish a baseline figure for packet loss. Real world packet loss will be higher due  to losses incurred by the RF channel. The classes implement a Java thread-based network contention simulation. 

The simulator code has the following features:

* The runner Java class is the primary test harness.
* This instantiates threads to simulate the behaviour of LoRaWAN nodes. These threads wait a random length of time then spawn connection requests.
* Connection threads attempt to acquire an available channel from the connection manager. Successful connections increment the channel occupancy count.
* When the connection drops, the occupancy count is decremented.
* If the selected channel is unavailable, the packet is marked as lost. The percentage of packets lost is recorded.

**UML and sequence diagram**
![UML Diagram](ContentionSimulator/UML.png?raw=true "UML Diagram")
