# Cloud-Text-Editor
- The Cloud Text Editor is essentially a normal text editor wherein multiple users can use the same application on the same device in order to privately access and work on their text documents.
- This is achieved through a login system – so a user logs in to their account in order to view and modify their text files.
- If we’re comparing it with other text editors, we can say that the Cloud Text Editor that we’ve developed is a combination of
   
        - the layout and features of Notepad or Notepad++
        - the cloud-based environment of Google Docs, fusing simplicity with effective storage.

- One thing to note, however, is that we’re not actually using a cloud service.
- We’re not using Google Cloud Platform or Amazon Web Services to make this possible.
- Instead, what we have done is simulate the working of a cloud service.
- The Cloud Text Editor actually does store files locally, but the actual end user does not know that.
- What happens is that a folder is created for the user based on their email – for example, if their email is test@example.com, then their folder name will be C:\...\testexample, and their files will be stored and retrieved from this specific folder.
- The user interface on the frontend does not showcase any of this.
- If a user wants to save or rename or open a file, our custom windows are what appear instead of anything that might indicate we’re using local storage.
- So, this Cloud Text Editor that we have developed is simulating a potential Cloud Text Editor that we could very well build with the necessary resources from an actual cloud service.
