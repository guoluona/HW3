// CS 6421 - Simple Message Board Client in C
// Yi Zhou
// Compile with: make
// Run with: ./msg_client "twood02.koding.com" "Chen" "this is my message"

#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <inttypes.h>
#include <string.h>

int main(int argc, char ** argv)
{
        //exit if there are not enough arguements
        if(argc <= 3)   
        {
            printf("ERROR, please input line arguements as host, username, message\n");
            printf("e.g.: ./msg_client \"twood02.koding.com\" \"Chen\"  \"this is my message\"\n");
            exit(-1);
        }
        
        char* server_port = "5555";
        struct addrinfo hints, *res;
        int sockfd, rv, rc, sd;
        
        //test if there is enough space to malloc merged messages to send
        char* msg = malloc((strlen(argv[2])+strlen(argv[3])) * sizeof(char));
        if (msg == NULL)
        {
            printf("messages are too long, try to limit their size");
            exit(-1);
        }

        memset(&hints, 0, sizeof hints);    //clean up memory space for ai
        hints.ai_family = AF_UNSPEC;     // look for both IPV4 and IPV6
        hints.ai_socktype = SOCK_STREAM; // set socket type to SOCK_STREAM
        hints.ai_protocol = 0;  // automatically choose protocol type

        //load up address structs
        rv = getaddrinfo(argv[1], server_port, &hints, &res);
        
        //exit if loading failed
        if (rv==-1)
        {
            fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
            exit(-1);
        }

        // make a socket
        sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);

        //exit if open socket failed
        if (sockfd == -1) 
        {
            perror("ERROR, open socket failed");
            freeaddrinfo(res);
            exit(-1);
        }

        //connect to the server
        rc = connect(sockfd, res->ai_addr, res->ai_addrlen);

        //exit if connect failed 
        if (rc == -1) 
        {
            perror("ERROR, connect failed");
            close(sockfd);
            freeaddrinfo(res);
            exit(-1);
        }

        //merge messages
        strcpy(msg,argv[2]);
        strcat(msg,"\n");  
        strcat(msg,argv[3]);
        
        //send messages
        sd = send(sockfd, msg, strlen(msg)+1, 0);
        
        //exit if send failed 
        if (sd == -1) 
        {
            perror("ERROR, send failed");
            free(msg);
            close(rc);
            close(sockfd);
            freeaddrinfo(res);
            exit(-1);
        }else if (sd != strlen(msg) + 1)
        {
            printf("send incomplete messages");
        }
        
        //free and close operation
        free(msg);
        close(rc);
        close(sockfd);
        freeaddrinfo(res); 

        printf("Done.\n");
        return 0;
}
