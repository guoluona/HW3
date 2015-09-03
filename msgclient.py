# CS 6421 - Simple Message Board Client in Python
# Yi Zhou
# Run with:     python msgclient.py

import socket
import sys

host = "finger92.koding.io";
portnum = 5555;

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);   #get socket
sock.connect((host,portnum));   #get connetion
sock.send(sys.argv[1]+'\n'+sys.argv[2]); #send messages
sock.shutdown(socket.SHUT_WR);    #shutdown socket before close
sock.close();   #close socket

print("Sent message to server!")
