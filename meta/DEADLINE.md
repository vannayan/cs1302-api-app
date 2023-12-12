# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

My app allows users to look up a Spotify artist's name with their Spotify ID and in addition to that, they can also retireve information about any upcoming events for that artist. There is a help button implemented into the app that provides an explanation and example of what a Spotify ID is. The Spotify ID is used in the Spotify API to retireve the artist's name and that name is used in the SeatGeek API to retrieve any events listed under that artist.

https://github.com/vannayan/cs1302-api-app

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### Spotify API

```
https://api.spotify.com/v1/artists/45eNHdiiabvmbp4erw26rg
```

> The access token is located as the header for the URL. This is included in my HttpRequest for Spotify and is not included in the URL. A Spotify access token is requested each time the "Load" button is pressed and is automatically included in the HttpRequest.

### SeatGeek API

```
https://api.seatgeek.com/2/events?performers.slug=ILLENIUM&client_id=Mzg3Mjc2NjZ8MTcwMTcyNjUxNS44MDcxOA&client_secret=a8cf88a987a471d2b101eaf57d1b41eb99c84dff3105a911cda3ce9ba4535e17
```

> The API key (client ID/client secret) are both included in the URL.

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

The most exciting part of this project was having the freedom to create and design my own app (with certain restrictions of course). This challenged me to learn how to overcome any layout problems and also pushed me to overcome any API issues/confusion.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

I would definitely try to see if I can implement any new functions for the user to make it easier or even more interactive. I also realized that implementing methods step-by-step was very important as it's more organized than trying to solve two different APIs at once.