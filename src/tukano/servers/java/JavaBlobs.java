package tukano.servers.java;

import tukano.api.java.Blobs;
import tukano.api.java.Result;

import java.util.logging.Logger;

public class JavaBlobs implements Blobs {

    //private final Map<String,User> users = new HashMap<>();

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

    @Override
    public Result<Void> upload(String blobId, byte[] bytes) {
        return null;
    }

    @Override
    public Result<byte[]> download(String blobId) {
        return null;
    }

}
