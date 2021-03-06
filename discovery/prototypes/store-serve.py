#!/usr/bin/env python

# Ahsen Uppal
#
# CS 6421 - Simple Python based single value store server.
# Uses a dictionary dispatch table to process simple single value get
# and set commands.
#

import socket, sys
import traceback
BUFFER_SIZE = 1024

# Key-Value store
values = {}

def cmd_get(tokens):
    if len(tokens) > 1:
        return 'Invalid command: Only single value get supported.\n'
    elif not 'single' in values:
        return 'Error: single value not set.\n'
    return values['single']

def cmd_set(tokens):
    if len(tokens) > 2:
        return 'Invalid command: Only single value get supported.\n'
    values['single'] = tokens[1]
    return ('Value set to %s\n' % values['single'])

commands = { "get" : cmd_get,
             "set" : cmd_set,
             }

def process(conn):
    greeting = "Welcome, you are connected to a Python-based simple command server.\n"
    conn.sendall(greeting.encode('UTF-8'))
    userInput = conn.recv(BUFFER_SIZE).decode('UTF-8')

    if not userInput:
        print("Error reading message")
        return

    sys.stdout.write("Received message: %s" % userInput)
    tokens = userInput.split()

    try:
        if tokens[0] in commands:
            print("Processing command")
            response = commands[tokens[0]](tokens)
        else:
            response = ("Invalid command: %s\n" % tokens[0])
        sys.stdout.write("Sending response: %s" % response)
        conn.sendall(response.encode('UTF-8'))
    except:
        traceback.print_exc(file=sys.stdout)
        msg = ("Exception occurred: %s\n" % (userInput))
        conn.sendall(msg.encode('UTF-8'))
    return


if __name__ == '__main__':
    interface = ""

    if len(sys.argv) < 2:
        sys.stderr.write("usage: python {0} portnum\n".format(sys.argv[0]))
        sys.exit(1)

    portnum = int(sys.argv[1])
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((interface, portnum))
    s.listen(5)
    exit_flag = False
    try:
        print("Started Python-based command server on port %s" % (portnum))
        while not exit_flag:
            conn, addr = s.accept()
            print ('Accepted connection from client ', addr)
            try:
                process(conn)
            except:
                traceback.print_exc(file=sys.stdout)
                print ('Failed to process request from client. Continuing.')
            conn.close()
    except KeyboardInterrupt:
        exit_flag = True

    print("Exiting...")
    s.close()
    sys.exit(0)
