`practice_03`

### Music Library App

Allows to store and edit your music library. You can add artists, their albums and tracks. Sort it by file type, quality (bit depth, sample rate) or genre. Also, there is an account system allows you to store tracks separately from others.

### Architecture

All of the data stored in Room library.\
App works with two main activities and most of the UI is based on fragments and Navigation.\
Database queries proceed with Coroutines not affecting UI thread.
