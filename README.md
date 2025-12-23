Repository for Kedai Runcit WIX1002 (Fundamentals of Programming) Semester 1 25/26 Project.

Meaning:
- git clone (To copy the GitHub repository code into your local IDE/machine.)
- git checkout (To switch between existing branches or to create and switch to a new branch.)
- git pull (To download (or "take") the latest changes from the remote repository branch (like main) and apply them to your local branch.)
- git add (To add whichever files which are ready to be saved (STEP 1)
- git commit -m "hello" (To actually save ur file progress with message "hello" (meaning u are ready to push to GitHub) (STEP 2)
- git push (To basically put your saved file code from your IDE to your branch on GitHub. (STEP 3 and done)


Be sure to have installed git on your device for using github, and use your command prompt for all of this

1. CREATE YOUR OWN BRANCH (DO THIS WHEN FIRST TIME DOING A CODE, IT IS TO CREATE A BRANCH (like a copy) THAT WONT AFFECT THE MAIN CODE IN CASE SOMETHING GOES WRONG):
- git clone https://github.com/aidelbtw/KedaiRuncit.git
- cd KedaiRuncit
- git checkout -b yourname-feature (example:aidel-storage)
- git push -u origin yourname-feature


2. WHEN CODING, DO THIS
(check if you have something like C:\Users\User\KedaiRuncit in your cmd, if dont have then do - cd KedaiRuncit)
- git checkout yourname-feature
- git pull origin main
- code .
- CAN START EDITING YOUR FILES
  

3. THIS IS FOR WHEN YOU FINISH AND WANT TO PUSH TO THE GITHUB
- git add filename.java (Example: Login.java) / git add . for all changed files
- git commit -m "Message of what u changed"
- git push origin yourname-feature
- go to github page
- click "compare & pull request"
- "create pull request", then let me know u made a request.
