package tukano.api.java;

import tukano.api.Discovery;
import tukano.clients.rest.User.RestUsersClient;

import java.net.URI;

public class UsersClientFactory {

    public static Users getClient(String service) throws InterruptedException {
        URI[] serverURI = Discovery.getInstance().knownUrisOf(service,1); // use discovery to find a uri of the Users service;

       return new RestUsersClient( serverURI[0] );
    }
}