/*
 * Crystal Cleaveland
 * Upload To Google Drive Tool
 * GoogleAuthorization.java - Authorization via Google Drive API
 * 29.Jun.2020
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

// Adapted from Google Drive API Java Quickstart
// https://developers.google.com/drive/api/v3/quickstart/java
public class GoogleAuthorization
{
    private static final String APPLICATION_NAME = "UploadToGoogleDrive";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Paths for credentials and refresh token values
    // See README.md for set up details
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String REFRESH_TOKEN = "/refresh_token.txt";

    public static Drive getGoogleDriveAuthorization() throws IOException, GeneralSecurityException
    {
        // Build a new authorized API client service
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder( transport, JSON_FACTORY, getCredentials( transport ) )
                .setApplicationName( APPLICATION_NAME )
                .build();
    }

    private static Credential getCredentials( final NetHttpTransport transport ) throws IOException
    {
        // Load client secrets
        InputStream in = UploadToGoogleDrive.class.getResourceAsStream( CREDENTIALS_FILE_PATH );
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( JSON_FACTORY, new InputStreamReader( in ) );

        // Create credential
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport( transport )
                .setJsonFactory( JSON_FACTORY )
                .setClientSecrets( clientSecrets )
                .build();

        // Set refresh token and retrieve access token
        String token = IOUtils.toString( UploadToGoogleDrive.class.getResourceAsStream( REFRESH_TOKEN ), StandardCharsets.UTF_8 );
        credential.setRefreshToken( token );
        credential.refreshToken();

        return credential;
    }
}
