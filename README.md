# About the Application

WickedLista is an application that provides an organizational tool for managing various lists. Groups of related lists can be placed into categories or hold a single list. The inspiration for the app was how to keep track of Christmas gifts. I needed to know which gifts were bought, ordered, shipped or wrapped. This application, however, is currently not meant to be released to the public. I used the application to introduce myself to Kotlin, Jetpack Compose, Hilt and Room technologies.

## Technical Features Within WickedLista
- Kotlin
- Jetpack Compose UI implementation
- Hilt dependency injection
- Room database to create, find, update and delete categories of lists and their items
- Utilizing Style API for Buttons and TextFields
- Unit testing
- Instrumented testing

## Usage of WickedLista

On initial launch, the user will be prompted to create their first category. A category can be considered a container for 1 or many lists that are focused on a particular topic. The category must be created with a list.
<p align="left">
  <img src="https://github.com/user-attachments/assets/71f1bbd6-7ce2-4bc6-85a4-421c81837aaa" width="40%" />
  <img src="https://github.com/user-attachments/assets/5b2ee2e3-c1c0-4bc0-afd8-8420bb0def97" width="40%" />
</p>


Once a category is successfully created with the first list, the user can add list items to it.

<p align="left">
  <img width="40%" src="https://github.com/user-attachments/assets/95dec916-a9d0-4417-9ac5-12da9669a45f" />
  <img width="40%" src="https://github.com/user-attachments/assets/8eb73d08-3a9c-411f-a913-409af205e42f" />
</p>


The user can add items continuously. After the statuses have been configured, they become a drop down menu selection.

<p align="left">
  <img width="40%" src="https://github.com/user-attachments/assets/a29dbdd1-c4eb-4e6b-9d12-d751bab29c83" />
  <img width="40%" src="https://github.com/user-attachments/assets/77c1a0c2-2415-4deb-b41e-bb8eb3f2b55f" />
</p>

git 
When the user is finished adding their items, they can now see these items as a list. The user can add more lists pertaining to the given topic.

<p align="left">
  <img width="40%" src="https://github.com/user-attachments/assets/2e8db731-04e6-41c3-84fd-5ac347d9b1a7" />
  <img width="40%" src="https://github.com/user-attachments/assets/65c26597-1efc-4e40-997b-8b9adac79ea5" />
</p>

The vertical tabs allow the user to view their many lists based on the configured topic.

<p align="left">
  <img width="40%" src="https://github.com/user-attachments/assets/2f329e98-3bff-4898-b1f8-9f5ca3b371ce" />
</p>

## Checkbox Style List Items
For a simplier list that only requires whether or not an event has occurred on an item, use a status as a checkbox.
<p align="left"> 
  <img width="40%" src="https://github.com/user-attachments/assets/bc6e6a45-e1b7-427e-93e1-49eda624867b" />
  <img width="40%" src="https://github.com/user-attachments/assets/f59c5d3b-cc77-4c87-81a4-185cef01b9d3" />
</p>

After configured with a label, it is ready for use in your list items.
<p align="left"> 
  <img width="40%" src="https://github.com/user-attachments/assets/69ad13fe-2136-438d-88e6-52a56b5ea297" />
  <img width="40%" src="https://github.com/user-attachments/assets/11d6bbb1-e535-4618-a83c-8dac6a15561c" />

</p>

## What's Next?
- ~~Single checkbox status that has one label and a checkbox in a row format~~ - Implemented, Unit Tested and Instrumented Tested as of 05/09
- ~~Implement Material Design Theme for some colors and fonts~~
- Implement new Style API from Android
