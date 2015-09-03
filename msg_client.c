// CS 6421 - Simple Message Board Client in C
// Yi Zhou
// Compile with: make
// Run with: ./msg_client

#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <inttypes.h>
#include <string.h>
#define MAX_MSG_SIZE 20 //maximum length of messages

int main(int argc, char ** argv)
{
        if(argc == 1)
        {
            perror("ERROR parameters");
            exit(-1);
        }
        char* server_port = "5555";
        char* server_ip = "finger92.koding.io";

        struct addrinfo hints, *res;
        int sockfd, rv, rc;

        memset(&hints, 0, sizeof hints);    //clean up memory space for ai
        hints.ai_family = AF_UNSPEC;     // look for both IPV4 and IPV6
        hints.ai_socktype = SOCK_STREAM; // set socket type to SOCK_STREAM
        hints.ai_protocol = 0;  // automatically choose protocol type

         //load up address structs
        
        rv = getaddrinfo(server_ip, server_port, &hints, &res);
        
        //exit if loading failed
        
        if(rv==-1)
        {
            fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
            exit(-1);
        }

        // make a socket

        sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);

        //exit if opening socket failed

        if (sockfd == -1) 
        {
            perror("ERROR opening socket");
            exit(-1);
        }

        //connect to the server

        rc = connect(sockfd, res->ai_addr, res->ai_addrlen);

        //exit if the connection is failed 

        if (rc == -1) 
        {
            perror("ERROR on connect");
            close(sockfd);
            exit(-1);
        }

        //send messages
        char* msg = malloc(MAX_MSG_SIZE * sizeof(char));
        strcpy(msg,argv[argc-2]);
        strcat(msg,"\n");   //merge messages
        strcat(msg,argv[argc-1]);
        send(sockfd, msg, strlen(msg)+1, 0);
        
        //free addrinfo 
        
        freeaddrinfo(res); 

        printf("Done.\n");
        return 0;
}
