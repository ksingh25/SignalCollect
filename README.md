# SignalCollect
SignalCollect: An android application to collect LTE signals

This tool was developed during an internship project by Yessine Ben Kheder and Roukaya Ben Salah. The project was supervised by me and Illyyne Saffar. This tool is an android application that collects LTE signals such as RSRP, RSRQ, CQI, etc.

The users can choose an activity from a drop down menu in order to label the data that is being collected at that instant. For example if the user is walking then the collected signals will be tagged as walking.

This data can be used to learn indoor/outdoor classification or user mobility classification based on mobile signals. The tool works, but some todo items are remaining such as solving the issue of no data collection when mobile is in sleep mode, etc.

TODOs
- Clean the code
- Add a write function that saves the data in a file on mobile phone
- There is a problem that when then phone is in sleep mode then it doesnt collect data
