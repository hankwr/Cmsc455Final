# Table of Contents
<!-- vscode-markdown-toc -->
* 1. [Project Description](#ProjectDescription)
* 2. [Getting Started](#GettingStarted)
	* 2.1. [Prerequisites](#Prerequisites)
	* 2.2. [Installing](#Installing)
* 3. [Running the Included Tests](#RunningtheIncludedTests)
* 4. [Deployment](#Deployment)
	* 4.1. [DTOs](#DTOs)
		* 4.1.1. [UserDTO](#UserDTO)
		* 4.1.2. [ProfileDTO](#ProfileDTO)
		* 4.1.3. [GenreTagDTO](#GenreTagDTO)
		* 4.1.4. [PlatformTagDTO](#PlatformTagDTO)
		* 4.1.5. [EventDTO](#EventDTO)
		* 4.1.6. [RegistrationDTO](#RegistrationDTO)
	* 4.2. [URL Endpoints](#URLEndpoints)
		* 4.2.1. [Authentication](#Authentication)
		* 4.2.2. [POST Requests](#POSTRequests)
		* 4.2.3. [GET Requests](#GETRequests)
* 5. [Built With](#BuiltWith)
* 6. [Versioning](#Versioning)
* 7. [Authors](#Authors)
* 8. [License](#License)
* 9. [Acknowledgments](#Acknowledgments)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
# Gaming Friend Finder: Server Backend

##  1. <a name='ProjectDescription'></a>Project Description

This project is the backend portion of the gaming friend finder project. The backend was built by the 455 team, and the front-end website was built by the 106 team.
The overall project is meant to be a resource for people interested in gaming to find friends or others interested in gaming and creating events together. Users can find others with similar interests, and invite them to related events.

##  2. <a name='GettingStarted'></a>Getting Started

These instructions will give you a basic understanding of the functions of the server and how to interact with it when developing the front-end counterpart.

###  2.1. <a name='Prerequisites'></a>Prerequisites

Requirements for the software and other tools to build, test and push 
- [Example 1](https://www.example.com)
- [Example 2](https://www.example.com)

###  2.2. <a name='Installing'></a>Installing

A step by step series of examples that tell you how to get a development
environment running

Say what the step will be

    Give the example

And repeat

    until finished

End with an example of getting some data out of the system or using it
for a little demo

##  3. <a name='RunningtheIncludedTests'></a>Running the Included Tests

All tests for this project are **end-to-end** tests contained within the *UserRegistrationTests.java* file. To run them, depending on your IDE, you should be able to right click on the file and click on an option similar to "Run Tests".

Note that running these tests only works if you have the backend opened yourself as a developer. There is no way to run them by interacting with the server through HTTP

##  4. <a name='Deployment'></a>Deployment

###  4.1. <a name='DTOs'></a>DTOs

DTOs (Data Transfer Objects) are the format for JSON objects that will be sent to and from the server in the body of the requests and responses. You must follow these formats to get the server to work.

####  4.1.1. <a name='UserDTO'></a>UserDTO

    {
        username: [String]
        password: [String]
        token: [String]
    }

####  4.1.2. <a name='ProfileDTO'></a>ProfileDTO

    {
        user: [String]
        fullname: [String]
        emailaddress: [String]
        countrycode: [int]
        phonenumber: [String]
        bio: [String]
        genres: [List<String>]
        platforms: [List<Strings>]
    }

##### Extra Details
The "user" field is userid of the user associated with this profile.
The genres and platforms lists will be turned into the tag objects on the backend, but should be sent as Strings if they are within a ProfileDTO.

####  4.1.3. <a name='GenreTagDTO'></a>GenreTagDTO
***Genre Tags*** represent the genres of games that the user is interested in.

    {
        profile: [Integer]
        name: [name]
    }

####  4.1.4. <a name='PlatformTagDTO'></a>PlatformTagDTO
***Platform Tags*** represent the gaming platforms the user uses.

    {
        platformTagID: [Integer]
        profile: [Profile]
        name: [String]
    }

####  4.1.5. <a name='EventDTO'></a>EventDTO

    {
        userid: [UUID]
        name: [String]
        description: [String]
        location: [String]
        timeZone: [String]
        startTime: [String]
        endTime: [String]
        numRegistered: [String]
    }

####  4.1.6. <a name='RegistrationDTO'></a>RegistrationDTO

    {
        eventid: [Integer]
        userid: [UUID]
    }

###  4.2. <a name='URLEndpoints'></a>URL Endpoints

As a rule of thumb, GET requests will return a DTO of the object you're requesting, and POST requests will return a DTO copy of the object you posted to the server.

####  4.2.1. <a name='Authentication'></a>Authentication
Most of the HTTP Requests (except registering and logging in) require authentication. This means that to be granted access to the resource the user is requesting, they must send their authentication token within the HTTP header. This takes the form:

    Authorization: Bearer <token>
where \<token\> is is the token they received from the login request and Authentication is the name of the correct header.

Thus, the authentication is linked to them logging in, and they *must do so before* being able to access the other portions of the server.

####  4.2.2. <a name='POSTRequests'></a>POST Requests

##### Post a New User

    [baseurl]/users

Request body should include a [UserDTO](#UserDTO)  with a unique username and password.

##### User Log-in

    [baseurl]/users/login

Request body should include a [UserDTO](#UserDTO)  with a unique username and password. The return body will send back the same object with the *assigned authentication token*. Thus, **the front-end should save this token for the user for authentication in future requests.**

##### Post a User Profile

    [baseurl]/users/profile
Request body should include a [ProfileDTO](#ProfileDTO). The profile is linked to the user submitting the request, and only one profile can exist per user, so if one already exists an exception will be thrown.

##### Post an Event

    [baseurl]/events

Request body should include an [EventDTO](#EventDTO). The *userid* field corresponds to the host of the event, which is the person posting the event to the server.

##### Post a Registration

    [baseurl]/registrations
Request body should include a [RegistrationDTO](#RegistrationDTO). The userid and eventid **must correspond** to objects that already exist in the database.

####  4.2.3. <a name='GETRequests'></a>GET Requests

##### Get User Profile

    [baseurl]/users/profile
Returns the [ProfileDTO](#ProfileDTO) corresponding to the requesting user.

##### Get Events the User is Hosting

    [baseurl]/users/events
Returns a *list* of [EventDTO](#EventDTO) objects corresponding to all events in which the current user is assigned to be the host.

##### Get All Future Events

    [baseurl]/events
Returns a *list* of [EventDTO](#EventDTO) objects corresponding to all events that have yet to begin.
##### Get Specific Event

    [baseurl]/events/{id}
The {id} field should be replaced with the id-value of the event being requesed. Returns an [EventDTO](#EventDTO) object corresponding to that event.

##  5. <a name='BuiltWith'></a>Built With

  - [Contributor Covenant](https://www.contributor-covenant.org/) - Used
    for the Code of Conduct
  - [Creative Commons](https://creativecommons.org/) - Used to choose
    the license

##  6. <a name='Versioning'></a>Versioning

Professor Gregg will send all contributors an email whenever he deploys a new version of the backend on the website. The new version is then live. This repository will not necessarily match that version since this repository has the most up-to-date version, but it should hopefully be as accurate as possible.

##  7. <a name='Authors'></a>Authors
  - **Jack Stuart** - *Project Co-Creator* - [Github](https://github.com/stuja16)
  - **Henry Roach** - *Project Co-Creator* - [Github](https://github.com/hankwr)
  - **Billie Thompson** - *Provided README Template* -
    [PurpleBooth](https://github.com/PurpleBooth)

##  8. <a name='License'></a>License

This project is licensed under the [CC0 1.0 Universal](LICENSE.md)
Creative Commons License - see the [LICENSE.md](LICENSE.md) file for
details

##  9. <a name='Acknowledgments'></a>Acknowledgments

  - Thank you to the 106 team for building the matching front-end for this project
  - Thank you to Professor Joe Gregg for the class, examples, and assistance as we learned and worked on this project
