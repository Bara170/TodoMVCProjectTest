TodoMVC Testing - Automation Challenge
——————————————————————
Authors/Testers: Barbora Srenkova and Faheem Kazi
Date: Thursday 22nd May 2025
——————————————————————
Project Summary
the pupose of this project was to test various elements and functions of the TODOMVC website - https://todomvc.com and the various JavaScript framworks. TodoMVC is useful for comparing syntax and solutions, is officially used in cross-browser benchmarks (e.g. Speedometer) and aims to stay up to date as trends change over time. 
This was a project that tested whether a "To-Do List" website (made with React) worked properly. We tested it in two ways:
	•	Automatically using code
	•	Manually by a human clicking and looking
We chose the testing method based on how important and how easy the test was.

What Was the Testing Strategy?
We used a balanced approach to test smarter:
	•	Automated the tests that were repeated, precise, or involved large amounts of data
	•	Manually tested features that were visual, temporary, or difficult to automate (like drag and drop)
We followed these guidelines:
	•	Covered the basic features first  - adding, deleting, editing tasks.
	•	Automated the core user paths - the actions most users were likely to take.
	•	Tested features that were likely to break or cause serious issues.
	•	Included boundary tests - such as empty input, emojis, symbols and special characters.
This strategy helped save time, avoid duplicated effort, and ensure we tested the right things in the right way.

Automated Test Cases (Selenium, Java, JUnit)
These tests ran automatically to check the website’s functionality:
	•	TC00: Opened the site and checked the title
	•	TC01: Added a new item
	•	TC02: Tried adding an empty item (expected failure)
	•	TC03: Added a single letter (expected failure)
	•	TC04: Added accented characters like é or ñ
	•	TC05: Added symbols like # or &
	•	TC06: Added emojis
	•	TC07: Deleted an item
	•	TC08: Edited an item
	•	TC10: Marked a task as done
	•	TC11: Unmarked a done task
	•	TC13–15: Checked the status bar (e.g. “1 item left”)
	•	TC17–19: Used the filter buttons (All, Active, Completed)
	•	TC20–21: Cleared completed tasks
	•	TC22: Toggled all items on/off

Manual Test Cases
These required human judgment and interaction:
	•	TC12: Reordered tasks by dragging (not reliable to automate)
	•	Visual checks: Colours, fonts, layout across browsers/devices
	•	Accessibility: Colour contrast, readable fonts, screen reader support
	•	User guidance: Clarity and usefulness of help content
	•	Performance: Speed and smoothness of loading and use

Tools and Tech Used
	•	Java – for writing test scripts
	•	Selenium WebDriver – to control the browser
	•	JUnit 5 – to run the test cases
	•	ChromeDriver – to run tests in Chrome
	•	Page Object Model (POM) – to organise reusable test code

Instructions to Run Automated Tests
To test this project:
	1	Clone the repo
	2	Open it in your Java IDE (e.g. IntelliJ)
	3	Make sure ChromeDriver is installed
	4	Use JUnit to run the test suite - in /tests folder

