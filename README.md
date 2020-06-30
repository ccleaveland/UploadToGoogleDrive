# UploadToGoogleDrive

Tool to upload directories and files to Google Drive of the specified user.

## Setup

### Google Account Configuration
- Go to the Google API Dashboard (https://console.developers.google.com).
- Create a new project with the name UploadToGoogleDrive.
- With project selected in the dropdown, enable the Google Drive API via the Library tab.
- On Credentials tab, configure an external consent screen.
- Create an OAuth client ID with an "Application type" of "Web Application" and "Authorized redirect URIs" of https://developers.google.com/oauthplayground.

### Retrieve Refresh Token
- Go to the OAuth Playground (https://developers.google.com/oauthplayground).
- In the settings, set "OAuth flow" to "Server-side", "Access type" to "Offline", and "Use your own OAuth credentials" to selected.
- Under Step 1, select Drive API v3 -> https://www.googleapis.com/auth/drive.file.
- Click "Authorize APIs" and allow access to your Google account when prompted.
- Under Step 2, click "Exchange authorization code for tokens" and retrieve the Refresh Token.

### File Changes
- Download the client_secret.json from the OAuth client ID created in the Google Account Configuration section
- Replace the contents of resources/credentials.json with the contents of client_secret.json.
- Replace the contents of resources/refresh_token.txt with the refresh token from the Retrieve Refresh Token section
- Build jar file.

Google Account Configuration steps only need to be performed once.  Retrieve Refresh Token and File Changes steps must be performed whenever the refresh token expires.
OAuth Playground currently advises that it revokes refresh tokens after 24 hours.

## Use

There are four command line flags associated with this tool: 
- -d      Upload a directory
- -f      Upload a file
- -n      Name of directory or file when uploaded
- -p      Path of directory or file to upload

Users must specify -n, -p, and either -d or -f.

For example, to upload a folder named "test" on a Mac Desktop to Google Drive as "newFolder" the user would use:
```
    java -jar uploadToGoogleDrive.jar -d -n newFolder -p /Users/[user]/Desktop/test
```

Notes:
- When uploading a folder, all contents, including contents of subfolders, will be uploaded to Google Drive.
- The uploadToGoogleDrive.jar file can be moved to a different computer and used to upload files from that computer to the specified Google Drive without reauthorizing as long as the refresh token remains valid.
