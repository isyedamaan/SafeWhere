# SafeWhere Android Studio Project

## Table of Contents
1. [Introduction](#introduction)
2. [Functional Requirements](#functional-requirements)
3. [Non-Functional Requirements](#non-functional-requirements)
4. [Modules Overview](#modules-overview)
5. [Testing](#testing)
6. [Conclusion and Future Work](#conclusion-and-future-work)

---

## Introduction

**Academic Session 2023/2024**  
**Semester 1**  
**Lecturer:** Dr. Ong  
**Company Name:** SafeWhere  
**Course:** WIA2007 Mobile Application Development - Team OSY11  
**Assigned SDG and Selected Targets:** SDG 16: Peace, Justice, and Strong Institutions  

**Developer:**[Amaan Geelani Sed](www.linkedin.com/in/amaangsyed)
**UI Design:**[Alyssa Atmasava](https://www.linkedin.com/in/alyssa-atmasava-87a875273/)

The SafeWhere project aims to address the challenges outlined in Sustainable Development Goal 16 (SDG 16), focusing on creating a safer environment for all. This Android application enables users to report incidents in real-time, access safety tips and legal information, and receive emergency assistance when needed. By leveraging technology, we strive to contribute to building peaceful, just, and secure societies.

---

## Functional Requirements

The functional requirements of SafeWhere are aligned with SDG 16 objectives and include:

- Safety notification and notification history for users
- Mapping of danger hotspots, geofencing and route planning
- SOS system for emergency assistance
- Access to legal aid, crime reports, and safety tips
- Incident reporting functionality

---

## Non-Functional Requirements

SafeWhere adheres to stringent non-functional requirements to ensure:

- **Security**: Utilization of Firebase for encrypted data storage and role-based access control.
- **Portability**: Compatibility with various Android devices and screen sizes, ensuring a consistent user experience.
  
---

## Modules Overview

### 1. Notification Module
- **Safety Alerts:** Sends timely notifications about safety tips, incident reports near the user's location, and updates on hotspots.
- **Custom Notifications:** Users can customize notification settings, choosing the types of alerts they wish to receive and their preferred notification sounds.

### 2. Startup Module
- **SignUp:** Allows new users to register with their email, creating a secure account with Firebase Authentication.
- **Login:** Facilitates user login with email and password verification, including error handling for unmatched records.
- **Profile Management:** Enables users to view and edit their personal information, ensuring their details are current and accurate.
- **Reset Password:** Offers a secure process for users to reset forgotten passwords, sending a reset link to their registered email.

### 3. Map Module
- **Destination Selection:** Users can search for and select destinations, with autocomplete suggestions for ease of use.
- **Route Navigation:** Provides turn-by-turn navigation to selected destinations, integrating with Google Maps API for real-time directions.
- **Hotspot Visualization:** Displays areas with high incident reports as hotspots on the map, helping users avoid potentially unsafe areas.
- **Geofencing:** Allows users to set up geofenced areas for automatic alerts when entering or leaving certain zones, enhancing personal safety measures.

### 4. SOS Module
- **Immediate Alert System:** An SOS button sends an immediate alert to emergency contacts and services, including the user's current location.
- **Panic Mode:** A discrete feature allowing users in distress to send silent alerts by pressing the power button a specified number of times.
- **Emergency Contact Communication:** Users can pre-configure emergency contacts within the app for quick access during emergencies.

### 5. Report Module
- **Incident Reporting:** Users can report incidents by filling out a detailed form, including the incident type, location, and optional photos or videos.
- **Report Management:** Allows users to view, edit, or delete their previous reports, fostering an environment of accountability and accuracy.
- **Community Engagement:** Reports are anonymized and shared with the community, alerting others to recent incidents and fostering a collective sense of security.

### 6. Information Module
- **Legal Information:** Provides users with access to legal resources and guidelines relevant to their safety and rights.
- **Crime Statistics:** Features up-to-date statistics on crime rates and safety indexes, helping users make informed decisions about their safety.
- **FAQs:** Addresses common questions and concerns, offering quick access to important information about the app and safety practices.

### 7. Data Classes and Helper Classes
- **Data Classes:** Define the structure of objects for users, reports, and incidents, ensuring a well-organized and scalable data model.
- **Helper Classes:** Include utility functions for common tasks such as form validation, encryption for data security, and Firebase operations to streamline database interactions.

---


## Testing

### Functional Features Testing
- Rigorous testing of each module to ensure accuracy, timeliness, and reliability of functionalities.
- Manual and automated testing performed for notification delivery, map accuracy, SOS button responsiveness, and information module accessibility.

### Non-Functional Requirement Testing
- Security testing to verify encryption, authentication, and access control measures.
- Portability testing on various devices to ensure compatibility and consistent user experience.

---

## Conclusion and Future Work

### Achievements
- Successful development of a comprehensive safety application aligned with SDG 16 objectives.
- Implementation of essential features including real-time mapping, incident reporting, and emergency assistance.

### Lessons Learned
- Insights gained into effective project management, user-centered design, and Android development intricacies.

### Future Directions
- Expand platform coverage to iOS and web platforms.
- Enhance features with live updates, AI-driven insights, and community engagement initiatives.

---

This documentation encapsulates the SafeWhere Android Studio project's objectives, architecture, features, and testing methodologies. I welcome feedback and contributions to make SafeWhere even better. 

Thank you for your interest and support in making our communities safer.

Regards,
Amaan.
