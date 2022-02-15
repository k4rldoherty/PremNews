import requests
import json
import re
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('firebase-sdk.json')
firebase_admin.initialize_app(cred, {
    'databaseURL' : 'https://premnews-99ac4-default-rtdb.europe-west1.firebasedatabase.app/'
})

ref = db.reference('/live_scores')
ref.delete()

url = "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=39&live=all"

headers = {
    'x-rapidapi-host': "api-football-v1.p.rapidapi.com",
    'x-rapidapi-key': "45cfe9e593msh04cf7c6f66c28c4p150625jsnc8dd59b1efa7"
    }

response = requests.request("GET", url, headers=headers)
content_response = response.content
content = content_response.decode('utf-8')
content_json = json.loads(content)
ref.push(content_json)
