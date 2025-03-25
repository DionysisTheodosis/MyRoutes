# Android GPX Tracker

This is an Android application developed as part of a university assignment for the course **Mobile Computing Applications Design and Development (321-9120)**. The app allows users to upload, store, and visualize hiking routes from GPX files on a map, as well as track their own paths in real-time.

## Features
- **Upload GPX Files**: Users can upload a GPX file either by selecting a file from their device or by providing a URL where the GPX file is hosted.
- **Parse GPX Data**: The app reads the uploaded GPX file and extracts route coordinates.
- **Store GPX Data**: Routes are saved  on the Firebase Firestore.
- **List Stored Routes**: A list of all stored hiking routes is displayed within the app.
- **Display Routes on Map**: When a user selects a stored route, it is displayed on a map with a distinct color.
- **Live User Tracking**: The app tracks and displays the user's current movement on the map in real-time using a different color.

## Technologies Used
- **Programming Language**: Kotlin  
- **Development Environment**: Android Studio  
- **Map Integration**: Google Maps API  
- **Data Storage**: Firebase Firestore  
- **XML Parsing**: To extract GPX file data  

## Deliverables
- The complete source code of the application.
- Screenshots showcasing the app’s functionality (stored in the `screenshots` folder).
- A `README.md` file (this document) with a brief description of the project.

---
This project was developed as part of an academic assignment and is not intended for commercial use.

