import requests
import smtplib 
# API key
api_key = 'AIzaSyATPwWeg_lhIP1Lh3Mls3U_WSaY3AI21G4'

def getStringFormatForStopsLocs(stopLocations):
    result = ""
    for location in stopLocations:
        result = result + "via:" + location + "|"
    return result[:-1]


def getTimeTakenWithStopPoints(source, destination, visitPoints):
    stopsLocationsString = getStringFormatForStopsLocs(visitPoints)
    url = "https://maps.googleapis.com/maps/api/directions/json?"
    requestContent = url + "origin=" + source + "&destination=" + destination + "&waypoints=" + stopsLocationsString + "&key=" + api_key
    r2 = requests.get(requestContent) 
    timetaken = r2.json()["routes"][0]["legs"][0]["duration"]
    return timetaken


def getTimeTakenWithoutStopPoints(source, destination):
    url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&"
    r = requests.get(url + "origins=" + source + "&destinations=" + destination + "&key=" + api_key) 
    time = r.json()["rows"][0]["elements"][0]["duration"]["text"]       
    seconds = r.json()["rows"][0]["elements"][0]["duration"]["value"]
    return time


# starting at Department of Coffee and Social Affairs we want to stop at Eton Crop Hairdressing then The Yard and finally reach at Luxury Beauty Room

start="51.513119,-0.138780" # Department of Coffee and Social Affairs

end="51.519290,-0.139890" # Luxury Beauty Room

stopLocations = ['51.517630,-0.125270', '51.511070,-0.132730'] # Eton Crop Hairdressing, The Yard



print(getTimeTakenWithoutStopPoints(start, end))

print(getTimeTakenWithStopPoints(start, end, stopLocations))


