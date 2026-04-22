# About the Application

WickedLista is an application that provides an organizational tool for managing various lists. Groups of related lists can be placed into categories or hold a single list. The inspiration for the app was how to keep track of Christmas gifts. I needed to know which gifts were bought, ordered, shipped or wrapped. This application, however, is currently not meant to be released to the public. I used the application to introduce myself to Kotlin, Jetpack Compose, Hilt and Room technologies.

## Technical Features Within WickedLista
- Kotlin
- Jetpack Compose UI implementation
- Hilt dependency injection
- Room database to create, find, update and delete categories of lists and their items
- Unit testing
- Instrumented testing

## Usage of WickedLista

On initial launch, the user will be prompted to create their first category. A category can be considered a container for 1 or many lists that are focused on a particular topic. The category must be created with a list.
<p align="left">
  <img src="https://github.com/user-attachments/assets/2bbc5824-d134-4a58-a7d7-8cb04215d87d" width="40%" />
  <img src="https://github.com/user-attachments/assets/567edbc6-a368-4064-ad44-2e9fc861f88f" width="40%"/>
</p>


Once a category is successfully created with the first list, the user can add list items to it.

<p align="left">
  <img src="https://github.com/user-attachments/assets/ed1c114d-9455-47a6-958c-91850f1b7a49" width="40%" />
  <img src="https://github.com/user-attachments/assets/52e8a3df-210d-498f-a792-68a6ce816dc4" width="40%"/>
</p>


The user can add items continuously. After the statuses have been configured, they become a drop down menu selection.

<p align="left">
  <img src="https://github.com/user-attachments/assets/01f8e98f-9140-4189-9e59-144dbc93326c" width="40%" />
  <img src="https://github.com/user-attachments/assets/44e5a3e5-d8c2-41bf-8fe8-99d1c03aacb4" width="40%"/>
</p>


When the user is finished adding their items, they can now see these items as a list. The user can add more lists pertaining to the given topic.

<p align="left">
  <img src="https://github.com/user-attachments/assets/a1a085fb-7edf-4c82-a495-cc548914b16a" width="40%" />
  <img src="https://github.com/user-attachments/assets/e036f1cb-cd62-4184-bb64-7998da1471d1" width="40%"/>
</p>

The vertical tabs allow the user to view their many lists based on the configured topic.

<p align="left">
  <img src="https://github.com/user-attachments/assets/39257a07-e09c-4503-a701-03e6241b3197" width="40%" />
</p>

## What's Next?
## 04/21/2026
- Single checkbox status that has one label and a checkbox in a row format
- Implement Material Design Theme for some colors and fonts
