# CS 6421 - Simple Message Board Client in Python
# Yi Zhou
# Run with:     python msgclient.py "user.koding.com"  "my name" "my message"

import socket
import sys

if len(sys.argv) <= 3:
    print('ERROR, please input line arguements as host, username, message')
    print('e.g.: python msgclient.py "user.koding.com"  "my name" "my message"')
    sys.exit(-1)

portnum = 5555

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)   #get socket
try:
    sock.connect((sys.argv[1],portnum))   #get connetion
    sock.send(sys.argv[2]+'\n'+sys.argv[3]) #send messages
    sock.shutdown(socket.SHUT_WR)    #shutdown socket before close
    sock.close()   #close socket
except socket.error, arg:
    print 'operations failed. More info:', arg
    sys.exit(-1)

print('Sent message to server!')
