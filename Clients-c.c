#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

void main()
{
    
struct addrinfo hints, *res;
int sockfd, rv, rc;

//load up address structs

memset(&hints, 0, sizeof hints);    //clean up memory space for ai
hints.ai_family = AF_UNSPEC;     // look for both IPV4 and IPV6
hints.ai_socktype = SOCK_STREAM; // set socket type to SOCK_STREAM
hints.ai_protocol = 0;  // automatically choose protocol type

//exit if loading is failed

if((rv = getaddrinfo("52.22.177.248", "5555", &hints, &res))!=0)
{
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
    exit(1);
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

char *temp = "zhouyi\\r\\nhi";

// send data normally:

send(sockfd, temp, sizeof temp, 0);

//free addrinfo 
freeaddrinfo(res); 

}