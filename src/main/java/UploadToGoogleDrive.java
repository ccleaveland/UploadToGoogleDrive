/*
 * Crystal Cleaveland
 * Upload To Google Drive Tool
 * UploadToGoogleDrive.java - Process commandline options to determine function
 * 29.Jun.2020
 */

import com.google.api.services.drive.Drive;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UploadToGoogleDrive
{
    public static void main( String[] args ) throws IOException, GeneralSecurityException
    {
        // Get Google Drive authorization
        Drive service = GoogleAuthorization.getGoogleDriveAuthorization();

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse( setOptions(), args );

            // Verify user has selected to upload either a file or directory
            if( cmd.hasOption( "d" ) == cmd.hasOption( "f" ) )
            {
                System.err.println( "Must contain either flag 'd' (directory) or 'f' (file)." );
            }
            // Verify user has specified path
            else if( !cmd.hasOption( "n" ) || !cmd.hasOption( "p" ) )
            {
                System.err.println( "Must contain flags 'n' (upload name) and 'p' (path of object to upload)." );
            }
            // Upload directory
            else if( cmd.hasOption( "d" ) )
            {
                FileProcessor.uploadDirectory( service, cmd.getOptionValue( "n" ), cmd.getOptionValue( "p" ), null);
            }
            // Upload file
            else if( cmd.hasOption( "f" )  )
            {
                FileProcessor.uploadFile( service, cmd.getOptionValue( "n" ), cmd.getOptionValue( "p" ), null );
            }
        }
        catch( ParseException ex )
        {
            System.err.println( "Parsing failed. " + ex.getMessage() );
        }
    }

    private static Options setOptions()
    {
        Options options = new Options();
        options.addOption("d", false, "Upload a directory" );
        options.addOption("f", false, "Upload a file" );
        options.addOption("n", true, "Name of directory or file when uploaded" );
        options.addOption("p", true, "Path of directory or file to upload" );

        return options;
    }
}
