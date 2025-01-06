# Cloud Text Editor  

A modern text editor that allows multiple users to privately access and work on their text documents on the same device.  

This is achieved through a **login system** – each user logs into their account to view, create, and edit their files.  

## Key Features  
- **Registration with OTP Verification**  
  EA registered email has to be verified through an OTP sent to that email using the SMTP service.
  
- **User-Specific File Access**  
  Each user has a private workspace accessible only through their login credentials. 

- **Familiar Layout**  
  Combines the simplicity and design of **Notepad** or **Notepad++** with modern enhancements.  

- **Simulated Cloud Environment**  
  Provides the feel of a cloud-based editor like **Google Docs** without using an actual cloud service.  

## Simulated Cloud Functionality  
Although called a "Cloud" Text Editor, the application does **not use cloud services** such as Google Cloud Platform or AWS. Instead, it simulates the behavior of a cloud-based system:  

1. **Local Storage with Cloud-Like Organization**  
   - Files are stored locally, but the folder structure is customized for each user.  
   - For example, a user with the email `test@example.com` will have their files stored in:  
     `C:\...\testexample`  

2. **Seamless User Interface**  
   - The frontend hides any indication of local storage.  
   - Custom windows are presented for actions like saving, renaming, or opening files, maintaining the illusion of a cloud environment.  

## Why Choose Cloud Text Editor?  
The Cloud Text Editor bridges the gap between traditional text editors and cloud-based systems by:  
- Offering a simple and familiar layout.  
- Simulating a cloud experience with local resources.  
- Enabling efficient, private access for multiple users on the same device.  

### A Glimpse Into the Future  
While this project currently simulates a cloud-based system, it lays the groundwork for integration with actual cloud services in the future, making it scalable and truly cloud-native.  
