## Using Github

(Since some of you haven't used it before)

### Installation 
There are two ways to install and use git on Windows, through a command line or though a GUI.

Command line: https://git-for-windows.github.io/
GUI: https://desktop.github.com/

I personally prefer the command line.

Mac: 
http://burnedpixel.com/blog/setting-up-git-and-github-on-your-mac/

There's also other options like SourceTree:
https://www.sourcetreeapp.com/

### Getting Started

If you use some kind of GUI this wont apply, and you'll have to learn how to use Git with whatever application you chose.

You'll need to create a folder where you're going to clone this repository into. Then with command promp/terminal open and navigated to that folder use:
`git clone https://github.com/MichaelCDormann/SQLToXMLConverter.git` 

Now you can get to work writing code. First, make sure you're working on the latest version with everyone's changes:
 `git pull`
 
Then to add your changes to the repository use:
`git add *`
This adds all your changes to staging to be committed. 

Next use:
`git commit -m "<comment>"`
Self explanitory - commits your changes

Finally, this pushes all of your commits to the repository:
`git push`

Git will automatically notify you and help resolve the issue if there are any merge conflicts with something someone else has done.

Another usefull command, to see the current status of your working folder.:
`git status` 

A good guide:
http://readwrite.com/2013/09/30/understanding-github-a-journey-for-beginners-part-1/
