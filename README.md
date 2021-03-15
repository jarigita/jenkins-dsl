# jenkins-dsl
proof of concept for jenkins dsl

This project is about to develop jenkins job as code, create docker image ad test that everything is fine before going into production.


# How does it work?

Due to automation, not repeating work, making it easier to read a new project, it is convenient that projects adopt a common structure. We have used the same base structure of the Netflix projects.
https://github.com/Netflix/gradle-template

Use gradle, this will maintain each project's isolation and keep the build as transparent as possible to the outside world.
While at the same time we want to be able to push out build updates in a predictable fashion.

The advantages we have are: Jenkins friendly version of gradlew, Build source jar and javadoc jar, Cobertura for code coverage, moving generic code into individual files in the gradle directory.

So in this project:

1. The initial jobs that we want jenkins to have are created in the jenkins-home/dsl folder with a groovy script, and the corresponding script that is launched with the job in the jenkins-home/jobs folder ( Example: Docker_Build_Mongo.script).
2. The seed job creates the initial jobs, distributes them in views, pipelines.
3. The seed job runs the DSL job `managedJobs.groovy` which generates the job `generate-org-jobs`, 
4. When running the job `generate-org-jobs` for a github organization, it searches the github organization for gradle files and generate Jenkins jobs for each repo found.
5. Create project image, run that image
   docker build -t=jenkins-dsl .   
   docker run -p8080:8080 jenkins-dsl 
   check it http://localhost:8080


# DevOps process flow 

devop philosophy in this project:

1. Automation: Automate everything, The tests and their reports are created automatically, if they do not pass, you cannot continue. The manuel step is to merge PR in github.
2. Iteration: Jenkins and github complement each other to show where we are and how we are going.
3. Continuous improvement: Continuously test, tests are in the same repository and the tests are passed every day. 
4. Collaboration: Everyone sees the same repository and everyone sees project progress, and if something goes wrong it's everyone's fault.

![devops_teams_test](https://user-images.githubusercontent.com/3600766/101514647-cabcd400-397d-11eb-9824-87a6104d4457.png)

We have four environments, each with its branches, its different tests and responsibilities.
The code flows between the different branches of github, and when a PR is created it is tested in the corresponding environment, and only if it passes all the tests and is successful, the right person on the team approves the PR, moving to the next level.

![devops_environments](https://user-images.githubusercontent.com/3600766/101514497-9b0dcc00-397d-11eb-8218-7216a2ee5af3.png)

**Developer**: they solve US or bugs modifying code with commits, then PRs are created on github. Due to the github hook, the associated jobs are executed, showing in github if they pass the tests, sonar, ... another developer seeing the results of the jobs, and checking the code merges the branch.

    1. branch: featureX bugX
    2. jobs: 
        * compile
        * unit test
        * code coverage
        * sonar
 
**QA**: every night the develop branch is deployed to the QA environment. All the integration tests are passed and reports of coverage of functionality is created.
       We verify that nothing has been broken. QA environmentis a real environment, although it doesn't need to look like production.

    1. branch: develop
    2. jobs: 
        * smoke test
        * sanity test
        * accept test
        
    When the QA team has created enough tests, develop branch is merged to the release/X.


**Security**: stage is an environment similar to the production one, where it is going to validate how the application reacts to unforeseen, adverse situations.

    1. branch: release/X
    2. jobs:
        * security test
        * performance test
        * HA test

    Only if all the jobs have passed is the release accepted and it is available for deployment. This operation is planned because they are very expensive tests and equipment that is not usually dedicated to a single project.

**Operations**: production environment with data and users. Measure actual use and user satisfaction and problems
    
    1. branch: master
    2. jobs: monitor traces
    monitor status of servers (cpu, disk, ...)
    monitor user operations
    Only here is where you see if all the work done has been useful, suggestions are created to improve development, ...

This project is just an example of using jenkins and dsl, it is not for real use, there are better options and more specific tools for each part of the development cycle, what we want to highlight is the idea of treating each part as a job , which returns some measurements (creates a statistic) and validates the development or stops it until the problem is solved.
The other idea is that the creation of the jobs of these projects is automatic, that everything is in the repo of the project, together with the projects they have the same structure and are
     
# Requirements

- Docker Version 1.10 or higher.

# Resources

- [Official Jenkins Docker image](https://github.com/jenkinsci/docker)
- [Job DSL Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin)
- [Jenkins Jobs as Code with Groovy DSL](https://tech.gogoair.com/jenkins-jobs-as-code-with-groovy-dsl-c8143837593a)
- [Ticketfly Tech: Automate your configuration with Jenkins DSL](https://tech.ticketfly.com/our-journey-to-continuous-delivery-chapter-3-automate-your-configuration-with-jenkins-dsl-1ff14d7de4c4)


