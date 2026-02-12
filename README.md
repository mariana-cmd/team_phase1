# CSE360 Team Phase 1 - Foundations Application

## Overview
This is Phase 1 of the CSE360 Project, featuring a **JavaFX-based user management application** that demonstrates:
- **Singleton Design Pattern** for GUI components
- **Model-View-Controller (MVC)** architecture
- **In-memory H2 database** integration
- **Role-based access control** with admin and user management capabilities

## Project Description
The **Foundations Application** is a demonstration JavaFX application built for CSE360 coursework. It serves as a foundation for implementing secure user authentication, role management, and account administration with proper design patterns and comprehensive documentation.

## Key Features

### Authentication & User Management
- **First-time Admin Setup**: Initial application launch requires admin account creation (no hard-coded credentials)
- **User Login System**: Secure username/password authentication
- **Invitation Code System**: New users can create accounts using invitation codes
- **One-Time Password Support**: Admin can set temporary passwords for users

### Role-Based Access Control
- **Multi-role Support**: Users can have multiple roles assigned
- **Admin Dashboard**: Comprehensive admin interface for user management
- **Add/Remove Roles**: Dynamic role assignment and removal for users
- **Role-specific Home Pages**: Different interfaces based on user roles

### Database Management
- **In-memory H2 Database**: Fast, lightweight database for user data storage
- **User Entity Management**: Complete CRUD operations for user records
- **Database Connection Handling**: Proper connection management with error handling

### Input Validation
- **Email Recognition**: Finite State Machine (FSM)-based email validation
- **Name Recognition**: FSM-based name validation
- **Password Validation**: Secure password requirements and matching verification

## Technology Stack
- **Java**: Core programming language
- **JavaFX**: GUI framework (requires `javafx.controls` and `javafx.graphics`)
- **H2 Database**: In-memory SQL database (`java.sql`)
- **Eclipse IDE**: Development environment

## Project Structure

```
team_phase1/
│
├── TEAMPHASE1CODE/
│   ├── src/
│   │   ├── applicationMain/          # Main application entry point
│   │   │   └── FoundationsMain.java  # JavaFX application launcher
│   │   │
│   │   ├── database/                 # Database layer
│   │   │   └── Database.java         # H2 database wrapper and utilities
│   │   │
│   │   ├── entityClasses/            # Data models
│   │   │   └── User.java             # User entity class
│   │   │
│   │   ├── guiFirstAdmin/            # First-time admin setup (MVC)
│   │   │   ├── ViewFirstAdmin.java
│   │   │   └── ControllerFirstAdmin.java
│   │   │
│   │   ├── guiUserLogin/             # User login page (MVC)
│   │   │   ├── ViewUserLogin.java
│   │   │   ├── ControllerUserLogin.java
│   │   │   └── Model.java
│   │   │
│   │   ├── guiNewAccount/            # New account creation (MVC)
│   │   │   ├── ViewNewAccount.java
│   │   │   └── ControllerNewAccount.java
│   │   │
│   │   ├── guiAdminHome/             # Admin dashboard (MVC)
│   │   │   ├── ViewAdminHome.java
│   │   │   └── ControllerAdminHome.java
│   │   │
│   │   ├── guiAddRemoveRoles/        # Role management (MVC)
│   │   │   ├── ViewAddRemoveRoles.java
│   │   │   └── ControllerAddRemoveRoles.java
│   │   │
│   │   ├── emailRecognizer/          # Email validation FSM
│   │   │   └── EmailRecognizer.java
│   │   │
│   │   ├── NameRecognizer/           # Name validation FSM
│   │   │   └── NameRecognizer.java
│   │   │
│   │   └── module-info.java          # Java module descriptor
│   │
│   ├── bin/                          # Compiled classes
│   ├── .classpath                    # Eclipse classpath configuration
│   ├── .project                      # Eclipse project configuration
│   └── build.fxbuild                 # JavaFX build configuration
│
└── README.md                         # This file
```

## Design Patterns

### Singleton Pattern
Each GUI page (View) is instantiated only once, controlled by static factory methods that ensure proper configuration and memory efficiency.

### MVC (Model-View-Controller)
- **Model**: Data representation and business logic
- **View**: JavaFX UI components and layout (singleton instances)
- **Controller**: Static methods handling user interactions and coordinating between Model and View

## Application Flow

1. **Startup**:
   - Connects to in-memory H2 database
   - Checks if database is empty
   
2. **First Launch** (Empty Database):
   - Displays First Admin Setup page
   - Admin creates username and password
   - System initializes with admin account
   
3. **Subsequent Launches**:
   - Displays User Login page
   - Options: Login with credentials OR create account with invitation code
   - Redirects to appropriate home page based on user role

4. **Admin Features**:
   - List all users
   - Add/remove user roles
   - Set one-time passwords
   - Delete users
   - Generate invitation codes

## GUI Layout
Each page follows a consistent 800x600 window size with multiple functional areas:
- **Area 1**: Page title and user information
- **Area 2-4**: Feature-specific controls and inputs
- **Area 5**: Navigation buttons (Return/Logout/Quit)

## Installation & Setup

### Prerequisites
- Java 17 or higher
- JavaFX SDK
- Eclipse IDE (recommended)

### Running the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/mariana-cmd/team_phase1.git
   ```
2. Open the project in Eclipse
3. Ensure JavaFX libraries are configured in the build path
4. Run `FoundationsMain.java` as a Java Application

## Development Notes

### Database Connection
- Uses H2 in-memory database (no persistent storage)
- Connection string managed internally by `Database.java`
- Automatic connection on startup with error handling for concurrent access

### Validation
- Email addresses validated using FSM with state transitions
- Names validated with whitespace and character rules
- Passwords require matching confirmation entries

### Security Features
- No hard-coded credentials
- First-time admin setup required
- Invitation code system for new user registration
- One-time password capability

## Authors
- **Original Framework**: Lynn Robert Carter © 2025
- **Team Project**: CSE360 Student Team

## Version History
- **v3.02** (2025-12-17): Spring 2026 enhancements
- **v3.00** (2025-08-17): Initial rewrite for Fall CSE360
- **v2.00** (2022-01-06): Email recognizer updates
- **v1.00**: Initial baseline

## Course Information
- **Course**: CSE360 - Introduction to Software Engineering
- **Institution**: Arizona State University (ASU)
- **Semester**: Spring 2026

## License
Copyright © 2025 Lynn Robert Carter. All rights reserved.
Educational use for ASU CSE360 coursework.

## Documentation
The codebase includes extensive JavaDoc documentation and inline comments explaining:
- **What**: Description of functionality
- **Why**: Rationale for design decisions
- **How**: Implementation details for complex logic

Refer to individual class files for detailed method-level documentation.
