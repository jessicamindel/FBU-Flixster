
FBU Seattle CodePath
# Flixster
A read-only movie listing app using the Movie Database API.

## Demo
![](https://github.com/jessicamindel/FBU-Flixster/raw/master/demo.gif)

## Required User Stories
- [x] User can view a list of movies (title, poster image, and overview) currently playing in theaters from the Movie Database API.

## Stretch User Stories
- [x] Display a nice default placeholder graphic (see links above) for each image during loading (read more about Glide) (1 point)
- [x] Add rounded corners for the images using the Glide transformations (1 point)
- [x] Views should be responsive for both landscape/portrait mode (2 points)
  - In portrait mode, the poster image, title, and movie overview is shown
  - In landscape mode, the rotated layout should use the backdrop image instead and show the title and movie overview to the right of it
- [x] When a movie is selected, expose details of a movie (ratings using RatingBar, popularity, and synopsis) in a separate activity (3 points)
- [x] Improve the user interface through styling and coloring (1 to 5 points depending on the difficulty of UI improvements)
  - The app now uses a dark theme
  - In the movie details view, gradients and a canvas-based overlay are drawn on top of the movie's backdrop image
- [ ] Apply the popular ButterKnife annotation library to reduce view boilerplate (2 points)
- [x] Allow video posts to be played in full-screen using the YouTubePlayerView (3 points)
  - From the movie details page, show an image preview that allows the user to initiate playing a YouTube video
  - Check the /movie/{movie_id}/videos endpoint API docs
  - Endpoint will provide a video "key" such as "key": "SUXWAEX2jlg" which can be used to construct a YouTube video URL such as  https://www.youtube.com/watch?v=SUXWAEX2jlg
