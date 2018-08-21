# Travis Alerts Application
A Java-based application designed to poll Travis CI on a basis specified by the user and notify specified, relevant Slack Channels should a branch be in failed state. 

#Getting Started (First Time Installation)
1. Create a .jar of this project (mvn package)
2. Run the .jar on a server with port 80 unused.

#Configuring new channels
1. Go to [YOURSERVERHOSTNAME]/newchannel and follow the instructions on-screen.
2. On the newly configured channel, type /addbranch [repo] [branch] to start polling a new branch.
3. Type /startpolling [time] to commence polling.
