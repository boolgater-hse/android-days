`practice_04`

### VK Albums App

Allows to view yours or someone else's photo albums uploaded on VK's servers

### Architecture

App uses VK API to get albums and images, therefore you might need API access key to use the app.\
Data is received from the API using the Retrofit2/OkHttp libraries.\
All API calls proceed inside of a ViewModel (with using Lifecycle component) to Repository to Retrofit.\
To display images, it was decided to use [Glide](https://bumptech.github.io/glide/) library.\
UI was built with Navigation component.

### Demo

<img src="./demo/demo.gif" width="303" height="640" />
