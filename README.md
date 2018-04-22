# Task Management Application
Feature lists:
------

* Rearrange tasks with drag and drop. Options :
  * https://clauderic.github.io/react-sortable-hoc/#/basic-configuration/horizontal?_k=iap35l
* Checkbox clicking should mark the task done and save its state as done.
* Make Create TODO input box richer.
  * Add an option for description.
  * Change urgency.
  * Add option for ETA
  * Add option to provide hours to complete the task.
  * Add option to mark work/personal
* Option to edit a todo.
* Urgency label in color with hover text - "urgent/not urgent" : this should be calculated by ETA, user should not be able to change this from UI.
* Importance label in color.
* Option for profile of a user.
  * User should be able to specify default ETA of a task if not specified.
* [IP]Webpack support for development with react.
 
Background Features
---
* Tasks should get sorted based on the Importance and Urgency weightage.
* User should get prompt that you are about to get overwhelmed by creating another task. This can be done by showing an overwhelming indicator.


# React, Webpack and Babel setup on mac.
https://scotch.io/tutorials/setup-a-react-environment-using-webpack-and-babel
 
1. install yarn ```brew install yarn```
2. (Assumig node is also installed, if not install it).
3. create and go to webapp folder ```cd src/main/webapp```
4. do ```yarn init```
5. ```yarn add webpack webpack-dev-server path```
6. create file webpack.config.js and add the content as its in this project.
7. ```yarn add babel-loader babel-core babel-preset-es2015 babel-preset-react --dev```
8. ```touch .babelrc```
9. Create client folder and add index.jsx and index.html
10. ```yarn add html-webpack-plugin```
11. 

 