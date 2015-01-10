MrCashManager
=========
The recognised world leading budget application paradigm


Changelog
=========

v3.4 (Mnemosyne)
- Categories can now be sorted

v3.3.1
- Hotfix. The currencies table was present for some old versions of MrCashManager which would crash the app upon db upgrade

v3.3 (Odysseus)
- Events. Log transactions to one or more events
- Improved currency functionality. Currencies can now be saved in the database
- When adding a transaction with a comment, another currency can now be used for that transaction
- Fixed bug where longpressing the last price in the price list would not work
- Fixed bug where longpressing a price to be able to add comment didn't work as expected

v3.2 (Vishwakarma)
- Added option to pause payment of installments
- Fixed bug that caused unnecessary processing of installments, speeding up app start
- Fixed bug where graph would jump around sometimes when zooming
- Implemented queue to transactions so they can be run in a ASyncTask
- StatsView now sets up the Composite in an ASyncTask

v3.1.1
- Fixed bug where the month spinner would not get updated when changing year in the statsview
- Fixed bug where a long category name would mess up the FavButtz
- Changed scaling of graph so that a fully saved daily budget will give a 45 degree rise
- Added the weekday to the transaction lists in the statsview
- Added the functionality to easier be able to send feedback and rate the app

v3.1
- Cost suggestion when clicking favourite button and no price present in the input field
- Fixed bug where long category names would screw up lists
- The color of the current budget now changes during the day
- The 'choose category' spinner now blinks when clicking on it
- Clearing autocomplete values now possible

v3.0
- Installments. Pay of big transactions over a period of time
- Auto-complete. The most used transaction amounts are remembered
- Graph scales depending on daily budget

v2.4
- Possibility to change currency symbol and exchange rate
- Better looking layout
- Adding transactions with 'Other...' will add the category to the category list

v2.3
- Adding comments to entries possible
- Editing and removing of old transactions possible
- Money now uses doubles instead of integers
- Weighted coloring of derivative
- Devices with Android API < 11 now uses the dark theme

v2.2
- Up to three favorite buttons for easy adding of transactions
- Improved statistics activity
- Support for multiple undo operations

v2.1
- Temporary category
- New layout of main screen
- The color of the current budget now depends on the mean derivative for the last 7 days
- Statistics activity showing all transactions made

v2.0
- Categories.
- Automating adding of daily budget.

v1.0
- Keeps track of current budget
