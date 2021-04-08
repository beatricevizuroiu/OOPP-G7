# RaisedHand
Client:  
![Client coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/team-repositories/oopp-group-07/repository-template/badges/master/coverage.svg?job=client-test)  
Server:  
![Server coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/team-repositories/oopp-group-07/repository-template/badges/master/coverage.svg?job=server-test)  


## Description of project
RaisedHand is tool for lecturers to recieve questions from their students during (online) lecturers. Next to that we also offer some extra features that allow the lecturer to get some more organized feedback from his/her students.

### Features
- Recieve questions from your students.
- Send polls to your students.
- Be informed by your students whether you are going to fast, to slow, or just at the right speed through the material.
- Ban students from sending questions, answering polls, and informing you about your speed by banning them.
- Delete questions from students.
- Edit questions.
- Assign trusted users permission to moderate the questions to help keep the room clean.


## Group members

| 📸 | Name | Email |
|---|---|---|
| ![](https://kallestruik.nl/images/self/128x128.png) | Kalle Struik | K.H.Struik@student.tudelft.nl |
| ![](https://cdn.discordapp.com/attachments/479231854193672212/813004391971291146/pf_128x128.jpg) | Cody Boon | C.M.Boon-1@student.tudelft.nl |
| ![](https://media.discordapp.net/attachments/813111618551939074/813112292198842448/Eu_res.png) | Beatrice-Andreea Vizuroiu | B.Vizuroiu@student.tudelft.nl |
| ![](https://cdn.discordapp.com/attachments/402133604379000838/813470529418821702/IMG_3489_1.JPG) | Elena Martellucci | E.Martellucci@student.tudelft.nl |
| ![](https://cdn.discordapp.com/attachments/814137071756247053/814138914611658772/photo.jpg) | Mehmet Alp Sozuduz | M.A.Sozuduz@student.tudelft.nl |


## How to run it
In this section you will find instructions on how to run the client and server parts of this project.

### Client
Follow the below instructions to run the client program.

#### Linux & Mac OS
```sh
# Clone the git repo into the OOPP-G7 folder.
git clone git@gitlab.ewi.tudelft.nl:cse1105/2020-2021/team-repositories/oopp-group-07/repository-template.git OOPP-G7

# Go into the folder we just cloned.
cd OOPP-G7/

# Compile a binary from the source code.
./gradlew :client:build

# Move the binary into a more managable folder.
mv client/build/libs/client-0.0.1-SNAPSHOT.jar client.jar

# Start the client.
java -jar client.jar
```

#### Windows
```bat
REM Clone the git repo into the OOPP-G7 folder.
git clone git@gitlab.ewi.tudelft.nl:cse1105/2020-2021/team-repositories/oopp-group-07/repository-template.git OOPP-G7

REM Go into the folder we just cloned.
cd OOPP-G7\

REM Compile a binary from the source code.
gradlew :client:build

REM Move the binary into a more managable folder.
move client\build\libs\client-0.0.1-SNAPSHOT.jar client.jar

REM Start the client.
java -jar client.jar
```

### Server
Follow the below instructions to run the server program.

#### Windows
The server is currently only supported on Linux and Mac OS so windows users will have to install a WSL distro. Additionally they also want to install docker in some way. Whether that is through the windows installer or via WSL itself.

#### Linux & Mac OS
Make sure docker and docker-compose are installed prior to following any of these steps.
```sh
# Clone the git repo into the OOPP-G7 folder.
git clone git@gitlab.ewi.tudelft.nl:cse1105/2020-2021/team-repositories/oopp-group-07/repository-template.git OOPP-G7

# Go into the folder we just cloned.
cd OOPP-G7/

# Compile a binary from the source code.
./gradlew :server:build

# Move the binary into a more managable folder.
mv server/build/libs/server-0.0.1-SNAPSHOT.jar server.jar

# Start the database.
sudo docker-compose up -d

# Start the server.
java -jar server.jar
```


## How to contribute to it

### Setting up a development environment
To start development you are going to need a couple of tools:
- git
- docker
- docker-compose

```sh
# Now you can clone the git repo into the OOPP-G7 folder.
git clone git@gitlab.ewi.tudelft.nl:cse1105/2020-2021/team-repositories/oopp-group-07/repository-template.git OOPP-G7
```

Now you are free to import the project into your favourite IDE and to start coding.

### Contribution guidlines
- Before contributing please read our [CODE\_OF\_CONDUCT](CODE_OF_CONDUCT.md).
- When writing commit messages try to make it clear to unkowning readers what is being changed in the commit.
- Never commit directly to master or development.

