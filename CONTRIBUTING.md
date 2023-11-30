Contributing to Eclipse SDV Developer Console
======================

Thanks for your interest in this project.

Project description:
--------------------
The Eclipse- SDV Developer Console, part of T-System's  Hybercube portfolio, addresses challenges faced by OEMs in software development, testing, and simulation in the automotive industry.

Hypercube aims to accelerate time-to-market for new features by automating and standardizing software components. The Developer Console helps solve business challenges related to decreasing logistical costs, managing changing demands, and improving time, cost, and quality of tests and simulations. It provides a solution for managing simulations of software-defined vehicle components throughout the software lifecycle, including creating simulation objects, simulation setup, and execution.
eTrice The ultimate goal is to encourage the use of Hybercube and open source solutions to transform vehicles into software-defined vehicles and create new use cases and integrations for a connected world.

Simulation is an essential tool in OTA updates for vehicles because it allows OEM to test and validate software updates in a virtual environment before deploying them to vehicles in the field. By simulating different vehicle components and systems, OEM's can identify potential issues or conflicts that may arise during the OTA update process and make modifications to ensure that the update is delivered seamlessly to the vehicle.
Some of Example of  Simulations like Functional Simulation ,Performance Simulations, Security Simulations, Compatibility Simulations.

To Create Simulation, prerequisite it to Create/use existing  1) Track/s  and  2) Scenario.

A Simulation includes : 

one or many scenario files (simulation object) in a specific order one track (simulation setup)
Thus, the simulation include the reference to the track and scenario, and request.



Developer resources:
--------------------

Information regarding source code management, builds, coding standards, and more about SDV- Developer Console. 

- https://gitlab.eclipse.org/eclipse/dco

  The SDV DCO code is stored in a git repository.

https://gitlab.eclipse.org/eclipse/dco
You can contribute bugfixes and new features by sending pull requests through GitHub.

Legal:
------------------------------
In order for your contribution to be accepted, it must comply with the Eclipse Foundation IP policy.

Please read the Eclipse Foundation policy on accepting contributions via Git.

1. Sign the Eclipse ECA
   1. Register for an Eclipse Foundation User ID. You can register here.
   2. Log into the Accounts Portal, and click on the 'Eclipse Contributor Agreement' link.
2. Go to your account settings and add your GitHub username to your account.
3. Make sure that you sign-off your Git commits in the following format: Signed-off-by: John Smith <johnsmith@nowhere.com> This is usually at the bottom of the commit message. You can automate this by adding the '-s' flag when you make the commits. e.g. git commit -s -m "Adding a cool feature"
4. Ensure that the email address that you make your commits with is the same one you used to sign up to the Eclipse Foundation website with.

Before your contribution can be accepted by the project, you need to create and electronically sign the Eclipse Foundation Contributor License Agreement (CLA).

- http://www.eclipse.org/legal/ECA.php

Contributing a change
------------------------------
Please refer below steps to contribute SDV DCO
1. Clone the repository onto your computer: git clone https://gitlab.eclipse.org/eclipse/dco/developer-console.git
2. Change the working directory to developer-console
3. Create a new branch from the latest master branch. 
4. Please use below prefixes while creating a new branch with git checkout -b {prefix}/{your-branch-name}
  a) If you are adding a new feature, then use git checkout -b feat/{your-branch-name}
  b) If you are fixing some bug, then use git checkout -b fix/your-branch-name
  c) If you are doing configuration/ci related changes, then use git checkout -b ci/{your-branch-name}
  d) If you are doing documentation related changes, then use git checkout -b docs/{your-branch-name}
1. Ensure that all new and existing tests pass.
2. Commit the changes into the branch using below commands: 
   a) git add .
   b) git commit -m "meaningful-commit--message"
   c) git push
3. Once you push the changes to remote feature branch, raise a merge request on Gitlab to merge the code to main branch
4. Make sure that your commit message is meaningful and describes your changes correctly.
5. If you have a lot of commits for the change, squash them into a single commit.
6. Add "Santosh Kanase" user as a reviewer while raising a merge request. 

##### What happens next?
Depends on the changes done by the contributor. If it is 100% authored by the contributor and meets the needs of the project, then it become eligile for the inclusion into the main repository. If you need more specific information/details, please consult with DCO owner through mailing-list. You can refer contact section.

Contact:
--------

Contact the project developers via the project's "dev" list.

- https://accounts.eclipse.org/mailing-list/dco-dev 

Search for bugs:
----------------

This project uses Bugzilla to track ongoing development and issues.

- https://bugs.eclipse.org/bugs/buglist.cgi?product=DCO

Create a new bug:
-----------------

Be sure to search for existing bugs before you create another one. Remember that contributions are always welcome!

- https://bugs.eclipse.org/bugs/enter_bug.cgi?product=DCO
