# msgboard
Msgboard client written in C, Java and python by Yi Zhou.

run:
  c: 
    cd c;
    make; 
    ./msg_client "twood02.koding.com" "Chen"  "this is my message";
  
  java: 
    cd java;
    javac MsgClient.java;
    java MsgClient "192.168.1.1" "Joe" "Hello World";
    
  
  python: 
    cd python;
    python msgclient.py "user.koding.com"  "my name" my message";
