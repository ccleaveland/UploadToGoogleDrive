/*
 * Crystal Cleaveland
 * Upload To Google Drive Tool
 * FileProcessor.java - Copy local directories and files to Google Drive
 * 29.Jun.2020
 */

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.common.base.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class FileProcessor
{
    static void uploadFile(Drive service, String fileName, String filePath, String parentDirectoryId ) throws IOException
    {
        // Determine file type
        String fileType = Files.probeContentType( Paths.get( filePath ) );

        // Retrieve local file and content
        java.io.File path = new java.io.File( filePath );
        FileContent mediaContent = new FileContent( fileType, path );

        // Create new file to upload
        File fileMetadata = new File();
        fileMetadata.setName( fileName );
        fileMetadata.setMimeType( fileType );
        if( !Strings.isNullOrEmpty( parentDirectoryId ) )
        {
            fileMetadata.setParents( Collections.singletonList( parentDirectoryId ) );
        }

        // Upload new file
        service.files().create( fileMetadata, mediaContent ).setFields( "id" ).execute();
    }

    static void uploadDirectory( Drive service, String directoryName, String directoryPath, String parentDirectoryId ) throws IOException
    {
        // Set google folder type
        String fileType = "application/vnd.google-apps.folder";

        // Create new directory to upload
        File fileMetadata = new File();
        fileMetadata.setName( directoryName );
        fileMetadata.setMimeType( fileType );
        if( !Strings.isNullOrEmpty( parentDirectoryId ) )
        {
            fileMetadata.setParents( Collections.singletonList( parentDirectoryId ) );
        }

        // Upload new directory
        File addedDirectory = service.files().create( fileMetadata ).setFields( "id" ).execute();

        // Determine if there are contents in the local directory
        java.io.File directory = new java.io.File( directoryPath );
        java.io.File[] filesInDirectory = directory.listFiles();

        // Upload any directories or files in the local directory
        if( filesInDirectory != null )
        {
            for( java.io.File file : filesInDirectory )
            {
                // listFiles method returns null if not a directory
                if( file.listFiles() == null )
                {
                    uploadFile( service, file.getName(), file.getPath(), addedDirectory.getId() );
                }
                // listFiles will return an array if directory, even if empty
                else
                {
                    uploadDirectory( service, file.getName(), file.getPath(), addedDirectory.getId() );
                }
            }
        }
    }
}
