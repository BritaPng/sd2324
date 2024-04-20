package tukano.clients.User;

import tukano.clients.rest.User.RestUsersClient;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class SearchUsers {
    private static Logger Log = Logger.getLogger(SearchUsers.class.getName());

    public static void main(String[] args) throws IOException {

        if( args.length != 2) {
            System.err.println( "Use: java lab2.clients.SearchUsers url pattern");
            return;
        }

        String serverUrl = args[0];
        String pattern = args[1];

        var client = new RestUsersClient( URI.create( serverUrl ) );

        var result = client.searchUsers(pattern);
        if( result.isOK()  )
            Log.info("Search Users:" + result.value() );
        else
            Log.info("Search Users failed with error: " + result.error());
    }

}
