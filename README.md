# Pre-work - *Taskly*

**Taskly** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Sai Pranesh Mukkala**

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Floating action button to add the items to the list
* [x] Snackbars to show user friendly messages based on adding, editing and deleting of items from the list
* [x] Sorted the list on priority basis
* [x] Added task status field, and also added due time (timepicker)
* [x] Added enter/exit transitions for the Dialogfragment
* [x] Swipe to delete functionality and ripple effect on the layout when pressed

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/k9h3t8a.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** I find it really amazing, it is such a powerful platform that can reach out to a huge consumer base. The way the android works using all the code and resource files is similar to what is used in Web development. CSS styles are seperated from the code, and we write controller code in a different file and modify the UI from the controller itself, it is the same way with Android. Although android provides more fine grain control over the UI, but it still has its limitations, the developer needs to be very careful with the memory and null pointer exceptions which could crash the app abruptly. I felt that Android is more user interface involved.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** Adapter acts as a helper class that a ListView uses to put the items on the screen and in appropriate positions. the listview first asks for how many items are there to display and then puts them on the screen based on individual item position in the list of items. Adapter is very important which helps us to customize to draw the items on the screen. 'convertView' is like a viewHolder which helps to recycle the views after enough views have been populated to fill the screen. It improves performance and reduces the overhead of calling findViewById which is a costly operation.

## Notes

Describe any challenges encountered while building the app.

The only challenge was to add the swipe to remove feature while developing the app and also to make sure that the app is easily expandable.

## License

    Copyright [2017] [Sai Pranesh Mukkala]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
