# Fetch Rewards Exercise

This project displays this list of items in the following way:
- Displays all the items grouped by "listId"
- Filters out any items where "name" is blank or null.
- Sorts the results first by "listId" then by "name" when displaying.

NOTE: "name" is sorted with a string-based comparison.

<div style="display: flex;">
  <img src="https://github.com/user-attachments/assets/616d23ee-d5c8-4783-ac59-0a855441a5d8" alt="app image 1" width="25%" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/06928fe4-17f3-4cd4-8363-16fd038980e9" alt="app image 2" width="25.28%">
</div>

## Features
- Click on each List ID to show or hide sorted item entries with that List ID.
- Displays grouped data in an expandable `RecyclerView`.
- Shows a loading indicator while data is fetched from the remote endpoint.
- Handles and displays error when the data fails to load due to network errors.

## Setup & Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/kevskillz/FetchRewardsExcercise.git
   ```

3. **Run the Project**
   Open the project in Android Studio and run it on an emulator or connected device.

## Technical Details 

### Key Components

#### `MainActivity.java`
`MainActivity` handles data fetching, error handling, and data processing. It uses a `RecyclerView` to display items in an expandable list format.

**Key Methods**:
- `fetchJsonData` – Fetches JSON data from https://fetch-hiring.s3.amazonaws.com/hiring.json
- ``processAndSortData`` – Processes, filters, and sorts JSON data, and passes it to `ExpandableAdapter` for display.
- `groupItemsByListId` – Groups data items by `List ID`.
  
#### `ExpandableAdapter.java`
`ExpandableAdapter` is a custom adapter for `RecyclerView` that displays data grouped by `List ID`. Each list can be expanded to show its items or collapsed to hide them.

**Key Methods & Classes**:
- `onBindViewHolder` – Sets up each group header and its items, and manages expand/collapse functionality.
- `GroupViewHolder` – Holds references to `TextView` and `ViewGroup` objects for displaying `List ID` and the expandable container.

#### `Item.java`
Class to set & get `ID`, `List ID`, and `name` for each data item.


### XML Layouts

#### `activity_main.xml`
The main layout containing:
- `LinearLayout` – Organizes the elements vertically.
- `TextView` – Displays the app title.
- `ProgressBar` – A loading indicator while fetching data.
- `RecyclerView` – Displays grouped data with expandable items.
- `TextView` – Shows an error message if data fails to load.

#### `group_list_id.xml`
The layout for each group header, displaying the `List ID` and the header for `ID` and `Name` fields when expanded:
- `LinearLayout` – Contains the group header and child items.
- `TextView` – Displays the `List ID` as the header for each group.
- `LinearLayout` – Header container shown only when the group is expanded.
- `TextView` – Header for `ID`.
- `TextView` – Header for `Name`.
- `LinearLayout` – Container for list of items.

#### `item_entry.xml`
The layout for each item in the expandable list, showing `ID` and `Name` in a row format:
- `LinearLayout` – Organizes `ID` and `Name` side-by-side for each item.
- `TextView` – Displays `ID` of each item.
- `TextView` – Displays `Name` of each item.
