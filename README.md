Flick_Gallary
=============

Android app : Download the images using flickr API HTTP call.


Api information:
http://www.flickr.com/services/api/

API endpoints to use:
http://www.flickr.com/services/api/flickr.interestingness.getList.html
http://www.flickr.com/services/api/flickr.photos.search.html

Info about the image source url:
http://www.flickr.com/services/api/misc.urls.html

Info about json responses:
http://www.flickr.com/services/api/response.json.html

URL Structure:

Endpoint: http://api.flickr.com/services/rest/

Params:

method: flickr.interestingness.getList // identifies which api call you are making
api_key: ****
format: json // requesting a json response
nojsoncallback: 1 // do not wrap response in a callback
