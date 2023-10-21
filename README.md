# SMS Sender

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Latest Release](https://img.shields.io/badge/Release-v1.0.0-green.svg)](https://github.com/YourUsername/SMS-Sender/releases)

Send scheduled messages and automate responses using SMS Sender.

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Installation](#installation)

## Features

- **Scheduling Messages**: Schedule messages to be sent at a later time.
- **Auto-Reply**: Automatically reply to incoming messages.
- **Jetpack Compose**: Built with the modern Jetpack Compose framework for a beautiful user interface.
- **Clean Architecture**: Designed with a clean architecture approach for maintainability.
- **MVVM**: Utilizes the MVVM (Model-View-ViewModel) design pattern.
- **Room Database**: Stores data locally with Room Database.
- **Manual Dependency Injection**: Implements manual dependency injection for flexibility.
- **Work Manager**: Utilizes Work Manager for efficient background task handling.
- **Broadcast Receiver**: Handles incoming SMS messages using a Broadcast Receiver.

## Screenshots

| Home Screen                                                                             | Add Message Screen                                                                           | Reply Screen                                                                            |
|-----------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| ![one](https://raw.githubusercontent.com/Kyawkk/SMS_Sender/master/screenshoots/home_screen.png)   | ![two](https://raw.githubusercontent.com/Kyawkk/SMS_Sender/master/screenshoots/add_message_screen.png)   | ![three](https://raw.githubusercontent.com/Kyawkk/SMS_Sender/master/screenshoots/reply_screen.png)   |

| Add Reply Screen                                                                            | Update Reply Screen                                                                                | Reply Messages Screen                                                                         |
|-----------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| ![four](https://raw.githubusercontent.com/Kyawkk/SMS_Sender/master/screenshoots/add_reply_screen.png) | ![five](https://raw.githubusercontent.com/Kyawkk/SMS_Sender/master/screenshoots/update_reply_screen.png) | ![six](https://raw.githubusercontent.com/Kyawkk/SMS_Sender/master/screenshoots/reply_message_screen.png)

## Architecture

SMS Sender is designed with a clean and maintainable architecture that separates concerns and promotes flexibility for future development. The app's architecture follows industry best practices, with a clear distinction between various components.

### Clean Architecture

SMS Sender adheres to the Clean Architecture pattern, which divides the codebase into three main layers:

- **Presentation Layer (UI)**: This layer is responsible for the user interface and user interactions. It is built using Jetpack Compose, offering a modern and dynamic UI.

- **Domain Layer**: The domain layer contains the business logic and use cases. It defines the core functionality of the app and is independent of the Android framework.

- **Data Layer**: This layer handles data access and storage. It interacts with the local Room Database and external data sources, ensuring a separation of concerns.

### MVVM Design

The app employs the Model-View-ViewModel (MVVM) design pattern, which enhances the separation of responsibilities within the app:

- **Model**: Represents the data and business logic. In SMS Sender, this includes message scheduling, auto-reply configuration, and message management.

- **View**: Represents the UI components, which are built using Jetpack Compose. Views are responsible for rendering the UI and user interactions.

- **ViewModel**: Acts as an intermediary between the Model and View. It holds and manages UI-related data, responds to user actions, and communicates with the Domain and Data layers.

### Room Database

SMS Sender uses the Room Database to manage local data storage. This provides a robust and efficient way to store and retrieve messages, configurations, and other data used within the app. Room Database simplifies data access, making it easy to maintain and extend.

### Manual Dependency Injection

The app implements manual dependency injection, which offers flexibility and control over the dependencies used in the app. By manually configuring and managing dependencies, SMS Sender can adapt to evolving requirements and maintain testability.

### Background Processing with Work Manager

Work Manager is employed for handling background tasks, such as sending scheduled messages and managing auto-replies. Work Manager ensures that tasks are executed reliably and efficiently, even when the app is in the background.

### Incoming SMS Handling

To handle incoming SMS messages, SMS Sender utilizes a Broadcast Receiver. This component efficiently processes incoming messages, allowing the app to trigger auto-replies and other actions based on message content.

The clean architecture, MVVM design, and the utilization of key Android components make SMS Sender a robust and maintainable app that provides a superior user experience. The architecture ensures that each component has a clear purpose and can be tested in isolation, making the app flexible and easy to extend.

For a detailed overview of the code structure and component interactions, refer to the source code and documentation.


## Technologies Used

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Work Manager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Broadcast Receiver](https://developer.android.com/guide/components/broadcasts)

## Installation

Provide instructions on how to install and run your app locally. Include information on prerequisites and any specific configuration that's required.

```bash
# Clone the repository
git clone https://github.com/Kyawkk/SMS_Sender.git
